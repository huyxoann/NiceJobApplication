package com.example.nicejobapplication.adapter

import com.example.nicejobapplication.modal.Jobs


interface OnItemClickListener {
    fun onItemClick(position: Int, jobsArrayList: ArrayList<Jobs>)
    fun onItemClickUpdate(position: Int)
}