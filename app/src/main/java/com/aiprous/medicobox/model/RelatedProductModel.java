package com.aiprous.medicobox.model;

import java.util.ArrayList;

public class RelatedProductModel {

    ArrayList<Data> dataArrayList;

    public static class Data {
        String name, id, companyName;
        int price;

        public Data(String id, String name, int price, String companyName) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.companyName = companyName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }
    }

}
