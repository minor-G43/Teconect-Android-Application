package com.services.teconectapp;

public class Info {
        private String username;
        private String email;
        private String PhoneNo;
        private String password;
        private String github;
        private String techstack;
        private String tags;
        private String project;
        private String description;


    public Info(String username, String email, String phoneNo, String password, String github, String techstack, String tags, String project, String description) {
        this.username = username;
        this.email = email;
        PhoneNo = phoneNo;
        this.password = password;
        this.github = github;
        this.techstack = techstack;
        this.tags = tags;
        this.project = project;
        this.description = description;
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

    public String getPassword() {
        return password;
    }

    public String getGithub() {
        return github;
    }

    public String getTechstack() {
        return techstack;
    }

    public String getTags() {
        return tags;
    }

    public String getProject() {
        return project;
    }

    public String getDescription() {
        return description;
    }
}
