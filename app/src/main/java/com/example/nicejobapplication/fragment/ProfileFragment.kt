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
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.authentication.LoginSignup
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {
    private lateinit var btnAccountLogin: Button
    private lateinit var emailTest1: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false)
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        btnAccountLogin = view.findViewById(R.id.btn_Account_Login)
        emailTest1 = view.findViewById(R.id.emailTest1)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
//        checkUser()

        btnAccountLogin.setOnClickListener {
//            checkUser()
            val i = Intent(activity, LoginSignup::class.java)
            startActivity(i)
        }

        // Inflate the layout for this fragment
        return view
    }
        private fun checkUser() {
            //get current user
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser==null){
                //not logged in , user can stay in user dashboard without login too
                //chưa đăng nhập, người dùng cũng có thể ở trong bảng điều khiển người dùng mà không cần đăng nhập
                emailTest1.text = "Not logged In"
                btnAccountLogin.text = "Đăng nhập"
                btnAccountLogin.setOnClickListener {
                    checkUser()
                    val i = Intent(activity, LoginSignup::class.java)
                    startActivity(i)
                }

            }else{
                //logged in , get and show user info
                val email = firebaseUser.email
                emailTest1.text = email
                btnAccountLogin.text = "Đăng xuất"
                btnAccountLogin.setOnClickListener {
                    firebaseAuth.signOut()
                    startActivity(Intent(activity,MainActivity::class.java))
                }

            }
        }
    }