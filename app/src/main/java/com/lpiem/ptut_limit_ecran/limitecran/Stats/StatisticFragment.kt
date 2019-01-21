package com.lpiem.ptut_limit_ecran.limitecran.Stats

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.lpiem.ptut_limit_ecran.limitecran.R
import kotlinx.android.synthetic.main.fragment_statistic.*

private const val ARG_PARAM1 = "context"
private const val ARG_PARAM2 = "param2"

class StatisticFragment : Fragment(), AdapterView.OnItemSelectedListener,
    PassContext {

    override fun putContext(context: Context) {
        appContext = context
        mInflater = appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mUsageStatsManager=  appContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        mPm = appContext.packageManager
    }


    private lateinit var appContext: Context
    private lateinit var mInflater: LayoutInflater
    private lateinit var mUsageStatsManager: UsageStatsManager
    private lateinit var mPm: PackageManager
    private lateinit var mAdapter: UsageStatsAdapter

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StatisticFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        appContext = requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        typeSpinner.onItemSelectedListener = this
        mAdapter = UsageStatsAdapter(appContext)
        pkg_list.adapter = mAdapter

    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mAdapter.sortList(position)
    }



}
