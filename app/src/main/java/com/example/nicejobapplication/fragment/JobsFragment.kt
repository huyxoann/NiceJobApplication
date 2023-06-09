package com.example.nicejobapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nicejobapplication.R
import com.example.nicejobapplication.adapter.JobsAdapter
import com.example.nicejobapplication.adapter.OnItemClickListener
import com.example.nicejobapplication.databinding.FragmentJobsBinding
import com.example.nicejobapplication.modal.Jobs
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JobsFragment : Fragment(), OnItemClickListener {
    private lateinit var binding:FragmentJobsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var rvJobs: RecyclerView
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle
    private lateinit var newestJobList: ArrayList<Jobs>
    private lateinit var internshipJobList: ArrayList<Jobs>
    private lateinit var db: FirebaseFirestore

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
        db = FirebaseFirestore.getInstance()
        val firebaseUser = auth.currentUser
        navController = findNavController()


        //test view profile fb
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken!=null && !accessToken.isExpired){

            val request = GraphRequest.newMeRequest(
                accessToken) { jsonObject, _ -> // Getting FB User Data
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
                val userId = firebaseUser.uid
                db.collection("users").document(userId).get().addOnSuccessListener {

                    val name = it.data?.get("name").toString()
                    val img =  it.data?.get("img").toString()

                    binding.txtNameHome.text = "Xin chào, $name"

                    //view image
                    activity?.let { it1 -> Glide.with(it1).load(img).into(binding.profileButtonHome) }

                }.addOnFailureListener {
                    Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
                }

            }else{
//                not logged in , user can stay in user dashboard without login too
//                chưa đăng nhập, người dùng cũng có thể ở trong bảng điều khiển người dùng mà không cần đăng nhập
                binding.txtNameHome.text = "Chưa đăng nhập"
            }
        }

        val currentTimestamp = Timestamp.now()


        db.collection("jobs").whereGreaterThan("deadline", currentTimestamp).orderBy("deadline", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                newestJobList = ArrayList()
                for (document in task.result){
                    val jobId = document.id
                    val jobName = document["jobTitle"].toString()
                    val corpId = document["corpId"].toString()
                    val expId = document["expId"].toString().toInt()
                    val salaryId = document["salaryId"].toString().toInt()
                    val workAddress = arrayOf(document["workAddress"].toString().replace("[", "").replace("]", ""))
                    val deadline = document["deadline"]

                    val job = Jobs(jobId, jobName, corpId, expId, salaryId, workAddress,
                        deadline as Timestamp
                    )
                    newestJobList.add(job)
                }
                rvJobs = binding.rvNewestJob
                val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                rvJobs.layoutManager = linearLayoutManager
                rvJobs.adapter = JobsAdapter(this.requireContext(), newestJobList, this)
            }
        }

        db.collection("jobs").where(Filter.or(
            Filter.equalTo("levelId", 8),
            Filter.equalTo("wayToWorkId", 3)
        )).limit(10).get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                internshipJobList = ArrayList()
                for (document in task.result){
                    val jobId = document.id
                    val jobName = document["jobTitle"].toString()
                    val corpId = document["corpId"].toString()
                    val expId = document["expId"].toString().toInt()
                    val salaryId = document["salaryId"].toString().toInt()
                    val workAddress = arrayOf(document["workAddress"].toString().replace("[", "").replace("]", ""))
                    val deadline = document["deadline"]

                    val job = Jobs(jobId, jobName, corpId, expId, salaryId, workAddress,
                        deadline as Timestamp
                    )

                    internshipJobList.add(job)
                }
                Log.e("internshipJobList", internshipJobList.toString())
                binding.internshipRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.internshipRv
                    .adapter = JobsAdapter(this.requireContext(), internshipJobList, this)

            }
        }

        binding.searchBar.setOnClickListener{

            navController.navigate(R.id.action_jobsFragment_to_searchViewJob)
        }

        binding.newestJobMore.setOnClickListener {

            val bundleViewMore = bundleOf(
                "idGroup" to 0
            )
            navController.navigate(R.id.action_jobsFragment_to_viewMoreJob, bundleViewMore)
        }

        binding.viewMore2.setOnClickListener {
            val bundleViewMore = bundleOf(
                "idGroup" to 1
            )
            navController.navigate(R.id.action_jobsFragment_to_viewMoreJob, bundleViewMore)
        }


        return binding.root
    }
    override fun onItemClick(position: Int, jobsArrayList: ArrayList<Jobs>) {

        bundle = bundleOf(
            "documentID" to jobsArrayList[position].jobID
        )

        navController.navigate(R.id.action_jobsFragment_to_jobDetail, bundle)
    }
    override fun onItemClickUpdate(position: Int) {
        TODO("Not yet implemented")
    }
}


