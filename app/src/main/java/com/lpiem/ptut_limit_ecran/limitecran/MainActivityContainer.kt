package com.lpiem.ptut_limit_ecran.limitecran

import android.Manifest
import android.app.AppOpsManager

import android.app.*
import android.content.*

import android.content.Context
import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
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
import com.lpiem.ptut_limit_ecran.limitecran.Model.BackgroundService
import com.lpiem.ptut_limit_ecran.limitecran.Model.Singleton
import kotlinx.android.synthetic.main.activity_main_container.*
import processing.core.PApplet


class MainActivityContainer : AppCompatActivity(), ChallengeUpdateManager {

    override fun setNewChallenge(challengeTime: Int) {
        viewPagerAdapter?.replaceFragment(fragmentChallenge, fragmentHome)
        viewPagerAdapter?.notifyDataSetChanged()
        //viewPagerAdapter?.update()
        val intent = Intent(applicationContext, MainActivityContainer::class.java )
        intent.putExtra("challenge", true)
        intent.putExtra("ChallengeTime", challengeTime)
        startActivity(intent)
        finish()
    }


    private var prevMenuItem: MenuItem? = null
    private lateinit var fragmentHome: TreeFragment
    private lateinit var fragmentStat: StatisticFragment
    private lateinit var fragmentGallery: GalleryFragment
    private lateinit var fragmentChallenge: ChallengeFragment

    private lateinit var singleton: Singleton

    private var sketch: PApplet? = null
    private val REQUEST_WRITE_STORAGE = 0
    private var viewPagerAdapter: ViewPagerAdapter? = null


    private lateinit var backgroundService: BackgroundService
    private var boundTo:Boolean = false
    private val serviceConnection= object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as BackgroundService.LocalBinder
            backgroundService = binder.getService()
            boundTo = true
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            boundTo = false
        }
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
                viewPagerAdapter?.notifyDataSetChanged()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onPause() {
        super.onPause()
        // Bind to LocalService
        if (!boundTo) {
            Intent(this, BackgroundService::class.java).also { intent ->
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            }
            boundTo = true
        }
    }

    override fun onStop() {
        super.onStop()
        if(boundTo){
            unbindService(serviceConnection)
            boundTo = false
        }
    }

    override fun onStart() {
        super.onStart()
        singleton = Singleton.getInstance(this,null)
        Intent(this, BackgroundService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
        println("hfkdshfkds")
    }

    override fun onDestroy() {
        if (boundTo) {
            unbindService(serviceConnection)
            boundTo = false
        }
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)
        Build.VERSION_CODES.O

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
        //startService()
    }

    private fun startService(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, BackgroundService::class.java).also { intent ->
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            })
        }else{
            Intent(this, BackgroundService::class.java).also { intent ->
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        if (viewPagerAdapter == null) viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        var challengeManager:ChallengeUpdateManager = this
        fragmentHome = TreeFragment.newInstance(param2 = intent.getIntExtra("ChallengeTime", 0), sketch = null)
        fragmentStat = StatisticFragment()
        fragmentGallery = GalleryFragment()
        fragmentChallenge = ChallengeFragment.newInstance(challengeManager)
        fragmentStat.putContext(applicationContext)
        viewPagerAdapter?.addFragment(fragmentStat)

        if(intent.getBooleanExtra("challenge",false)) {
            viewPagerAdapter?.addFragment(fragmentHome)
        }
        else {
            viewPagerAdapter?.addFragment(fragmentChallenge)
        }
        viewPagerAdapter?.addFragment(fragmentGallery)
        viewPager.adapter = viewPagerAdapter
        singleton.TreeFragment = fragmentHome
        backgroundService.registerBroadcastReceiver()
        startService()
        viewPager.currentItem = 1
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