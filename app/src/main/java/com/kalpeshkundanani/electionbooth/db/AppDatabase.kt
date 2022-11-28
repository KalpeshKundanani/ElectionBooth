package com.kalpeshkundanani.electionbooth.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kalpeshkundanani.electionbooth.db.entity.Voters

@Database(entities = [Voters::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun voterDao(): VoterDao
}