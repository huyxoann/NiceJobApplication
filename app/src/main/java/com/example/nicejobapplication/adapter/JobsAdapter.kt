package com.example.nicejobapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nicejobapplication.R
import com.example.nicejobapplication.modal.Jobs
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.collections.ArrayList

class JobsAdapter(private val context: Context, private val jobsArrayList: ArrayList<Jobs>, private val listener: OnItemClickListener):RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {
    inner class JobsViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
            val jobName: TextView = itemView.findViewById(R.id.jobName)
            val corpLogo: ImageButton = itemView.findViewById(R.id.corpLogo)
            val corpName: TextView = itemView.findViewById(R.id.corpName)
            val address: TextView = itemView.findViewById(R.id.address)
            val exp: TextView = itemView.findViewById(R.id.exp)
            val salary: TextView = itemView.findViewById(R.id.salary)
            val deadline: TextView = itemView.findViewById(R.id.deadline)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.jobs_item, parent, false)
        return JobsViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = jobsArrayList.size

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        val item = jobsArrayList[position]
        holder.jobName.text = item.jobName

        val corpID = item.corpID
        var db = FirebaseFirestore.getInstance()

        db.collection("corporations").document(corpID).get().addOnSuccessListener {
            val urlImage = "gs://nicejob-367709.appspot.com/corporation_image/"+it["corpLogo"].toString()
            val storageRef = Firebase.storage.getReferenceFromUrl(urlImage)
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(holder.itemView.context)
                    .load(uri)
                    .into(holder.corpLogo)
            }
        }
        db.collection("corporations").document(corpID).get().addOnSuccessListener {
            holder.corpName.text = it["corpName"].toString()
        }
        holder.address.text = item.workAddress?.get(0)?.let { item.getAddress(it) }
        holder.exp.text = item.getExp(item.expId)
        holder.salary.text = item.getSalary(item.salaryId)
        holder.deadline.text = "Còn "+ item.getDeadline(item.expertDay as Timestamp).toInt().toString() + " ngày để ứng tuyển"

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }



}

