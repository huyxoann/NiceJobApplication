package com.example.nicejobapplication.DetailFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.nicejobapplication.databinding.FragmentCvDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CvDetail : Fragment() {
    private lateinit var binding:FragmentCvDetailBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCvDetailBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()

        val bundle = arguments
        val documentID = bundle?.getString("cvName")
        val userEmail = auth.currentUser!!.email
        db = FirebaseFirestore.getInstance()
        db.collection("create_cv").document(userEmail!!).collection(userEmail).document(documentID!!)
            .get()
            .addOnSuccessListener { document ->
                val cvName = document.data?.get("cvName").toString()
                val avatar = document.data?.get("avatar").toString()
                val employerName = document.data?.get("employerName").toString()
                val jobPosition = document.data?.get("jobPosition").toString()
                val email = document.data?.get("email").toString()
                val phoneNumber = document.data?.get("phoneNumber").toString()
                val gentle = document.data?.get("gentle").toString()
                val address = document.data?.get("address").toString()
                val dayOfBirth = document.data?.get("dayOfBirth").toString()
                val careerGoal = document.data?.get("careerGoal").toString()
                val salary = document.data?.get("salary").toString()
                val introduceYourself = document.data?.get("introduceYourself").toString()
                val workExperience = document.data?.get("workExperience").toString()
                val academicLevel = document.data?.get("academicLevel").toString()

                binding.txtCvNameView.text = cvName
                Glide.with(requireActivity()).load(avatar).into(binding.avtViewCV)
                binding.txtViewNameEmployee.text = employerName
                binding.txtViewPosition.text = jobPosition
                binding.txtViewEmail.text = email
                binding.txtViewPhone.text = phoneNumber
                binding.txtViewGender.text = gentle
                binding.txtViewAddress.text = address
                binding.txtViewBirthday.text = dayOfBirth
                binding.txtViewCareerGoal.text = careerGoal
                binding.txtViewSalary.text = salary
                binding.txtViewIntroduce.text = introduceYourself
                binding.txtViewExp.text = workExperience
                binding.txtViewAcademicLevel.text = academicLevel
            }
            .addOnFailureListener {
                Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }

}