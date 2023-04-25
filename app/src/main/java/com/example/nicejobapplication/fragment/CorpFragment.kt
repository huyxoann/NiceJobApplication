package com.example.nicejobapplication.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nicejobapplication.DetailFragment.CorporationDetail
import com.example.nicejobapplication.R
import com.example.nicejobapplication.adapter.CorpAdapter
import com.example.nicejobapplication.adapter.OnItemClickListener
import com.example.nicejobapplication.modal.Corporation
import com.google.firebase.firestore.FirebaseFirestore

class CorpFragment : Fragment(), OnItemClickListener {

    private lateinit var rvCorporation: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle
    private lateinit var corpArrayList: ArrayList<Corporation>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_corp, container, false)


        //Kiểm tra kết nối Internet
        if(isNetworkConnected(requireContext())){
            db = FirebaseFirestore.getInstance()
            db.collection("corporations")
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        corpArrayList = ArrayList()
                        for (document in it.result){
                            var corpID = document.id
                            var corpName = document.data["corpName"].toString()
                            var corpDescription = document.data["corpDescription"].toString()
                            var corpLogo = "gs://nicejob-367709.appspot.com/corporation_image/" + document.data["corpLogo"].toString()
                            var corp = Corporation(corpID = corpID, corpName = corpName, corpDescription = corpDescription, corpLogo = corpLogo)
                            corpArrayList.add(corp)
                        }


                        rvCorporation = view.findViewById(R.id.rvCorp)
                        val linearLayoutManager = LinearLayoutManager(requireContext())
                        rvCorporation.layoutManager = linearLayoutManager
                        rvCorporation.adapter = CorpAdapter(this.requireContext(), corpArrayList, this)
                    }else{
                    }
                }

        }else{

        }
        return view
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    override fun onItemClick(position: Int) {

        bundle = bundleOf(
            "documentID" to corpArrayList[position].corpID
        )


        navController = findNavController()

        navController.navigate(R.id.action_corpFragment_to_corporationDetail, bundle)

    }
}