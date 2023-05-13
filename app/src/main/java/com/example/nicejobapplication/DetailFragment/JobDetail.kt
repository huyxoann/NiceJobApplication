package com.example.nicejobapplication.DetailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentJobDetailBinding
import com.example.nicejobapplication.modal.Employee
import com.example.nicejobapplication.modal.Jobs
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class JobDetail : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: FragmentJobDetailBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var bundleCorp : Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navController = findNavController()
        binding = FragmentJobDetailBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        val userEmail = auth.currentUser!!.email



        val bundle = arguments
        val documentID = bundle?.getString("documentID")
        if (documentID != null) {

            //Kiểm tra xem đã nộp CV cho công việc này hay chưa
            isApplied(userEmail!!, documentID)

            getDataFromFireStore(documentID, binding)
            binding.extendedFab.setOnClickListener {
                navController.navigate(R.id.action_jobDetail_to_chooseCVApplication, bundle)
            }

            //Set sự kiện khi nhấn vào logo corp và tên corp
            binding.corpLogo.setOnClickListener {
                navController.navigate(R.id.action_jobDetail_to_corporationDetail, bundleCorp)
            }
            binding.corpName.isClickable = true
            binding.corpName.setOnClickListener {
                navController.navigate(R.id.action_jobDetail_to_corporationDetail, bundleCorp)
            }

        }else{
            Snackbar.make(binding.root, "Không tìm thấy thông tin người dùng", Snackbar.LENGTH_LONG)
                .show()
        }

        //Set sự kiện cho nút back
        binding.topAppBar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        return binding.root
    }

    private fun isApplied(employeeEmail: String, jobId: String){
        db = FirebaseFirestore.getInstance()
        val search = db.collection("applications").whereEqualTo("employeeId", employeeEmail).whereEqualTo("jobId", jobId).get()
        search.addOnSuccessListener {
            binding.extendedFab.isEnabled = it.isEmpty
        }
    }

    private fun getDataFromFireStore(documentID: String, binding: FragmentJobDetailBinding) {
        db = FirebaseFirestore.getInstance()
        db.collection("jobs").document(documentID).get().addOnSuccessListener {
            val job = Jobs(
                it.id,
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
            binding.address.text = job.workAddress?.let { it1 -> job.displayWorkAddress(it1) }
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

            bundleCorp = bundleOf(
                "documentID" to corpId
            )
        }

    }



}