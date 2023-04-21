package com.example.nicejobapplication.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.ActivityMainBinding
import com.example.nicejobapplication.databinding.FragmentLoginFacbookProfileBinding
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class LoginFacbookProfileFragment : Fragment() {
    lateinit var binding: FragmentLoginFacbookProfileBinding

    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginFacbookProfileBinding.inflate(layoutInflater)

//        binding.txtNameFB.setText(auth.currentUser?.displayName)
//        Picasso.get().load(auth.currentUser?.photoUrl).into(binding.profileFBButton)

        val accessToken = AccessToken.getCurrentAccessToken()

        val request = GraphRequest.newMeRequest(
            accessToken) { jsonObject, response -> // Getting FB User Data
            val email = jsonObject?.getString("email")
            val name = jsonObject?.getString("name")
            val profileUrl = jsonObject?.getJSONObject("picture")
                ?.getJSONObject("data")?.getString("url")

            binding.txtNameFB.text = name
            binding.txtEmailFB.text = email
//            Glide.with(applicationContext).
            Picasso.get().load(profileUrl).into(binding.profileFBButton)
        }

        val parameters = Bundle()
        parameters.putString("fields","id,name,link,picture.type(large),email")
        request.parameters = parameters
        request.executeAsync()

        //logout
        binding.btnFBLogout.setOnClickListener {
            LoginManager.getInstance().logOut()
            startActivity(Intent(activity,MainActivity::class.java))
        }

        return binding.root
    }


}