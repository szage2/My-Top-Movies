package com.example.szage.mytopmovies.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Api client is responsible for network connectivity and also generates implementation
 * of the defined interface(s).
 */
public class ApiClient {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public static void setLogging() {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static OkHttpClient.Builder getHttpClient() {
        return httpClient.addInterceptor(logging);
    }

    private static Retrofit retrofit;

    public static Retrofit getClient() {

        setLogging();
        getHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit;
    }
}
