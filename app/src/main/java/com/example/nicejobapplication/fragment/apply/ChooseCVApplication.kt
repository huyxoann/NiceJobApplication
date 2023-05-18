package com.example.nicejobapplication.fragment.apply

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentChooseCVApplicationBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChooseCVApplication : Fragment() {
    private lateinit var binding: FragmentChooseCVApplicationBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    lateinit var selectedCV: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        var bundle = arguments

        val userEmail = auth.currentUser?.email

        navController = findNavController()

        binding = FragmentChooseCVApplicationBinding.inflate(layoutInflater)

        //Kiểm tra xem người dùng nhấn vào Radio Button nào
        binding.cvSelection.isEnabled = false
        binding.cvOnlineRadioBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.cvSelection.isEnabled = binding.cvOnlineRadioBtn.isChecked
            }else{
                binding.cvSelection.isEnabled = binding.cvOnlineRadioBtn.isChecked
            }
        }
        binding.uploadLocalCV.isEnabled = false
        binding.cvOnLocalRadioBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.uploadLocalCV.isEnabled = binding.cvOnLocalRadioBtn.isChecked
            }else{
                binding.uploadLocalCV.isEnabled = binding.cvOnLocalRadioBtn.isChecked
            }
        }

        //Set sự kiện cho nút back
        binding.topAppBar.setNavigationOnClickListener {
            navController.popBackStack()
        }




        //Xử lý phần chọn CV online
        var cvArray = HashMap<String, String>()
        if (userEmail != null) {
            db.collection("created_cv").document(userEmail).collection(userEmail).get().addOnSuccessListener {
                for (document in it.documents){
                    cvArray[document["cvName"].toString()] = document.id
                }

            }
        }
        val keys = arrayOf(cvArray.keys)
        val values = arrayOf(cvArray.values)
        val adapter = ArrayAdapter(requireContext(), R.layout.cv_dropdown, keys)
        adapter.setDropDownViewResource(R.layout.cv_dropdown)
        binding.autoCompleteTextView.setAdapter(adapter)


        binding.autoCompleteTextView.onItemClickListener = object: AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {

            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != -1){
                    selectedCV = values[position].toString()
//                    Toast.makeText(requireContext(), "Selected Item: $selectedCV", Toast.LENGTH_SHORT).show()
                    Log.e("CV Selected", cvArray.toString())
                }
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TODO("Not yet implemented")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.applyBtn.setOnClickListener {
            //Kiểm tra xem đã chọn bất kì CV nào chưa
            if (binding.radioGroup.checkedRadioButtonId != -1){
                //Kiểm tra xem có nội dung ở phần giới thiệu hay không
                if(binding.introduceContent.text!!.isBlank()){
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Thông báo")
                        .setMessage("Việc ứng tuyển thiếu phần giới thiệu có thể ảnh hưởng đến khả năng được nhà tuyển dụng để ý thấp hơn. Bạn có chắc muốn tiếp tục?")
                        .setNeutralButton("Hủy") { _, _ ->
                            // Respond to neutral button press
                        }
                        .setPositiveButton("Tiếp tục") { _, _ ->
                            sendApplication(db, selectedCV, binding.introduceContent.text, bundle?.getString("documentID")!!, userEmail!!)
                        }
                        .show()
                }else{
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Thông báo")
                        .setMessage("Bạn có chắc chắn muốn nộp CV cho công việc này không? Bạn không thể thay đổi tác vụ này.")
                        .setNeutralButton("Hủy") { _, _ -> }
                        .setPositiveButton("Đồng ý") { _, _ ->
                            sendApplication(db, selectedCV, binding.introduceContent.text, bundle?.getString("documentID").toString(), userEmail!!)
                        }
                        .show()
                }
            }else{
                Snackbar.make(binding.root, R.string.noneCVChosenError, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        binding.goBackBtn.setOnClickListener {
            navController.popBackStack()
        }

        return binding.root
    }

    private fun sendApplication(db: FirebaseFirestore, cvID: String, introduce: Editable?, jobId: String?, employeeId: String?) {
        cvID.replace("[", "").replace("]", "")
        val application = hashMapOf(
            "employeeId" to employeeId,
            "cvId" to cvID,
            "jobId" to jobId,
            "introduction" to introduce.toString()
        )

        db.collection("applications").add(application)
        .addOnSuccessListener {
            navController.popBackStack()
            Snackbar.make(binding.root, "Đã ứng tuyển thành công!", Snackbar.LENGTH_SHORT)
                .show()
        }
        .addOnFailureListener{
            Snackbar.make(binding.root, "Lỗi không thể nộp được, vui lòng thử lại sau.", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

}

