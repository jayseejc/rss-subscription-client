package com.jayseeofficial.rssconsumer.rest.retrofit;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by jon on 21/03/15.
 */
public interface RssProviderService {

    @FormUrlEncoded
    @POST("/user/new")
    public void newUser(@Field("username") String username, @Field("password") String password,
                        Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/feed/{id}")
    public void getFeed(@Field("username") String username, @Field("password") String password,
                        @Path("id") int id, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/feeds")
    public void getAllFeeds(@Field("username") String username, @Field("password") String password,
                            Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST("/feed/new")
    public void newFeed(@Field("username") String username, @Field("password") String password,
                        @Field("title") String title, @Field("uri") String url, Callback<JsonElement> callback);

    @FormUrlEncoded
    @DELETE("/feed/{id}")
    public void deleteFeed(@Field("username") String username, @Field("password") String password,
                           @Path("id") int id, Callback<JsonElement> callback);

}
