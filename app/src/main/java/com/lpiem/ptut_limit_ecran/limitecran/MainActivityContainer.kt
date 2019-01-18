package com.lpiem.ptut_limit_ecran.limitecran

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
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import kotlinx.android.synthetic.main.activity_main_container.*
import processing.core.PApplet


class MainActivityContainer() : AppCompatActivity() {


//    override fun setNewChallenge(challengeTime: Int) {
//        viewPagerAdapter?.replaceFragment(fragmentChallenge, fragmentHome)
//        viewPagerAdapter?.notifyDataSetChanged()
//        val intent = Intent(applicationContext, MainActivityContainer::class.java )
//        intent.putExtra("challenge", true)
//        intent.putExtra("ChallengeTime", challengeTime)
//        startActivity(intent)
//        finish()
//    }


    private var prevMenuItem: MenuItem? = null
    private lateinit var fragmentHome: TreeFragment
    private lateinit var fragmentStat: StatisticFragment
    private lateinit var fragmentGallery: GalleryFragment
    private lateinit var fragmentChallenge: ChallengeFragment
    private lateinit var singleton: Singleton
    private var sketch: PApplet? = null
    private val REQUEST_WRITE_STORAGE = 0
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var screenOnOffReceiver:BroadcastReceiver
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
                fragment_container.currentItem = 2
                viewPagerAdapter?.notifyDataSetChanged()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)
        singleton = Singleton.getInstance(this)
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
                Log.d("page", "onPageSelected: $position")
                navigation.menu.getItem(position).isChecked = true
                prevMenuItem = navigation.menu.getItem(position)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        requestStoragePermission()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        if (viewPagerAdapter == null) viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        fragmentHome = TreeFragment()
        fragmentStat = StatisticFragment()
        fragmentGallery = GalleryFragment()
        fragmentChallenge = ChallengeFragment()
        fragmentStat.putContext(applicationContext)
        viewPagerAdapter?.addFragment(fragmentStat)

        if(singleton.ChallengeAccepted) {
            viewPagerAdapter?.addFragment(fragmentHome)
        }
        else {
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
        intentFilter.addAction(Intent.ACTION_USER_UNLOCKED)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)

        screenOnOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                Log.d("Intent", intent?.action.toString())

                val keyguardManager = context?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (action == Intent.ACTION_USER_PRESENT ||
                    action == Intent.ACTION_USER_UNLOCKED ||
                    action == Intent.ACTION_SCREEN_OFF ||
                    action == Intent.ACTION_SCREEN_ON ||
                    action == Context.FINGERPRINT_SERVICE
                )
                    if (action == Intent.ACTION_SCREEN_ON) {
                        Log.d("Screen","Screen turned on")
                        if(keyguardManager.isKeyguardLocked){
                            Log.d("Screen", "Screen locked")
                            singleton.IsDeviceOn = true
                            startOrResumeCountDownTimer()
                        }
                    }
                if (action == Intent.ACTION_USER_PRESENT) {
                    Log.d("Screen", "Screen unlocked")
                    if (singleton.IsRunning) {
                        singleton.IsRunning = false
                        singleton.pauseCountDownTimer()
                        //fragmentHome.updateTextView(singleton.formatTime(singleton.CurrentCountDownTimer))
                    }
                }
                if (action == Intent.ACTION_SCREEN_OFF) {
                        Log.d("Screen", "Screen locked")
                        Log.d("Screen", "Phone screen turned off")
                        singleton.IsDeviceOn = false
                        startOrResumeCountDownTimer()
                    }
//                val openMainActivity= Intent(context, MainActivityContainer::class.java)
//                openMainActivity.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                startActivityIfNeeded(openMainActivity, 0)
            }
        }
        registerReceiver(screenOnOffReceiver, intentFilter)
    }

    /**
     * Start or resume countDownTimer
     */
    private fun startOrResumeCountDownTimer() {
        if (!singleton.IsRunning) {
            singleton.IsRunning = true
            if (singleton.CurrentCountDownTimer == 0L) {
                singleton.startCountDownTimer()
            } else {
                singleton.resumeCountDownTimer(fragmentHome)
            }
        }
    }

    /**
     * Create an instance of the notification channel
     */
    private fun createNotificationChannel() {
        singleton.initNotificationChannel(this, getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
    }

    /**
     * Create the notification
     */
    private fun createNotification() {
        singleton.initNotification(
            this,
            getString(R.string.app_name),
            getString(R.string.channelId),
            getString(R.string.channel_description)
        )
    }

    /**
     * Draw part
     * */

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        sketch?.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )

        when (requestCode) {
            REQUEST_WRITE_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    challengeTime = singleton.ChallengeTime
                    initSketch()
                    initCountDownTimer()
                } else {
                    Toast.makeText(
                        this,
                        "Storage permission is required for saving picture in gallery",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    requestStoragePermission()
                }
            }
        }

    }

    private fun initCountDownTimer() {
        if (singleton.ChallengeAccepted) {
            singleton.initCountDownTimer(challengeTime, fragmentHome)
            singleton.FirstTime = true
        }
    }

    public override fun onNewIntent(intent: Intent) {
        sketch?.onNewIntent(intent)
    }

    fun initSketch() {
        sketch = Sketch("", false)
        if (viewPagerAdapter == null) {
            setupViewPager(fragment_container)
        }
        if (!checkForPermission(applicationContext)) {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_STORAGE
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



}