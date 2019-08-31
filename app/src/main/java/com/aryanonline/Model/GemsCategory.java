package com.aryanonline.Model;

import java.io.Serializable;

public class GemsCategory implements Serializable
{
    private String categoryId;
    private String image;
    private String parentId;
    private String top;
    private String column;
    private String sortOrder;
    private String status;
    private String dateAdded;
    private String dateModified;
    private String languageId;
    private String name;
    private String description;
    private String metaDescription;
    private String metaKeyword;

    public GemsCategory(String categoryId, String image, String parentId, String top, String column, String sortOrder, String status, String dateAdded, String dateModified, String languageId, String name, String description, String metaDescription, String metaKeyword) {
        this.categoryId = categoryId;
        this.image = image;
        this.parentId = parentId;
        this.top = top;
        this.column = column;
        this.sortOrder = sortOrder;
        this.status = status;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.languageId = languageId;
        this.name = name;
        this.description = description;
        this.metaDescription = metaDescription;
        this.metaKeyword = metaKeyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword;
    }
}
