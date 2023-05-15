package com.example.nicejobapplication.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.nicejobapplication.TabFragment.CorpDetailIntroductionTabFragment
import com.example.nicejobapplication.TabFragment.JobsCorpListFragment

class CorpViewPagerAdapter(fragmentManager: FragmentManager, private val totalTab: Int, private val bundle: Bundle): FragmentPagerAdapter(fragmentManager){
    override fun getCount(): Int = totalTab

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> CorpDetailIntroductionTabFragment(bundle)
            1 -> JobsCorpListFragment(bundle)
            else -> CorpDetailIntroductionTabFragment(bundle)
        }
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Giới thiệu"
        1 -> "Tuyển dụng"
        else -> "Giới thiệu"
    }


}