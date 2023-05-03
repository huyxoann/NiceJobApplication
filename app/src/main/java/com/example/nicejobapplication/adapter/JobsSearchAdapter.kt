package com.example.nicejobapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nicejobapplication.R
import com.example.nicejobapplication.fragment.SearchViewJob
import com.example.nicejobapplication.modal.Jobs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class JobsSearchAdapter(private val jobArrayList: ArrayList<Jobs>, private val listener: SearchViewJob):RecyclerView.Adapter<JobsSearchAdapter.JobsSearchViewHolder>() {

    inner class JobsSearchViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val logo: ImageView = itemView.findViewById(R.id.logoSearch)
        val jobName: TextView = itemView.findViewById(R.id.jobNameSearch)
        val corpName: TextView = itemView.findViewById(R.id.corpNameSearch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsSearchViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.jobs_search_item, parent, false)
        return JobsSearchViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = jobArrayList.size

    override fun onBindViewHolder(holder: JobsSearchViewHolder, position: Int) {
        val item = jobArrayList[position]
        val corpID = item.corpID
        var db = FirebaseFirestore.getInstance()

        holder.jobName.text = item.jobName

        db.collection("corporations").document(corpID).get().addOnSuccessListener {
            holder.corpName.text = it["corpName"].toString()
        }

        db.collection("corporations").document(corpID).get().addOnSuccessListener {
            val urlImage = "gs://nicejob-367709.appspot.com/corporation_image/"+it["corpLogo"].toString()
            val storageRef = Firebase.storage.getReferenceFromUrl(urlImage)
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(holder.itemView.context)
                    .load(uri)
                    .into(holder.logo)
            }
        }
    }
}