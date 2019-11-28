package com.curryoutseller.model;

import android.content.Context;

import java.util.ArrayList;

public class CuisinesDataModel {

    public String cuisineID,name;
    boolean status;
    public CuisinesDataModel() {

    }

    public CuisinesDataModel(String name) {
        this.name = name;
    }

    public CuisinesDataModel(String cuisineID, String name) {
        this.cuisineID = cuisineID;
        this.name = name;
    }

    public CuisinesDataModel(String cuisineID, String name,boolean status) {
        this.cuisineID = cuisineID;
        this.name = name;
        this.status = status;
    }
    public CuisinesDataModel(ArrayList<CuisinesDataModel> alist, Context context) {

    }

    public String getCuisineID() {
        return cuisineID;
    }

    public void setCuisineID(String cuisineID) {
        this.cuisineID = cuisineID;
    }


    public String getName() {
        return name;
    }

//    public Collection<? extends String> getName() {
//        return name;
//    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
