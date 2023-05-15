package com.example.nicejobapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nicejobapplication.adapter.JobsSearchAdapter
import com.example.nicejobapplication.adapter.OnItemClickListener
import com.example.nicejobapplication.databinding.FragmentSearchViewJobBinding
import com.example.nicejobapplication.modal.Jobs
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class SearchViewJob : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentSearchViewJobBinding
    private lateinit var rvJobResult : RecyclerView
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchViewJobBinding.inflate(layoutInflater)

        binding.jobSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
//                    binding.jobResultList.visibility = View.VISIBLE
//                    binding.searchJobNotification.visibility = View.GONE
                    performSearch(newText)
                }else{
//                    binding.jobResultList.visibility = View.GONE
//                    binding.searchJobNotification.visibility = View.VISIBLE
                }
                return true
            }

        })
        navController = findNavController()
        binding.jobSearchView.setOnCloseListener {
            navController.popBackStack()
        }

        return binding.root
    }

    private fun performSearch(query: String) {
        // Thực hiện tìm kiếm dựa trên query
        // Ví dụ: truy vấn cơ sở dữ liệu, xử lý danh sách dữ liệu, vv.
        // Hiển thị kết quả tìm kiếm cho người dùng
        var jobItem =  arrayListOf<Jobs>()
        var searchResults = arrayListOf<Jobs>()

        val db = FirebaseFirestore.getInstance()
        db.collection("jobs").get().addOnSuccessListener {
            for (doc in it.documents){

                var job = Jobs(doc.id, doc["jobTitle"].toString(), doc["corpId"].toString())

                jobItem.add(job)
            }
            for (job in jobItem) {
                if(job.jobName.contains(query, ignoreCase = true)){
                    searchResults.add(job)
                }
            }

            Log.e("searchResults", searchResults.toString())
            Log.e("jobItem", jobItem.toString())
            showSearchResults(searchResults)
        }


    }

    private fun showSearchResults(arrayList: ArrayList<Jobs>) {
        // Hiển thị kết quả tìm kiếm cho người dùng
        // Ví dụ: Cập nhật RecyclerView hoặc ListView với danh sách kết quả

        // Ví dụ: Hiển thị danh sách kết quả trong RecyclerView
        val adapter = JobsSearchAdapter(arrayList, this)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.jobResultList.layoutManager = linearLayoutManager
        binding.jobResultList.adapter = adapter
    }
    override fun onItemClick(position: Int) {
    }

    override fun onItemClickUpdate(position: Int) {
        TODO("Not yet implemented")
    }

}