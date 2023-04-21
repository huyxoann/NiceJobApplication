package com.example.nicejobapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.nicejobapplication.databinding.ActivityLoginFacebookBinding
import com.example.nicejobapplication.databinding.ActivityMainBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class LoginFacebook : AppCompatActivity() {

    lateinit var binding: ActivityLoginFacebookBinding

    var callbackManager = CallbackManager.Factory.create()
    var auth= FirebaseAuth.getInstance()
    private val accessToken =AccessToken.getCurrentAccessToken()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login_facebook)

        binding = ActivityLoginFacebookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (accessToken!=null && !accessToken.isExpired){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        LoginManager.getInstance().registerCallback(callbackManager,
            object :FacebookCallback<LoginResult>{
                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException) {
                    TODO("Not yet implemented")
                }

                override fun onSuccess(result: LoginResult) {
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    finish()
                }
            })

        binding.loginButtonFB.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile","email"))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }
}