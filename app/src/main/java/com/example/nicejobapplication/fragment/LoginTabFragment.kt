package com.example.nicejobapplication.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.authentication.LoginSignup
import com.example.nicejobapplication.authentication.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class LoginTabFragment : Fragment() {
    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var btn_login: Button
    private lateinit var login_email: EditText
    private lateinit var login_password: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_tab, container, false)

        login_email = view.findViewById(R.id.login_email)
        login_password = view.findViewById(R.id.login_password)
        btn_login = view.findViewById(R.id.btn_login)

        btn_login.setOnClickListener {
            ValidationInfor()
        }


        return view
    }

    private fun ValidationInfor() {
        val email = login_email.text.toString()
        val password = login_password.text.toString()

        //validation
        if ( email.isEmpty() && password.isEmpty() ) {
            if (email.isEmpty()) {
                login_email.error = "Enter your email address"
            }
            if (password.isEmpty()) {
                login_password.error = "Enter your password"
            }
            Toast.makeText(activity,"Please enter valid details", Toast.LENGTH_SHORT).show()
        }else if (password.length <6){
            login_password.error = "Enter password more than 6 characters"
            Toast.makeText(
                activity,
                "Please enter password more than 6 characters",
                Toast.LENGTH_SHORT
            ).show()
        } else{
            // calling signInWithEmailAndPassword(email, pass) function using Firebase auth object
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                        if (it.isSuccessful){
                            //chuyển đến fragment main
                            val i = Intent(activity, MainActivity::class.java)
                            startActivity(i)
                            Toast.makeText(activity,"Login successfully !", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(activity,"Something went wrong, try again", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
