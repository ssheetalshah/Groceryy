package com.aryanonline.Model;

import java.io.Serializable;

public class SubCatModel implements Serializable {

    private String productId;
    private String model;
    private String quantity;
    private String image;
    private String stockStatus;
    private String price;
    private String productname;
    private String description;

    public SubCatModel(String productId, String model, String quantity, String image, String stockStatus, String price, String productname, String description) {
        this.productId = productId;
        this.model = model;
        this.quantity = quantity;
        this.image = image;
        this.stockStatus = stockStatus;
        this.price = price;
        this.productname = productname;
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
