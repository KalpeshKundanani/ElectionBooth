package com.kalpeshkundanani.electionbooth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "newE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        disableAppAfter("08/12/2022") ?: return
        if(Cache.getIsLoggedIn(this)) {
            openApp()
            return
        }
        setPhoneNumberInputView()
        findViewById<View>(R.id.btn_resend).setOnClickListener(this)
        findViewById<View>(R.id.btn_submit).setOnClickListener(this)
        findViewById<View>(R.id.btn_get_otp).setOnClickListener(this)
    }

    private fun setPhoneNumberInputView() {
        findViewById<View>(R.id.tv_cant_use_app).visibility = View.GONE
        findViewById<View>(R.id.ti_otp).visibility = View.GONE
        findViewById<View>(R.id.btn_resend).visibility = View.GONE
        findViewById<View>(R.id.btn_submit).visibility = View.GONE

        findViewById<View>(R.id.btn_get_otp).visibility = View.VISIBLE
        findViewById<View>(R.id.ti_phone_number).visibility = View.VISIBLE
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
            findViewById<View>(R.id.btn_get_otp).visibility = View.GONE
            findViewById<View>(R.id.btn_resend).visibility = View.GONE
            findViewById<View>(R.id.btn_submit).visibility = View.GONE
            null
        } else {
            findViewById<View>(R.id.tv_cant_use_app).visibility = View.GONE
            findViewById<View>(R.id.ti_phone_number).visibility = View.VISIBLE
            findViewById<View>(R.id.ti_otp).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_get_otp).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_resend).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_submit).visibility = View.VISIBLE
            true
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_resend -> onResendClick()
            R.id.btn_submit -> onSubmitClick()
            R.id.btn_get_otp -> onGetOtpClick()
        }
    }

    private fun onGetOtpClick() {
        val phoneInput = findViewById<EditText>(R.id.et_phone_number).text.toString()
        val hasPhoneInput = phoneInput.isNotEmpty() && phoneInput.length == 10
        if (hasPhoneInput) {
            Toast.makeText(this, "OTP sent to $phoneInput", Toast.LENGTH_LONG).show()
            findViewById<View>(R.id.ti_otp).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_get_otp).visibility = View.GONE
            findViewById<View>(R.id.btn_resend).visibility = View.VISIBLE
            findViewById<View>(R.id.btn_submit).visibility = View.VISIBLE
            findViewById<EditText>(R.id.et_otp).text = null
        } else {
            Toast.makeText(this, "Please enter 10 digit phone number", Toast.LENGTH_LONG).show()
        }
    }

    private fun onSubmitClick() {
        val otp = findViewById<EditText>(R.id.et_otp).text.toString()
        val hasOtp = otp.isNotEmpty()
        if (hasOtp) {
            Cache.setIsLoggedIn(this, true)
            openApp()
        } else {
            Toast.makeText(this, "Please enter OTP.", Toast.LENGTH_LONG).show()
        }
    }

    private fun openApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun onResendClick() {
        onGetOtpClick()
    }
}