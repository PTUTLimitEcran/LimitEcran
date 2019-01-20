package com.lpiem.ptut_limit_ecran.limitecran

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.text.format.DateUtils
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.durranilab.labprogresslayout.LabProgressLayout
import java.util.*

const val TAG = "TEST_WARN"

class UsageStatsAdapter(private var context: Context): BaseAdapter() {

    private val _DISPLAY_ORDER_USAGE_TIME = 0
    private val _DISPLAY_ORDER_LAST_TIME_USED = 1
    private val _DISPLAY_ORDER_APP_NAME = 2
    private val localLOGV = false
    private var totalMilli: Long = 0

    private var mInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var mDisplayOrder = _DISPLAY_ORDER_USAGE_TIME
    private val mLastTimeUsedComparator = LastTimeUsedComparator()
    private val mUsageTimeComparator = UsageTimeComparator()
    private lateinit var mAppLabelComparator: AppNameComparator
    private var mAppLabelMap = ArrayMap<String, String>()
    private var mPackageStats = ArrayList<UsageStats>()
    private var mUsageStatsManager: UsageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    private val mPm = context.packageManager

    init {

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -5)
        val stats = mUsageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            cal.timeInMillis, System.currentTimeMillis()
        )
        if (stats != null) {

            val map = ArrayMap<String, UsageStats>()
            val statCount = stats.size

            for (i in 0 until statCount) {
                val pkgStats = stats[i]

                try {
                    val pkgName = pkgStats.packageName
                    val appInfo = mPm.getApplicationInfo(pkgName, 0)

                    val label = appInfo.loadLabel(mPm).toString()
                    mAppLabelMap[pkgName] = label

                    val existingStats = map[pkgName]

                    if (existingStats == null) {
                        map[pkgName] = pkgStats
                    } else {
                        existingStats.add(pkgStats)
                    }

                } catch (e: PackageManager.NameNotFoundException) {
                    // This package may be gone.
                }
            }

            mPackageStats.addAll(map.values)

            for (i in mPackageStats.indices) {
                totalMilli += mPackageStats[i].totalTimeInForeground
            }

            mAppLabelComparator = AppNameComparator(mAppLabelMap)
            sortList()
        }
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: AppViewHolder?
        val viewToReturn: View

        if (convertView == null) {
            viewToReturn = mInflater.inflate(R.layout.usage_stats_item, null)

            holder = AppViewHolder()

            holder.Icon = viewToReturn.findViewById(R.id.Icon) as ImageView?
            holder.pkgName = viewToReturn.findViewById(R.id.package_name) as TextView?
            holder.usageTime = viewToReturn.findViewById(R.id.usage_time) as TextView?
            holder.labProgressLayout = viewToReturn.findViewById(R.id.labProgressLayout) as LabProgressLayout?
            viewToReturn?.tag = holder
        } else {
            viewToReturn = convertView
            holder = convertView.tag as AppViewHolder?
        }

        val pkgStats = mPackageStats[position]
        if (pkgStats != null) {
            val label = mAppLabelMap[pkgStats.packageName]

            try {

                val appIcon = context.packageManager.getApplicationIcon(pkgStats.packageName)
                holder?.Icon?.setImageDrawable(appIcon)

            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            holder?.pkgName?.text = label
            holder?.usageTime?.text = DateUtils.formatElapsedTime(pkgStats.totalTimeInForeground / 1000)
            val totalApp = pkgStats.totalTimeInForeground.toFloat()
            val percent = (totalApp / totalMilli * 100).toInt()
            holder?.labProgressLayout?.setCurrentProgress(percent)

        } else {
            Log.w(TAG, "No usage stats info for package:$position")
        }

        return viewToReturn
        }




    override fun getItem(position: Int): Any {
        return mPackageStats[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mPackageStats.size
    }

    fun sortList(sortOrder: Int) {
        if (mDisplayOrder == sortOrder) {
            return
        }
        mDisplayOrder = sortOrder
        sortList()
    }

    private fun sortList() {

        when (mDisplayOrder) {
            _DISPLAY_ORDER_USAGE_TIME -> {
                if (localLOGV) Log.i(TAG, "Sorting by usage time")
                Collections.sort(mPackageStats, mUsageTimeComparator)
            }
            _DISPLAY_ORDER_LAST_TIME_USED -> {
                if (localLOGV) Log.i(TAG, "Sorting by last time used")
                Collections.sort(mPackageStats, mLastTimeUsedComparator)
            }
            _DISPLAY_ORDER_APP_NAME -> {
                if (localLOGV) Log.i(TAG, "Sorting by application name")
                Collections.sort(mPackageStats, mAppLabelComparator)
            }
        }

        notifyDataSetChanged()
    }

    class AppNameComparator internal constructor(private val mAppLabelList: Map<String, String>) :
        Comparator<UsageStats> {

        override fun compare(a: UsageStats, b: UsageStats): Int {
            val alabel = mAppLabelList[a.packageName]
            val blabel = mAppLabelList[b.packageName]
            return alabel?.compareTo(blabel!!)!!
        }
    }

    class LastTimeUsedComparator : Comparator<UsageStats> {
        override fun compare(a: UsageStats, b: UsageStats): Int {
            return (b.lastTimeUsed - a.lastTimeUsed).toInt()
        }
    }

    class UsageTimeComparator : Comparator<UsageStats> {
        override fun compare(a: UsageStats, b: UsageStats): Int {
            return (b.totalTimeInForeground - a.totalTimeInForeground).toInt()
        }
    }

    internal class AppViewHolder {
        var Icon: ImageView? = null
        var pkgName: TextView? = null
        var usageTime: TextView? = null
        var labProgressLayout: LabProgressLayout? = null
    }

}