package com.example.budgetProject

import com.google.gson.annotations.SerializedName

class FacebookDTO {
    @SerializedName("name")
    lateinit var name: String

    @SerializedName("email")
    lateinit var email: String

    @SerializedName("id")
    lateinit var id: String
}