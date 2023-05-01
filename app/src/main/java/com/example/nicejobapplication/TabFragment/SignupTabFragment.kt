package com.example.nicejobapplication.TabFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.nicejobapplication.R
import com.example.nicejobapplication.authentication.LoginSignup
import com.example.nicejobapplication.authentication.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class SignupTabFragment : Fragment() {

    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var db: FirebaseFirestore

    private lateinit var btn_signup: Button
    private lateinit var signup_name: EditText
    private lateinit var signup_email: EditText
    private lateinit var signup_password: EditText
    private lateinit var signup_confirm_password: EditText

    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup_tab, container, false)

        //back end login & signup
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        db = FirebaseFirestore.getInstance()

        signup_name = view.findViewById(R.id.signup_name)
        signup_email = view.findViewById(R.id.signup_email)
        signup_password = view.findViewById(R.id.signup_password)
        signup_confirm_password = view.findViewById(R.id.signup_confirm_password)
        btn_signup = view.findViewById(R.id.btn_signup)


        btn_signup.setOnClickListener {
            ValidationInfor()
        }
        return view
    }

    private fun ValidationInfor() {
        val name = signup_name.text.toString()
        val email = signup_email.text.toString()
        val password = signup_password.text.toString()
        val confirm_password = signup_confirm_password.text.toString()
        //validation
        if (name.isEmpty() && email.isEmpty() && password.isEmpty() && confirm_password.isEmpty()) {
            if (name.isEmpty()) {
                signup_name.error = "Enter your name"
            }
            if (email.isEmpty()) {
                signup_email.error = "Enter your email"
            }
            if (password.isEmpty()) {
                signup_password.error = "Enter your password"
            }
            if (confirm_password.isEmpty()) {
                signup_confirm_password.error = "Re Enter your password"
            }
            Toast.makeText(activity,"Please enter valid details", Toast.LENGTH_SHORT).show()
        }else if (!email.matches(emailPattern.toRegex())){
            signup_email.error = "Enter valid email address"
            Toast.makeText(
                activity,
                "Please enter valid email address",
                Toast.LENGTH_SHORT
            ).show()
        }else if (password.length <6){
            signup_password.error = "Enter password more than 6 characters"
            Toast.makeText(
                activity,
                "Please enter password more than 6 characters",
                Toast.LENGTH_SHORT
            ).show()
        }else if (password!=confirm_password){
            signup_confirm_password.error = "Password not matched, try again"
            Toast.makeText(
                activity,
                "Password not matched, try again",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            //call createUserWithEmailAndPassword using auth object and pass the email and pass in it.
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    //signup success -> email verification
                      auth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {

                            val userId = auth.currentUser!!.uid
                            val users = Users(userId,name,email, password)

                            db.collection("users").document(userId).set(users).addOnCompleteListener {
                                if (it.isSuccessful){
                                    //chuyển đến fragment login
                                    val i = Intent(activity, LoginSignup::class.java)
                                    startActivity(i)
                                    Toast.makeText(activity,"Signup successfully, Please verify your Email !",Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(activity,"Something went wrong, try again",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(activity,it.toString(),Toast.LENGTH_SHORT).show()
                        }
                }else{
                    //failed
                    Toast.makeText(activity,"Something went wrong, try again",Toast.LENGTH_SHORT).show()
                    //fix bug
//                        Toast.makeText(activity,it.exception.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}