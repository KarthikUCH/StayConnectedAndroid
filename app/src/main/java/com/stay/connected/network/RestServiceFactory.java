package com.stay.connected.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by karthikeyan on 7/8/17.
 */

public interface RestServiceFactory {

    /**
     * @see <a herf "https://stackoverflow.com/a/5806384/2790197"> Connect to PC Local host</a>
     */


    <T> T create(Class<T> clazz, String url);

    class Impl implements RestServiceFactory {


        @Override
        public <T> T create(Class<T> clazz, String url) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create());

            Retrofit retrofit = builder
                    .client(httpClient.build())
                    .build();
            return retrofit.create(clazz);
        }
    }
}
