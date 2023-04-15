package com.example.nicejobapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nicejobapplication.databinding.ActivityMainBinding
import com.example.nicejobapplication.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser = firebaseAuth.currentUser

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
                    if (firebaseUser==null){
                        supportFragmentManager.beginTransaction().replace(
                            R.id.fragment_container, ProfileFragment()
                        ).commit()
                    }else{
                        supportFragmentManager.beginTransaction().replace(
                            R.id.fragment_container, LoginProfileFragment()
                        ).commit()
                    }
                    true
                }
                else -> false
            }
        }
    }


}