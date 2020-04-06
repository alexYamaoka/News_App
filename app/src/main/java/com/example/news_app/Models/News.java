package com.example.news_app.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class News implements Serializable
{
    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private List<Article> articlesList;



    // serialization: converting object into json string
    // deserialization: converting json string into object




    public News(String status, int totalResults, List<Article> articlesList)
    {
        setStatus(status);
        setTotalResults(totalResults);
        setArticlesList(articlesList);
    }

    public News()
    {
        this(null, 0, null);
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setTotalResults(int totalResults)
    {
        this.totalResults = totalResults;
    }

    public void setArticlesList(List<Article> articlesList)
    {
        this.articlesList = articlesList;
    }

    public String getStatus()
    {
        return status;
    }

    public int getTotalResults()
    {
        return totalResults;
    }

    public List<Article> getArticlesList()
    {
        return articlesList;
    }
}
