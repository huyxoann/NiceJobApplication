package com.example.nicejobapplication.TabFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.nicejobapplication.R
import com.example.nicejobapplication.authentication.LoginSignup
import com.example.nicejobapplication.authentication.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class SignupTabFragment : Fragment() {

    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var db: FirebaseFirestore

    private lateinit var btn_signup: Button
    private lateinit var signup_name: EditText
    private lateinit var signup_email: EditText
    private lateinit var signup_phone: EditText
    private lateinit var signup_password: EditText
    private lateinit var signup_confirm_password: EditText

    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup_tab, container, false)

        //back end login & signup
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        db = FirebaseFirestore.getInstance()

        signup_name = view.findViewById(R.id.signup_name)
        signup_email = view.findViewById(R.id.signup_email)
        signup_phone = view.findViewById(R.id.signup_phone)
        signup_password = view.findViewById(R.id.signup_password)
        signup_confirm_password = view.findViewById(R.id.signup_confirm_password)
        btn_signup = view.findViewById(R.id.btn_signup)


        btn_signup.setOnClickListener {
            ValidationInfor()
        }
        return view
    }

    private fun ValidationInfor() {
        val name = signup_name.text.toString()
        val email = signup_email.text.toString()
        val phone = signup_phone.text.toString()
        val password = signup_password.text.toString()
        val confirm_password = signup_confirm_password.text.toString()
        //validation
        if (name.isEmpty() && email.isEmpty()&& phone.isEmpty() && password.isEmpty() && confirm_password.isEmpty()) {
            if (name.isEmpty()) {
                signup_name.error = "Vui lòng nhập tên"
            }
            if (email.isEmpty()) {
                signup_email.error = "Vui lòng nhập email"
            }
            if (phone.isEmpty()) {
                signup_phone.error = "Vui lòng nhập số điện thoại"
            }
            if (password.isEmpty()) {
                signup_password.error = "Vui lòng nhập mật khẩu"
            }
            if (confirm_password.isEmpty()) {
                signup_confirm_password.error = "Vui lòng nhập lại mật khẩu"
            }
            Toast.makeText(activity,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        }else if (!email.matches(emailPattern.toRegex())){
            signup_email.error = "Sai cú pháp email"
            Toast.makeText(
                activity,
                "Vui lòng nhập chính xác cú pháp email",
                Toast.LENGTH_SHORT
            ).show()
        }else if (password.length <6){
            signup_password.error = "Độ dài mật khẩu phải lớn hơn 6 kí tự"
            Toast.makeText(
                activity,
                "Độ dài mật khẩu phải lớn hơn 6 kí tự",
                Toast.LENGTH_SHORT
            ).show()
        }else if (password!=confirm_password){
            signup_confirm_password.error = "Mật khẩu không khớp"
            Toast.makeText(
                activity,
                "Mật khẩu không khớp, vui lòng thử lại",
                Toast.LENGTH_SHORT
            ).show()
        }
        else{
            //call createUserWithEmailAndPassword using auth object and pass the email and pass in it.
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    //signup success -> email verification
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {

                            val userId = auth.currentUser!!.uid
                            val users = Users(userId,name,email,phone, password)

//                            db.collection("users").document(userId).set(users).addOnCompleteListener {
                            db.collection("users").document(userId).set(email).addOnCompleteListener {
                                if (it.isSuccessful){
                                    //chuyển đến fragment login
                                    val i = Intent(activity, LoginSignup::class.java)
                                    startActivity(i)
                                    Toast.makeText(activity,"Đăng kí thành công ! \n Vui lòng xác minh email của bạn",Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(activity,"Đã xảy ra lỗi , vui lòng thử lại !",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(activity,it.toString(),Toast.LENGTH_SHORT).show()
                        }
                }else{
                    //failed
//                    Toast.makeText(activity,"Đã xảy ra lỗi , vui lòng thử lại !",Toast.LENGTH_SHORT).show()
                    Toast.makeText(activity,"Email đã tồn tại , vui lòng nhập email khác !",Toast.LENGTH_SHORT).show()
                    //fix bug
//                        Toast.makeText(activity,it.exception.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}