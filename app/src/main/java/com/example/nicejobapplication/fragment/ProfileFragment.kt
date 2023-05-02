package com.example.nicejobapplication.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.authentication.LoginSignup
import com.example.nicejobapplication.databinding.FragmentCvBinding
import com.example.nicejobapplication.databinding.FragmentProfileBinding
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private val accessToken = AccessToken.getCurrentAccessToken()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        if (accessToken!=null && !accessToken.isExpired){
//
//            //login fb -> view profile fb
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace( R.id.fragment_container, LoginFacbookProfileFragment()
            ).commit()
        }else {
            // not login
            if (firebaseUser==null){
//                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//                    transaction.replace( R.id.fragment_container, ProfileFragment()
//                    ).commit()
                binding.btnAccountLogin.setOnClickListener {
//            checkUsers()
                    val i = Intent(activity, LoginSignup::class.java)
                    startActivity(i)
                }
                //login with email and pass
            }else{
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.replace( R.id.fragment_container, LoginProfileFragment()
                ).commit()

            }
        }

        return binding.root
    }

    }