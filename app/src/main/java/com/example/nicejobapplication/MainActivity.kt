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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.corporationDetail -> {
                    bottomNavigation.visibility = View.GONE
                }
                R.id.jobDetail -> {
                    bottomNavigation.visibility = View.GONE
                }
                R.id.chooseCVApplication -> {
                    bottomNavigation.visibility = View.GONE
                }
                else -> {
                    bottomNavigation.visibility = View.VISIBLE
                }
            }
        }



    }

}
