package com.example.nicejobapplication.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.storage.UploadTask

class LoginProfileFragment : Fragment() {
    private lateinit var binding: FragmentLoginProfileBinding
    private lateinit var db: FirebaseFirestore

    //Select image from gallery
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var edtNameUpdate: EditText
    private lateinit var imgUpdate: ImageButton

    private val PICK_IMAGE_REQUEST = 71
    private var selectedImg: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

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

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        val userId = auth.currentUser!!.uid

        //view in fragment account : image , name , email
        db.collection("users").document(userId).get().addOnSuccessListener {

            val name = it.data?.get("name").toString()
            val email = it.data?.get("email").toString()
            val img = it.data?.get("img").toString()

            binding.txtName.text = name
            binding.txtEmail.text = "Email:$email"


            //view image
            Glide.with(requireActivity()).load(img).into(binding.profileButton)

        }.addOnFailureListener {
            Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
        }

        //click button update
        binding.btnUpdate.setOnClickListener {
            //view dialog Update
            val builder = AlertDialog.Builder(activity)
            val view = layoutInflater.inflate(R.layout.dialog_updating_profile, null)
            val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
            val btnCancelUpdating = view.findViewById<Button>(R.id.btnCancelUpdating)
            imgUpdate = view.findViewById(R.id.imgUpdate)
            edtNameUpdate = view.findViewById(R.id.edtNameUpdate)

            //view screen update
            builder.setView(view)
            val dialog = builder.create()

            setData()

            //select image
            imgUpdate.setOnClickListener {
                launchGallery()
            }

            //click update
            btnUpdate.setOnClickListener {
                if (edtNameUpdate.text!!.isEmpty()) {
                    Toast.makeText(activity, "Vui lòng nhập tên của mình !", Toast.LENGTH_SHORT).show()
                }
                else if (selectedImg == null) {
                    uploadDataWithoutImage()
                }
                else {
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
        if(selectedImg != null){

//            val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
//            val uploadTask = ref?.putFile(selectedImg!!)

            //update in Storage
            val reference = storage.reference.child("Profile").child(Date().time.toString())
            reference.putFile(selectedImg!!).addOnCompleteListener{
                if (it.isSuccessful){
                    reference.downloadUrl.addOnSuccessListener {task->
                        //update in Firestore
                        uploadInfo(task.toString())
                    }
                }
                else{
                    Toast.makeText(activity, "Đã xảy ra lỗi, Vui lòng thử lại !", Toast.LENGTH_SHORT).show()
                }
            }


        }else{
            Toast.makeText(activity, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadDataWithoutImage() {
        uploadInfoWithoutImage()
    }

    private fun uploadInfo(selectedImg: String) {
        val userId = auth.currentUser!!.uid
        val updateMap = mapOf(
            "name" to edtNameUpdate.text.toString(),
            "img" to selectedImg
        )
        db.collection("users").document(userId).update(updateMap)
            .addOnSuccessListener {
                Toast.makeText(activity, "Cập nhật thông tin thành công !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity,MainActivity::class.java))
            }
    }

    private fun uploadInfoWithoutImage() {
        val userId = auth.currentUser!!.uid
        val updateMap = mapOf(
            "name" to edtNameUpdate.text.toString()
        )
        db.collection("users").document(userId).update(updateMap)
            .addOnSuccessListener {
                Toast.makeText(activity, "Cập nhật thông tin thành công !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity,MainActivity::class.java))
            }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Vui lòng lựa chọn ảnh"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            selectedImg = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,selectedImg)
                imgUpdate.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setData() {
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