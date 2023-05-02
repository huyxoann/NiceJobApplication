package com.example.nicejobapplication.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import java.text.SimpleDateFormat
import java.util.*


class EditCvFragment : Fragment() {
    private lateinit var binding:FragmentEditCvBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var selectedAvt: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var db: FirebaseFirestore


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

        val bundle = arguments
        val documentID = bundle?.getString("cvName")

        val userEmail = auth.currentUser!!.email

        db.collection("create_cv").document(userEmail!!).collection(userEmail).document(documentID!!)
            .get()
            .addOnSuccessListener {document->
                val avatar = document.data?.get("avatar").toString()
                val employerName = document.data?.get("employerName").toString()
                val jobPosition = document.data?.get("jobPosition").toString()
                val email = document.data?.get("email").toString()
                val phoneNumber = document.data?.get("phoneNumber").toString()
                val gentle = document.data?.get("gentle").toString()
                val address = document.data?.get("address").toString()
                val dayOfBirth = document.data?.get("dayOfBirth").toString()
                val careerGoal = document.data?.get("careerGoal").toString()
                val salary = document.data?.get("salary").toString()
                val introduceYourself = document.data?.get("introduceYourself").toString()
                val workExperience = document.data?.get("workExperience").toString()
                val academicLevel = document.data?.get("academicLevel").toString()


                binding.edtNameCvEditCv.setText(documentID)
                Glide.with(requireActivity()).load(avatar).into(binding.avtEditCV)
                binding.edtNameEditCV.setText(employerName)
                binding.edtPositionEditCV.setText(jobPosition)
                binding.edtEmailEditCV.setText(email)
                binding.edtPhoneEditCV.setText(phoneNumber)
                binding.autoCompleteTextViewGentleEdit.setText(gentle)
                binding.edtAddressEditCV.setText(address)
                binding.edtBirthdayEditCV.setText(dayOfBirth)
                binding.edtcareerGoalEditCV.setText(careerGoal)
                binding.autoCompleteTextViewSalaryEdit.setText(salary)
                binding.edtIntroduceEditCV.setText(introduceYourself)
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
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        return binding.root
    }

