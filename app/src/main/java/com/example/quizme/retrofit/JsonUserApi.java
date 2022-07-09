package com.example.quizme.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface JsonUserApi {


    @POST("login")
    Call<User> loginUser(@Body User user);

    @POST("tokenMobile")
    Call<ResponseToken> getToken(@Body User user,@Header("Authorization")String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("coinAdd")
    Call<FlagObject> getToken(@Body Coin coin,@Header("Authorization")String token);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("userAdd")
    Call<FlagObject> setUser(@Body User user,@Header("Authorization")String token);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("passwordChange")
    Call<FlagObject> setPassword(@Body User user,@Header("Authorization")String token);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("leaderboard")
    Call<List<User>> getLeaderboard(@Header("Authorization")String token);


}
