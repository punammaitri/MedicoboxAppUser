package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.activity.ProductDetailActivity;
import com.aiprous.medicobox.activity.ProductDetailBActivity;
import com.aiprous.medicobox.activity.SignUpActivity;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.AddToCartOptionDetailModel;
import com.aiprous.medicobox.model.ListModel;
import com.aiprous.medicobox.utils.BaseActivity;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.aiprous.medicobox.activity.ListActivity.rlayout_cart;
import static com.aiprous.medicobox.activity.ListActivity.tv_cart_size;
import static com.aiprous.medicobox.utils.APIConstant.ADDTOCART;
import static com.aiprous.medicobox.utils.APIConstant.Authorization;
import static com.aiprous.medicobox.utils.APIConstant.BEARER;

import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<ListModel> mDataArrayList;
    private ArrayList<AddToCartOptionDetailModel> ItemModelList;
    private Context mContext;

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




    public ListAdapter(Context mContext, ArrayList<ListModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
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

       // holder.img_medicine.setImageResource(mDataArrayList.get(position).getImage_url());
        holder.tv_medicine_name.setText(mDataArrayList.get(position).getTitle());
        holder.tv_content.setText(mDataArrayList.get(position).getShort_description());
       // holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs)+mDataArrayList.get(position).getMrp());
        holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
      //  holder.tv_discount.setText(mDataArrayList.get(position).getDiscount()+" OFF");
        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs)+mDataArrayList.get(position).getPrice());

        holder.llayout_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position%2==0)
                {
                    mContext.startActivity(new Intent(mContext,ProductDetailActivity.class));
                }else {
                    mContext.startActivity(new Intent(mContext,ProductDetailBActivity.class));
                }

            }
        });

        holder.rlayout_add.setTag(position);
        holder.rlayout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rlayout_number_of_item.setVisibility(View.VISIBLE);
                holder.rlayout_add.setVisibility(View.GONE);
                holder.tv_plus.performClick();

            }
        });

        holder.tv_plus.setTag(position);
        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rlayout_cart.setVisibility(View.VISIBLE);

                int lItemIndex = Integer.parseInt("" + v.getTag());
                mMedicineName=mDataArrayList.get(lItemIndex).getTitle();
                mValue=mDataArrayList.get(lItemIndex).getShort_description();
               // mMrp=mDataArrayList.get(lItemIndex).getMrp();
               // mdiscount=mDataArrayList.get(lItemIndex).getDiscount();
                mSku=mDataArrayList.get(lItemIndex).getSku();
                mPrice=mDataArrayList.get(lItemIndex).getPrice();
                mImageURL=mDataArrayList.get(lItemIndex).getImage();
                setValuePosition = Integer.parseInt(holder.tv_value.getText().toString()) + 1;
                mQty=setValuePosition;
                holder.tv_value.setText("" + setValuePosition);
                AddItemsToCart();

                //call guest add to cart api
                try {
                    JSONObject object = new JSONObject();
                    object.put("quote_id", MedicoboxApp.onGetCartID());
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

        holder.tv_minus.setTag(position);
        holder.tv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int lItemIndex = Integer.parseInt("" + v.getTag());
                setValuePosition = Integer.parseInt(holder.tv_value.getText().toString());
                if (setValuePosition != 0) {
                    --setValuePosition;
                    holder.tv_value.setText("" + setValuePosition);
                    mQty=setValuePosition;
                    mMedicineName=mDataArrayList.get(lItemIndex).getTitle();
                    mValue=mDataArrayList.get(lItemIndex).getShort_description();
                   // mMrp=mDataArrayList.get(lItemIndex).getMrp();
                   // mdiscount=mDataArrayList.get(lItemIndex).getDiscount();
                    mSku=mDataArrayList.get(lItemIndex).getSku();
                    mPrice=mDataArrayList.get(lItemIndex).getPrice();
                    mImageURL=mDataArrayList.get(lItemIndex).getImage();
                    if (holder.tv_value.getText().equals("0")) {

                        holder.rlayout_number_of_item.setVisibility(View.GONE);
                        holder.rlayout_add.setVisibility(View.VISIBLE);

                    }
                    AddItemsToCart();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

     private void AddItemsToCart() {
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
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount,mPrice,""+mQty,mSku);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
                md.setSku(mSku);
                singletonOptionData.option.add(md);
            } else if (foundduplicateItem && mQty == 0 && total == 0) {

                for (int i = 0; i < ItemModelList.size(); i++) {
                    if (ItemModelList.get(i).getQty().equals("0")) {
                        ItemModelList.remove(i);
                        if(!SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()){
                            //this is for make cart icon visible
                            rlayout_cart.setVisibility(View.VISIBLE);
                        }
                        else {
                            rlayout_cart.setVisibility(View.GONE);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        } else {
            if (mQty != 0) {
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount,mPrice,""+mQty,mSku);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
                md.setSku(mSku);
                singletonOptionData.option.add(md);
            }
        }

       String cart_size = String.valueOf(singletonOptionData.getOptionList().size());
        tv_cart_size.setText(cart_size + " Items | ");
       // calculateTotalPrice();

    }

    //api

    private void AttemptAddToCart(JSONObject jsonObject) {
       // mAlert.onShowProgressDialog(this, true);
        if (!isNetworkAvailable(mContext)) {
            Toast.makeText(mContext, "Check Your Network", Toast.LENGTH_SHORT).show();
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
                            //String getId = String.valueOf(response.get("id"));
                            // String getFirstname = String.valueOf(response.get("firstname"));
                            // String getLastname = String.valueOf(response.get("lastname"));
                            // String getEmail = String.valueOf(response.get("email"));

                            BaseActivity.printLog("response-success : ", response.toString());
                            //  mAlert.onShowProgressDialog(SignUpActivity.this, false);
                              /*  startActivity(new Intent(SignUpActivity.this, MainActivity.class)
                                        .putExtra("id", "" + getId)
                                        .putExtra("firstname", "" + getFirstname)
                                        .putExtra("lastname", "" + getLastname)
                                        .putExtra("email", "" + getEmail));*/

                            //  MedicoboxApp.onSaveLoginDetail(getId,"", getFirstname, getLastname, "", getEmail);
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Log.e("Error", "onError errorCode : " + error.getErrorCode());
                            Log.e("Error", "onError errorBody : " + error.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                        }
                    });
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_medicine)
        ImageView img_medicine;
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
}
