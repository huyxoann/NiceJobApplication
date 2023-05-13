package com.example.nicejobapplication.TabFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nicejobapplication.R
import com.example.nicejobapplication.adapter.JobsAdapter
import com.example.nicejobapplication.adapter.OnItemClickListener
import com.example.nicejobapplication.databinding.FragmentJobsCorpListBinding
import com.example.nicejobapplication.modal.Jobs
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class JobsCorpListFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentJobsCorpListBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var newestJobList: ArrayList<Jobs>
    private lateinit var rvJobs: RecyclerView
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobsCorpListBinding.inflate(layoutInflater)

        db = FirebaseFirestore.getInstance()

        db.collection("jobs").limit(10).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                newestJobList = ArrayList()
                for (document in task.result){
                    val jobId = document.id
                    val jobName = document["jobTitle"].toString()
                    val corpId = document["corpId"].toString()
                    val expId = document["expId"].toString().toInt()
                    val salaryId = document["salaryId"].toString().toInt()
                    val workAddress = arrayOf(document["workAddress"].toString())
                    val deadline = document["deadline"]

                    var job = Jobs(jobId, jobName, corpId, expId, salaryId, workAddress,
                        deadline as Timestamp
                    )

                    newestJobList.add(job)
                }
                rvJobs = binding.jobList
                val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                rvJobs.layoutManager = linearLayoutManager
                rvJobs.adapter = JobsAdapter(this.requireContext(), newestJobList, this)
            }
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {
        bundle = bundleOf(
            "documentID" to newestJobList[position].jobID
        )

        navController = findNavController()

        navController.navigate(R.id.action_jobsFragment_to_jobDetail, bundle)
    }

    override fun onItemClickUpdate(position: Int) {
        TODO("Not yet implemented")
    }

}