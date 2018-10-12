package com.aiprous.medicobox.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public abstract class RegisterModel {

    @Expose
    @SerializedName("extension_attributes")
    public Extension_attributes Extension_attributes;
    @Expose
    @SerializedName("disable_auto_group_change")
    public int Disable_auto_group_change;
    @Expose
    @SerializedName("addresses")
    public ArrayList<String> Addresses;
    @Expose
    @SerializedName("website_id")
    public int Website_id;
    @Expose
    @SerializedName("store_id")
    public int Store_id;
    @Expose
    @SerializedName("lastname")
    public String Lastname;
    @Expose
    @SerializedName("firstname")
    public String Firstname;
    @Expose
    @SerializedName("email")
    public String Email;
    @Expose
    @SerializedName("created_in")
    public String Created_in;
    @Expose
    @SerializedName("updated_at")
    public String Updated_at;
    @Expose
    @SerializedName("created_at")
    public String Created_at;
    @Expose
    @SerializedName("group_id")
    public int Group_id;
    @Expose
    @SerializedName("id")
    public int Id;

    public static class Extension_attributes {
        @Expose
        @SerializedName("is_subscribed")
        public boolean Is_subscribed;
    }

    public RegisterModel.Extension_attributes getExtension_attributes() {
        return Extension_attributes;
    }

    public void setExtension_attributes(RegisterModel.Extension_attributes extension_attributes) {
        Extension_attributes = extension_attributes;
    }

    public int getDisable_auto_group_change() {
        return Disable_auto_group_change;
    }

    public void setDisable_auto_group_change(int disable_auto_group_change) {
        Disable_auto_group_change = disable_auto_group_change;
    }

    public ArrayList<String> getAddresses() {
        return Addresses;
    }

    public void setAddresses(ArrayList<String> addresses) {
        Addresses = addresses;
    }

    public int getWebsite_id() {
        return Website_id;
    }

    public void setWebsite_id(int website_id) {
        Website_id = website_id;
    }

    public int getStore_id() {
        return Store_id;
    }

    public void setStore_id(int store_id) {
        Store_id = store_id;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCreated_in() {
        return Created_in;
    }

    public void setCreated_in(String created_in) {
        Created_in = created_in;
    }

    public String getUpdated_at() {
        return Updated_at;
    }

    public void setUpdated_at(String updated_at) {
        Updated_at = updated_at;
    }

    public String getCreated_at() {
        return Created_at;
    }

    public void setCreated_at(String created_at) {
        Created_at = created_at;
    }

    public int getGroup_id() {
        return Group_id;
    }

    public void setGroup_id(int group_id) {
        Group_id = group_id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
