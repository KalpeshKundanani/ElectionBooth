package com.kalpeshkundanani.electionbooth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kalpeshkundanani.electionbooth.data.Cache
import com.kalpeshkundanani.electionbooth.models.BoothUser
import java.text.SimpleDateFormat
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "newE"
    private var boothUsers: List<BoothUser?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        disableAppAfter("08/12/2022") ?: return
        loadData {
            if (Cache.getIsLoggedIn(this)) {
                openApp()
                return@loadData
            }
            findViewById<View>(R.id.btn_submit).setOnClickListener(this)
        }
    }

    private fun loadData(next: () -> Unit) {
        val dialog = ProgressDialog.show(
            this, "",
            "Loading. Please wait...", true
        )
        dialog.show()
        Thread {
            val gson = Gson()
            val listType = object : TypeToken<List<BoothUser?>?>() {}.type
            val fileName = "booth_users.json"
            val json = application.assets.open(fileName).bufferedReader().use {
                it.readText()
            }
            boothUsers = gson.fromJson(json, listType)
            runOnUiThread {
                dialog.dismiss()
                next()
            }
        }.start()
    }

    private fun disableAppAfter(dateStr: String): Boolean? {
        val dateObj: Date? = try {
            val curFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            curFormatter.parse(dateStr)
        } catch (e: Exception) {
            null
        }
        return if (dateObj == null || Date().after(dateObj)) {
            findViewById<View>(R.id.tv_cant_use_app).visibility = View.VISIBLE
            findViewById<View>(R.id.ti_phone_number).visibility = View.GONE
            findViewById<View>(R.id.ti_otp).visibility = View.GONE
            findViewById<View>(R.id.btn_submit).visibility = View.GONE
            null
        } else {
            findViewById<View>(R.id.tv_cant_use_app).visibility = View.GONE
            findViewById<View>(R.id.ti_phone_number).visibility = View.VISIBLE
            findViewById<View>(R.id.ti_otp).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_submit).visibility = View.VISIBLE
            true
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_submit -> onSubmitClick()
        }
    }

    private fun onSubmitClick() {

        val phoneNumberInput = findViewById<EditText>(R.id.et_phone_number)
        val phoneNumber = phoneNumberInput.text.toString()
        val hasPhoneNumber = phoneNumber.isNotEmpty() && phoneNumber.length == 10

        if (!hasPhoneNumber) {
            phoneNumberInput.requestFocus()
            phoneNumberInput.error = "Please enter 10 digit phone number."
        } else {
            val user = boothUsers?.filterNotNull()
                ?.firstOrNull { it.phoneNumber == phoneNumber.toLongOrNull() }
            if (user == null) {
                phoneNumberInput.requestFocus()
                phoneNumberInput.error = "Number not found."
            } else {
                val otpForNumber = user.otp

                val otpTextField = findViewById<EditText>(R.id.et_otp)
                otpTextField.requestFocus()
                val otp = otpTextField.text.toString()
                val hasOtp = otp.isNotEmpty()
                if (hasOtp) {
                    if (otpForNumber == otp.toLongOrNull()) {
                        Cache.setIsLoggedIn(this, true)
                        openApp()
                    } else {
                        otpTextField.error = "Incorrect OTP."
                    }
                } else {
                    otpTextField.error = "Please enter OTP."
                }

            }
        }
    }

    private fun openApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}