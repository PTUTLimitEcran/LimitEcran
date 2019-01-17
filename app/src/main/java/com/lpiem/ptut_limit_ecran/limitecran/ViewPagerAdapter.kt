package com.lpiem.ptut_limit_ecran.limitecran

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private val mFragmentList: MutableList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    fun replaceFragment(toDelete: Fragment, toAdd: Fragment) {
        mFragmentList.add(1, toAdd)
        mFragmentList.remove(toDelete)
    }

    fun newChallengeFragment(delete1: Fragment, delete2:Fragment, delete3:Fragment, toAdd: Fragment) {
        mFragmentList.add(1, toAdd)
            mFragmentList.remove(delete1)
            mFragmentList.remove(delete2)
            mFragmentList.remove(delete3)
    }


}