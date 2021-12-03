package com.services.teconectapp;

import java.util.ArrayList;

public class fetch_project_response
{
    String success;
    ArrayList<project_list> data;

    public String getSuccess() {
        return success;
    }

    public ArrayList<project_list> getData() {
        return data;
    }
}
