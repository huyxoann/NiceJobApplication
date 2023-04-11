package com.example.nicejobapplication.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.nicejobapplication.R
import com.example.nicejobapplication.authentication.LoginSignup


class ProfileFragment : Fragment() {
    private lateinit var btnAccountLogin: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false)
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        btnAccountLogin = view.findViewById(R.id.btn_Account_Login)
        btnAccountLogin.setOnClickListener {
            val i = Intent(activity, LoginSignup::class.java)
            startActivity(i)
        }
        // Inflate the layout for this fragment
        return view
    }
}