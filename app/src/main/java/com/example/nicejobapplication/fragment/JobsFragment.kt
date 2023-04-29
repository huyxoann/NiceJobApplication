package com.example.nicejobapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nicejobapplication.R
import com.example.nicejobapplication.adapter.CorpAdapter
import com.example.nicejobapplication.adapter.JobsAdapter
import com.example.nicejobapplication.adapter.OnItemClickListener
import com.example.nicejobapplication.databinding.FragmentJobsBinding
import com.example.nicejobapplication.modal.Jobs
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.Arrays

class JobsFragment : Fragment(), OnItemClickListener {
    private lateinit var binding:FragmentJobsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var rvJobs: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobsBinding.inflate(layoutInflater)

        //init firebase Auth
        auth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        dbRef = Firebase.database.reference
        storage = FirebaseStorage.getInstance()

        val firebaseUser = auth.currentUser

        //test view profile fb
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken!=null && !accessToken.isExpired){

            val request = GraphRequest.newMeRequest(
                accessToken) { jsonObject, response -> // Getting FB User Data
                val name = jsonObject?.getString("name")
                val profileUrl = jsonObject?.getJSONObject("picture")
                    ?.getJSONObject("data")?.getString("url")

                binding.txtNameHome.text = name

                Picasso.get().load(profileUrl).into(binding.profileButtonHome)

            }

            val parameters = Bundle()
            parameters.putString("fields","id,name,link,picture.type(large),email")
            request.parameters = parameters
            request.executeAsync()

        }else{

            if (firebaseUser!=null){

                //logged in , get and show user info
                val userId = firebaseUser!!.uid
                dbRef.child("users").child(userId).get().addOnSuccessListener {

                    val name = it.child("name").value.toString()
                    //here
                    val img = it.child("imgUrl").value.toString()

                    binding.txtNameHome.text = "Xin chào $name"

                    //view image
                    activity?.let { it1 -> Glide.with(it1).load(img).into(binding.profileButtonHome) }

                }.addOnFailureListener {
                    Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
                }

            }else{
//                not logged in , user can stay in user dashboard without login too
//                chưa đăng nhập, người dùng cũng có thể ở trong bảng điều khiển người dùng mà không cần đăng nhập
                binding.txtNameHome.text = "Not logged In"
            }
        }
        val newestJobList = ArrayList<Jobs>()
        val address = arrayOf("HaNoi", "Hue")
        var job = Jobs("daklsdjklasjd", "Tên Công việc 1", "đâsdasdas", 1,1, address)
        var job2 = Jobs("daklsdjklasjd", "Tên Công việc 2", "đâsdasdas", 1,1, address)
        newestJobList.add(job)
        newestJobList.add(job2)
        val db = FirebaseFirestore.getInstance()
        rvJobs = binding.rvNewestJob
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvJobs.layoutManager = linearLayoutManager
        rvJobs.adapter = JobsAdapter(this.requireContext(), newestJobList, this)



        return binding.root
    }


    fun getExp(exp: Int): String{
        return when(exp){
            1 -> "Mới ra trường"
            2 -> "1-2 năm"
            3 -> "3-4 năm"
            4 -> "5 năm trở lên"
            else -> "Không yêu cầu"
        }
    }

    fun getSalary(exp: Int): String{
        return when(exp){
            1 -> "Dưới 3 triệu"
            2 -> "3-5 triệu"
            3 -> "5-7 triệu"
            4 -> "7-10 triệu"
            5 -> "10-12 triệu"
            6 -> "12-15 triệu"
            7 -> "15-20 triệu"
            8 -> "20-25 triệu"
            9 -> "25-30 triệu"
            10 -> "Trên 30 triệu"
            else -> "Thỏa thuân"
        }
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }


}