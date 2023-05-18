package com.example.nicejobapplication.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentCreateCvBinding
import com.example.nicejobapplication.databinding.FragmentEditCvBinding
import com.example.nicejobapplication.modal.CV
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditCvFragment : Fragment() {
    private lateinit var binding:FragmentEditCvBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var db: FirebaseFirestore

    private val PICK_IMAGE_REQUEST = 71
    private var selectedAvt: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null


    //get create at
    val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    var dt = dateFormat.format(calendar.time)


    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var dayOfBirthPattern ="^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))\$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCvBinding.inflate(layoutInflater)
//        return inflater.inflate(R.layout.fragment_edit_cv, container, false)
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("CV")
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        db = FirebaseFirestore.getInstance()

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val bundle = arguments
        val documentID = bundle?.getString("cvName")

        val userEmail = auth.currentUser!!.email

        db.collection("created_cv").document(userEmail!!).collection(userEmail).document(documentID!!)
            .get()
            .addOnSuccessListener {document->
                val cvName = document.data?.get("cvName").toString()
                val avatar = document.data?.get("avatar").toString()
                val employerName = document.data?.get("employerName").toString()
                val email = document.data?.get("email").toString()
                val phoneNumber = document.data?.get("phoneNumber").toString()
                val gentle = document.data?.get("gentle").toString()
                val address = document.data?.get("address").toString()
                val dayOfBirth = document.data?.get("dayOfBirth").toString()
                val careerGoal = document.data?.get("careerGoal").toString()
                val workExperience = document.data?.get("workExperience").toString()
                val academicLevel = document.data?.get("academicLevel").toString()


                binding.edtNameCvEditCv.setText(cvName)
                Glide.with(requireActivity()).load(avatar).into(binding.avtEditCV)
                binding.edtNameEditCV.setText(employerName)
                binding.edtEmailEditCV.setText(email)
                binding.edtPhoneEditCV.setText(phoneNumber)
                binding.autoCompleteTextViewGentleEdit.setText(gentle)
                binding.edtAddressEditCV.setText(address)
                binding.edtBirthdayEditCV.setText(dayOfBirth)
                binding.edtcareerGoalEditCV.setText(careerGoal)
                binding.autoCompleteTextViewExperienceEdit.setText(workExperience)
                binding.edtAcademicLevelEditCV.setText(academicLevel)
                //dropdown item
                dropdownItem()
            }
            .addOnFailureListener {
                Toast.makeText(activity,it.toString(), Toast.LENGTH_SHORT).show()
            }

        binding.btnUpdateCv.setOnClickListener {
            updateCV()
        }
        //select avt
        binding.avtEditCV.setOnClickListener {
            launchGallery()
        }

        return binding.root
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Vui lòng chọn ảnh"), PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            selectedAvt = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,selectedAvt)
                binding.avtEditCV.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun updateCV() {
        //getting values
        val name = binding.edtNameEditCV.text.toString()
        val email = binding.edtEmailEditCV.text.toString()
        val phone = binding.edtPhoneEditCV.text.toString()
        val gentle = binding.autoCompleteTextViewGentleEdit.text.toString()
        val address = binding.edtAddressEditCV.text.toString()
        val dateOfBirth = binding.edtBirthdayEditCV.text.toString()
        val careerGoal = binding.edtcareerGoalEditCV.text.toString()
        val workExp = binding.autoCompleteTextViewExperienceEdit.text.toString()
        val academicLevel = binding.edtAcademicLevelEditCV.text.toString()

        if ( name.isEmpty() && email.isEmpty() && phone.isEmpty() && gentle.isEmpty() && address.isEmpty()
            && workExp.isEmpty() && academicLevel.isEmpty() && dateOfBirth.isEmpty() && careerGoal.isEmpty() ) {
            if (name.isEmpty()) {
                binding.edtNameEditCV.error = "Vui lòng nhập tên "
            }
            if (email.isEmpty()) {
                binding.edtEmailEditCV.error = "Vui lòng nhập email"
            }
            if (dateOfBirth.isEmpty()) {
                binding.edtBirthdayEditCV.error = "Vui lòng nhập ngày sinh"
            }
            if (phone.isEmpty()) {
                binding.edtPhoneEditCV.error = "Vui lòng nhập số điện thoại"
            }
            if (workExp.isEmpty()) {
                binding.autoCompleteTextViewExperienceEdit.error = "Vui lòng nhập kinh nghiệm"
            }
            if (gentle.isEmpty()) {
                binding.autoCompleteTextViewGentleEdit.error = "Vui lòng chọn giới tính"
            }
            if (address.isEmpty()) {
                binding.edtAddressEditCV.error = "Vui lòng nhập địa chỉ"
            }
            if (careerGoal.isEmpty()) {
                binding.edtcareerGoalEditCV.error = "Vui lòng nhập mục tiêu sự nghiệp"
            }
            if (academicLevel.isEmpty()) {
                binding.edtAcademicLevelEditCV.error = "Vui lòng nhập trình độ học vấn"
            }
            Toast.makeText(
                activity,
                "Vui lòng nhập đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!email.matches(emailPattern.toRegex())) {
            binding.edtEmailEditCV.error ="Vui lòng nhập email đúng cú pháp"
            Toast.makeText(
                activity,
                "Vui lòng nhập email đúng cú pháp",
                Toast.LENGTH_SHORT
            ).show()
        }else if (!dateOfBirth.matches(dayOfBirthPattern.toRegex())) {
            binding.edtBirthdayEditCV.error = "Vui lòng nhập ngày sinh ( yyyy/mm/dd )"
            Toast.makeText(
                activity,
                "Vui lòng nhập ngày sinh ( yyyy/mm/dd )",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (selectedAvt == null) {
            uploadDataWithoutImage()
        }
        else {
            uploadData()
        }
    }

    private fun uploadDataWithoutImage() {
       uploadInfoWithoutImage()
    }


    private fun uploadData() {
        val reference = storage.reference.child("CV_Avt").child(Date().time.toString())
        reference.putFile(selectedAvt!!).addOnCompleteListener {
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            } else {
                Toast.makeText(activity, "Đã xảy ra lỗi, Vui lòng thử lại !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadInfo(avtUrl: String) {
        val date = dateToMilliseconds(dt.toString(),dateFormat)

        val bundle = arguments
        val documentID = bundle?.getString("cvName")

        val userEmail = auth.currentUser!!.email

        val updateMap = mapOf(
            "avatar" to avtUrl,
            "employerName" to binding.edtNameEditCV.text.toString(),
            "email" to  binding.edtEmailEditCV.text.toString(),
            "phoneNumber" to binding.edtPhoneEditCV.text.toString(),
            "gentle" to  binding.autoCompleteTextViewGentleEdit.text.toString(),
            "address" to  binding.edtAddressEditCV.text.toString(),
            "dayOfBirth" to  binding.edtBirthdayEditCV.text.toString(),
           "careerGoal" to  binding.edtcareerGoalEditCV.text.toString(),
           "workExperience" to binding.autoCompleteTextViewExperienceEdit.text.toString(),
           "academicLevel" to binding.edtAcademicLevelEditCV.text.toString(),
            "createAt" to date
        )

        db.collection("created_cv").document(userEmail!!).collection(userEmail).document(documentID!!)
            .update(updateMap).addOnCompleteListener {
                Toast.makeText(activity, "Cập nhật CV thành công !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadInfoWithoutImage() {
        val date = dateToMilliseconds(dt.toString(),dateFormat)

        val bundle = arguments
        val documentID = bundle?.getString("cvName")

        val userEmail = auth.currentUser!!.email

        val updateMap = mapOf(
            "employerName" to binding.edtNameEditCV.text.toString(),
            "email" to  binding.edtEmailEditCV.text.toString(),
            "phoneNumber" to binding.edtPhoneEditCV.text.toString(),
            "gentle" to  binding.autoCompleteTextViewGentleEdit.text.toString(),
            "address" to  binding.edtAddressEditCV.text.toString(),
            "dayOfBirth" to  binding.edtBirthdayEditCV.text.toString(),
            "careerGoal" to  binding.edtcareerGoalEditCV.text.toString(),
            "workExperience" to binding.autoCompleteTextViewExperienceEdit.text.toString(),
            "academicLevel" to binding.edtAcademicLevelEditCV.text.toString(),
            "createAt" to date
        )

        db.collection("created_cv").document(userEmail!!).collection(userEmail).document(documentID!!)
            .update(updateMap).addOnCompleteListener {
                Toast.makeText(activity, "Cập nhật CV thành công !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun dropdownItem() {
        // dropdown item experience
        val experience = resources.getStringArray(R.array.exp)
        val arrAdapterExp = ArrayAdapter(requireContext(),R.layout.dropdown_item,experience)
        binding.autoCompleteTextViewExperienceEdit.setAdapter(arrAdapterExp)


        //dropdown item gentle
        val gentle = resources.getStringArray(R.array.gentle)
        val arrAdapterGen = ArrayAdapter(requireContext(),R.layout.dropdown_item,gentle)
        binding.autoCompleteTextViewGentleEdit.setAdapter(arrAdapterGen)
    }

    private fun dateToMilliseconds(date:String,dateFormat: SimpleDateFormat):Long{
        val mDate = dateFormat.parse(date)
        return mDate.time
    }

}