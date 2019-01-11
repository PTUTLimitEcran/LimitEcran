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
    private val mAppLabelMap = ArrayMap<String, String>()
    private val mPackageStats = ArrayList<UsageStats>()
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
            val statCount = stats!!.size

            for (i in 0 until statCount) {
                val pkgStats = stats!!.get(i)

                // load application labels for each application
                try {
                    val appInfo = mPm.getApplicationInfo(pkgStats.getPackageName(), 0)

                    val label = appInfo.loadLabel(mPm).toString()
                    mAppLabelMap[pkgStats.getPackageName()] = label

                    val existingStats = map[pkgStats.getPackageName()]

                    if (existingStats == null) {
                        map[pkgStats.getPackageName()] = pkgStats
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


            // Sort list
            mAppLabelComparator = AppNameComparator(mAppLabelMap)
            sortList()
        }
    }



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        val holder: AppViewHolder
        var viewToReturn: View

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            viewToReturn = mInflater.inflate(R.layout.usage_stats_item, null)

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = AppViewHolder()

            holder.Icon = convertView?.findViewById(R.id.Icon) as ImageView
            holder.pkgName = convertView.findViewById(R.id.package_name) as TextView
            //holder.lastTimeUsed = (TextView) convertView.findViewById(R.id.last_time_used);
            holder.usageTime = convertView.findViewById(R.id.usage_time) as TextView
            holder.labProgressLayout = convertView.findViewById(R.id.labProgressLayout) as LabProgressLayout
            convertView?.tag = holder
        } else {
            viewToReturn = convertView
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = convertView.tag as AppViewHolder
        }

        // Bind the data efficiently with the holder
        val pkgStats = mPackageStats[position]
        if (pkgStats != null) {
            val label = mAppLabelMap[pkgStats.packageName]

            try {

                val appIcon = context.packageManager.getApplicationIcon(pkgStats.packageName)
                holder.Icon?.setImageDrawable(appIcon)

            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            holder.pkgName?.text = label
            /* holder.lastTimeUsed.setText(DateUtils.formatSameDayTime(pkgStats.getLastTimeUsed(),
                        System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM));*/
            holder.usageTime?.text = DateUtils.formatElapsedTime(pkgStats.totalTimeInForeground / 1000)
            val totalApp = pkgStats.totalTimeInForeground.toFloat()
            val percent = (totalApp / totalMilli * 100).toInt()/*floatPercent*/
            holder.labProgressLayout?.setCurrentProgress(percent)

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
            // do nothing
            return
        }
        mDisplayOrder = sortOrder
        sortList()
    }

    private fun sortList() {
        if (mDisplayOrder == _DISPLAY_ORDER_USAGE_TIME) {
            if (localLOGV) Log.i(TAG, "Sorting by usage time")
            Collections.sort(mPackageStats, mUsageTimeComparator)
        } else if (mDisplayOrder == _DISPLAY_ORDER_LAST_TIME_USED) {
            if (localLOGV) Log.i(TAG, "Sorting by last time used")
            Collections.sort(mPackageStats, mLastTimeUsedComparator)
        } else if (mDisplayOrder == _DISPLAY_ORDER_APP_NAME) {
            if (localLOGV) Log.i(TAG, "Sorting by application name")
            Collections.sort(mPackageStats, mAppLabelComparator)
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
            // return by descending order
            return (b.lastTimeUsed - a.lastTimeUsed).toInt()
        }
    }

    class UsageTimeComparator : Comparator<UsageStats> {
        override fun compare(a: UsageStats, b: UsageStats): Int {
            return (b.totalTimeInForeground - a.totalTimeInForeground).toInt()
        }
    }

    // View Holder used when displaying views
    internal class AppViewHolder {
        var Icon: ImageView? = null
        var pkgName: TextView? = null
        //TextView lastTimeUsed;
        var usageTime: TextView? = null
        var labProgressLayout: LabProgressLayout? = null
    }

}