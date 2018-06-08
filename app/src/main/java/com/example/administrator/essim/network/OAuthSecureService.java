package com.example.administrator.essim.network;

import com.example.administrator.essim.response.PixivOAuthResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OAuthSecureService {

    @FormUrlEncoded
    @POST("/auth/token")
    Call<PixivOAuthResponse> postAuthToken(@FieldMap Map<String, Object> paramMap);

    @FormUrlEncoded
    @POST("/auth/token")
    Call<PixivOAuthResponse> postRefreshAuthToken(@Field("client_id") String paramString1,
                                                  @Field("client_secret") String paramString2,
                                                  @Field("grant_type") String paramString3,
                                                  @Field("refresh_token") String paramString4,
                                                  @Field("device_token") String paramString5,
                                                  @Field("get_secure_url") boolean paramBoolean);

}
