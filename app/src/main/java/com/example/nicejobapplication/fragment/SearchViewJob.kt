package com.example.nicejobapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nicejobapplication.adapter.JobsSearchAdapter
import com.example.nicejobapplication.databinding.FragmentSearchViewJobBinding
import com.example.nicejobapplication.modal.Jobs
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class SearchViewJob : Fragment() {
    private lateinit var binding: FragmentSearchViewJobBinding
    private lateinit var rvJobResult : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchViewJobBinding.inflate(layoutInflater)

        return binding.root
    }

    private fun getDataJob(db: FirebaseFirestore):ArrayList<Jobs>{
        var jobsArrayList = ArrayList<Jobs>()
        db.collection("jobs").get().addOnSuccessListener {
            for (doc in it){
                val job = Jobs(doc.id, doc["jobTitle"].toString(), doc["corpId"].toString(), 0, 0, null, 0, 0, 0, 0, 0, 0, Timestamp.now(), null, null, null, null)
                jobsArrayList.add(job)
            }

        }
        return jobsArrayList
    }

}