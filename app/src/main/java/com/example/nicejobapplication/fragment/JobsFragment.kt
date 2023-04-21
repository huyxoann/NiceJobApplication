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
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class JobsFragment : Fragment() {
    private lateinit var binding:FragmentJobsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobsBinding.inflate(layoutInflater)

        //init firebase Auth
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        dbRef = Firebase.database.reference
        storage = FirebaseStorage.getInstance()

        val firebaseUser = auth.currentUser

        //test view profile fb
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken!=null && !accessToken.isExpired){

            val request = GraphRequest.newMeRequest(
                accessToken) { jsonObject, response -> // Getting FB User Data
                val name = jsonObject?.getString("name")
                val profileUrl = jsonObject?.getJSONObject("picture")
                    ?.getJSONObject("data")?.getString("url")

                binding.txtNameHome.text = name

                Picasso.get().load(profileUrl).into(binding.profileButtonHome)

            }

            val parameters = Bundle()
            parameters.putString("fields","id,name,link,picture.type(large),email")
            request.parameters = parameters
            request.executeAsync()

        }else{

            if (firebaseUser!=null){

                //logged in , get and show user info
                val userId = firebaseUser.uid
                dbRef.child("users").child(userId).get().addOnSuccessListener {

                    val name = it.child("name").value.toString()
                    //here
                    val img = it.child("imgUrl").value.toString()

                    binding.txtNameHome.text = "Xin chào $name"

                    //view image
                    activity?.let { it1 -> Glide.with(it1).load(img).into(binding.profileButtonHome) }

                }.addOnFailureListener {
                    Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
                }

            }else{
                //not logged in , user can stay in user dashboard without login too
                //chưa đăng nhập, người dùng cũng có thể ở trong bảng điều khiển người dùng mà không cần đăng nhập
                binding.txtNameHome.text = "Not logged In"
            }
        }


        return binding.root
    }

}