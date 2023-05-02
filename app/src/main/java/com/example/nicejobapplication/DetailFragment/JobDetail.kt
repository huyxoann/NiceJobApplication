package com.example.nicejobapplication.DetailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nicejobapplication.databinding.FragmentJobDetailBinding
import com.example.nicejobapplication.modal.Jobs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class JobDetail : Fragment() {
    private lateinit var binding: FragmentJobDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobDetailBinding.inflate(layoutInflater)

        val bundle = arguments
        val documentID = bundle?.getString("documentID")
        if (documentID != null) {
            getDataFromFireStore(documentID, binding)
        }else{
            Snackbar.make(binding.root, "Không tìm thấy thông tin người dùng", Snackbar.LENGTH_SHORT)
                .show()
        }

//        inflater.inflate(R.layout.fragment_job_detail, container, false)
        return binding.root
    }

    private fun getDataFromFireStore(documentID: String, binding: FragmentJobDetailBinding) {
        val db = FirebaseFirestore.getInstance()
        db.collection("jobs").document(documentID).get().addOnSuccessListener {
            val job = Jobs(
                it.data.toString(),
                it["jobTitle"].toString(),
                it["corpId"].toString(),
                it["numOfRecruit"].toString().toInt(),
                it["genderJob"].toString().toInt(),
                arrayOf(it["workAddress"].toString()),
                0,
                it["expId"].toString().toInt(),
                it["wayToWorkId"].toString().toInt(),
                it["salaryId"].toString().toInt(),
                it["levelId"].toString().toInt(),
                it["state"].toString().toInt(),
                Timestamp.now(),
                it["deadline"] as Timestamp?,
                arrayOf(it["jobDescription"].toString()),
                arrayOf(it["recruitRequire"].toString()),
                arrayOf(it["benefit"].toString())
            )
            binding.jobName.text = job.jobName
            binding.salary.text = job.getSalary(job.salaryId)
            binding.wayToWork.text = job.getWayToWork(job.wayToWorkId)
            binding.numOfRecruit.text = job.numOfRecruit.toString()
            binding.genderRequire.text = job.getGenderRequire(job.genderJob)
            binding.exp.text = job.getExp(job.expId)
            binding.level.text = job.getLevel(job.levelId)
            binding.address.text = job.displayWorkAddress(job.workAddress)
            binding.jobDescription.text = job.displayJobDescription(job.jobDescription)
            binding.recruitRequire.text = job.displayRecruitRequire(job.recruitRequire)
            binding.benefit.text = job.displayBenefit(job.benefit)


            val corpId = it["corpId"].toString()
            db.collection("corporations").document(corpId).get().addOnSuccessListener { corp ->
                val corpLogoUrl = corp["corpLogo"].toString()
                binding.corpName.text = corp["corpName"].toString()
                val urlImage = "gs://nicejob-367709.appspot.com/corporation_image/$corpLogoUrl"
                val storageRef = Firebase.storage.getReferenceFromUrl(urlImage)
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(binding.corpLogo)
                }
            }
        }

    }



}