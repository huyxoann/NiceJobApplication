package com.example.nicejobapplication.adapter

import com.example.nicejobapplication.modal.Corporation
import com.example.nicejobapplication.modal.Jobs

interface OnClickCorpListener {
    fun onItemClick(position: Int, jobsArrayList: ArrayList<Corporation>)
    fun onItemClickUpdate(position: Int)
}