package com.example.animals.model

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnimalApi {
    @GET("getKey")
    //A single is a observable that can be observed by another enity
    fun getApiKey(): Single<ApiKey>

    @FormUrlEncoded
    @POST("getAnimals")
    fun getAnimals(@Field("key")key:String) :Single<List<Animal>>

}