package com.aiprous.medicobox.utils;


public class APIConstant {

    //API URL
    public static String LOGIN = "https://user8.itsindev.com/medibox/API/login.php";
    public static String REGISTER = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers";
    public static String ISEMAILAVAILABLE = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/isEmailAvailable";
    public static String GETPRODUCT = "http://user8.itsindev.com/medibox/API/products.php";
    public static String GETCATEGORY = "http://user8.itsindev.com/medibox/API/categories.php";
    public static String FEATUREDPRODUCT = "http://user8.itsindev.com/medibox/featured-products.php";
    public static String CONFIRMKEY = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/me/activate";
    public static String BANNERAPI = "http://user8.itsindev.com/medibox/API/home-banners.php";
    public static String GETBEARERTOKEN = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/me";
    public static String UPDATEUSERINFO = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/me";
    public static String Authorization = "Authorization";
    public static String BEARER = "Bearer ";
    public static String GETCARTID="http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine";
    public  static String ADDTOCART="http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine/items";
    public static String GETCARTITEMS="http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine";


    //Response Status Code
    public static final int SUCCESS_CODE = 200;
}

