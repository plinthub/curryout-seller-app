package com.curryoutseller.model;

public class RestaurantDataModel {

    private String restrauntID;
    private String txtRestaurantName;
    private String txtRestaurantAddress;
    private String imgRestaurantBG;

    public RestaurantDataModel(String restrauntID, String txtRestaurantName, String txtRestaurantAddress, String imgRestaurantBG) {
        this.restrauntID = restrauntID;
        this.txtRestaurantName = txtRestaurantName;
        this.txtRestaurantAddress = txtRestaurantAddress;
        this.imgRestaurantBG = imgRestaurantBG;
    }

    public String getRestrauntID() {
        return restrauntID;
    }

    public void setRestrauntID(String restrauntID) {
        this.restrauntID = restrauntID;
    }

    public String getTxtRestaurantName() {
        return txtRestaurantName;
    }

    public void setTxtRestaurantName(String txtRestaurantName) {
        this.txtRestaurantName = txtRestaurantName;
    }

    public String getTxtRestaurantAddress() {
        return txtRestaurantAddress;
    }

    public void setTxtRestaurantAddress(String txtRestaurantAddress) {
        this.txtRestaurantAddress = txtRestaurantAddress;
    }

    public String getImgRestaurantBG() {
        return imgRestaurantBG;
    }

    public void setImgRestaurantBG(String imgRestaurantBG) {
        this.imgRestaurantBG = imgRestaurantBG;
    }

}
