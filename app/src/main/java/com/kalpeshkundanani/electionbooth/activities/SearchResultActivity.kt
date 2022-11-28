package com.kalpeshkundanani.electionbooth.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SimpleSQLiteQuery
import com.kalpeshkundanani.electionbooth.Constants
import com.kalpeshkundanani.electionbooth.R
import com.kalpeshkundanani.electionbooth.VoterApplication
import com.kalpeshkundanani.electionbooth.db.VoterDao
import com.kalpeshkundanani.electionbooth.db.entity.Voters
import com.kalpeshkundanani.electionbooth.recyclerview.SearchResultAdapter

class SearchResultActivity : AppCompatActivity() {
    private lateinit var PART_NO: String
    private lateinit var PART_NAME_EN: String
    private lateinit var SECTION_NO: String
    private lateinit var SECTION_NAME_EN: String
    private lateinit var FM_NAME_EN: String
    private lateinit var LASTNAME_EN: String
    private lateinit var RLN_FM_NM_EN: String
    private lateinit var GENDER: String
    private lateinit var MOBILE_NO: String
    private lateinit var EPIC_NO: String

    private val voterDao: VoterDao
        get() {
            val voterApplication = applicationContext as VoterApplication
            return voterApplication.db.voterDao()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        PART_NO = intent.getStringExtra("PART_NO") ?: ""
        PART_NAME_EN = intent.getStringExtra("PART_NAME_EN") ?: ""
        SECTION_NO = intent.getStringExtra("SECTION_NO") ?: ""
        SECTION_NAME_EN = intent.getStringExtra("SECTION_NAME_EN") ?: ""
        FM_NAME_EN = intent.getStringExtra("FM_NAME_EN") ?: ""
        LASTNAME_EN = intent.getStringExtra("LASTNAME_EN") ?: ""
        RLN_FM_NM_EN = intent.getStringExtra("RLN_FM_NM_EN") ?: ""
        GENDER = intent.getStringExtra("GENDER") ?: ""
        MOBILE_NO = intent.getStringExtra("MOBILE_NO") ?: ""
        EPIC_NO = intent.getStringExtra("EPIC_NO") ?: ""

        findVoters {
            if (it.isEmpty()) {
                findViewById<TextView>(R.id.tv_no_data_found).visibility = View.VISIBLE
                findViewById<RecyclerView>(R.id.rv_search_results).visibility = View.GONE
            } else {
                findViewById<RecyclerView>(R.id.rv_search_results).visibility = View.VISIBLE
                findViewById<TextView>(R.id.tv_no_data_found).visibility = View.GONE
                initSearchResultView(it)
            }
        }
    }

    private fun initSearchResultView(voters: List<Voters>) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_search_results)
        var searchResultAdapter: SearchResultAdapter? = null
        searchResultAdapter = SearchResultAdapter(voters) { voter ->
            AlertDialog.Builder(this)
                .setMessage("Are you sure this person has voted?")
                .setPositiveButton("Yes") { _, _ ->
                    Thread {
                        voterDao.markVoted(voter.EPIC_NO, 1)
                        runOnUiThread {
                            findVoters { voters ->
                                val indexOfFirst =
                                    voters.indexOfFirst { it.EPIC_NO == voter.EPIC_NO }
                                searchResultAdapter?.markVoted(indexOfFirst)
                            }
                        }
                    }.start()
                }
                .setNegativeButton("No", null)
                .show()
        }
        recyclerView.adapter = searchResultAdapter
    }

    private fun findVoters(onComplete: (List<Voters>) -> Unit) {
        val dialog = ProgressDialog.show(
            this, "",
            "Loading. Please wait...", true
        )
        dialog.show()
        Thread {
            val list = arrayListOf<String>()
            val arrayList = arrayListOf<String>()
            checkParameter("PART_NO", PART_NO, list, arrayList)
            checkParameter("PART_NAME_EN", PART_NAME_EN, list, arrayList)
            checkParameter("SECTION_NO", SECTION_NO, list, arrayList)
            checkParameter("SECTION_NAME_EN", SECTION_NAME_EN, list, arrayList)
            checkParameter("FM_NAME_EN", FM_NAME_EN, list, arrayList)
            checkParameter("LASTNAME_EN", LASTNAME_EN, list, arrayList)
            checkParameter("RLN_FM_NM_EN", RLN_FM_NM_EN, list, arrayList)
            checkParameter("GENDER", GENDER, list, arrayList)
            checkParameter("MOBILE_NO", MOBILE_NO, list, arrayList)
            checkParameter("EPIC_NO", EPIC_NO, list, arrayList)

            val queryString =
                if (list.isEmpty()) "SELECT * FROM voters" else "SELECT * FROM voters WHERE ${
                    list.joinToString(" AND ")
                }"
            val rawQuery = SimpleSQLiteQuery(queryString, arrayList.toArray())

            val result = voterDao.find(rawQuery)
            runOnUiThread {
                onComplete(result)
                dialog.dismiss()
            }
        }.start()
    }

    private fun checkParameter(
        key: String,
        value: String,
        array: ArrayList<String>,
        arrayList: ArrayList<String>
    ) {
        if (value.isNotBlank() && value != Constants.ALL) {
            array.add("UPPER($key) LIKE '%' || ? || '%' ")
            arrayList.add(value.uppercase())
        }
    }
}