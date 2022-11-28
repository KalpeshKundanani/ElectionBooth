package com.kalpeshkundanani.electionbooth

import android.app.Application
import androidx.room.Room
import com.kalpeshkundanani.electionbooth.db.AppDatabase
import com.pluto.Pluto
import com.pluto.plugins.exceptions.PlutoExceptionsPlugin

class VoterApplication: Application() {
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        Pluto.Installer(this)
            .addPlugin(PlutoExceptionsPlugin("exceptions"))
            .install()

        createDataBase()
    }

    private fun createDataBase() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        )
            .createFromAsset("voters_database.db")
            .build()
    }
}