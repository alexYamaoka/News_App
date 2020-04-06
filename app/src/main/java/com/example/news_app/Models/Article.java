package com.example.news_app.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Article implements Serializable
{
    @SerializedName("source")
    private Source source;

    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("urlToImage")
    private String urlToImage;

    @SerializedName("publishedAt")
    private String datePublished;

    @SerializedName("content")
    private String content;

    @SerializedName("description")
    private String description;




    public Article(Source source, String author, String title, String url, String urlToImage, String datePublished, String content, String description)
    {
        setSource(source);
        setAuthor(author);
        setTitle(title);
        setUrl(url);
        setUrlToImage(urlToImage);
        setDatePublished(datePublished);
        setContent(content);
        setDescription(description);
    }

    public Article()
    {
        this(null, null, null, null, null, null, null, null);
    }

    public void setSource(Source source)
    {
        this.source = source;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void setUrlToImage(String urlToImage)
    {
        this.urlToImage = urlToImage;
    }

    public void setDatePublished(String datePublished)
    {
        this.datePublished = datePublished;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Source getSource()
    {
        return source;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getTitle()
    {
        return title;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUrlToImage()
    {
        return urlToImage;
    }

    public String getDatePublished()
    {
        return datePublished;
    }

    public String getContent()
    {
        return content;
    }

    public String getDescription()
    {
        return description;
    }
}
