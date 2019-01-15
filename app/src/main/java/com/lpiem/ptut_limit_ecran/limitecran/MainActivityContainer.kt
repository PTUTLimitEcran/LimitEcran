package com.lpiem.ptut_limit_ecran.limitecran

import android.Manifest
import android.app.*
import android.content.Context
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.os.SystemClock
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.RemoteViews
import android.widget.Toast
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import kotlinx.android.synthetic.main.activity_main_container.*
import kotlinx.android.synthetic.main.fragment_tree.*
import processing.core.PApplet



class MainActivityContainer : AppCompatActivity(){

    private var prevMenuItem: MenuItem? = null
    private lateinit var fragmentHome: TreeFragment
    private lateinit var fragmentStat: StatisticFragment
    private lateinit var fragmentGallery: GalleryFragment
    private val singleton: Singleton = Singleton.getInstance(this)
    private var sketch: PApplet? = null
    private val REQUEST_WRITE_STORAGE = 0
    private var viewPagerAdapter: ViewPagerAdapter? = null

    companion object {
        val PACKAGE_NAME = MainActivityContainer::getPackageName
    }

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
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)
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
        fragmentStat.putContext(applicationContext)
        viewPagerAdapter?.addFragment(fragmentStat)
        viewPagerAdapter?.addFragment(fragmentHome)
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

        val screenOnOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action

                val keyguardManager = context?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (action == Intent.ACTION_USER_PRESENT ||
                    action == Intent.ACTION_USER_UNLOCKED ||
                    action == Intent.ACTION_SCREEN_OFF ||
                    action == Intent.ACTION_SCREEN_ON ||
                    action == Context.FINGERPRINT_SERVICE
                )
                    if (keyguardManager.isDeviceLocked){
                        singleton.IsDeviceOn = true
                    }
                    else if (keyguardManager.isKeyguardLocked) {
                        Log.d("Screen", "Screen locked")
                        singleton.IsDeviceOn = false
                        if (!singleton.IsRunning) {
                            singleton.IsRunning = true
                            if(singleton.CurrentCountDownTimer == 0L){
                                singleton.startCountDownTimer()
                            }else{
                                singleton.resumeCountDownTimer(fragmentHome)
                            }
                        }
                    } else {
                        Log.d("Screen", "Screen unlocked")
                        if (singleton.IsRunning) {
                            singleton.IsRunning = false
                            singleton.pauseCountDownTimer()
                            fragmentHome.updateTextView(singleton.formatTime(singleton.CurrentCountDownTimer))
                        }
                    }
                val openMainActivity = Intent(context, MainActivityContainer::class.java)
                openMainActivity.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivityIfNeeded(openMainActivity, 0)
            }
        }
        this.registerReceiver(screenOnOffReceiver, intentFilter)
    }

    /**
     * Create an instance of the notification channel
     */
    private fun createNotificationChannel() {
        this.singleton.NotificationChannel = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Register the channel with the system
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    getString(R.string.channelId),
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = getString(R.string.channel_description)
                })
        }
        this.singleton.Notification.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            .setVibrate(longArrayOf(0L)) // Passing null here silently fails
        this.singleton.NotificationChannel!!.notify(0, this.singleton.Notification!!.build())
    }

    /**
     * Create the notification
     */
    private fun createNotification() {
        singleton.initNotification(this,
            getString(R.string.app_name),
            getString(R.string.channelId),
            getString(R.string.channel_description))
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

    private fun initCountDownTimer(){
        if(!singleton.FirstTime){
            singleton.initCountDownTimer(300000,fragmentHome)
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
}