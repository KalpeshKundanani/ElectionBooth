package com.kalpeshkundanani.electionbooth.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.sqlite.db.SimpleSQLiteQuery
import com.kalpeshkundanani.electionbooth.Constants
import com.kalpeshkundanani.electionbooth.R
import com.kalpeshkundanani.electionbooth.VoterApplication
import com.kalpeshkundanani.electionbooth.data.Cache
import com.kalpeshkundanani.electionbooth.db.VoterDao


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var PART_NO_Suggestions: Array<String>
    private lateinit var SECTION_NO_Suggestions: Array<String>
    private lateinit var SECTION_NAME_EN_Suggestions: Array<String>
    private lateinit var GENDER_Suggestions: Array<String>
    private lateinit var EPIC_NO_Suggestions: Array<String>
    private lateinit var MOBILE_NO_Suggestions: Array<String>

    private val voterDao: VoterDao
        get() {
            val voterApplication = applicationContext as VoterApplication
            return voterApplication.db.voterDao()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_search).setOnClickListener(this)
        findViewById<Button>(R.id.btn_clear).setOnClickListener(this)
        withSuggestions {
            setAutoComplete(EPIC_NO_Suggestions, R.id.ac_epic_number)
            setAutoComplete(PART_NO_Suggestions, R.id.ac_part_number)
            setAutoComplete(SECTION_NO_Suggestions, R.id.ac_section_number)
            setAutoComplete(SECTION_NAME_EN_Suggestions, R.id.ac_section_name)
            setAutoComplete(GENDER_Suggestions, R.id.ac_gender)
            setAutoComplete(MOBILE_NO_Suggestions, R.id.ac_mobile_number)
        }
    }

    private fun setAutoComplete(options: Array<String>, id: Int) {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line, options
        )
        val textView = findViewById<View>(id)
        if (textView is AutoCompleteTextView) {
            textView.setAdapter(adapter)
        }
    }

    private fun withSuggestions(onComplete: () -> Unit) {
        val dialog = ProgressDialog.show(
            this, "",
            "Loading. Please wait...", true
        )
        dialog.show()
        Thread {
            PART_NO_Suggestions = findDistinct("PART_NO")
            SECTION_NO_Suggestions = findDistinct("SECTION_NO")
            SECTION_NAME_EN_Suggestions = findDistinct("SECTION_NAME_EN")
            GENDER_Suggestions = findDistinct("GENDER")
            MOBILE_NO_Suggestions = findDistinct("MOBILE_NO")
            EPIC_NO_Suggestions = findDistinct("EPIC_NO")
            runOnUiThread {
                dialog.dismiss()
                onComplete()
            }
        }.start()
    }

    private fun findDistinct(columnName: String): Array<String> {
        val queryText = "SELECT DISTINCT $columnName FROM voters ORDER BY $columnName+0 ASC"
        val rawQuery = SimpleSQLiteQuery(queryText)
        val list = arrayListOf(Constants.ALL)
        list.addAll(voterDao.findDistinct(rawQuery).filterNotNull())
        return list.toTypedArray()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure that you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                Cache.setIsLoggedIn(this@MainActivity, false)
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                this@MainActivity.startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_search -> onSearchClick()
            R.id.btn_clear -> onClearClicked()
        }
    }

    private fun onClearClicked() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to clear filters?")
            .setPositiveButton("Yes") { _, _ ->
                findViewById<AutoCompleteTextView>(R.id.ac_part_number).setText("")
                findViewById<EditText>(R.id.ac_part_name).setText("")
                findViewById<AutoCompleteTextView>(R.id.ac_section_number).setText("")
                findViewById<AutoCompleteTextView>(R.id.ac_section_name).setText("")
                findViewById<EditText>(R.id.ac_first_name).setText("")
                findViewById<EditText>(R.id.ac_last_name).setText("")
                findViewById<EditText>(R.id.ac_middle_name).setText("")
                findViewById<AutoCompleteTextView>(R.id.ac_gender).setText("")
                findViewById<AutoCompleteTextView>(R.id.ac_mobile_number).setText("")
                findViewById<AutoCompleteTextView>(R.id.ac_epic_number).setText("")
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun onSearchClick() {
        val intent = Intent(this, SearchResultActivity::class.java)
        intent.putExtra(
            "PART_NO",
            findViewById<AutoCompleteTextView>(R.id.ac_part_number).text.toString()
        )
        intent.putExtra("PART_NAME_EN", findViewById<EditText>(R.id.ac_part_name).text.toString())
        intent.putExtra(
            "SECTION_NO",
            findViewById<AutoCompleteTextView>(R.id.ac_section_number).text.toString()
        )
        intent.putExtra(
            "SECTION_NAME_EN",
            findViewById<AutoCompleteTextView>(R.id.ac_section_name).text.toString()
        )
        intent.putExtra("FM_NAME_EN", findViewById<EditText>(R.id.ac_first_name).text.toString())
        intent.putExtra("LASTNAME_EN", findViewById<EditText>(R.id.ac_last_name).text.toString())
        intent.putExtra("RLN_FM_NM_EN", findViewById<EditText>(R.id.ac_middle_name).text.toString())
        intent.putExtra(
            "GENDER",
            findViewById<AutoCompleteTextView>(R.id.ac_gender).text.toString()
        )
        intent.putExtra(
            "MOBILE_NO",
            findViewById<AutoCompleteTextView>(R.id.ac_mobile_number).text.toString()
        )
        intent.putExtra(
            "EPIC_NO",
            findViewById<AutoCompleteTextView>(R.id.ac_epic_number).text.toString()
        )
        startActivity(intent)
    }
}