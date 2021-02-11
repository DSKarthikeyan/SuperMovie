package com.fynd.themoviedb.data.api

import com.fynd.themoviedb.util.APIConstants
import com.fynd.themoviedb.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiInstance {

    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(TokenInterceptor(APIConstants.API_KEY, Constants.LANGUAGE_US_ENG))
                .build()
            Retrofit.Builder()
                .baseUrl(APIConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val movieRepoApi by lazy {
            retrofit.create(MovieRepoApi::class.java)
        }
    }
}


class TokenInterceptor(private val apiKey: String, private val language: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter(Constants.API_KEY, apiKey)
            .addQueryParameter(Constants.LANGUAGE, language)
            .build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}