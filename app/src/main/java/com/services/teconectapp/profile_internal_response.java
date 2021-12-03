package com.services.teconectapp;

import java.util.List;

public class profile_internal_response
{
    private String _id;
    private String username;
    private String email;
    private String PhoneNo;
    private String github;
    private String project;
    private String description;
    private List<String> tags;


    public String get_id() {
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

    public String getProject() {
        return project;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }
}

