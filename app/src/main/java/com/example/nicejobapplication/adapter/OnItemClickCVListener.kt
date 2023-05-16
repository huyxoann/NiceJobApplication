package com.example.nicejobapplication.adapter

import com.example.nicejobapplication.modal.CV
import com.example.nicejobapplication.modal.Jobs

interface OnItemClickCVListener {
    fun onItemClick(position: Int, cvArrayList: ArrayList<CV>)
    fun onItemClickUpdate(position: Int)
}