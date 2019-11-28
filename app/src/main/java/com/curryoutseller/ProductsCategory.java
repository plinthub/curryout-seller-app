package com.curryoutseller;

import java.util.ArrayList;

public class ProductsCategory {

    private int id = -1;
    private String name = "";
    String idd;
    private ArrayList<ProductsSubCategory> productsSubCategory = null;

    public ProductsCategory(int id, String name, ArrayList<ProductsSubCategory> productsSubCategory) {
        this.id = id;
        this.name = name;
        this.productsSubCategory = productsSubCategory;
    }

    public ProductsCategory(String id, String name, ArrayList<ProductsSubCategory> productsSubCategory) {
        this.idd = id;
        this.name = name;
        this.productsSubCategory = productsSubCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ProductsSubCategory> getProductsSubCategory() {
        return productsSubCategory;
    }

    public void setProductsSubCategory(ArrayList<ProductsSubCategory> productsSubCategory) {
        this.productsSubCategory = productsSubCategory;
    }
}
