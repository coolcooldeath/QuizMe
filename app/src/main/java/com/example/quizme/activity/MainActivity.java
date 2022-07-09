package com.example.quizme.activity;

import static com.example.quizme.retrofit.ApiManager.ip;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizme.R;
import com.example.quizme.db.AnswerModel;
import com.example.quizme.db.CategoryModel;
import com.example.quizme.db.DbHelper;
import com.example.quizme.db.Question;
import com.example.quizme.fragments.HomeFragment;
import com.example.quizme.fragments.LeaderboardsFragment;
import com.example.quizme.fragments.ProfileFragment;
import com.example.quizme.databinding.ActivityMainBinding;
import com.example.quizme.fragments.StartupFragment;
import com.example.quizme.retrofit.JsonPlaceholderQuestionApi;

import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import me.ibrahimsn.lib.OnItemSelectedListener;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DbHelper databaseHelper;
    SQLiteDatabase db;
    SharedPreferences mSettings;
    private void openInternetDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
        quitDialog.setTitle(R.string.internet).setMessage(R.string.internet_message);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }
    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = getSharedPreferences("mysettings", 0);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        databaseHelper.onCreate(db);
        setSupportActionBar(binding.toolbar);


        if(isNetworkAvailable(this.getApplicationContext())){

            Retrofit retrofit = new Retrofit.Builder()
                    /*.baseUrl("http://quizapp.ddns.net:8082/api/")*/
                    .baseUrl(ip)
                    .addConverterFactory(GsonConverterFactory.create()).client(getUnsafeOkHttpClient().build())
                    .build();

            JsonPlaceholderQuestionApi jsonPlaceHolderApi = retrofit.create(JsonPlaceholderQuestionApi.class);

            Call<List<Question>> callQuestion = jsonPlaceHolderApi.getQuestions("Bearer "+mSettings.getString("access_token",""));
            Call<List<CategoryModel>> callCategory = jsonPlaceHolderApi.getCategories("Bearer "+mSettings.getString("access_token",""));
            Call<List<AnswerModel>> callAnswer = jsonPlaceHolderApi.getAnswers("Bearer "+mSettings.getString("access_token",""));



            callCategory.enqueue(new Callback<List<CategoryModel>>() {
                @Override
                public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {

                    if (!response.isSuccessful()) {

                        Log.d("HttpCodeID", "Code: " + response.code());
                        return;
                    }

                    List<CategoryModel> categories = response.body();
                    List<CategoryModel> categoriesMobile = databaseHelper.getAllCategories();
                    if(categories!=null&&!categoriesMobile.equals(categories)){
                        databaseHelper.deleteAllCategories();
                        databaseHelper.addCategories(categories);
                        Log.d("HttpCodeID", "Обновились категории");


                    }



                }

                @Override
                public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                    Log.d("HttpCodeID", t.getMessage());
                }
            });
            callQuestion.enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {

                    if (!response.isSuccessful()) {

                        Log.d("HttpCodeID", "Code: " + response.code());
                        return;
                    }

                    List<Question> questions = response.body();

                    List<Question> questionsMobile = databaseHelper.getAllQuestions();
                    if(questions!=null&&!questionsMobile.equals(questions)){
                        databaseHelper.deleteAllQuestions();
                        databaseHelper.addQuestions(questions);

                    }




                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    Log.d("HttpCodeID", t.getMessage());
                }
            });
            callAnswer.enqueue(new Callback<List<AnswerModel>>() {
                @Override
                public void onResponse(Call<List<AnswerModel>> call, Response<List<AnswerModel>> response) {

                    if (!response.isSuccessful()) {

                        Log.d("HttpCodeID", "Code: " + response.code());
                        return;
                    }

                    List<AnswerModel> answers = response.body();

                    List<AnswerModel> answersmobile = databaseHelper.getAllAnswers();
                    if(answers!=null&&!answersmobile.equals(answers)){
                        databaseHelper.deleteAllAnswers();
                        databaseHelper.addAnswers(answers);
                        Log.d("HttpCodeID", "Обновились ответы");
                        /*for (AnswerModel answer:
                                answers) {
                            Log.d("HttpCodeID", "Answer - "+answer.getAnswer());
                            Log.d("HttpCodeID", "Id - "+answer.getId());
                            Log.d("HttpCodeID", "Id question - "+answer.getIdQuestion());

                        }

                        for (AnswerModel answer:
                                answersmobile) {
                            Log.d("HttpCodeID", "Answer answersmobile - "+answer.getAnswer());
                            Log.d("HttpCodeID", "Id answersmobile - "+answer.getId());
                            Log.d("HttpCodeID", "Id question answersmobile - "+answer.getIdQuestion());

                        }
*/
                    }



                }

                @Override
                public void onFailure(Call<List<AnswerModel>> call, Throwable t) {
                    Log.d("HttpCodeID", t.getMessage());

                }
            });
        }
        else{
            Toast.makeText(this, R.string.internet_message, Toast.LENGTH_SHORT).show();
           /* openInternetDialog();*/
        }

















        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new StartupFragment());
        transaction.commit();

        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        transaction.replace(R.id.content, new StartupFragment());
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.content, new HomeFragment());
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.content, new LeaderboardsFragment());
                        transaction.commit();
                        break;
                    case 3:
                        transaction.replace(R.id.content, new ProfileFragment());
                        transaction.commit();
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}