package com.curryoutseller;

public class MyProductsDataModel {

    private String txtTitle;
    private String txtSubTitle;
    private String txtPrice;
    private int imgView;

    public MyProductsDataModel(String txtTitle, String txtSubTitle, String txtPrice, int imgView) {
        this.txtTitle = txtTitle;
        this.txtSubTitle = txtSubTitle;
        this.txtPrice = txtPrice;
        this.imgView = imgView;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public String getTxtSubTitle() {
        return txtSubTitle;
    }

    public void setTxtSubTitle(String txtSubTitle) {
        this.txtSubTitle = txtSubTitle;
    }

    public String getTxtPrice() {
        return txtPrice;
    }

    public void setTxtPrice(String txtPrice) {
        this.txtPrice = txtPrice;
    }

    public int getImgView() {
        return imgView;
    }

    public void setImgView(int imgView) {
        this.imgView = imgView;
    }
}
