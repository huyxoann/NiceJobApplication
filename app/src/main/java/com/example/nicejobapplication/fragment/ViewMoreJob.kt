package com.example.nicejobapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nicejobapplication.R
import com.example.nicejobapplication.adapter.JobsAdapter
import com.example.nicejobapplication.adapter.OnItemClickListener
import com.example.nicejobapplication.databinding.FragmentViewMoreJobBinding
import com.example.nicejobapplication.modal.Jobs
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ViewMoreJob : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentViewMoreJobBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var newestJobList: ArrayList<Jobs>
    private lateinit var internshipJobList: ArrayList<Jobs>
    private lateinit var navController: NavController

    private val currentTimestamp = Timestamp.now()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = findNavController()
        binding = FragmentViewMoreJobBinding.inflate(layoutInflater)
        val bundle = arguments
        if (bundle != null) {
            when(bundle.getInt("idGroup")){
                0 -> showNewestJob()
                1 -> showInternshipJob()

            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        return binding.root
    }

    private fun showInternshipJob() {
        db = FirebaseFirestore.getInstance()
        db.collection("jobs").where(
            Filter.or(
            Filter.equalTo("levelId", 8),
            Filter.equalTo("wayToWorkId", 3)
        )).limit(10).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                internshipJobList = ArrayList()
                for (document in task.result){
                    val jobId = document.id
                    val jobName = document["jobTitle"].toString()
                    val corpId = document["corpId"].toString()
                    val expId = document["expId"].toString().toInt()
                    val salaryId = document["salaryId"].toString().toInt()
                    val workAddress = arrayOf(document["workAddress"].toString().replace("[", "").replace("]", ""))
                    val deadline = document["deadline"]

                    val job = Jobs(jobId, jobName, corpId, expId, salaryId, workAddress,
                        deadline as Timestamp
                    )

                    internshipJobList.add(job)
                }
                Log.e("internshipJobList", internshipJobList.toString())
                binding.moreJobRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.moreJobRv
                    .adapter = JobsAdapter(this.requireContext(), internshipJobList, this)

            }
        }
    }

    private fun showNewestJob() {
        db = FirebaseFirestore.getInstance()
        db.collection("jobs").whereGreaterThan("deadline", currentTimestamp).orderBy("deadline", Query.Direction.DESCENDING).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                newestJobList = ArrayList()
                for (document in task.result){
                    val jobId = document.id
                    val jobName = document["jobTitle"].toString()
                    val corpId = document["corpId"].toString()
                    val expId = document["expId"].toString().toInt()
                    val salaryId = document["salaryId"].toString().toInt()
                    val workAddress = arrayOf(document["workAddress"].toString().replace("[", "").replace("]", ""))
                    val deadline = document["deadline"]

                    val job = Jobs(jobId, jobName, corpId, expId, salaryId, workAddress,
                        deadline as Timestamp
                    )
                    newestJobList.add(job)
                }
                binding.moreJobRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.moreJobRv.adapter = JobsAdapter(this.requireContext(), newestJobList, this)
            }
        }

    }

    override fun onItemClick(position: Int, jobsArrayList: ArrayList<Jobs>) {
        val bundle = bundleOf(
            "documentID" to jobsArrayList[position].jobID
        )

        navController.navigate(R.id.action_viewMoreJob_to_jobDetail, bundle)
    }

    override fun onItemClickUpdate(position: Int) {
        TODO("Not yet implemented")
    }

}