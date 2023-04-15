package com.example.nicejobapplication.fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nicejobapplication.MainActivity
import com.example.nicejobapplication.R
import com.example.nicejobapplication.databinding.FragmentLoginTabBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth


class LoginTabFragment : Fragment() {
    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginTabBinding

//    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest
//
//    private lateinit var mGoogleSignInClient: GoogleSignIn
    private lateinit var gso: GoogleSignInOptions

    private lateinit var gsc:GoogleSignInClient


    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginTabBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        //Hiện đăng nhập tự điền nếu người dùng đã đang nhập
        binding.loginPassword.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)

        binding.btnLogin.setOnClickListener {
            ValidationInfor()
        }

        //login google
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
//====================================================================
//        gsc = activity?.let { GoogleSignIn.getClient(it,gso) }!!
        gsc = GoogleSignIn.getClient(requireActivity(),gso)
//====================================================================

        val account:GoogleSignInAccount? = GoogleSignIn
            .getLastSignedInAccount(requireActivity())

        if (account!=null){
            goToHome()
        }

        binding.imgBtnLoginGoogle.setOnClickListener {
            goToSignIn()
        }



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
            if(dialog.window!=null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()

        }

        //Đăng nhập Google bằng Google OneTap
//        binding.btnGoogle.setOnClickListener{
//        }

        return binding.root
    }

    private fun goToSignIn() {
        val signInIntent = gsc.signInIntent

        startActivityForResult(signInIntent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val task:Task<GoogleSignInAccount> = GoogleSignIn
            .getSignedInAccountFromIntent(data)

        try {
                task.getResult(ApiException::class.java)
                goToHome()
        }
        catch (e:java.lang.Exception){
            Toast.makeText(activity,e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToHome() {
//        val intent = Intent(activity,MainActivity::class.java)
//        startActivity(intent)
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
                Toast.makeText(activity,"Check your email", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun ValidationInfor() {
        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        //validation
        if ( email.isEmpty() && password.isEmpty() ) {
            if (email.isEmpty()) {
                binding.loginEmail.error = "Enter your email address"
            }
            if (password.isEmpty()) {
                binding.loginPassword.error = "Enter your password"
            }
            Toast.makeText(activity,"Please enter valid details", Toast.LENGTH_SHORT).show()
        }else if (!email.matches(emailPattern.toRegex())){
            binding.loginEmail.error = "Enter valid email address"
            Toast.makeText(
                activity,
                "Please enter valid email address",
                Toast.LENGTH_SHORT
            ).show()
        }else if (password.length <6){
            binding.loginPassword.error = "Enter password more than 6 characters"
            Toast.makeText(
                activity,
                "Please enter password more than 6 characters",
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
                        Toast.makeText(activity,"Login successfully !", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(activity,"Please verify your Email !", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(activity,"Something went wrong, try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}