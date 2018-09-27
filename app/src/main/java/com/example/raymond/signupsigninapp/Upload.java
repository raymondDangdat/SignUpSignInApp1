package com.example.raymond.signupsigninapp;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mName;
    private String mDescription;
    private String mKey;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String description){
        mName = name;
        mDescription = description;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public String getDescription(){
        return mDescription;
    }

    public void setDescription(String description){
        mDescription = description;
    }

//exclude it because is already in the database
    @Exclude
    public String getKey(){
        return mKey;
    }


    @Exclude
    public void setKey(String key){

        mKey = key;
    }

}
