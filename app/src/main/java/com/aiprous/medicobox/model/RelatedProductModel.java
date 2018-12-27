package com.aiprous.medicobox.model;

import java.util.ArrayList;

public class RelatedProductModel {

    ArrayList<Data> dataArrayList;

    public static class Data{
       String name;
       int price;
       String companyName;

        public Data(String name, int price, String companyName) {
            this.name = name;
            this.price = price;
            this.companyName = companyName;
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
