package com.example.nicejobapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nicejobapplication.TabFragment.LoginTabFragment
import com.example.nicejobapplication.TabFragment.SignupTabFragment

class ViewPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 1) {
            SignupTabFragment()
        } else LoginTabFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}