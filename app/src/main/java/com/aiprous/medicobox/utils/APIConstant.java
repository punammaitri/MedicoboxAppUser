package com.aiprous.medicobox.utils;


public class APIConstant {

    //API BASE URL
    public static String LOGIN = "http://user8.itsindev.com/medibox/API/login.php";
    public static String REGISTER = "http://user8.itsindev.com/medibox/API/register.php";
    public static String ISEMAILAVAILABLE = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/isEmailAvailable";
    public static String GETPRODUCT = "http://user8.itsindev.com/medibox/API/products.php";
    public static String GETCATEGORY = "http://user8.itsindev.com/medibox/API/categories.php";
    public static String FEATUREDPRODUCT = "http://user8.itsindev.com/medibox/API/featured-products.php";
    public static String CONFIRMKEY = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/me/activate";
    public static String BANNERAPI = "http://user8.itsindev.com/medibox/API/home-banners.php";
    public static String GETUSERINFO = "http://user8.itsindev.com/medibox/API/customer_self.php";
    public static String UPDATEUSERINFO = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/me";
    public static String GETCARTID = "http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine";
    public static String ADDTOCART = "http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine/items";
    //old API
    public static String GETCARTITEMS = "http://user8.itsindev.com/medibox/API/get_cart_item.php";
    //public static String GETCARTITEMS = "http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine";


    public static String DELETECARTITEMS = "http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine/items/";
    public static String EDITCARTITEM = "http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine/items/";
  //  public static String SINGLEPRODUCT = "http://user8.itsindev.com/medibox/index.php/rest/V1/products/id/";
    public static String SINGLEPRODUCT = "http://user8.itsindev.com/medibox/API/single_product.php?product_id=";
    //Updated API
    public static String UPDATEDGETCATEGORY="http://user8.itsindev.com/medibox/API/categories_new.php";
    public static String SEARCHPRODUCT="http://user8.itsindev.com/medibox/API/filter-product.php";

    //login with otp
    public static String SIGN_IN_WITH_OTP_VERIFY_MOBILE_NUMBER="http://user8.itsindev.com/medibox/API/otp_login.php";
    public static String SIGN_IN_WITH_OTP_VERIFY_OTP="http://user8.itsindev.com/medibox/API/verify-login-otp.php";

    //forgot password
    public static String FORGOT_PASSWORD_VERIFY_MOBILE_NUMBER="http://user8.itsindev.com/medibox/API/otp-fogot-password.php";
    public static String FORGOT_PASSWORD_VERIFY_OTP="http://user8.itsindev.com/medibox/API/verify-forgot-password-otp.php";
    public static String SET_NEW_PASSWORD="http://user8.itsindev.com/medibox/API/set-new-password.php";

    //Response Status Code
    public static final int SUCCESS_CODE = 200;
    /*For Sweet alert*/
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int PROGRESS_TYPE = 4;
    public static final String SOME_THING_WENT_WRONG = "Oops! Something went wrong!";
    public static String Authorization = "Authorization";
    public static String BEARER = "Bearer ";
}

