package com.kalpeshkundanani.electionbooth.models

import com.google.gson.annotations.SerializedName

data class BoothUser(
    @SerializedName("phone_number")
    val phoneNumber: Long,
    @SerializedName("otp")
    val otp: Long
)
