package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersModel {

    @Expose
    @SerializedName("entity_id")
    private String entity_id;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("base_subtotal")
    private String base_subtotal;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("order_inc_id")
    private String order_inc_id;

    public MyOrdersModel(String entity_id, String status, String base_subtotal, String created_at, String order_inc_id) {
        this.entity_id = entity_id;
        this.status = status;
        this.base_subtotal = base_subtotal;
        this.created_at = created_at;
        this.order_inc_id = order_inc_id;
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

    public String getBase_subtotal() {
        return base_subtotal;
    }

    public void setBase_subtotal(String base_subtotal) {
        this.base_subtotal = base_subtotal;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getOrder_inc_id() {
        return order_inc_id;
    }

    public void setOrder_inc_id(String order_inc_id) {
        this.order_inc_id = order_inc_id;
    }
}