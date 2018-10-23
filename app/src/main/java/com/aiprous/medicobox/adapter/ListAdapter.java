package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.activity.ProductDetailActivity;
import com.aiprous.medicobox.activity.ProductDetailBActivity;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.AddToCartOptionDetailModel;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiprous.medicobox.activity.ListActivity.rlayout_cart;
import static com.aiprous.medicobox.activity.ListActivity.tv_cart_size;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<ListActivity.ListModel> mDataArrayList;
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
    int mImageURL;




    public ListAdapter(Context mContext, ArrayList<ListActivity.ListModel> mDataArrayList) {
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

        holder.img_medicine.setImageResource(mDataArrayList.get(position).getImage());
        holder.tv_medicine_name.setText(mDataArrayList.get(position).getMedicineName());
        holder.tv_content.setText(mDataArrayList.get(position).getValue());
        holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs)+mDataArrayList.get(position).getMrp());
        holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_discount.setText(mDataArrayList.get(position).getDiscount()+" OFF");
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
                mMedicineName=mDataArrayList.get(lItemIndex).getMedicineName();
                mValue=mDataArrayList.get(lItemIndex).getValue();
                mMrp=mDataArrayList.get(lItemIndex).getMrp();
                mdiscount=mDataArrayList.get(lItemIndex).getDiscount();
                mPrice=mDataArrayList.get(lItemIndex).getPrice();
                mImageURL=mDataArrayList.get(lItemIndex).getImage();
                setValuePosition = Integer.parseInt(holder.tv_value.getText().toString()) + 1;
                mQty=setValuePosition;
                holder.tv_value.setText("" + setValuePosition);
                AddItemsToCart();
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
                    mMedicineName=mDataArrayList.get(lItemIndex).getMedicineName();
                    mValue=mDataArrayList.get(lItemIndex).getValue();
                    mMrp=mDataArrayList.get(lItemIndex).getMrp();
                    mdiscount=mDataArrayList.get(lItemIndex).getDiscount();
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
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount,mPrice,""+mQty);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
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
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mImageURL, mMedicineName, mValue, mMrp, mdiscount,mPrice,""+mQty);
                md.setImage(mImageURL);
                md.setMedicineName(mMedicineName);
                md.setValue(mValue);
                md.setMrp(mMrp);
                md.setDiscount(mdiscount);
                md.setPrice(mPrice);
                md.setQty("" + mQty);
                singletonOptionData.option.add(md);
            }
        }

       String cart_size = String.valueOf(singletonOptionData.getOptionList().size());
        tv_cart_size.setText(cart_size + " Items | ");
       // calculateTotalPrice();

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

    public void setFilter(ArrayList<ListActivity.ListModel> Models) {
        mDataArrayList = new ArrayList<>();
        mDataArrayList.addAll(Models);
        notifyDataSetChanged();
    }
}
