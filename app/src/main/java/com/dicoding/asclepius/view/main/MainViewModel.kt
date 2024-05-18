package com.dicoding.asclepius.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.CancerResponse
import com.dicoding.asclepius.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _articlesList = MutableLiveData<List<ArticlesItem>>()
    val articlesList: LiveData<List<ArticlesItem>> = _articlesList

    init {
        fetchArticles()
    }

    private fun fetchArticles() {
        val apiKey = BuildConfig.NEWS_API_KEY
        val apiService = ApiConfig().getApiService().getCancerHeadlines(apiKey)
        apiService.enqueue(object : Callback<CancerResponse> {
            override fun onResponse(
                call: Call<CancerResponse>,
                response: Response<CancerResponse>
            ) {
                if (response.isSuccessful) {
                    // Filter out articles that have been removed
                    val filteredArticles =
                        response.body()?.articles?.filterNot { it.title == "[Removed]" }
                    _articlesList.value = filteredArticles
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<CancerResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}