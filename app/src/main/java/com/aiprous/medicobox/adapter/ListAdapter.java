package com.aiprous.medicobox.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.activity.ProductDetailBActivity;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.AddToCartOptionDetailModel;
import com.aiprous.medicobox.model.GetWishListModel;
import com.aiprous.medicobox.model.ListModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.aiprous.medicobox.activity.ListActivity.rlayout_cart;
import static com.aiprous.medicobox.activity.ListActivity.tv_cart_size;
import static com.aiprous.medicobox.utils.APIConstant.ADDTOCART;
import static com.aiprous.medicobox.utils.APIConstant.ADD_ITEM_WISHLIST;
import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;
import static com.aiprous.medicobox.utils.APIConstant.CREATE_WISHLIST;
import static com.aiprous.medicobox.utils.APIConstant.EDITCARTITEM;
import static com.aiprous.medicobox.utils.APIConstant.GET_ALL_WISHLIST;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private static String mId;
    private static String getWishListId = "";
    private ArrayList<ListModel> mDataArrayList;
    private ArrayList<AddToCartOptionDetailModel> ItemModelList;
    private static Context mContext;
    boolean foundduplicateItem;
    int setValuePosition = 1;
    int total = 0;
    private String mMedicineName;
    private String mValue;
    private String mMrp;
    private String mdiscount;
    private String mPrice;
    int mQty;
    private String mImageURL;
    private String mSku;
    private String mCartId;
    CustomProgressDialog mAlert;
    private String mItemId;
    private String mVisibiltyFlag;
    private float mCalculatePrice;
    private Dialog dialog;
    private ImageView imgCancel;
    private RecyclerView recycler_view_all_wishlist;
    private EditText edt_wishlist_name;
    private Button btnAdd, btnSave;
    private GetWishListAdapter mGetWishListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<GetWishListModel> mGetWishListModels = new ArrayList<GetWishListModel>();
    private DismissAlertInterface mDismissAlert;

    public ListAdapter(ListActivity mContext, ArrayList<ListModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
        this.mDismissAlert = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        mAlert = CustomProgressDialog.getInstance();


        //remove double qoute
        final String getCartId = MedicoboxApp.onGetCartID();
        String lCartId = getCartId;
        mCartId = lCartId.replace("\"", "");


        SingletonAddToCart singletonAddToCart = SingletonAddToCart.getGsonInstance();
        ItemModelList = singletonAddToCart.getOptionList();

        if (!ItemModelList.isEmpty()) {

            for (int i = 0; i < ItemModelList.size(); i++) {
                if (ItemModelList.get(i).getMedicineName().equals(mDataArrayList.get(position).getTitle())) {

                    //make cart layout  visible
                    holder.rlayout_number_of_item.setVisibility(View.VISIBLE);
                    holder.rlayout_add.setVisibility(View.GONE);


                    Picasso.with(mContext)
                            .load(ItemModelList.get(i).getImage())
                            .into(holder.img_medicine, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {
                                }
                            });

                    holder.tv_medicine_name.setText(ItemModelList.get(i).getMedicineName());
                    holder.tv_content.setText(ItemModelList.get(i).getValue());
                    holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs) + ItemModelList.get(i).getMrp());
                    if (ItemModelList.get(i).getDiscount().equals("0")) {
                        holder.tv_discount.setVisibility(View.GONE);
                    } else {
                        holder.tv_discount.setVisibility(View.VISIBLE);
                        holder.tv_discount.setText("" + ItemModelList.get(i).getDiscount() + "%" + " OFF");

                    }

                    if (ItemModelList.get(i).getPrice().isEmpty()) {
                        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + ItemModelList.get(i).getMrp());
                    } else {
                        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + ItemModelList.get(i).getPrice());
                        holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }

                    holder.tv_value.setText(ItemModelList.get(i).getQty());

                    // addItemsSingleton();
                    break;
                } else {

                    Picasso.with(mContext)
                            .load(mDataArrayList.get(position).getImage())
                            .into(holder.img_medicine, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });

                    holder.tv_medicine_name.setText(mDataArrayList.get(position).getTitle());
                    holder.tv_content.setText(mDataArrayList.get(position).getShort_description());
                    holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getPrice());

                    if (mDataArrayList.get(position).getDiscount().equals("0")) {
                        holder.tv_discount.setVisibility(View.GONE);
                    } else {
                        holder.tv_discount.setVisibility(View.VISIBLE);
                        holder.tv_discount.setText(mDataArrayList.get(position).getDiscount() + "%" + " OFF");
                    }

                    if (mDataArrayList.get(position).getSale_price().isEmpty()) {
                        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getPrice());
                    } else {
                        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getSale_price());
                        holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }


                    //make add button visible
                    holder.rlayout_number_of_item.setVisibility(View.GONE);
                    holder.rlayout_add.setVisibility(View.VISIBLE);

                }
            }
        } else {

            Picasso.with(mContext)
                    .load(mDataArrayList.get(position).getImage())
                    .into(holder.img_medicine, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });

            holder.tv_medicine_name.setText(mDataArrayList.get(position).getTitle());
            holder.tv_content.setText(mDataArrayList.get(position).getShort_description());
            holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getPrice());

            if (mDataArrayList.get(position).getDiscount().equals("0")) {
                holder.tv_discount.setVisibility(View.GONE);
            } else {
                holder.tv_discount.setVisibility(View.VISIBLE);
                holder.tv_discount.setText(mDataArrayList.get(position).getDiscount() + "%" + " OFF");
            }

            if (mDataArrayList.get(position).getSale_price().isEmpty()) {
                holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getPrice());
            } else {
                holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getSale_price());
                holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

        if (mDataArrayList.get(position).getWishlist().equals("1")) {
            holder.img_wishlist.setImageResource(R.drawable.heart_active);
        } else {
            holder.img_wishlist.setImageResource(R.drawable.heart_inactive);
        }

        //add item to wishlist
        holder.img_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProductInfoAlert(mContext, mDataArrayList.get(position).getId());
            }
        });

        holder.llayout_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rlayout_number_of_item.getVisibility() == View.VISIBLE) {
                    // Its visible
                    mVisibiltyFlag = "1";
                } else {
                    // Either gone or invisible
                    mVisibiltyFlag = "2";
                }

                Intent intent = new Intent(mContext, ProductDetailBActivity.class);
                intent.putExtra("productId", mDataArrayList.get(position).getId());
                intent.putExtra("VisibiltyFlag", mVisibiltyFlag);
                intent.putExtra("SKU", mDataArrayList.get(position).getSku());
                intent.putExtra("Qty", holder.tv_value.getText().toString());
                intent.putExtra("imageUrl", mDataArrayList.get(position).getImage());
                intent.putExtra("MedicineName", mDataArrayList.get(position).getTitle());
                intent.putExtra("value", mDataArrayList.get(position).getShort_description());
                intent.putExtra("price", mDataArrayList.get(position).getSale_price());
                intent.putExtra("prescription", mDataArrayList.get(position).getPrescription_required());
                intent.putExtra("MrpPrice", mDataArrayList.get(position).getPrice());
                intent.putExtra("discount", mDataArrayList.get(position).getDiscount());

                Activity activity = (Activity) mContext;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

        holder.rlayout_add.setTag(position);
        holder.rlayout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rlayout_number_of_item.setVisibility(View.VISIBLE);
                holder.rlayout_add.setVisibility(View.GONE);
                rlayout_cart.setVisibility(View.VISIBLE);

                int lItemIndex = Integer.parseInt("" + v.getTag());

                mMedicineName = mDataArrayList.get(lItemIndex).getTitle();
                mValue = mDataArrayList.get(lItemIndex).getShort_description();
                mMrp = mDataArrayList.get(lItemIndex).getPrice();
                mdiscount = mDataArrayList.get(lItemIndex).getDiscount();
                mSku = mDataArrayList.get(lItemIndex).getSku();

                if (mDataArrayList.get(lItemIndex).getSale_price().isEmpty()) {
                    mPrice = mDataArrayList.get(lItemIndex).getPrice();
                } else {
                    mPrice = mDataArrayList.get(lItemIndex).getSale_price();
                }
                mImageURL = mDataArrayList.get(lItemIndex).getImage();
                setValuePosition = Integer.parseInt(holder.tv_value.getText().toString()) ;
                mQty = setValuePosition;
                holder.tv_value.setText("" + setValuePosition);
                mCalculatePrice = mQty * Float.parseFloat(mPrice);

                // AddItemsToCart();
                String getCartId = MedicoboxApp.onGetCartID();
                String lCartId = getCartId;
                mCartId = lCartId.replace("\"", "");

                //call guest add to cart api
                try {

                    JSONObject object = new JSONObject();
                    object.put("quote_id", mCartId);
                    object.put("sku", mSku);
                    object.put("qty", mQty);

                    //Add Json Object
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cart_item", object);

                    AttemptAddToCart(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        holder.tv_plus.setTag(position);
        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rlayout_cart.setVisibility(View.VISIBLE);

                int lItemIndex = Integer.parseInt("" + v.getTag());
                mMedicineName = mDataArrayList.get(lItemIndex).getTitle();
                mValue = mDataArrayList.get(lItemIndex).getShort_description();
                mMrp = mDataArrayList.get(lItemIndex).getPrice();
                mdiscount = mDataArrayList.get(lItemIndex).getDiscount();
                mSku = mDataArrayList.get(lItemIndex).getSku();
                if (mDataArrayList.get(lItemIndex).getSale_price().isEmpty()) {
                    mPrice = mDataArrayList.get(lItemIndex).getPrice();
                } else {
                    mPrice = mDataArrayList.get(lItemIndex).getSale_price();
                }

                mImageURL = mDataArrayList.get(lItemIndex).getImage();
                setValuePosition = Integer.parseInt(holder.tv_value.getText().toString()) + 1;
                mQty = setValuePosition;
                holder.tv_value.setText("" + setValuePosition);
                mCalculatePrice = mQty * Float.parseFloat(mPrice);

                //call edit cart api
                try {

                    for (int i = 0; i < ItemModelList.size(); i++) {
                        if (mMedicineName.equals(ItemModelList.get(i).getMedicineName())) {
                            mItemId = ItemModelList.get(i).getItem_id();
                            break;
                        }
                    }

                    JSONObject object = new JSONObject();
                    object.put("quote_id", mCartId);
                    object.put("item_id", mItemId);
                    object.put("qty", mQty);


                    //Add Json Object
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("cart_item", object);

                    callEditCartItem(jsonObject, MedicoboxApp.onGetAuthToken());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        holder.tv_minus.setTag(position);
        holder.tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int lItemIndex = Integer.parseInt("" + v.getTag());
                setValuePosition = Integer.parseInt(holder.tv_value.getText().toString());
                if (setValuePosition != 1) {
                    --setValuePosition;
                    holder.tv_value.setText("" + setValuePosition);
                    mQty = setValuePosition;
                    mMedicineName = mDataArrayList.get(lItemIndex).getTitle();
                    mValue = mDataArrayList.get(lItemIndex).getShort_description();
                    mMrp = mDataArrayList.get(lItemIndex).getPrice();
                    mdiscount = mDataArrayList.get(lItemIndex).getDiscount();
                    mSku = mDataArrayList.get(lItemIndex).getSku();
                    if (mDataArrayList.get(lItemIndex).getSale_price().isEmpty()) {
                        mPrice = mDataArrayList.get(lItemIndex).getPrice();
                    } else {
                        mPrice = mDataArrayList.get(lItemIndex).getSale_price();
                    }
                    mImageURL = mDataArrayList.get(lItemIndex).getImage();
                    if (holder.tv_value.getText().equals("0")) {

                        holder.rlayout_number_of_item.setVisibility(View.GONE);
                        holder.rlayout_add.setVisibility(View.VISIBLE);

                    }
                    // AddItemsToCart();
                    //call edit cart api
                    try {

                        for (int i = 0; i < ItemModelList.size(); i++) {
                            if (mMedicineName.equals(ItemModelList.get(i).getMedicineName())) {
                                mItemId = ItemModelList.get(i).getItem_id();
                                break;
                            }
                        }

                        JSONObject object = new JSONObject();
                        object.put("quote_id", mCartId);
                        object.put("item_id", mItemId);
                        object.put("qty", mQty);


                        //Add Json Object
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("cart_item", object);


                        callEditCartItem(jsonObject, MedicoboxApp.onGetAuthToken());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    private void addItemsSingleton() {
        SingletonAddToCart singletonOptionData = SingletonAddToCart.getGsonInstance();
        ItemModelList = singletonOptionData.getOptionList();

        if (ItemModelList != null && !ItemModelList.isEmpty()) {
            Iterator<AddToCartOptionDetailModel> iterator = ItemModelList.iterator();
            foundduplicateItem = false;

            while (iterator.hasNext()) {
                AddToCartOptionDetailModel tempObj = iterator.next();
                if (tempObj.getMedicineName() != null && mMedicineName != null && tempObj.getMedicineName().equalsIgnoreCase(mMedicineName)) {
                    //  tempObj.setPrice("" + total);
                    tempObj.setQty("" + mQty);
                    // tempObj.setUrl(image_url);
                    foundduplicateItem = true;
                }
            }
            if (!foundduplicateItem) {
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount, mPrice, "" + mQty, mSku, mItemId, mCalculatePrice);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
                md.setSku(mSku);
                md.setItem_id(mItemId);
                md.setCalculatePrice(mCalculatePrice);
                singletonOptionData.option.add(md);
            } else if (foundduplicateItem && mQty == 0 && total == 0) {

                for (int i = 0; i < ItemModelList.size(); i++) {
                    if (ItemModelList.get(i).getQty().equals("0")) {
                        ItemModelList.remove(i);
                        if (!SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
                            //this is for make cart icon visible
                            rlayout_cart.setVisibility(View.VISIBLE);
                        } else {
                            rlayout_cart.setVisibility(View.VISIBLE);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        } else {
            if (mQty != 0) {
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount, mPrice, "" + mQty, mSku, mItemId, mCalculatePrice);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
                md.setSku(mSku);
                md.setItem_id(mItemId);
                md.setCalculatePrice(mCalculatePrice);
                singletonOptionData.option.add(md);
            }
        }

        String cart_size = String.valueOf(singletonOptionData.getOptionList().size());
        tv_cart_size.setText(cart_size);
        // calculateTotalPrice();

    }

    //Add to cart API
    private void AttemptAddToCart(JSONObject jsonObject) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            AndroidNetworking.post(ADDTOCART)
                    .addJSONObjectBody(jsonObject)
                    .addHeaders(Authorization, BEARER + MedicoboxApp.onGetAuthToken())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response

                            try {
                                mItemId = response.getString("item_id");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            BaseActivity.printLog("response-success : ", response.toString());
                            //save item id into itemid variable
                            addItemsSingleton();
                            CustomProgressDialog.getInstance().dismissDialog();
                        }

                        @Override
                        public void onError(ANError error) {
                            CustomProgressDialog.getInstance().dismissDialog();
                            // handle error
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }
    }

    private void callEditCartItem(JSONObject jsonObject, String bearerToken) {

        if (!isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "Check Your Network", Toast.LENGTH_SHORT).show();
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
            AndroidNetworking.put(EDITCARTITEM + mItemId)
                    .addJSONObjectBody(jsonObject)
                    .addHeaders(Authorization, BEARER + bearerToken)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response

                            try {
                                mItemId = response.getString("item_id");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            BaseActivity.printLog("response-success : ", response.toString());
                            //save item id into itemid variable
                            addItemsSingleton();
                            CustomProgressDialog.getInstance().dismissDialog();
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            // Toast.makeText(mContext, "Failed to load data", Toast.LENGTH_SHORT).show();
                            CustomProgressDialog.getInstance().dismissDialog();
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }
    }

    public static void checkBoxClickEvent(String wishList_id) {
        getWishListId = wishList_id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_medicine)
        ImageView img_medicine;
        @BindView(R.id.img_wishlist)
        ImageView img_wishlist;
        @BindView(R.id.tv_medicine_name)
        TextView tv_medicine_name;
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.tv_mrp)
        TextView tv_mrp;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.tv_mrp_price)
        TextView tv_mrp_price;
        @BindView(R.id.tv_discount)
        TextView tv_discount;
        @BindView(R.id.llayout_listing)
        LinearLayout llayout_listing;
        @BindView(R.id.rlayout_number_of_item)
        RelativeLayout rlayout_number_of_item;
        @BindView(R.id.rlayout_add)
        RelativeLayout rlayout_add;
        @BindView(R.id.tv_minus)
        TextView tv_minus;
        @BindView(R.id.tv_plus)
        TextView tv_plus;
        @BindView(R.id.tv_value)
        TextView tv_value;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setFilter(ArrayList<ListModel> Models) {
        mDataArrayList = new ArrayList<>();
        mDataArrayList.addAll(Models);
        notifyDataSetChanged();
    }

    private void ShowProductInfoAlert(final Context mContext, final String itemId) {
        dialog = new Dialog(mContext, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.alert_for_wishlist_popup);

        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        imgCancel = dialog.findViewById(R.id.imgCancel);
        edt_wishlist_name = dialog.findViewById(R.id.edt_wishlist_name);
        recycler_view_all_wishlist = dialog.findViewById(R.id.recycler_view_all_wishlist);

        btnAdd = dialog.findViewById(R.id.btnAdd);
        btnSave = dialog.findViewById(R.id.btnSave);

        CallGetAllWishListAPI(itemId);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDismissAlert.DismissAlert(dialog);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getValueFromEdittext = edt_wishlist_name.getText().toString().trim();
                if (getValueFromEdittext.isEmpty()) {
                    edt_wishlist_name.setError("Please add wishlist here");
                } else {
                    CallCreateWishListAPI(getValueFromEdittext, dialog, itemId, edt_wishlist_name);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!getWishListId.isEmpty()) {
                        CallAddProductToWishListAPI(itemId, getWishListId, dialog);
                    }else {
                        Toast.makeText(mContext, "Please add item to wishlist", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void CallGetAllWishListAPI(String itemId) {
        if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", MedicoboxApp.onGetId());
                Log.e("data", "" + jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(GET_ALL_WISHLIST)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

                            if (response.contains("There is no wishlist")) {
                                JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                                String responseMsg = getAllResponse.get("response").getAsString();
                                CustomProgressDialog.getInstance().dismissDialog();
                                Toast.makeText(mContext, "" + responseMsg, Toast.LENGTH_SHORT).show();
                            } else {
                                JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                                JsonArray asJsonArray = getAllResponse.getAsJsonArray("response");

                                if (asJsonArray != null) {
                                    mGetWishListModels.clear();
                                    for (int i = 0; i < asJsonArray.size(); i++) {
                                        String wishlist_name_id = ((JsonObject) asJsonArray.get(i)).get("wishlist_name_id").getAsString();
                                        String wishlist_name = ((JsonObject) asJsonArray.get(i)).get("wishlist_name").getAsString();
                                        JsonArray items = ((JsonObject) asJsonArray.get(i)).get("items").getAsJsonArray();

                                        GetWishListModel getWishListModel = new GetWishListModel(wishlist_name_id, wishlist_name, items);
                                        getWishListModel.setWishlist_name_id(wishlist_name_id);
                                        getWishListModel.setWishlist_name(wishlist_name);
                                        getWishListModel.setItems(items);
                                        mGetWishListModels.add(getWishListModel);
                                    }
                                }
                                //set all wishlist adapter
                                recycler_view_all_wishlist.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                recycler_view_all_wishlist.setHasFixedSize(true);
                                recycler_view_all_wishlist.setAdapter(new GetWishListAdapter(mContext, mGetWishListModels));
                                CustomProgressDialog.getInstance().dismissDialog();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            CustomProgressDialog.getInstance().dismissDialog();
                            //  Toast.makeText(ListActivity.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                            Log.e("Error", "onError errorCode : " + anError.getErrorCode());
                            Log.e("Error", "onError errorBody : " + anError.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    });
        }
    }

    private void CallCreateWishListAPI(String getValueFromEdittext, final Dialog dialog, final String itemId, final EditText edt_wishlist_name) {

        if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", MedicoboxApp.onGetId());
                jsonObject.put("wishlist_name", getValueFromEdittext);
                Log.e("data", "" + jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(CREATE_WISHLIST)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JsonObject asJsonObject = getAllResponse.getAsJsonObject("response");
                            String name_id = asJsonObject.get("name_id").getAsString();
                            String wishlist_name = asJsonObject.get("wishlist_name").getAsString();

                            CallGetAllWishListAPI(itemId);
                            edt_wishlist_name.setText("");
                            CustomProgressDialog.getInstance().dismissDialog();
                        }

                        @Override
                        public void onError(ANError anError) {
                            CustomProgressDialog.getInstance().dismissDialog();
                            //  Toast.makeText(ListActivity.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                            Log.e("Error", "onError errorCode : " + anError.getErrorCode());
                            Log.e("Error", "onError errorBody : " + anError.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    });
        }
    }

    private void CallAddProductToWishListAPI(final String itemId, String getWishListId, final Dialog dialog) {
        if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", MedicoboxApp.onGetId());
                jsonObject.put("wishlist_name_id", getWishListId);
                jsonObject.put("item_id", itemId);
                Log.e("data", "" + jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(ADD_ITEM_WISHLIST)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JsonObject jsonObject = getAllResponse.get("response").getAsJsonObject();
                            String responseMsg = jsonObject.get("status").getAsString();
                            String msg = jsonObject.get("msg").getAsString();

                            if (responseMsg.equals("success")) {
                                CustomProgressDialog.getInstance().dismissDialog();
                                mDismissAlert.DismissAlert(dialog);
                                Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
                            } else {
                                CustomProgressDialog.getInstance().dismissDialog();
                                Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
                            }
                            CallGetAllWishListAPI(itemId);
                        }

                        @Override
                        public void onError(ANError anError) {
                            CustomProgressDialog.getInstance().dismissDialog();
                            //  Toast.makeText(ListActivity.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                            Log.e("Error", "onError errorCode : " + anError.getErrorCode());
                            Log.e("Error", "onError errorBody : " + anError.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    });
        }
    }

    public interface DismissAlertInterface {
        public void DismissAlert(Dialog id);
    }
}