package com.example.quizme.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.quizme.R;
import com.example.quizme.databinding.ActivitySignupBinding;
import com.example.quizme.retrofit.ApiManager;
import com.example.quizme.retrofit.FlagObject;
import com.example.quizme.retrofit.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AppCompatActivity {
    SharedPreferences mSettings;
    ActivitySignupBinding binding;
    ProgressDialog dialog;

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

    public void openEmptyLoginDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
        quitDialog.setTitle(R.string.login_invalid).setMessage(R.string.login_empty);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }

    public void openInvalidLoginDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
        quitDialog.setTitle(R.string.login_invalid).setMessage(R.string.login_repeat);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new ProgressDialog(this);
        dialog.setMessage("Создаем аккаунт...");


        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String login, pass;
                login = binding.nameBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                mSettings = getSharedPreferences("mysettings", 0);
                User user = new User();
                ApiManager apiManager = ApiManager.getInstance();
                user.setLogin(login);
                user.setPassword(pass);
                user.setCoins(0);
                Log.d("HttpCodeID","login + pass " + user.getLogin() + " "+user.getPassword()+" "+user.getCoins());

                if(isNetworkAvailable(getApplicationContext())){
                    apiManager.setUser(user,"Bearer "+mSettings.getString("access_token",""), new Callback<FlagObject>() {
                        @Override
                        public void onResponse(Call<FlagObject> call, Response<FlagObject> response) {
                            FlagObject responseFlag = response.body();

                            if(!login.isEmpty()&&!pass.isEmpty()){ if (response.isSuccessful() && responseFlag != null && responseFlag.isSuccessful()) {
                                dialog.dismiss();
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putBoolean("authorize", true);
                                editor.putLong("coins", user.getCoins());
                                editor.putString("username", user.getLogin());
                                editor.apply();
                                finish();
                                binding.createNewBtn.setEnabled(false);
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));


                            } else {
                                dialog.dismiss();
                                openInvalidLoginDialog();
                                Log.d("HttpCodeID","Error is " + response.code());

                            }}else{
                                dialog.dismiss();
                                openEmptyLoginDialog();
                            }

                        }

                        @Override
                        public void onFailure(Call<FlagObject> call, Throwable t) {
                            /*openInvalidLoginDialog();*/
                            dialog.dismiss();
                            Log.d("HttpCodeID","Error is " + t.getMessage());
                        }
                    });
                }

                else{
                    dialog.dismiss();
                    openInternetDialog();

                }





            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.loginBtn.setEnabled(false);
                finish();
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}