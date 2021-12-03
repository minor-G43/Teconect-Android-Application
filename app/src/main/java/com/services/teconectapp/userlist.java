package com.services.teconectapp;

import java.util.ArrayList;

public class userlist
{
    private String _id;
    private String username;
    private String email;
    private String PhoneNo;
    private String github;
    private ArrayList<String> tags;
    private String project;
    private String description;

    public String getId() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public String getGithub() {
        return github;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getProject() {
        return project;
    }

    public String getDescription() {
        return description;
    }
}
