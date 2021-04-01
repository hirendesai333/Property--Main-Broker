package com.illopen.agent.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.illopen.agent.R
import com.illopen.agent.databinding.ActivityLoginBinding
import com.illopen.agent.network.ServiceApi
import com.illopen.agent.utils.AppPreferences
import com.illopen.agent.utils.Params
import com.illopen.agent.utils.ProgressDialog
import com.illopen.properybroker.utils.toast
import kotlinx.coroutines.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val TAG = "LoginActivity"
    private lateinit var progressDialog: ProgressDialog

    var regex =
        "[A-Z0-9a-z]+([._%+-][A-Z0-9a-z]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    var pattern: Pattern = Pattern.compile(regex)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    private lateinit var forgotPassPopUp : Dialog

    var loginType : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppPreferences.initialize(this.applicationContext)
        progressDialog = ProgressDialog(this)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        initializeViews()

        binding.loginButton.setOnClickListener {
            loginDetails()
        }

        binding.register.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        binding.forgotPassword.setOnClickListener {
            forgotPasswordPopUp()
        }
    }

    private fun forgotPasswordPopUp() {

        forgotPassPopUp = Dialog(this, R.style.Theme_PropertyMainBroker)
        forgotPassPopUp.setContentView(R.layout.forgot_password_popup)
        forgotPassPopUp.window!!.setWindowAnimations(R.style.Theme_PropertyMainBroker_Slide)


        val emailForgotPassword: TextInputEditText = forgotPassPopUp.findViewById(R.id.emailIdForgotPassword)
        val verifyEmail: TextView = forgotPassPopUp.findViewById(R.id.verifyEmail)
        val emailInputLayout: TextInputLayout = forgotPassPopUp.findViewById(R.id.emailInputLayout)

        forgotPassPopUp.show()

        verifyEmail.setOnClickListener {
            if (emailForgotPassword.text.toString().trim().isEmpty()) {
                emailInputLayout.error = "Please enter Email ID"
            } else {
                forgotPasswordAPI(emailForgotPassword.text.toString().trim(),forgotPassPopUp)
            }
        }
    }

    private fun forgotPasswordAPI(emailID: String,dialog: Dialog) {
        progressDialog.dialog.show()
        coroutineScope.launch {
            try {

                val response = ServiceApi.retrofitService.forgotPassword(
                    emailID,
                    true
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        Log.d("Forgot-Password", response.code().toString())
                        Log.d("Forgot-Password", response.body().toString())

                        if (response.code() == 200){
                            toast("Reset Password Successfully")
                            dialog.dismiss()
                        }else{
                            toast("Please try again")
                        }
                        progressDialog.dialog.dismiss()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "something wrong")
                        progressDialog.dialog.dismiss()
                    }
                }

            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                toast("Please try again")
                progressDialog.dialog.dismiss()
            }
        }
    }

    private fun loginDetails() {

        val email = binding.emailID.text.toString().trim()
        val pass = binding.password.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailID.error = "Please Enter Email"
            binding.emailID.requestFocus()
        } else if (!pattern.matcher(email).matches()) {
            binding.emailID.error = "Invalid Email"
            binding.emailID.requestFocus()
        } else if (pass.isEmpty()) {
            binding.password.error = "Please Enter Password"
            binding.password.requestFocus()
//        }else if (pass.length >= 8){
//            binding.password.error = "Password Should be 8 or more Character"
//            binding.password.requestFocus()
//        }
        } else {
            progressDialog.dialog.show()
            coroutineScope.launch {
                try {
                    val response = ServiceApi.retrofitService.login(
                        email,
                        pass
                    )
                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            Log.d("login", response.code().toString())
                            Log.d("login", response.body().toString())
                            if (response.code() == 200) {
                                toast("Login Success")
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                                AppPreferences.setUserData(
                                    Params.UserId,
                                    response.body()!!.item!!.id.toString()
                                )
                                AppPreferences.setUserData(
                                    Params.UserTypeMasterId,
                                    response.body()!!.item!!.userTypeMasterId.toString()
                                )
                                AppPreferences.setUserData(
                                    Params.Username,
                                    response.body()!!.item!!.userTypeName.toString()
                                )
                                AppPreferences.setUserData(
                                    Params.Email,
                                    response.body()!!.item!!.email.toString()
                                )
                                AppPreferences.setUserData(
                                    Params.MobileNumber,
                                    response.body()!!.item!!.phoneNumber.toString()
                                )
                                AppPreferences.setUserData(
                                    Params.ProfileUrl,
                                    response.body()!!.item!!.profileUrl.toString()
                                )
                            } else {
                                toast("Please Enter Valid Email & Password")
                            }
                            progressDialog.dialog.dismiss()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Log.d("login fail", response.code().toString())
                            toast("Please Enter Valid Email & Password")
                            progressDialog.dialog.dismiss()
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, e.message.toString())
                    toast("Please Enter Valid Email & Password")
                    progressDialog.dialog.dismiss()
                }
            }
        }
    }

    private fun initializeViews() {
        binding.loginButton.apply {
            translationY = 300F
            alpha = 0F
            animate().translationY(0F).alpha(1F).setDuration(800).setStartDelay(400).start()
        }

        binding.loginCard.apply {
            translationX = 800F
            alpha = 0F
            animate().translationX(0F).alpha(1F).setDuration(800).setStartDelay(300).start()
        }

        binding.verticalView.apply {
            translationZ = 800F
            alpha = 0F
            animate().translationZ(0F).alpha(1F).setDuration(800).setStartDelay(300).start()
        }
    }

    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this)
    }
}