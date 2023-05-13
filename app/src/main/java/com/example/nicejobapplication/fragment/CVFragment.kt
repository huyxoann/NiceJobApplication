package com.example.nicejobapplication.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nicejobapplication.R
import com.example.nicejobapplication.adapter.CorpAdapter
import com.example.nicejobapplication.adapter.CvAdapter
import com.example.nicejobapplication.adapter.OnItemClickListener
import com.example.nicejobapplication.authentication.LoginSignup
import com.example.nicejobapplication.databinding.FragmentCvBinding
import com.example.nicejobapplication.modal.CV
import com.example.nicejobapplication.modal.Corporation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CVFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentCvBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var db: FirebaseFirestore
    private lateinit var cvArrayList: ArrayList<CV>
    private lateinit var rvCv: RecyclerView
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle

    val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCvBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
//        dbRef = FirebaseDatabase.getInstance().getReference("create_cv")
        val firebaseUser = auth.currentUser

        if (firebaseUser == null) {
            binding.btnCreateCV.text = "Login"
            binding.btnCreateCV.setOnClickListener {
                startActivity(Intent(activity, LoginSignup::class.java))
            }
        } else {
            binding.txtTitleLogin.text = ""
            binding.btnCreateCV.setOnClickListener {
                val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                transaction.replace(
                    R.id.fragment_container, CreateCVFragment()
                ).commit()
            }

            rvCv = binding.rvCv
            rvCv.layoutManager = LinearLayoutManager(activity)

            //view cv
            val userEmail = auth.currentUser!!.email
            db = FirebaseFirestore.getInstance()
            db.collection("cv").document(userEmail!!).collection(userEmail)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
//                        rvCv.adapter?.notifyDataSetChanged()
                        cvArrayList = arrayListOf()
                        for (data in it.documents) {
                            val cv: CV? = data.toObject(CV::class.java)
                            if (cv != null) {
                                cvArrayList.add(cv)
                            }
                        }

                        rvCv.adapter =
                            activity?.let { it1 -> CvAdapter(it1, cvArrayList, this) }
                        rvCv.adapter?.notifyDataSetChanged()
                    }
                }.addOnFailureListener {
                    Toast.makeText(activity,it.toString(),Toast.LENGTH_SHORT).show()
                }
        }

        return binding.root
    }

    override fun onItemClick(position: Int) {

        bundle = bundleOf(
            "cvName" to cvArrayList[position].cvName
        )

        navController = findNavController()

        navController.navigate(R.id.action_CVFragment_to_cvDetail2, bundle)
    }

    override fun onItemClickUpdate(position: Int) {
        bundle = bundleOf(
            "cvName" to cvArrayList[position].cvName
        )

        navController = findNavController()

        navController.navigate(R.id.action_CVFragment_to_editCvFragment, bundle)
    }
}