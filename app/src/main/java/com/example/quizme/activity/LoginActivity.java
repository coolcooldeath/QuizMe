package com.example.quizme.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.quizme.R;
import com.example.quizme.databinding.ActivityLoginBinding;
import com.example.quizme.db.DbHelper;
import com.example.quizme.retrofit.ApiManager;
import com.example.quizme.retrofit.ResponseToken;
import com.example.quizme.retrofit.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private void openInternetDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
        quitDialog.setTitle(R.string.internet).setMessage(R.string.internet_message_login);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void openInvalidLoginDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
        quitDialog.setTitle(R.string.login_invalid).setMessage(R.string.login_message);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }
    ActivityLoginBinding binding;
    ProgressDialog dialog;
    DbHelper databaseHelper;
    SQLiteDatabase db;
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        mSettings = getSharedPreferences("mysettings", 0);
        boolean authorize = mSettings.getBoolean("authorize", false);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Подождите...");
        ApiManager apiManager = ApiManager.getInstance();
        if(authorize) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
                String login, pass;
                login = binding.loginBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                User user = new User();
                user.setLogin(login);
                user.setPassword(pass);
                if(isNetworkAvailable(getApplicationContext())){


                    apiManager.loginUser(user, new Callback<User>(){
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        User responseUser = response.body();

                        if (response.isSuccessful() && responseUser.getLogin() != null) {
                            dialog.dismiss();
                            apiManager.getToken(user,"Bearer "+mSettings.getString("access_token",""), new Callback<ResponseToken>() {
                                @Override
                                public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                                    dialog.show();
                                    ResponseToken responseToken = response.body();
                                    Log.d("HttpCodeID","responseUser.getCoins() " + responseUser.getCoins());
                                    Log.d("HttpCodeID","response.code " + response.code());

                                    if (response.isSuccessful() && responseToken != null) {
                                        binding.createNewBtn.setEnabled(false);
                                        dialog.dismiss();
                                        Log.d("HttpCodeID","responseToken.getAccess_token() " + responseToken.getAccess_token());

                                    } else {
                                        dialog.dismiss();
                                        openInvalidLoginDialog();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseToken> call, Throwable t) {
                                    openInvalidLoginDialog();
                                    Log.d("HttpCodeID","Error is " + t.getMessage());
                                }
                            });

                        } else {
                            dialog.dismiss();
                            openInvalidLoginDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        openInvalidLoginDialog();
                        Log.d("HttpCodeID","Error is " + t.getMessage());
                        dialog.dismiss();
                    }
                });}

                else{
                    dialog.dismiss();
                    openInternetDialog();}



            }

        });






        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.createNewBtn.setEnabled(false);
                finish();
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));

            }
        });




    }

    @Override
    public void onBackPressed() {
        finish();
    }
}