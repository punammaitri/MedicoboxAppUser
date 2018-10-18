package com.aiprous.medicobox.designpattern;


import com.aiprous.medicobox.activity.ListActivity;


import java.util.ArrayList;

/**
 * Created by USER1 on 10/31/2017.
 */

public class SingletonAddToCart {
    private static SingletonAddToCart gsonInstance;
    public ArrayList<ListActivity.ListModel> option = new ArrayList<ListActivity.ListModel>();

    public static SingletonAddToCart getGsonInstance() {
        if (gsonInstance == null) {
            gsonInstance = new SingletonAddToCart();
        }
        return gsonInstance;
    }

    public ArrayList<ListActivity.ListModel> getOptionList() {
        return option;
    }

    public void clearOptionList() {
        option.clear();
    }


    public void setOptionlist(ListActivity.ListModel temp) {
        option.add(temp);

    }
}
