package com.example.administrator.essim.network;


import com.example.administrator.essim.response.BookmarkAddResponse;
import com.example.administrator.essim.response.CollectionResponse;
import com.example.administrator.essim.response.IllustCommentsResponse;
import com.example.administrator.essim.response.IllustDetailResponse;
import com.example.administrator.essim.response.IllustRankingResponse;
import com.example.administrator.essim.response.IllustfollowResponse;
import com.example.administrator.essim.response.PixivResponse;
import com.example.administrator.essim.response.RecommendResponse;
import com.example.administrator.essim.response.SearchIllustResponse;
import com.example.administrator.essim.response.SearchUserResponse;
import com.example.administrator.essim.response.SpecialCollectionResponse;
import com.example.administrator.essim.response.TrendingtagResponse;
import com.example.administrator.essim.response.UgoiraMetadataResponse;
import com.example.administrator.essim.response.UserDetailResponse;
import com.example.administrator.essim.response.UserIllustsResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AppApiPixivService {

    @GET("/v1/search/illust?filter=for_ios")
    Call<SearchIllustResponse> getSearchIllust(@Query("word") String paramString1,
                                               @Query("sort") String paramString2,
                                               @Query("search_target") String paramString3,
                                               @Query("bookmark_num") Integer paramInteger,
                                               @Query("duration") String paramString4,
                                               @Header("Authorization") String paramString5);

    @FormUrlEncoded
    @POST("/v2/illust/bookmark/add")
    Call<BookmarkAddResponse> postLikeIllust(@Header("Authorization") String paramString1,
                                             @Field("illust_id") long paramLong,
                                             @Field("restrict") String paramString2,
                                             @Field("tags[]") List<String> paramList);

    @GET("/v1/user/bookmarks/illust")
    Call<UserIllustsResponse> getLikeIllust(@Header("Authorization") String paramString1,
                                            @Query("user_id") long paramLong,
                                            @Query("restrict") String paramString2,
                                            @Query("tag") String paramString3);

    @FormUrlEncoded
    @POST("v1/illust/comment/add")
    Call<ResponseBody> postIllustComment(@Header("Authorization") String paramString1,
                                         @Field("illust_id") long paramLong,
                                         @Field("comment") String paramString2,
                                         @Field("parent_comment_id") Integer paramInteger);

    @GET("/v1/user/following?filter=for_ios")
    Call<SearchUserResponse> getUserFollowing(@Header("Authorization") String paramString1,
                                              @Query("user_id") long paramLong,
                                              @Query("restrict") String paramString2);

    @GET("v1/ugoira/metadata")
    Call<UgoiraMetadataResponse> getUgoiraMetadata(@Header("Authorization") String paramString, @Query("illust_id") long paramLong);

    @GET("/v1/user/illusts?filter=for_ios")
    Call<UserIllustsResponse> getUserIllusts(@Header("Authorization") String paramString1,
                                             @Query("user_id") long paramLong,
                                             @Query("type") String paramString2);

    @GET("/v1/illust/ranking?filter=for_ios")
    Call<IllustRankingResponse> getIllustRanking(@Header("Authorization") String paramString1,
                                                 @Query("mode") String paramString2,
                                                 @Query("date") String paramString3);

    @FormUrlEncoded
    @POST("/v1/user/follow/add")
    Call<BookmarkAddResponse> postFollowUser(@Header("Authorization") String paramString1,
                                             @Field("user_id") long paramLong,
                                             @Field("restrict") String paramString2);

    @GET("/v2/illust/follow")
    Call<IllustfollowResponse> getFollowIllusts(@Header("Authorization") String paramString1, @Query("restrict") String paramString2);

    @FormUrlEncoded
    @POST("/v1/user/follow/delete")
    Call<ResponseBody> postUnfollowUser(@Header("Authorization") String paramString,
                                        @Field("user_id") long paramLong);

    @GET
    Call<IllustCommentsResponse> getNextComment(@Header("Authorization") String paramString1,
                                                @Url String paramString2);

    @GET("/v1/search/user?filter=for_ios")
    Call<SearchUserResponse> getSearchUser(@Header("Authorization") String paramString1,
                                           @Query("word") String paramString2);

    @GET("/v1/illust/detail?filter=for_ios")
    Call<IllustDetailResponse> getIllust(@Header("Authorization") String paramString, @Query("illust_id") long paramLong);


    @GET("/v1/search/autocomplete")
    Call<PixivResponse> getSearchAutoCompleteKeywords(@Header("Authorization") String paramString1,
                                                      @Query("word") String paramString2);

    @GET
    Call<SearchUserResponse> getNextUser(@Header("Authorization") String paramString1,
                                         @Url String paramString2);

    @FormUrlEncoded
    @POST("/v1/illust/bookmark/delete")
    Call<ResponseBody> postUnlikeIllust(@Header("Authorization") String paramString,
                                        @Field("illust_id") long paramLong);

    @GET("/v1/illust/comments")
    Call<IllustCommentsResponse> getIllustComments(@Header("Authorization") String paramString,
                                                   @Query("illust_id") long paramLong);

    @GET("/v1/user/detail?filter=for_ios")
    Call<UserDetailResponse> getUserDetail(@Header("Authorization") String paramString,
                                           @Query("user_id") long paramLong);

    @GET("/v1/illust/recommended?content_type=illust&filter=for_ios&include_ranking_label=true")
    Call<RecommendResponse> getRecommend(@Header("Authorization") String paramString);

    @GET
    Call<RecommendResponse> getNext(@Header("Authorization") String paramString1,
                                    @Url String paramString2);

    @GET("/v1/trending-tags/illust?filter=for_ios")
    Call<TrendingtagResponse> getIllustTrendTags(@Header("Authorization") String paramString);

    @GET
    Call<UserIllustsResponse> getNextUserIllusts(@Header("Authorization") String paramString1,
                                                 @Url String paramString2);

    @GET("/pixiv/v1/")
    Call<SpecialCollectionResponse> getSpecialCollection(@Query("type") String cate,
                                                         @Query("page") int page);

    @GET("/pixiv/v1/?type=showcase")
    Call<CollectionResponse> getCollecDetail(@Query("id") String cate);
}
