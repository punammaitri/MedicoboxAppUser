package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public  class MyOrdersModel {

    @Expose
    @SerializedName("entity_id")
    private String entity_id;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("grand_total")
    private String grand_total;
    @Expose
    @SerializedName("created_at")
    private String created_at;


    public MyOrdersModel(String entity_id, String status, String grand_total, String created_at) {
        this.entity_id = entity_id;
        this.status = status;
        this.grand_total = grand_total;
        this.created_at = created_at;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}