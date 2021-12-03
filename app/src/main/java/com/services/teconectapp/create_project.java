package com.services.teconectapp;

import com.google.gson.annotations.SerializedName;

public class create_project
{
    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("des")
    private String description;

    @SerializedName("tags")
    private String tech;

    public create_project(String title, String url, String description, String tech) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.tech = tech;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getTech() {
        return tech;
    }
}
