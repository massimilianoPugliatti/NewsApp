package com.example.newsapp.core.di

import com.example.newsapp.core.network.ApiService
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideApiService( okHttpClient: OkHttpClient): ApiService {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, object :
                JsonDeserializer<LocalDateTime> {
                override fun deserialize(
                    json: JsonElement,
                    typeOfT: Type,
                    context: JsonDeserializationContext
                ): LocalDateTime {
                    return OffsetDateTime.parse(json.asString).toLocalDateTime()
                }
            })
            .registerTypeAdapter(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime> {
                override fun serialize(
                    src: LocalDateTime,
                    typeOfSrc: Type,
                    context: JsonSerializationContext
                ): JsonElement {
                    return JsonPrimitive(src.toString())
                }
            })
            .create()
        return Retrofit.Builder().client(okHttpClient).baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService::class.java)
    }
}