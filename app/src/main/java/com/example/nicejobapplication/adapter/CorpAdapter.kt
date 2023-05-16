package com.example.nicejobapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nicejobapplication.R
import com.example.nicejobapplication.modal.Corporation
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CorpAdapter(private val context: Context, private val corpArrayList: ArrayList<Corporation>, private val listener: OnClickCorpListener): RecyclerView.Adapter<CorpAdapter.CorpViewHolder>() {

    inner class CorpViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val corpTitle: TextView =
            itemView.findViewById(R.id.corpTitle)
        val corpDes: TextView =
            itemView.findViewById(R.id.corpDes)
        val corpLogo: ImageView =
            itemView.findViewById(R.id.corpImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorpViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.corp_item, parent, false)

        return CorpViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = corpArrayList.size

    override fun onBindViewHolder(holder: CorpViewHolder, position: Int) {
        val item: Corporation = corpArrayList[position]
        holder.corpTitle.text = item.corpName
        holder.corpDes.text = item.corpDescription

        val storageRef = Firebase.storage.getReferenceFromUrl(item.corpLogo)
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(holder.itemView.context)
                .load(uri)
                .into(holder.corpLogo)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position, corpArrayList)
        }

    }
}