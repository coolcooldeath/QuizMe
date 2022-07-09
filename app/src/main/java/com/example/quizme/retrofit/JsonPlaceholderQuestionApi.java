package com.example.quizme.retrofit;
import android.content.SharedPreferences;

import com.example.quizme.db.AnswerContract;
import com.example.quizme.db.AnswerModel;
import com.example.quizme.db.CategoryModel;
import com.example.quizme.db.Question;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface JsonPlaceholderQuestionApi {

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("questionmobile")
    Call<List<Question>> getQuestions(@Header("Authorization")String token);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("categorymobile")
    Call<List<CategoryModel>> getCategories(@Header("Authorization")String token);
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("answermobile")
    Call<List<AnswerModel>> getAnswers(@Header("Authorization")String token);
}
