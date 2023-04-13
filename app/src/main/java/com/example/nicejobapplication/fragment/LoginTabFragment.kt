package com.example.nicejobapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentLoginTabBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class LoginTabFragment : Fragment() {
    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginTabBinding

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private lateinit var mGoogleSignInClient: GoogleSignIn
    private lateinit var gso: GoogleSignInOptions


    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginTabBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()









        //Hiện đăng nhập tự điền nếu người dùng đã đang nhập
        binding.loginPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)




        binding.btnLogin.setOnClickListener {
            ValidationInfor()
        }

        //Đăng nhập Google bằng Google OneTap
        binding.btnGoogle.setOnClickListener{
        }


        return binding.root
    }


    private fun ValidationInfor() {
        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        //validation
        if ( email.isEmpty() && password.isEmpty() ) {
            if (email.isEmpty()) {
                binding.loginEmail.error = "Enter your email address"
            }
            if (password.isEmpty()) {
                binding.loginPassword.error = "Enter your password"
            }
            Toast.makeText(activity,"Please enter valid details", Toast.LENGTH_SHORT).show()
        }else if (!email.matches(emailPattern.toRegex())){
            binding.loginEmail.error = "Enter valid email address"
            Toast.makeText(
                activity,
                "Please enter valid email address",
                Toast.LENGTH_SHORT
            ).show()
        }else if (password.length <6){
            binding.loginPassword.error = "Enter password more than 6 characters"
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