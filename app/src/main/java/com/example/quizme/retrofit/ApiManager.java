package com.example.quizme.retrofit;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static JsonUserApi service;
    private static ApiManager apiManager;
    public static String ip = "https://10.208.97.63:443/api/";
    private ApiManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ip)
                .addConverterFactory(GsonConverterFactory.create()).client(getUnsafeOkHttpClient().build())
                .build();




        service = retrofit.create(JsonUserApi.class);
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
    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void loginUser(User user,Callback<User> callback) {
        Call<User> userCall = service.loginUser(user);
        userCall.enqueue(callback);
    }

    public void getToken(User user,String token, Callback<ResponseToken> callback) {
        Call<ResponseToken> tokenCall = service.getToken(user,token);
        tokenCall.enqueue(callback);
    }


    public void addCoins(Coin coin,String token, Callback<FlagObject> callback) {
        Call<FlagObject> tokenCall = service.getToken(coin,token);
        tokenCall.enqueue(callback);
    }


    public void setUser(User user,String token, Callback<FlagObject> callback) {
        Call<FlagObject> setUser = service.setUser(user,token);
        setUser.enqueue(callback);
    }


    public void setPassword(User user,String token,Callback<FlagObject> callback) {
        Call<FlagObject> setPassword = service.setPassword(user,token);
        setPassword.enqueue(callback);
    }


}