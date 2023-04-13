package com.example.nicejobapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nicejobapplication.databinding.ActivityMainBinding
import com.example.nicejobapplication.fragment.JobsFragment
import com.example.nicejobapplication.fragment.CVFragment
import com.example.nicejobapplication.fragment.ProfileFragment
import com.example.nicejobapplication.fragment.CorpFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container, JobsFragment()
        ).commit()


        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_navigation -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        JobsFragment()
                    ).commit()
                    true
                }
                R.id.search_navigation -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        CorpFragment()
                    ).commit()
                    true
                }
                R.id.notification_navigation -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        CVFragment()
                    ).commit()
                    true
                }
                R.id.personal_navigation -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container, ProfileFragment()
                    ).commit()
                    true
                }
                else -> false
            }
        }
    }


}