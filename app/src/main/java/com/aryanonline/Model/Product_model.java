package com.aryanonline.Model;


import java.io.Serializable;

public class Product_model implements Serializable {

    String product_id;
    String product_name;
    String product_description;
    String product_image;
    String category_id;
    String in_stock;
    String price;
    String unit_value;
    String unit;
    String increament;
    String title;
    String Mrp;


    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public String getPrice() {
        return price;
    }

    public String getUnit_value() {
        return unit_value;
    }

    public String getUnit() {
        return unit;
    }


    public String getIncreament() {
        return increament;
    }

    public String getTitle() {
        return title;
    }

    public String getMrp() {
        return Mrp;
    }

}
