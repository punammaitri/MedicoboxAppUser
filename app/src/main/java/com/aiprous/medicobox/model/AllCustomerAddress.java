package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AllCustomerAddress {

    @Expose
    @SerializedName("default_shipping")
    private boolean default_shipping;
    @Expose
    @SerializedName("default_billing")
    private boolean default_billing;
    @Expose
    @SerializedName("customer_id")
    private String customer_id;
    @Expose
    @SerializedName("id")
    private String id;
    @Expose
    @SerializedName("region")
    private String region;
    @Expose
    @SerializedName("fax")
    private String fax;
    @Expose
    @SerializedName("telephone")
    private String telephone;
    @Expose
    @SerializedName("postcode")
    private String postcode;
    @Expose
    @SerializedName("region_id")
    private String region_id;
    @Expose
    @SerializedName("country_id")
    private String country_id;
    @Expose
    @SerializedName("city")
    private String city;
    @Expose
    @SerializedName("flat")
    private String flat;
    @Expose
    @SerializedName("landmark")
    private String landmark;
    @Expose
    @SerializedName("street")
    private String street;
    @Expose
    @SerializedName("company")
    private String company;
    @Expose
    @SerializedName("lastname")
    private String lastname;
    @Expose
    @SerializedName("firstname")
    private String firstname;
    @Expose
    @SerializedName("lat_long")
    private String lat_long;

    public AllCustomerAddress(String id, String telephone, String postcode, String region_id,
                              String country_id, String city, String street, String lastname, String firstname, String flat, String landmark, String lat_long) {
        this.id = id;
        this.telephone = telephone;
        this.postcode = postcode;
        this.region_id = region_id;
        this.country_id = country_id;
        this.city = city;
        this.street = street;
        this.lastname = lastname;
        this.firstname = firstname;
        this.flat = flat;
        this.landmark = landmark;
        this.lat_long = lat_long;
    }

    public boolean isDefault_shipping() {
        return default_shipping;
    }

    public void setDefault_shipping(boolean default_shipping) {
        this.default_shipping = default_shipping;
    }

    public boolean isDefault_billing() {
        return default_billing;
    }

    public void setDefault_billing(boolean default_billing) {
        this.default_billing = default_billing;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLat_long() {
        return lat_long;
    }

    public void setLat_long(String lat_long) {
        this.lat_long = lat_long;
    }
}
