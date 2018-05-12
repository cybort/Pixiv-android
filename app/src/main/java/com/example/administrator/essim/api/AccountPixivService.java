package com.example.administrator.essim.api;

import com.example.administrator.essim.response.PixivAccountsResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AccountPixivService
{
    @FormUrlEncoded
    @POST("/api/provisional-accounts/create")
    Call<PixivAccountsResponse> createProvisionalAccount(@Field("user_name") String paramString1,
                                                         @Field("ref") String paramString2,
                                                         @Header("Authorization") String paramString3);
}