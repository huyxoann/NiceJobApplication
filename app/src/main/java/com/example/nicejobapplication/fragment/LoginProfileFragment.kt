package com.example.nicejobapplication.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.authentication.Users
import com.example.nicejobapplication.databinding.FragmentLoginProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class LoginProfileFragment : Fragment() {
    private lateinit var binding:FragmentLoginProfileBinding
    private lateinit var dbRef:DatabaseReference
    private lateinit var db: FirebaseFirestore
    //Select image from gallery
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage:FirebaseStorage
    private lateinit var selectedImg:Uri
    private lateinit var edtNameUpdate:EditText
    private lateinit var imgUpdate:ImageButton

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginProfileBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        db = FirebaseFirestore.getInstance()

        val userId = auth.currentUser!!.uid

        db.collection("users").document(userId).get().addOnSuccessListener {

            val name = it.data?.get("name").toString()
            val email =  it.data?.get("email").toString()
            val img =  it.data?.get("img").toString()

            binding.txtName.text = name
            binding.txtEmail.text = "Email:$email"


            //view image
            Glide.with(requireActivity()).load(img).into(binding.profileButton)

        }.addOnFailureListener {
            Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
        }

        //update
        binding.btnUpdate.setOnClickListener {

            val builder = AlertDialog.Builder(activity)
            val view = layoutInflater.inflate(R.layout.dialog_updating_profile,null)
            val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
            val btnCancelUpdating = view.findViewById<Button>(R.id.btnCancelUpdating)
            imgUpdate = view.findViewById(R.id.imgUpdate)
            edtNameUpdate = view.findViewById(R.id.edtNameUpdate)

            //view screen update
            builder.setView(view)
            val dialog = builder.create()

            setName()

            //select image
            imgUpdate.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                startActivityForResult(intent,1)
            }
            //update
            btnUpdate.setOnClickListener {
                if (edtNameUpdate.text!!.isEmpty()){
                    Toast.makeText(activity, "Please enter Your name !", Toast.LENGTH_SHORT).show()
                }else if (selectedImg == null){
                    Toast.makeText(activity, "Please enter Your Image !", Toast.LENGTH_SHORT).show()
                }else{
                    uploadData()
                }
            }
            //cancel
            btnCancelUpdating.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity, MainActivity::class.java))
        }

        return binding.root
    }

    private fun uploadData() {
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {task->
                    uploadInfo(task.toString())
                }
            }
            else{
                Toast.makeText(activity, "Something went wrong !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val userId = auth.currentUser!!.uid
//        val user = Users(userId,edtNameUpdate.text.toString(), auth.currentUser?.email, imgUrl)

        val updateMap = mapOf(
            "name" to edtNameUpdate.text.toString(),
            "img" to imgUrl
        )
       db.collection("users").document(userId).update(updateMap)
           .addOnSuccessListener {
                Toast.makeText(activity, "Update Profile success !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity,MainActivity::class.java))
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!=null){
            if (data.data!=null){
                selectedImg = data.data!!

                imgUpdate.setImageURI(selectedImg)
            }
        }
    }

    private fun setName() {
        val userId = auth.currentUser!!.uid

        db.collection("users").document(userId).get().addOnSuccessListener {

            val name = it.data?.get("name").toString()
            val img = it.data?.get("img").toString()

            edtNameUpdate.setText(name)
            //view image
            Glide.with(requireActivity()).load(img).into(imgUpdate)
        }
    }

}