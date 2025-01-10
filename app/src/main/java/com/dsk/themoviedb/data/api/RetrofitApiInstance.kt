package com.dsk.themoviedb.data.api

import com.dsk.themoviedb.util.APIConstants
import com.dsk.themoviedb.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiInstance {

    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(TokenInterceptor(APIConstants.API_KEY))
                .build()
            Retrofit.Builder()
                .baseUrl(APIConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val movieRepoApi: MovieRepoApi by lazy {
            retrofit.create(MovieRepoApi::class.java)
        }
    }
}


class TokenInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter(Constants.API_KEY, apiKey)
            .build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}