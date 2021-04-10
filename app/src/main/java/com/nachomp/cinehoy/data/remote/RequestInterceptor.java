package com.nachomp.cinehoy.data.remote;

import com.nachomp.cinehoy.app.MyApp;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Para concatenar mas parametros en la baseUrl se usa un Interceptor
 */
public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl originalHttpUrl = originalRequest.url();

        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", ApiContants.API_KEY)
                .addQueryParameter("language", MyApp.getInstance().getCurrentLanguage())
                .build();

        Request request = originalRequest.newBuilder()
                .url(url)
                .build();

        return chain.proceed(request);
    }
}
