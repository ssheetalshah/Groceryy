package com.aryanonline.Model;

import java.io.Serializable;

public class WishModel implements Serializable {

    private String id;
    private String userId;
    private String productId;
    private String productName;
    private String model;
    private String stock;
    private String price;

    public WishModel(String id, String userId, String productId, String productName, String model, String stock, String price) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.model = model;
        this.stock = stock;
        this.price = price;
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

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
