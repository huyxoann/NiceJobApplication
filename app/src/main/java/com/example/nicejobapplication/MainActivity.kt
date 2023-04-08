package com.example.nicejobapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.ui.setupWithNavController
import com.example.nicejobapplication.databinding.ActivityMainBinding
import com.example.nicejobapplication.fragment.HomeFragment
import com.example.nicejobapplication.fragment.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container, HomeFragment()
        ).commit()


        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_navigation -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        HomeFragment()
                    ).commit()
                    true
                }
                R.id.search_navigation -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container,
                        SearchFragment()
                    ).commit()
                    true
                }
//                R.id.notification_navigation -> {
//                    supportFragmentManager.beginTransaction().replace(
//                        R.id.fragment_container,
////                        NotificationsFragment()
//                    ).commit()
//                    true
//                }
                else -> false
            }
        }
    }
}