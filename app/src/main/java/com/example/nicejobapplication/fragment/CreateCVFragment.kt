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
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentCreateCvBinding
import com.example.nicejobapplication.modal.CV
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class CreateCVFragment : Fragment() {
    private lateinit var binding: FragmentCreateCvBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var selectedAvt: Uri
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase

    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCvBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("CV")
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnContinue.setOnClickListener {
            saveCV()
        }
        //select avt
        binding.avtCreateCV.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

        //dropdown item
        dropdownItem()

        //click on Birthday
        binding.edtBirthdayCreateCV.setOnClickListener {
            birhday()
        }



        return binding.root
    }

    private fun saveCV()
    {
        //getting values
        val cvName = binding.edtCvName.text.toString()
        val name = binding.edtNameCreateCV.text.toString()
        val position = binding.edtPositionCreateCV.text.toString()
        val email = binding.edtEmailCreateCV.text.toString()
        val phone = binding.edtPhoneCreateCV.text.toString()
        val gentle = binding.autoCompleteTextViewGentle.text.toString()
        val address = binding.edtAddressCreateCV.text.toString()
//        val birthday = binding.edtBirthdayCreateCV.text.toString()
        val careerGoal = binding.edtcareerGoalCreateCV.text.toString()
        val salary = binding.autoCompleteTextViewSalary.text.toString()
        val introduceYourself = binding.edtIntroduceCreateCV.text.toString()
        val exp = binding.autoCompleteTextViewExperience.text.toString()
        val academicLevel = binding.edtAcademicLevelCreateCV.text.toString()

        if (cvName.isEmpty() &&name.isEmpty() && email.isEmpty() && position.isEmpty() && phone.isEmpty()  &&
            salary.isEmpty()  && exp.isEmpty() && academicLevel.isEmpty()
//          && gentle.isEmpty() && address.isEmpty()  && birthday.isEmpty() && careerGoal.isEmpty() && introduceYourself.isEmpty()
        ) {
            if (cvName.isEmpty()) {
                binding.edtCvName.error = "Please enter CV name"
            }
            if (name.isEmpty()) {
                binding.edtNameCreateCV.error = "Please enter name"
            }
            if (position.isEmpty()) {
                binding.edtPositionCreateCV.error = "Please enter job position"
            }
            if (email.isEmpty()) {
                binding.edtEmailCreateCV.error = "Please enter email"
            }
            if (phone.isEmpty()) {
                binding.edtPhoneCreateCV.error = "Please enter phone"
            }
//                if (gentle.isEmpty()) {
//                    binding.autoCompleteTextViewGentle.error = "Please enter gentle"
//                }
//                if (address.isEmpty()) {
//                    binding.edtAddressCreateCV.error = "Please enter address"
//                }
//                if (birthday.isEmpty()) {
//                    binding.edtBirthdayCreateCV.error = "Please enter date of birth"
//                }
//                if (careerGoal.isEmpty()) {
//                    binding.edtcareerGoalCreateCV.error = "Please enter Career Goal"
//                }
            if (salary.isEmpty()) {
                binding.autoCompleteTextViewSalary.error = "Please enter salary"
            }
//                if (introduceYourself.isEmpty()) {
//                    binding.edtIntroduceCreateCV.error = "Please enter Introduce Yourself"
//                }
            if (exp.isEmpty()) {
                binding.autoCompleteTextViewExperience.error = "Please enter experience"
            }
            if (academicLevel.isEmpty()) {
                binding.edtAcademicLevelCreateCV.error = "Please enter Academic Level"
            }
        }
        else if (!email.matches(emailPattern.toRegex())){
            binding.edtEmailCreateCV.error = "Enter valid email address"
        }else if (selectedAvt == null){
            Toast.makeText(activity, "Please enter Your Avatar !", Toast.LENGTH_SHORT).show()
        }else{
            uploadData()
        }

    }

    private fun uploadData() {
        val reference = storage.reference.child("CV_Avt").child(Date().time.toString())
        reference.putFile(selectedAvt).addOnCompleteListener{
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

    private fun uploadInfo(avtUrl: String) {
        val cvId = dbRef.push().key!!
        val cv = CV(
//            auth.currentUser!!.uid,binding.edtCvName.text.toString(),avtUrl,binding.edtNameCreateCV.text.toString(),
            cvId,binding.edtCvName.text.toString(),avtUrl,binding.edtNameCreateCV.text.toString(),
            binding.edtPositionCreateCV.text.toString(),binding.edtEmailCreateCV.text.toString(),binding.edtEmailCreateCV.text.toString(),
            binding.edtPhoneCreateCV.text.toString(),binding.autoCompleteTextViewGentle.text.toString(), binding.edtBirthdayCreateCV.text.toString()
            , binding.edtcareerGoalCreateCV.text.toString(),//here
            binding.autoCompleteTextViewSalary.text.toString(),binding.edtIntroduceCreateCV.text.toString(),binding.autoCompleteTextViewExperience.text.toString(),
            binding.edtAcademicLevelCreateCV.text.toString())

//        dbRef.child("cv")
        database.reference.child("create_cv")
//            .child(auth.uid.toString())
            //open
//            .child(auth.currentUser!!.uid)
            .child(auth.currentUser!!.uid)
//            .child(auth.uid.toString())
            //open
            .child(cvId)
            .setValue(cv)
            .addOnSuccessListener {
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

                binding.avtCreateCV.setImageURI(selectedAvt)
            }
        }
    }

    private fun birhday() {
        //birthday
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dp =  DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            //i:năm
            // i2 :tháng
            // i3 : ngày
            binding.edtBirthdayCreateCV.setText("$i3/${i2+1}/$i")
        },year,month,day)

        //giới hạn 1 ngày trước
        dp.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dp.show()

    }

    private fun dropdownItem() {
        // dropdown item experience
        val experience = resources.getStringArray(R.array.exp)
        val arrAdapterExp = ArrayAdapter(requireContext(),R.layout.dropdown_item,experience)
        binding.autoCompleteTextViewExperience.setAdapter(arrAdapterExp)

        //dropdown item salary
        val salary = resources.getStringArray(R.array.salary)
        val arrAdapterSal = ArrayAdapter(requireContext(),R.layout.dropdown_item,salary)
        binding.autoCompleteTextViewSalary.setAdapter(arrAdapterSal)

        //dropdown item gentle
        val gentle = resources.getStringArray(R.array.gentle)
        val arrAdapterGen = ArrayAdapter(requireContext(),R.layout.dropdown_item,gentle)
        binding.autoCompleteTextViewGentle.setAdapter(arrAdapterGen)
    }


}