    private fun updateCV() {
        //getting values
        val name = binding.edtNameEditCV.text.toString()
        val jobPosition = binding.edtPositionEditCV.text.toString()
        val email = binding.edtEmailEditCV.text.toString()
        val phone = binding.edtPhoneEditCV.text.toString()
        val gentle = binding.autoCompleteTextViewGentleEdit.text.toString()
        val address = binding.edtAddressEditCV.text.toString()
        val dateOfBirth = binding.edtBirthdayEditCV.text.toString()
        val careerGoal = binding.edtcareerGoalEditCV.text.toString()
        val salary = binding.autoCompleteTextViewSalaryEdit.text.toString()
        val introduceYourself = binding.edtIntroduceEditCV.text.toString()
        val workExp = binding.autoCompleteTextViewExperienceEdit.text.toString()
        val academicLevel = binding.edtAcademicLevelEditCV.text.toString()

        if ( name.isEmpty() && email.isEmpty() && jobPosition.isEmpty() && phone.isEmpty() &&
            salary.isEmpty() && workExp.isEmpty() && academicLevel.isEmpty()
//          && gentle.isEmpty() && address.isEmpty()  && birthday.isEmpty() && careerGoal.isEmpty() && introduceYourself.isEmpty()
            && dateOfBirth.isEmpty()
        ) {
            if (name.isEmpty()) {
                binding.edtNameEditCV.error = "Please enter name"
            }
            if (jobPosition.isEmpty()) {
                binding.edtPositionEditCV.error = "Please enter job position"
            }
            if (email.isEmpty()) {
                binding.edtEmailEditCV.error = "Please enter email"
            }
            if (dateOfBirth.isEmpty()) {
                binding.edtBirthdayEditCV.error = "Please enter date of birth"
            }
            if (phone.isEmpty()) {
                binding.edtPhoneEditCV.error = "Please enter phone"
            }
            if (salary.isEmpty()) {
                binding.autoCompleteTextViewSalaryEdit.error = "Please enter salary"
            }
            if (workExp.isEmpty()) {
                binding.autoCompleteTextViewExperienceEdit.error = "Please enter experience"
            }
            if (academicLevel.isEmpty()) {
                binding.edtAcademicLevelEditCV.error = "Please enter Academic Level"
            }
            Toast.makeText(
                activity,
                "Please enter valid details",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!email.matches(emailPattern.toRegex())) {
            binding.edtEmailEditCV.error = "Enter valid email"
            Toast.makeText(
                activity,
                "Please Enter valid email address",
                Toast.LENGTH_SHORT
            ).show()
        }else if (!dateOfBirth.matches(dayOfBirthPattern.toRegex())) {
            binding.edtBirthdayEditCV.error = "Enter valid dates ( yyyy/mm/dd )"
            Toast.makeText(
                activity,
                "Please Enter valid dates ( yyyy/mm/dd )",
                Toast.LENGTH_SHORT
            ).show()
        }
//        else if (selectedAvt == null) {
//            Toast.makeText(activity, "Please enter Your Avatar !", Toast.LENGTH_SHORT).show()
//        }
        else {
            uploadData()
        }
    }

    private fun uploadData() {
        val reference = storage.reference.child("CV_Avt").child(Date().time.toString())
        reference.putFile(selectedAvt).addOnCompleteListener {
            if (it.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            } else {
                Toast.makeText(activity, "Something went wrong !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadInfo(avtUrl: String) {
        val date = dateToMilliseconds(dt.toString(),dateFormat)

        val bundle = arguments
        val documentID = bundle?.getString("cvName")

        val userEmail = auth.currentUser!!.email

        val updateMap = mapOf(
            "img" to avtUrl,
            "employerName" to binding.edtNameEditCV.text.toString(),
            "jobPosition" to binding.edtPositionEditCV.text.toString(),
            "email" to  binding.edtEmailEditCV.text.toString(),
            "phoneNumber" to binding.edtPhoneEditCV.text.toString(),
            "gentle" to  binding.autoCompleteTextViewGentleEdit.text.toString(),
            "address" to  binding.edtAddressEditCV.text.toString(),
            "dayOfBirth" to  binding.edtBirthdayEditCV.text.toString(),
           "careerGoal" to  binding.edtcareerGoalEditCV.text.toString(),
           "salary" to binding.autoCompleteTextViewSalaryEdit.text.toString(),
           "introduceYourself" to binding.edtIntroduceEditCV.text.toString(),
           "workExperience" to binding.autoCompleteTextViewExperienceEdit.text.toString(),
           "academicLevel" to binding.edtAcademicLevelEditCV.text.toString(),
            "createAt" to date
        )

        db.collection("create_cv").document(userEmail!!).collection(userEmail).document(documentID!!)
            .update(updateMap).addOnCompleteListener {
                Toast.makeText(activity, "Create CV success !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!=null){
            if (data.data!=null){
                selectedAvt = data.data!!

                binding.avtEditCV.setImageURI(selectedAvt)
            }
        }
    }


    private fun dropdownItem() {
        // dropdown item experience
        val experience = resources.getStringArray(R.array.exp)
        val arrAdapterExp = ArrayAdapter(requireContext(),R.layout.dropdown_item,experience)
        binding.autoCompleteTextViewExperienceEdit.setAdapter(arrAdapterExp)

        //dropdown item salary
        val salary = resources.getStringArray(R.array.salary)
        val arrAdapterSal = ArrayAdapter(requireContext(),R.layout.dropdown_item,salary)
        binding.autoCompleteTextViewSalaryEdit.setAdapter(arrAdapterSal)

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