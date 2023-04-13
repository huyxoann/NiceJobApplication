package com.example.nicejobapplication.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.google.firebase.auth.FirebaseAuth

class JobsFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var emailTest: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_jobs, container, false)

        emailTest = view.findViewById(R.id.emailTest)

            //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        return view
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser==null){
            //not logged in , user can stay in user dashboard without login too
            //chưa đăng nhập, người dùng cũng có thể ở trong bảng điều khiển người dùng mà không cần đăng nhập
            emailTest.text = "Not logged In"
        }else{
            //logged in , get and show user info
            val email = firebaseUser.email
            emailTest.text = email

        }
    }
}