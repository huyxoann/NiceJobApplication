package com.example.nicejobapplication.TabFragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nicejobapplication.LoginFacebook
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentLoginTabBinding
import com.google.firebase.auth.FirebaseAuth


class LoginTabFragment : Fragment() {

    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginTabBinding


    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginTabBinding.inflate(layoutInflater)

        // Initialize firebase auth
        auth = FirebaseAuth.getInstance()

        //login google
        binding.imgBtnLoginGoogle.setOnClickListener {

        }

        //login fb
        binding.imgBtnLoginFB.setOnClickListener {
            startActivity(Intent(activity,LoginFacebook::class.java))
        }

        //Hiện đăng nhập tự điền nếu người dùng đã đang nhập
        binding.loginPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)

        //login with gmail and password
        binding.btnLogin.setOnClickListener {
            ValidationInfor()
        }

    //forgot password
        binding.txtForgotPass.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            val view = layoutInflater.inflate(R.layout.dialog_forgot,null)
            val userEmail = view.findViewById<EditText>(R.id.edtBox)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                compareEmail(userEmail)
                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
//            if(dialog.window!=null){
//                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
//            }
            dialog.show()
        }

        return binding.root
    }

    private fun compareEmail(email:EditText) {
        if (email.text.toString().isEmpty()){
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }
        auth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(activity,"Vui lòng kiểm tra email của bạn", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun ValidationInfor() {
        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        //validation
        if ( email.isEmpty() && password.isEmpty() ) {
            if (email.isEmpty()) {
                binding.loginEmail.error = "Vui lòng nhập email"
            }
            if (password.isEmpty()) {
                binding.loginPassword.error = "Vui lòng nhập mật khẩu"
            }
            Toast.makeText(activity,"Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        }else if (!email.matches(emailPattern.toRegex())){
           binding.loginEmail.error = "Sai cú pháp email"
            Toast.makeText(
                activity,
                "Vui lòng nhập chính xác cú pháp email",
                Toast.LENGTH_SHORT
            ).show()
        }else if (password.length <6){
            binding.loginPassword.error = "Độ dài mật khẩu phải lớn hơn 6 kí tự"
            Toast.makeText(
                activity,
                "Độ dài mật khẩu phải lớn hơn 6 kí tự",
                Toast.LENGTH_SHORT
            ).show()
        } else{
            // calling signInWithEmailAndPassword(email, pass) function using Firebase auth object
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    //check email verification
                    val verification = auth.currentUser?.isEmailVerified
                    if (verification==true){
                        //chuyển đến fragment main
                        val i = Intent(activity, MainActivity::class.java)
                        startActivity(i)
                        Toast.makeText(activity,"Đăng nhập thành công !", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(activity,"Vui lòng xác minh Email !", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(activity,"Đã xảy ra lỗi , vui lòng thử lại !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}