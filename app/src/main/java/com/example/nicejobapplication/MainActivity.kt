package com.example.nicejobapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nicejobapplication.databinding.ActivityMainBinding
import com.facebook.AccessToken
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val accessToken = AccessToken.getCurrentAccessToken()
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        navController = findNavController(R.id.fragment_container)
        bottomNavigation.setupWithNavController(navController)

        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser = firebaseAuth.currentUser



//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.corporationDetail -> {
                    bottomNavigation.visibility = View.GONE
                }
                R.id.jobDetail -> {
                    bottomNavigation.visibility = View.GONE
                }
                else -> {
                    bottomNavigation.visibility = View.VISIBLE
                }
            }
        }



//        bottomNavigation.setOnNavigationItemSelectedListener   { menuItem ->
//            when (menuItem.itemId) {
//                R.id.home_navigation -> {
//                    supportFragmentManager.beginTransaction().replace(
//                        R.id.fragment_container,
//                        JobsFragment()
//                    ).commit()
//                    true
//
//                }
//                R.id.search_navigation -> {
//                    supportFragmentManager.beginTransaction().replace(
//                        R.id.fragment_container,
//                        CorpFragment()
//                    ).commit()
//                    true
//                }
//                R.id.notification_navigation -> {
//                    supportFragmentManager.beginTransaction().replace(
//                        R.id.fragment_container,
//                        CorporationDetail()
//                    ).commit()
//                    true
//                }
//                R.id.personal_navigation -> {
//
//                    if (accessToken != null && !accessToken.isExpired) {
//                        supportFragmentManager.beginTransaction().replace(
//                            R.id.fragment_container, LoginFacbookProfileFragment()
//                        ).commit()
//                    } else {
//                        if (firebaseUser == null) {
//                            supportFragmentManager.beginTransaction().replace(
//                                R.id.fragment_container, ProfileFragment()
//                            ).commit()
//                        } else {
//                            supportFragmentManager.beginTransaction().replace(
//                                R.id.fragment_container, LoginProfileFragment()
//                            ).commit()
//                        }
//                    }
//
//                    true
//                }
//                else -> false
//            }
//        }
    }
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

}
