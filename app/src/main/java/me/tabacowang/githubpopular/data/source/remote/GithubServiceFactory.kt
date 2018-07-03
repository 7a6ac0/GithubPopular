package me.tabacowang.githubpopular.data.source.remote

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object GithubServiceFactory {
    private val BASE_URL: String = "https://api.github.com/"
    private val TREND_URL: String = "http://trending.codehub-app.com/v2/trending"

    val APIService by lazy { create() }

    val trendingService by lazy { createTrending() }

    private fun create(): GithubService {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(createGsonConverter())
                .client(makeOkHttpClient())
                .build()

        return retrofit.create(GithubService::class.java)
    }

    private fun createTrending(): TrendService {
        val retrofit = Retrofit.Builder()
                .baseUrl(TREND_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(createGsonConverter())
                .client(makeOkHttpClient())
                .build()

        return retrofit.create(TrendService::class.java)
    }

    private fun makeOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().apply {
            networkInterceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original
                        .newBuilder()
                        .method(original.method(), original.body())
                        .build()
                chain.proceed(request)
            })
            addInterceptor(interceptor)
        }.build()
    }

    private fun createGsonConverter(): GsonConverterFactory {
        val builder = GsonBuilder().serializeNulls()
        return GsonConverterFactory.create(builder.create())
    }
}