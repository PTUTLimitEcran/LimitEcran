package com.lpiem.ptut_limit_ecran.limitecran.Application

import android.Manifest
import android.app.AppOpsManager
import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.lpiem.ptut_limit_ecran.limitecran.Gallery.GalleryFragment
import com.lpiem.ptut_limit_ecran.limitecran.Home.Challenge.ChallengeFragment
import com.lpiem.ptut_limit_ecran.limitecran.Home.Sketch.Sketch
import com.lpiem.ptut_limit_ecran.limitecran.Home.Tree.TreeFragment
import com.lpiem.ptut_limit_ecran.limitecran.Manager.Manager
import com.lpiem.ptut_limit_ecran.limitecran.R
import com.lpiem.ptut_limit_ecran.limitecran.Stats.StatisticFragment
import kotlinx.android.synthetic.main.activity_main_container.*
import processing.core.PApplet


class MainActivityContainer : AppCompatActivity() {

    private var prevMenuItem: MenuItem? = null
    private lateinit var fragmentHome: TreeFragment
    private lateinit var fragmentStat: StatisticFragment
    private lateinit var fragmentGallery: GalleryFragment
    private lateinit var fragmentChallenge: ChallengeFragment
    private lateinit var manager: Manager
    private var sketch: PApplet? = null
    private val REQUEST_STORAGE_PERMISSION = 0
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var screenOnOffReceiver: BroadcastReceiver
    private var challengeTime = 0L

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_stat -> {
                fragment_container.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_home -> {
                requestStoragePermission()
                fragment_container.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_gallery -> {
                viewPagerAdapter?.notifyDataSetChanged()
                fragment_container.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)
        manager = Manager.getInstance(this)
        createNotification()
        createNotificationChannel()
        registerBroadcastReceiver()

        fragment_container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    prevMenuItem?.isChecked = false
                } else {
                    navigation.menu.getItem(1).isChecked = false
                }
                navigation.menu.getItem(position).isChecked = true
                prevMenuItem = navigation.menu.getItem(position)

            }

            override fun onPageScrollStateChanged(state: Int) {}

        })

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        requestStoragePermission()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        if (viewPagerAdapter == null) viewPagerAdapter =
                ViewPagerAdapter(supportFragmentManager)
        fragmentHome = TreeFragment()
        fragmentStat = StatisticFragment()
        fragmentGallery = GalleryFragment()
        fragmentChallenge = ChallengeFragment()
        fragmentStat.putContext(applicationContext)
        viewPagerAdapter?.addFragment(fragmentStat)

        if (manager.ChallengeAccepted) {
            viewPagerAdapter?.addFragment(fragmentHome)
        } else {
            viewPagerAdapter?.addFragment(fragmentChallenge)
        }
        viewPagerAdapter?.addFragment(fragmentGallery)
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = 1
    }

    /**
     * Function about managing activity before screen lock
     */
    private fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter()
        /** System Defined Broadcast */
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)

        screenOnOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                val keyguardManager = context?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (action == Intent.ACTION_SCREEN_ON) {
                    if (keyguardManager.isKeyguardLocked) {
                        manager.IsDeviceOn = true
                        startOrResumeCountDownTimer()
                    }
                }
                if (action == Intent.ACTION_USER_PRESENT) {
                    if (manager.IsRunning) {
                        manager.IsRunning = false
                        manager.pauseCountDownTimer()
                    }
                }
                if (action == Intent.ACTION_SCREEN_OFF) {
                    manager.IsDeviceOn = false
                    startOrResumeCountDownTimer()
                }
            }
        }
        registerReceiver(screenOnOffReceiver, intentFilter)
    }

    private fun startOrResumeCountDownTimer() {
        if (!manager.IsRunning) {
            manager.IsRunning = true
            if (manager.CurrentCountDownTimer == 0L) {
                manager.startCountDownTimer()
            } else {
                manager.resumeCountDownTimer(fragmentHome)
            }
        }
    }

    private fun createNotificationChannel() {
        manager.initNotificationChannel(this, getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
    }

    private fun createNotification() {
        manager.initNotification(
            this,
            getString(R.string.app_name),
            getString(R.string.channelId),
            getString(R.string.channel_description)
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        sketch?.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )

        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    challengeTime = manager.ChallengeTime
                    initSketch()
                    initCountDownTimer()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.StoragePermissionsWarning),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    requestStoragePermission()
                }
            }
        }
    }

    private fun initCountDownTimer() {
        if (manager.ChallengeAccepted) {
            manager.initCountDownTimer(challengeTime, fragmentHome)
            manager.FirstTime = true
        }
    }

    public override fun onNewIntent(intent: Intent) {
        sketch?.onNewIntent(intent)
    }

    private fun initSketch() {
        sketch = Sketch("", false)
        if (viewPagerAdapter == null) {
            setupViewPager(fragment_container)
        }
        if (!checkForPermission(applicationContext)) {
            dialogViewForSystemAuth()
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_STORAGE_PERMISSION
        )
    }

    private fun checkForPermission(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
        return mode == AppOpsManagerCompat.MODE_ALLOWED
    }

    override fun onDestroy() {
        unregisterReceiver(screenOnOffReceiver)
        super.onDestroy()
    }

    private fun dialogViewForSystemAuth() {

        val builder: AlertDialog.Builder? = this.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage(R.string.requestSystemAuth)
            ?.setTitle(getString(R.string.systemPermissionRequestTitle))
            ?.setPositiveButton("Go") { dialog, id ->
                run {
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }
            }


        val dialog: AlertDialog? = builder?.create()
        dialog?.show()

    }
}