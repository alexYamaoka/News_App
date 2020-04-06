package com.example.news_app.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Source implements Serializable
{
    @SerializedName("id")
    private String id;

    @SerializedName("sourceName")
    private String sourceName;


    public Source(String id, String sourceName)
    {
        setId(id);
        setSourceName(sourceName);
    }

    public Source()
    {
        this(null, null);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setSourceName(String sourceName)
    {
        this.sourceName = sourceName;
    }

    public String getId()
    {
        return id;
    }

    public String getSourceName()
    {
        return sourceName;
    }
}
