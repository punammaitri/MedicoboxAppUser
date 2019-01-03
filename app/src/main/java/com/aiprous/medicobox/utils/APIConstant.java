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

    public static String ADD_TO_CART_ORDER_PLACE = "http://user8.itsindev.com/medibox/API/order-place.php";
    public static String DELETECARTITEMS = "http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine/items/";
    public static String EDITCARTITEM = "http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine/items/";
    //  public static String SINGLEPRODUCT = "http://user8.itsindev.com/medibox/index.php/rest/V1/products/id/";


    public static String SINGLEPRODUCT = "http://user8.itsindev.com/medibox/API/single_product.php?product_id=";
    //Updated API
    public static String UPDATEDGETCATEGORY = "http://user8.itsindev.com/medibox/API/categories_new.php";
    public static String SEARCHPRODUCT = "http://user8.itsindev.com/medibox/API/filter-product.php";

    //login with otp
    public static String SIGN_IN_WITH_OTP_VERIFY_MOBILE_NUMBER = "http://user8.itsindev.com/medibox/API/otp_login.php";
    public static String SIGN_IN_WITH_OTP_VERIFY_OTP = "http://user8.itsindev.com/medibox/API/verify-login-otp.php";

    //forgot password
    public static String FORGOT_PASSWORD_VERIFY_MOBILE_NUMBER = "http://user8.itsindev.com/medibox/API/otp-fogot-password.php";
    public static String FORGOT_PASSWORD_VERIFY_OTP = "http://user8.itsindev.com/medibox/API/verify-forgot-password-otp.php";
    public static String SET_NEW_PASSWORD = "http://user8.itsindev.com/medibox/API/set-new-password.php";

    //wishlist api
    public static String CREATE_WISHLIST = "http://user8.itsindev.com/medibox/API/create_wishlist.php";
    public static String GET_ALL_WISHLIST = "http://user8.itsindev.com/medibox/API/get_user_wishlist_products.php";
    public static String ADD_ITEM_WISHLIST = "http://user8.itsindev.com/medibox/API/add_item_wishlist.php";
    public static String DELETE_WISHLIST = "http://user8.itsindev.com/medibox/API/delete_wishlist.php";
    public static String SHARE_WISHLIST = "http://user8.itsindev.com/medibox/API/share-wishlist.php";
    public static String ADD_TO_CART_WISHLIST = "http://user8.itsindev.com/medibox/API/wishlist-all-product-to-cart.php";

    //upload prescription
    public static String UPLOAD_PRESCRIPTION_IMAGE = "https://user8.itsindev.com/medibox/API/upload-prescription/image_action.php";
    public static String DELETE_IMAGE_PRESCRIPTION = "https://user8.itsindev.com/medibox/API/upload-prescription/image_action.php";
    public static String GET_UPLOADED_PRESCRIPTION = "https://user8.itsindev.com/medibox/API/upload-prescription/uploaded_prescriptions.php";
    public static String UPLOADED_PRESCRIPTION_ADD_CART = "https://user8.itsindev.com/medibox/API/upload-prescription/cart_action.php";
    public static String UPLOADED_PRESCRIPTION_DELETE_CART = "https://user8.itsindev.com/medibox/API/upload-prescription/cart_action.php";
    public static String UPLOADED_PRESCRIPTION_PLACE_ORDER = "https://user8.itsindev.com/medibox/API/upload-prescription/finalorder_step.php";

    //user order
    public static String USERORDER = "http://user8.itsindev.com/medibox/API/user_orders.php";
    public static String SINGLE_ORDER = "http://user8.itsindev.com/medibox/API/single-order.php";
    public static String ORDER_TRACKING = "http://user8.itsindev.com/medibox/API/order_address.php/?order_id=";

    // add address
    public static String SHIPPING_ADDRESS = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/me/shippingAddress";
    public static String BILLING_ADDRESS = "http://user8.itsindev.com/medibox/index.php/rest/V1/customers/me/billingAddress";

    public static String ADD_ADDRESS = "http://user8.itsindev.com/medibox/API/add_address.php";
    public static String GET_ALL_ADDRESS = "http://user8.itsindev.com/medibox/API/customer-addresses.php";
    public static String UPDATE_ADDRESS = "http://user8.itsindev.com/medibox/API/update-address.php";
    public static String DELETE_ADDRESS = "http://user8.itsindev.com/medibox/API/delete-address.php";

    //Substitutes product for any specific tablet
    public static String RELATED_PRODUCT = "http://user8.itsindev.com/medibox/API/related_product.php?product_id=";

    //coupon
    public static String APPLY_COUPON="http://user8.itsindev.com/medibox/API/apply_coupon.php";

    //new order place with coupon code
    public static String NEW_ADD_TO_CART_ORDER_PLACE = "http://user8.itsindev.com/medibox/API/order-place-new.php";
    public static String GET_CART_TOTAL="http://user8.itsindev.com/medibox/index.php/rest/V1/carts/mine/totals";

    //send sms API
    public static String SEND_SMS="http://user8.itsindev.com/medibox/API/sms-notification.php";
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

