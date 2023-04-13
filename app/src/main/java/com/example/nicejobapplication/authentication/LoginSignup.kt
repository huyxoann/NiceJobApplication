package com.example.nicejobapplication.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.ActivityLoginSignupBinding
import com.example.nicejobapplication.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginSignup : AppCompatActivity() {
    private lateinit var binding: ActivityLoginSignupBinding
    private lateinit var viewPagerAdapter: ViewPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login_signup)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.tabLayout.addTab(binding.tabLayout.newTab().setText("LOGIN"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("SIGNUP"))

        val fragmentManager : FragmentManager =supportFragmentManager
        viewPagerAdapter = ViewPageAdapter(fragmentManager,lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })




    }
}