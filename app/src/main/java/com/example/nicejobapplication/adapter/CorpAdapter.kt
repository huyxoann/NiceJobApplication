package com.example.nicejobapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nicejobapplication.R
import com.example.nicejobapplication.modal.Corporation

class CorpAdapter(private val context: Context, private val corpArrayList: ArrayList<Corporation>): RecyclerView.Adapter<CorpAdapter.CorpViewHolder>() {

    inner class CorpViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val corpTitle: TextView =
            itemView.findViewById(R.id.corpTitle)
         val corpDes: TextView =
            itemView.findViewById(R.id.corpDes)
        fun bind(corp: Corporation) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorpViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.corp_item, parent, false)
        return CorpViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = corpArrayList.size

    override fun onBindViewHolder(holder: CorpViewHolder, position: Int) {
        val item: Corporation = corpArrayList[position]
        holder.corpTitle.text = item.corpName
        holder.corpDes.text = item.corpDescription
    }
}