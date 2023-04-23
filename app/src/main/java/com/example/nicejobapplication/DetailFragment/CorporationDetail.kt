package com.example.nicejobapplication.DetailFragment;
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.nicejobapplication.R
import com.google.android.material.tabs.TabLayout


class CorporationDetail : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_corporation_detail, container, false)

//        val viewPager: ViewPager = findViewById(R.id.view_pager)
//        val adapter = MyPagerAdapter(supportFragmentManager)
//        adapter.addFragment(FirstFragment(), "Tab 1")
//        adapter.addFragment(SecondFragment(), "Tab 2")
//        viewPager.adapter = adapter
//
//        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
//        tabLayout.setupWithViewPager(viewPager)


        return view
    }


}