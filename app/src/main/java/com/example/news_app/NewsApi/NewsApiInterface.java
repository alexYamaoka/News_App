package com.example.news_app.NewsApi;

import com.example.news_app.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface
{
    @GET("top-headlines")
    Call<News> getTopHeadlinesNews(@Query("country") String country, @Query("apiKey") String apiKey);

    @GET("everything")
    Call<News> getNewsSearch(@Query("q") String keyword, @Query("apiKey") String apiKey);
}
