package com.aiprous.medicobox.designpattern;


import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.model.AddToCartOptionDetailModel;


import java.util.ArrayList;

/**
 * Created by USER1 on 10/31/2017.
 */

public class SingletonAddToCart {
    private static SingletonAddToCart gsonInstance;
    public ArrayList<AddToCartOptionDetailModel> option = new ArrayList<AddToCartOptionDetailModel>();

    public static SingletonAddToCart getGsonInstance() {
        if (gsonInstance == null) {
            gsonInstance = new SingletonAddToCart();
        }
        return gsonInstance;
    }

    public ArrayList<AddToCartOptionDetailModel> getOptionList() {
        return option;
    }

    public void clearOptionList() {
        option.clear();
    }


    public void setOptionlist(AddToCartOptionDetailModel temp) {
        option.add(temp);

    }
}
