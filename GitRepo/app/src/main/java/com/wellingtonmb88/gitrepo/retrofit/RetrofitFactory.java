package com.wellingtonmb88.gitrepo.retrofit;


import java.util.concurrent.TimeUnit;

import me.drakeet.retrofit2.adapter.agera.AgeraCallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static final String BASE_URL = "https://api.github.com/";
    private static final int TIME_OUT = 10;

    /**
     * Creates a Retrofit service from an arbitrary class (clazz)
     *
     * @param clazz Java interface of the retrofit service
     * @return a Retrofit service with defined endpoint
     */
    public static <T> T createRetrofitService(final Class<T> clazz) {
        final OkHttpClient.Builder httpClientBuilder = new OkHttpClient
                .Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS);


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClientBuilder.build())
                .addCallAdapterFactory(AgeraCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(clazz);
    }

}
