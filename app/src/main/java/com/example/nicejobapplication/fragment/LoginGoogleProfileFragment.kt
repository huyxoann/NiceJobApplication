package com.example.nicejobapplication.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentLoginGoogleProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class LoginGoogleProfileFragment : Fragment() {
    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    //init view Binding
    private lateinit var binding:FragmentLoginGoogleProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginGoogleProfileBinding.inflate(layoutInflater)

        //firebase
        auth = FirebaseAuth.getInstance()
        // Initialize firebase user
        val firebaseUser = auth.currentUser

        if (firebaseUser!=null){
            binding.txtNameGoogle.text = firebaseUser.displayName
            binding.txtEmailGoogle.text = firebaseUser.email

            Glide.with(requireActivity()).load(firebaseUser.photoUrl).into(binding.profileGoogleButton)
        }

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN)
        binding.btnGoogleLogout.setOnClickListener {
            // Sign out from google
            googleSignInClient.signOut().addOnCompleteListener { task ->
                // Check condition
                if (task.isSuccessful) {
                    // When task is successful sign out from firebase
                    auth.signOut()
                    // Display Toast
                    Toast.makeText(activity, "Logout successful", Toast.LENGTH_SHORT).show()
                    // Finish activity
                    startActivity(Intent(activity, MainActivity::class.java))
                }
            }
        }

        return binding.root
    }
}