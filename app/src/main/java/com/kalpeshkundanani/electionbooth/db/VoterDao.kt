package com.kalpeshkundanani.electionbooth.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.kalpeshkundanani.electionbooth.db.entity.Voters


@Dao
interface VoterDao {
    @RawQuery
    fun find(query: SupportSQLiteQuery): List<Voters>

    @RawQuery
    fun findDistinct(query: SupportSQLiteQuery): List<String?>

    @Query("UPDATE voters SET voted = :voted WHERE EPIC_NO =:epicId")
    fun markVoted(epicId: String, voted: Int)
}