package com.example.administrator.essim.network

import com.example.administrator.essim.response.PixivAccountsResponse

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountPixivService {
    @FormUrlEncoded
    @POST("/api/provisional-accounts/create")
    fun createProvisionalAccount(@Field("user_name") paramString1: String,
                                 @Field("ref") paramString2: String,
                                 @Header("Authorization") paramString3: String): Call<PixivAccountsResponse>
}