package com.aryanonline.Model;

import java.io.Serializable;

public class OfferModel implements Serializable {

    private String id;
    private String title;
    private String parentId;
    private String groupId;
    private String params;
    private String layersparams;
    private String image;
    private String status;
    private String position;

    public OfferModel(String id, String title, String parentId, String groupId, String params, String layersparams, String image, String status, String position) {
        this.id = id;
        this.title = title;
        this.parentId = parentId;
        this.groupId = groupId;
        this.params = params;
        this.layersparams = layersparams;
        this.image = image;
        this.status = status;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getLayersparams() {
        return layersparams;
    }

    public void setLayersparams(String layersparams) {
        this.layersparams = layersparams;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
