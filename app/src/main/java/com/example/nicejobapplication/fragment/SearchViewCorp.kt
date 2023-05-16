package com.example.nicejobapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nicejobapplication.R
import com.example.nicejobapplication.adapter.CorpAdapter
import com.example.nicejobapplication.adapter.JobsSearchAdapter
import com.example.nicejobapplication.adapter.OnClickCorpListener
import com.example.nicejobapplication.databinding.FragmentCorpBinding
import com.example.nicejobapplication.databinding.FragmentSearchViewCorpBinding
import com.example.nicejobapplication.modal.Corporation
import com.example.nicejobapplication.modal.Jobs
import com.google.firebase.firestore.FirebaseFirestore

class SearchViewCorp : Fragment(), OnClickCorpListener {
    lateinit var binding: FragmentSearchViewCorpBinding
    private lateinit var searchResults: ArrayList<Corporation>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchViewCorpBinding.inflate(layoutInflater)

        binding.corpSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    performSearch(newText)
                }else{
                }
                return true
            }

        })

        return binding.root

    }

    private fun performSearch(query: String) {
        var corpItem =  arrayListOf<Corporation>()
        searchResults = arrayListOf()

        val db = FirebaseFirestore.getInstance()
        db.collection("corporations").get().addOnSuccessListener {
            for (doc in it.documents){

                var corp = Corporation(doc.id, doc["corpName"].toString(), doc["corpDescription"].toString(), "gs://nicejob-367709.appspot.com/corporation_image/"+doc["corpLogo"].toString())

                corpItem.add(corp)
            }
            for (corp in corpItem) {
                if(corp.corpName.contains(query, ignoreCase = true)){
                    searchResults.add(corp)
                }
            }

            showSearchResults(searchResults)
        }
    }
    private fun showSearchResults(arrayList: ArrayList<Corporation>) {
        // Hiển thị kết quả tìm kiếm cho người dùng
        // Ví dụ: Cập nhật RecyclerView hoặc ListView với danh sách kết quả
        // Ví dụ: Hiển thị danh sách kết quả trong RecyclerView
        val adapter = CorpAdapter(requireContext(), arrayList, this)
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.corpSearchResult.layoutManager = linearLayoutManager
        binding.corpSearchResult.adapter = adapter
    }

    override fun onItemClick(position: Int, jobsArrayList: ArrayList<Corporation>) {
        TODO("Not yet implemented")
    }

    override fun onItemClickUpdate(position: Int) {
        TODO("Not yet implemented")
    }

}