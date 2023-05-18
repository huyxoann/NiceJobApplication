package com.example.nicejobapplication.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentCreateCvBinding
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


class CreateCVFragment : Fragment() {
    private lateinit var binding: FragmentCreateCvBinding
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
    private val calendar: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    var dt: String = dateFormat.format(calendar.time)


    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var dayOfBirthPattern ="^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))\$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})\$"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCvBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("CV")
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        db = FirebaseFirestore.getInstance()

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        binding.btnContinue.setOnClickListener {
            saveCV()
        }
        //select avt
        binding.avtCreateCV.setOnClickListener {
            launchGallery()
        }

        //dropdown item
        dropdownItem()


        return binding.root
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Vui lòng lựa chọn ảnh !"), PICK_IMAGE_REQUEST)
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
                binding.avtCreateCV.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun saveCV() {
        //getting values
        val cvName = binding.edtCvName.text.toString()
        val name = binding.edtNameCreateCV.text.toString()
        val email = binding.edtEmailCreateCV.text.toString()
        val phone = binding.edtPhoneCreateCV.text.toString()
        val gentle = binding.autoCompleteTextViewGentle.text.toString()
        val address = binding.edtAddressCreateCV.text.toString()
        val dateOfBirth = binding.edtBirthdayCreateCV.text.toString()
        val careerGoal = binding.edtcareerGoalCreateCV.text.toString()
        val workExp = binding.autoCompleteTextViewExperience.text.toString()
        val academicLevel = binding.edtAcademicLevelCreateCV.text.toString()

        if (cvName.isEmpty() && name.isEmpty() && email.isEmpty() && phone.isEmpty() && gentle.isEmpty() && address.isEmpty()
            && workExp.isEmpty() && academicLevel.isEmpty() && dateOfBirth.isEmpty() && careerGoal.isEmpty() )
        {
            if (cvName.isEmpty()) {
                binding.edtCvName.error = "Vui lòng nhập tên CV"
            }
            if (name.isEmpty()) {
                binding.edtNameCreateCV.error = "Vui lòng nhập tên "
            }
            if (email.isEmpty()) {
                binding.edtEmailCreateCV.error = "Vui lòng nhập email"
            }
            if (dateOfBirth.isEmpty()) {
                binding.edtBirthdayCreateCV.error = "Vui lòng nhập ngày sinh"
            }
            if (phone.isEmpty()) {
                binding.edtPhoneCreateCV.error = "Vui lòng nhập số điện thoại"
            }
            if (gentle.isEmpty()) {
                binding.autoCompleteTextViewGentle.error = "Vui lòng chọn giới tính"
            }
            if (address.isEmpty()) {
                binding.edtAddressCreateCV.error = "Vui lòng nhập địa chỉ"
            }
            if (workExp.isEmpty()) {
                binding.autoCompleteTextViewExperience.error = "Vui lòng nhập kinh nghiệm"
            }
            if (careerGoal.isEmpty()) {
                binding.edtcareerGoalCreateCV.error = "Vui lòng nhập mục tiêu sự nghiệp"
            }
            if (academicLevel.isEmpty()) {
                binding.edtAcademicLevelCreateCV.error = "Vui lòng nhập trình độ học vấn"
            }
            Toast.makeText(
                activity,
                "Vui lòng nhập đầy đủ thông tin",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!email.matches(emailPattern.toRegex())) {
            binding.edtEmailCreateCV.error = "Vui lòng nhập email đúng cú pháp"
            Toast.makeText(
                activity,
                "Vui lòng nhập email đúng cú pháp",
                Toast.LENGTH_SHORT
            ).show()
        }else if (!dateOfBirth.matches(dayOfBirthPattern.toRegex())) {
            binding.edtBirthdayCreateCV.error = "Vui lòng nhập ngày sinh ( yyyy/mm/dd )"
            Toast.makeText(
                activity,
                "Vui lòng nhập ngày sinh ( yyyy/mm/dd )",
                Toast.LENGTH_SHORT
            ).show()
        }
        else if (selectedAvt == null) {
            Toast.makeText(activity, "Vui lòng chọn ảnh !", Toast.LENGTH_SHORT).show()
        } else {
            uploadData()
        }
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
        val date = dateToMilliseconds(dt,dateFormat)
//        val cvId = dbRef.push().key!!

//        val userId = auth.currentUser!!.uid
        val userEmail = auth.currentUser!!.email
        val cv = CV(
            "",binding.edtCvName.text.toString(),avtUrl,binding.edtNameCreateCV.text.toString(),
            binding.edtEmailCreateCV.text.toString(),
            binding.edtPhoneCreateCV.text.toString(), binding.autoCompleteTextViewGentle.text.toString(),
            binding.edtAddressCreateCV.text.toString(), binding.edtBirthdayCreateCV.text.toString()
            ,binding.edtcareerGoalCreateCV.text.toString(),binding.autoCompleteTextViewExperience.text.toString(),
            binding.edtAcademicLevelCreateCV.text.toString(),date
        )

        db.collection("created_cv").document(userEmail!!).collection(userEmail).document()
            .set(cv).addOnCompleteListener {
                Toast.makeText(activity, "Tạo CV thành công !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, MainActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(activity, it.message.toString(), Toast.LENGTH_SHORT).show()
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

        //dropdown item gentle
        val gentle = resources.getStringArray(R.array.gentle)
        val arrAdapterGen = ArrayAdapter(requireContext(),R.layout.dropdown_item,gentle)
        binding.autoCompleteTextViewGentle.setAdapter(arrAdapterGen)
    }
    private fun dateToMilliseconds(date:String,dateFormat: SimpleDateFormat):Long{
        val mDate = dateFormat.parse(date)
        return mDate.time
    }
}