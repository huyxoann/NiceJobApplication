package com.example.nicejobapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nicejobapplication.TabFragment.CorpDetailIntroductionTabFragment
import com.example.nicejobapplication.TabFragment.JobsCorpListFragment

class CorpViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        when (position){
            0 -> return CorpDetailIntroductionTabFragment()
            1 -> return JobsCorpListFragment()
        }
        return CorpDetailIntroductionTabFragment()

    }

}