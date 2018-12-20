package com.aiprous.medicobox.prescription;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public abstract class GetAddress {

    @Expose
    @SerializedName("address")
    private ArrayList<Address> address;
    @Expose
    @SerializedName("status")
    private String status;


    public static class Address {
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
        @SerializedName("company")
        private String company;
        @Expose
        @SerializedName("lastname")
        private String lastname;
        @Expose
        @SerializedName("firstname")
        private String firstname;
    }

    public static class RegionEntity {
        @Expose
        @SerializedName("region_id")
        private int region_id;
    }
}
