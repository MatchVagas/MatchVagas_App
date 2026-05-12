package com.edu.matchvagasapp.data.network;

import com.edu.matchvagasapp.BuildConfig;
import com.edu.matchvagasapp.MatchVagasApp;
import com.edu.matchvagasapp.data.local.TokenManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static RetrofitClient instance;
    private final ApiService apiService;

    private RetrofitClient() {
        TokenManager tokenManager = new TokenManager(MatchVagasApp.getAppContext());
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenManager))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
