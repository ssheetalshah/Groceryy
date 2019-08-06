package com.aryanonline.Model;

import java.io.Serializable;

public class CompareModel implements Serializable {

    private String id;
    private String userId;
    private String productId;
    private String productName;
    private String model;
    private String price;
    private String description;
    private String image;

    public CompareModel(String id, String userId, String productId, String productName, String model, String price, String description, String image) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.model = model;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
