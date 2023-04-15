package com.example.nicejobapplication.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentLoginProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginProfileFragment : Fragment() {
    private lateinit var binding:FragmentLoginProfileBinding
    private lateinit var dbRef:DatabaseReference

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginProfileBinding.inflate(layoutInflater)

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, MainActivity::class.java))
        }

        dbRef = Firebase.database.reference

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        dbRef.child("users").child(userId).get().addOnSuccessListener {

            val name = it.child("name").value.toString()
            val email = it.child("email").value.toString()

            binding.txtName.text = name
            binding.txtEmail.text = "Email:$email"

        }.addOnFailureListener {
            Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}