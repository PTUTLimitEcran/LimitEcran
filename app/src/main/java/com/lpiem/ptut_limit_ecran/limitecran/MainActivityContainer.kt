package com.lpiem.ptut_limit_ecran.limitecran

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.RemoteViews
import android.widget.Toast
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import kotlinx.android.synthetic.main.activity_main_container.*
import processing.core.PApplet
import java.util.*


class MainActivityContainer : AppCompatActivity() {

    private var prevMenuItem: MenuItem? = null
    private lateinit var fragmentHome: TreeFragment
    private lateinit var fragmentStat: StatisticFragment
    private lateinit var fragmentGallery: GalleryFragment
    private lateinit var singleton: Singleton
    private var sketch: PApplet? = null
    private val REQUEST_WRITE_STORAGE = 0


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_stat -> {
                fragment_container.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_home -> {
                requestStoragePermission()
                fragment_container.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_gallery -> {
                fragment_container.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun createNotificationChannel() {
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

    fun createNotification(){
        val notificationCompat = RemoteViews(BuildConfig.APPLICATION_ID, R.layout.notification_small)
        val notificationExpanded = RemoteViews(BuildConfig.APPLICATION_ID,R.layout.notification_large)
        this.singleton.Notification = NotificationCompat.Builder(this, getString(R.string.channelId))
            .setSmallIcon(R.drawable.ic_phonelink_erase_black_24dp)
            .setContentTitle(getString(R.string.app_name))
            .setOngoing(true)
            .setUsesChronometer(true)
            .setContentText(getString(R.string.channel_description))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCustomContentView(notificationCompat)
            .setCustomBigContentView(notificationExpanded)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)
        this.singleton = Singleton.getInstance(this)
        this.createNotification()
        this.createNotificationChannel()

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
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        fragmentHome = TreeFragment.newInstance(sketch = sketch as Sketch, param2 = null)
        fragmentStat = StatisticFragment()
        fragmentGallery = GalleryFragment()
        viewPagerAdapter.addFragment(fragmentStat)
        viewPagerAdapter.addFragment(fragmentHome)
        viewPagerAdapter.addFragment(fragmentGallery)
        viewPager.adapter = viewPagerAdapter
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

    public override fun onNewIntent(intent: Intent) {
        sketch?.onNewIntent(intent)
    }

    fun initSketch() {
        sketch = Sketch(Date())
        setupViewPager(fragment_container)
        this.singleton.startChronometer()
        this.singleton.loadImages()
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_STORAGE
        )
    }
}
