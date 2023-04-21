package com.example.nicejobapplication.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentJobsBinding
import com.example.nicejobapplication.databinding.FragmentLoginTabBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class JobsFragment : Fragment() {
    private lateinit var binding:FragmentJobsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentJobsBinding.inflate(layoutInflater)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        dbRef = Firebase.database.reference

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser==null){
            //not logged in , user can stay in user dashboard without login too
            //chưa đăng nhập, người dùng cũng có thể ở trong bảng điều khiển người dùng mà không cần đăng nhập
            binding.txtNameHome.text = "Not logged In"
        }else{
            //logged in , get and show user info
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            dbRef.child("users").child(userId).get().addOnSuccessListener {

                val img = it.child("imgUrl").value.toString()

                val name = it.child("name").value.toString()

                binding.txtNameHome.text = "Xin chào $name"

                Glide.with(requireActivity()).load(img).into(binding.profileButtonHome)

            }.addOnFailureListener {
                Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }

}