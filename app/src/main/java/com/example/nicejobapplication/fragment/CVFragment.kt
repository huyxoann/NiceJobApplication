package com.example.nicejobapplication.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.nicejobapplication.R
import com.example.nicejobapplication.authentication.LoginSignup
import com.example.nicejobapplication.databinding.FragmentCvBinding
import com.google.firebase.auth.FirebaseAuth

class CVFragment : Fragment() {
    private lateinit var binding: FragmentCvBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
//open
//        return inflater.inflate(R.layout.fragment_cv, container, false)
//    }
//}
//close
        binding = FragmentCvBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        if (firebaseUser==null){
            binding.btnCreateCV.text = "Login"
            binding.btnCreateCV.setOnClickListener {
//                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//                transaction.replace( R.id.fragment_container, LoginTabFragment()
//                ).commit()
                startActivity(Intent(activity, LoginSignup::class.java))
            }
        }else{
            binding.txtTitleLogin.text = ""
            binding.btnCreateCV.setOnClickListener {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.replace( R.id.fragment_container, CreateCVFragment()
                ).commit()
            }
        }




        return binding.root
    }
}