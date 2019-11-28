package com.curryoutseller;

import java.io.Serializable;

public class ProductsSubCategory implements Serializable {

    private String name = "", subName = "", price = "";
    private int id = -1;
    private int flag = -1;
    private String productID;
    private String imgView;

    String restaurant_id,category,sub_Category;
    String food_type;
    String descriptio;
    String quantity;
    String ingrediants;
    String available_start_time;

    public String getAvailable_days() {
        return available_days;
    }

    public void setAvailable_days(String available_days) {
        this.available_days = available_days;
    }

    String available_end_time;
    String createdAt;
    String status;
    String category_name1;
    String sub_category_name;
    String available_days;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_Category() {
        return sub_Category;
    }

    public void setSub_Category(String sub_Category) {
        this.sub_Category = sub_Category;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getDescriptio() {
        return descriptio;
    }

    public void setDescriptio(String descriptio) {
        this.descriptio = descriptio;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIngrediants() {
        return ingrediants;
    }

    public void setIngrediants(String ingrediants) {
        this.ingrediants = ingrediants;
    }

    public String getAvailable_start_time() {
        return available_start_time;
    }

    public void setAvailable_start_time(String available_start_time) {
        this.available_start_time = available_start_time;
    }

    public String getAvailable_end_time() {
        return available_end_time;
    }

    public void setAvailable_end_time(String available_end_time) {
        this.available_end_time = available_end_time;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory_name1() {
        return category_name1;
    }

    public void setCategory_name1(String category_name1) {
        this.category_name1 = category_name1;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getImgView() {
        return imgView;
    }

    public void setImgView(String imgView) {
        this.imgView = imgView;
    }

    public ProductsSubCategory(String name, String subName, String price, String productID, String imgView, String restaurant_id, String category, String sub_Category, String food_type, String descriptio, String quantity, String ingrediants, String available_start_time, String available_end_time, String createdAt, String status, String category_name1, String sub_category_name,String available_days) {
        this.name = name;
        this.subName = subName;
        this.price = price;
        this.productID = productID;
        this.imgView = imgView;
        this.restaurant_id = restaurant_id;
        this.category = category;
        this.sub_Category = sub_Category;
        this.food_type = food_type;
        this.descriptio = descriptio;
        this.quantity = quantity;
        this.ingrediants = ingrediants;
        this.available_start_time = available_start_time;
        this.available_end_time = available_end_time;
        this.createdAt = createdAt;
        this.status = status;
        this.category_name1 = category_name1;
        this.sub_category_name = sub_category_name;
        this.available_days = available_days;
    }

    public ProductsSubCategory(String name, String subName, String price, int id, int flag) {
        this.name = name;
        this.subName = subName;
        this.price = price;
        this.id = id;
        this.flag = flag;
    }
    public ProductsSubCategory(String name, String subName, String price, String productID,String imgView) {
        this.name = name;
        this.subName = subName;
        this.price = price;
        this.productID = productID;
        this.imgView = imgView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
