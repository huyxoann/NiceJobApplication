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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class LoginProfileFragment : Fragment() {
    private lateinit var binding:FragmentLoginProfileBinding
    private lateinit var dbRef:DatabaseReference
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

        //update
        binding.btnUpdate.setOnClickListener {

            val builder = AlertDialog.Builder(activity)
            val view = layoutInflater.inflate(R.layout.dialog_updating_profile,null)
            val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
            val btnCancelUpdating = view.findViewById<Button>(R.id.btnCancelUpdating)
            imgUpdate = view.findViewById(R.id.imgUpdate)
            edtNameUpdate = view.findViewById(R.id.edtNameUpdate)

            builder.setView(view)
            val dialog = builder.create()

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

        dbRef = Firebase.database.reference

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        dbRef.child("users").child(userId).get().addOnSuccessListener {

            val name = it.child("name").value.toString()
            //here
            val img = it.child("imgUrl").value.toString()
            val email = it.child("email").value.toString()

            binding.txtName.text = name

            Glide.with(requireActivity()).load(img).into(binding.profileButton)


            binding.txtEmail.text = "Email:$email"

        }.addOnFailureListener {
            Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

//    private fun selectImgFromGallery() {
//        val intent = Intent()
//        intent.action = Intent.ACTION_GET_CONTENT
//        intent.type = "image/*"
//        startActivityForResult(intent,1)
//    }

    private fun uploadData() {
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener{
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener {task->
                    uploadInfo(task.toString())
//                    Toast.makeText(activity, "Add Image successful", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(activity, "Something went wrong !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
//        val user = Users(auth.currentUser?.displayName.toString(), auth.currentUser?.email, auth.uid.toString(), imgUrl)
        val user = Users(edtNameUpdate.text.toString(), auth.currentUser?.email, auth.uid.toString(), imgUrl)

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
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
}