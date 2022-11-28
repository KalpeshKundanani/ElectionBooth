package com.kalpeshkundanani.electionbooth.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Voters(
    @ColumnInfo(name = "PART_NO") val PART_NO: String?,
    @ColumnInfo(name = "PART_NAME_EN") val PART_NAME_EN: String?,
    @ColumnInfo(name = "PSBUILDING_NAME_EN") val PSBUILDING_NAME_EN: String?,
    @ColumnInfo(name = "SECTION_NO") val SECTION_NO: String?,
    @ColumnInfo(name = "SECTION_NAME_EN") val SECTION_NAME_EN: String?,
    @ColumnInfo(name = "FM_NAME_EN") val FM_NAME_EN: String?,
    @ColumnInfo(name = "LASTNAME_EN") val LASTNAME_EN: String?,
    @ColumnInfo(name = "RLN_FM_NM_EN") val RLN_FM_NM_EN: String?,
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "EPIC_NO") val EPIC_NO: String,
    @ColumnInfo(name = "GENDER") val GENDER: String?,
    @ColumnInfo(name = "AGE") val AGE: String?,
    @ColumnInfo(name = "MOBILE_NO") val MOBILE_NO: String?,
    @ColumnInfo(name = "voted", defaultValue = "0") var voted: Int?
)
