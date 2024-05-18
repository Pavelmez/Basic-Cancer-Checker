package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.data.response.CancerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines?q=cancer&category=health&language=en")
    fun getCancerHeadlines(@Query("apiKey") apiKey: String): Call<CancerResponse>
}