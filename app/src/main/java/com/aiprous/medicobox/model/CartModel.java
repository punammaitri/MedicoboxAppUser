package com.aiprous.medicobox.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartModel {
    @Expose
    @SerializedName("extension_attributes")
    public Extension_attributes extension_attributes;
    @Expose
    @SerializedName("store_id")
    public int store_id;
    @Expose
    @SerializedName("customer_tax_class_id")
    public int customer_tax_class_id;
    @Expose
    @SerializedName("customer_note_notify")
    public boolean customer_note_notify;
    @Expose
    @SerializedName("customer_is_guest")
    public boolean customer_is_guest;
    @Expose
    @SerializedName("currency")
    public Currency currency;
    @Expose
    @SerializedName("orig_order_id")
    public int orig_order_id;
    @Expose
    @SerializedName("billing_address")
    public Billing_address billing_address;
    @Expose
    @SerializedName("customer")
    public Customer customer;
    @Expose
    @SerializedName("items_qty")
    public int items_qty;
    @Expose
    @SerializedName("items_count")
    public int items_count;
    @Expose
    @SerializedName("items")
    public ArrayList<Items> items;
    @Expose
    @SerializedName("is_virtual")
    public boolean is_virtual;
    @Expose
    @SerializedName("is_active")
    public boolean is_active;
    @Expose
    @SerializedName("updated_at")
    public String updated_at;
    @Expose
    @SerializedName("created_at")
    public String created_at;
    @Expose
    @SerializedName("id")
    public int id;

    public static class Extension_attributes {
        @Expose
        @SerializedName("shipping_assignments")
        public List<String> shipping_assignments;
    }

    public static class Currency {
        @Expose
        @SerializedName("base_to_quote_rate")
        public int base_to_quote_rate;
        @Expose
        @SerializedName("base_to_global_rate")
        public int base_to_global_rate;
        @Expose
        @SerializedName("store_to_quote_rate")
        public int store_to_quote_rate;
        @Expose
        @SerializedName("store_to_base_rate")
        public int store_to_base_rate;
        @Expose
        @SerializedName("quote_currency_code")
        public String quote_currency_code;
        @Expose
        @SerializedName("store_currency_code")
        public String store_currency_code;
        @Expose
        @SerializedName("base_currency_code")
        public String base_currency_code;
        @Expose
        @SerializedName("global_currency_code")
        public String global_currency_code;

        public int getBase_to_quote_rate() {
            return base_to_quote_rate;
        }

        public void setBase_to_quote_rate(int base_to_quote_rate) {
            this.base_to_quote_rate = base_to_quote_rate;
        }

        public int getBase_to_global_rate() {
            return base_to_global_rate;
        }

        public void setBase_to_global_rate(int base_to_global_rate) {
            this.base_to_global_rate = base_to_global_rate;
        }

        public int getStore_to_quote_rate() {
            return store_to_quote_rate;
        }

        public void setStore_to_quote_rate(int store_to_quote_rate) {
            this.store_to_quote_rate = store_to_quote_rate;
        }

        public int getStore_to_base_rate() {
            return store_to_base_rate;
        }

        public void setStore_to_base_rate(int store_to_base_rate) {
            this.store_to_base_rate = store_to_base_rate;
        }

        public String getQuote_currency_code() {
            return quote_currency_code;
        }

        public void setQuote_currency_code(String quote_currency_code) {
            this.quote_currency_code = quote_currency_code;
        }

        public String getStore_currency_code() {
            return store_currency_code;
        }

        public void setStore_currency_code(String store_currency_code) {
            this.store_currency_code = store_currency_code;
        }

        public String getBase_currency_code() {
            return base_currency_code;
        }

        public void setBase_currency_code(String base_currency_code) {
            this.base_currency_code = base_currency_code;
        }

        public String getGlobal_currency_code() {
            return global_currency_code;
        }

        public void setGlobal_currency_code(String global_currency_code) {
            this.global_currency_code = global_currency_code;
        }
    }

    public static class Billing_address {
        @Expose
        @SerializedName("save_in_address_book")
        public int save_in_address_book;
        @Expose
        @SerializedName("same_as_billing")
        public int same_as_billing;
        @Expose
        @SerializedName("email")
        public String email;
        @Expose
        @SerializedName("customer_id")
        public int customer_id;
        @Expose
        @SerializedName("street")
        public ArrayList<String> street;
        @Expose
        @SerializedName("id")
        public int id;

        public int getSave_in_address_book() {
            return save_in_address_book;
        }

        public void setSave_in_address_book(int save_in_address_book) {
            this.save_in_address_book = save_in_address_book;
        }

        public int getSame_as_billing() {
            return same_as_billing;
        }

        public void setSame_as_billing(int same_as_billing) {
            this.same_as_billing = same_as_billing;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public ArrayList<String> getStreet() {
            return street;
        }

        public void setStreet(ArrayList<String> street) {
            this.street = street;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Customer {
        @Expose
        @SerializedName("extension_attributes")
        public Extension_attributes extension_attributes;
        @Expose
        @SerializedName("disable_auto_group_change")
        public int disable_auto_group_change;
        @Expose
        @SerializedName("addresses")
        public ArrayList<String> addresses;
        @Expose
        @SerializedName("website_id")
        public int website_id;
        @Expose
        @SerializedName("store_id")
        public int store_id;
        @Expose
        @SerializedName("gender")
        public int gender;
        @Expose
        @SerializedName("lastname")
        public String lastname;
        @Expose
        @SerializedName("firstname")
        public String firstname;
        @Expose
        @SerializedName("email")
        public String email;
        @Expose
        @SerializedName("created_in")
        public String created_in;
        @Expose
        @SerializedName("updated_at")
        public String updated_at;
        @Expose
        @SerializedName("created_at")
        public String created_at;
        @Expose
        @SerializedName("default_shipping")
        public String default_shipping;
        @Expose
        @SerializedName("default_billing")
        public String default_billing;
        @Expose
        @SerializedName("group_id")
        public int group_id;
        @Expose
        @SerializedName("id")
        public int id;

        public Extension_attributes getExtension_attributes() {
            return extension_attributes;
        }

        public void setExtension_attributes(Extension_attributes extension_attributes) {
            this.extension_attributes = extension_attributes;
        }

        public int getDisable_auto_group_change() {
            return disable_auto_group_change;
        }

        public void setDisable_auto_group_change(int disable_auto_group_change) {
            this.disable_auto_group_change = disable_auto_group_change;
        }

        public ArrayList<String> getAddresses() {
            return addresses;
        }

        public void setAddresses(ArrayList<String> addresses) {
            this.addresses = addresses;
        }

        public int getWebsite_id() {
            return website_id;
        }

        public void setWebsite_id(int website_id) {
            this.website_id = website_id;
        }

        public int getStore_id() {
            return store_id;
        }

        public void setStore_id(int store_id) {
            this.store_id = store_id;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreated_in() {
            return created_in;
        }

        public void setCreated_in(String created_in) {
            this.created_in = created_in;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getDefault_shipping() {
            return default_shipping;
        }

        public void setDefault_shipping(String default_shipping) {
            this.default_shipping = default_shipping;
        }

        public String getDefault_billing() {
            return default_billing;
        }

        public void setDefault_billing(String default_billing) {
            this.default_billing = default_billing;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

   /* public static class Extension_attributes {
        @Expose
        @SerializedName("is_subscribed")
        public boolean is_subscribed;
    }*/

    public static class Items {
        @Expose
        @SerializedName("quote_id")
        public String quote_id;
        @Expose
        @SerializedName("product_type")
        public String product_type;
        @Expose
        @SerializedName("price")
        public int price;
        @Expose
        @SerializedName("name")
        public String name;
        @Expose
        @SerializedName("qty")
        public int qty;
        @Expose
        @SerializedName("sku")
        public String sku;
        @Expose
        @SerializedName("item_id")
        public int item_id;

        public Items(String quote_id, String product_type, int price, String name, int qty, String sku, int item_id) {
            this.quote_id = quote_id;
            this.product_type = product_type;
            this.price = price;
            this.name = name;
            this.qty = qty;
            this.sku = sku;
            this.item_id = item_id;
        }

        public String getQuote_id() {
            return quote_id;
        }

        public void setQuote_id(String quote_id) {
            this.quote_id = quote_id;
        }

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }
    }
}
