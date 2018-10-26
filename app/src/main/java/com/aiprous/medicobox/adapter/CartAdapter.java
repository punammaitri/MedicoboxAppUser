package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.model.AddToCartOptionDetailModel;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiprous.medicobox.activity.CartActivity.nestedscroll_cart;
import static com.aiprous.medicobox.activity.CartActivity.tv_cart_empty;
import static com.aiprous.medicobox.activity.CartActivity.tv_cart_size;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    private ArrayList<AddToCartOptionDetailModel> mCartArrayList;
    private Context mContext;

    private ArrayList<AddToCartOptionDetailModel> ItemModelList;
    boolean foundduplicateItem;
    int setValuePosition = 1;
    int total = 0;
    private String mMedicineName;
    private String mValue;
    private String mMrp;
    private String mdiscount;
    private String mPrice;
    int mQty;
    String mImageURL;
    public int mTotalMRPprice = 0;
    private int mTotalPrice=0;
    private String mSku;


    public CartAdapter(Context mContext, ArrayList<AddToCartOptionDetailModel> mCartArrayList) {
        this.mContext = mContext;
        this.mCartArrayList = mCartArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

     holder.tv_item_name.setText(mCartArrayList.get(position).getMedicineName());
     holder.tv_medicine_contains.setText(mCartArrayList.get(position).getValue());
     holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs)+mCartArrayList.get(position).getMrp());
     holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
     holder.tv_price.setText(mContext.getResources().getString(R.string.Rs)+mCartArrayList.get(position).getPrice());


     //set number of items
        holder.tv_value.setText(mCartArrayList.get(position).getQty());

        //Add to cart
        mMedicineName=mCartArrayList.get(position).getMedicineName();
        mValue=mCartArrayList.get(position).getValue();
        mMrp=mCartArrayList.get(position).getMrp();
        mdiscount=mCartArrayList.get(position).getDiscount();
        mPrice=mCartArrayList.get(position).getPrice();
        mImageURL=mCartArrayList.get(position).getImage();
        mQty= Integer.parseInt(holder.tv_value.getText().toString());

        AddItemsToCart();



     if(position==getItemCount()-1)
     {
         holder.view_cart_item.setVisibility(View.GONE);
     }


        holder.tv_plus.setTag(position);
        holder.tv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int lItemIndex = Integer.parseInt("" + v.getTag());
                mMedicineName=mCartArrayList.get(lItemIndex).getMedicineName();
                mValue=mCartArrayList.get(lItemIndex).getValue();
                mMrp=mCartArrayList.get(lItemIndex).getMrp();
                mdiscount=mCartArrayList.get(lItemIndex).getDiscount();
                mPrice=mCartArrayList.get(lItemIndex).getPrice();
                mImageURL=mCartArrayList.get(lItemIndex).getImage();
                mSku=mCartArrayList.get(lItemIndex).getSku();
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
                    mMedicineName=mCartArrayList.get(lItemIndex).getMedicineName();
                    mValue=mCartArrayList.get(lItemIndex).getValue();
                    mMrp=mCartArrayList.get(lItemIndex).getMrp();
                    mdiscount=mCartArrayList.get(lItemIndex).getDiscount();
                    mPrice=mCartArrayList.get(lItemIndex).getPrice();
                    mImageURL=mCartArrayList.get(lItemIndex).getImage();
                    mSku=mCartArrayList.get(lItemIndex).getSku();
                    if (holder.tv_value.getText().equals("0")) {

                       // holder.rlayout_number_of_item.setVisibility(View.GONE);
                      //  holder.rlayout_add.setVisibility(View.VISIBLE);


                    }
                    AddItemsToCart();
                }
            }
        });

        holder.rlayout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SingletonAddToCart singletonOptionData = SingletonAddToCart.getGsonInstance();
                ItemModelList = singletonOptionData.getOptionList();
                for (int i = 0; i < ItemModelList.size(); i++) {
                    if (ItemModelList.get(i).getMedicineName().equals(mCartArrayList.get(position).getMedicineName())) {
                        ItemModelList.remove(i);
                        notifyDataSetChanged();
                        if(!SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()){
                            //this is for make cart icon visible
                            CartActivity.rlayout_cart.setVisibility(View.VISIBLE);
                            tv_cart_empty.setVisibility(View.GONE);
                            nestedscroll_cart.setVisibility(View.VISIBLE);
                        }
                        }
                        else {

                        CartActivity.rlayout_cart.setVisibility(View.GONE);
                        tv_cart_empty.setVisibility(View.VISIBLE);
                        nestedscroll_cart.setVisibility(View.GONE);

                    }
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return (mCartArrayList == null) ? 0 : mCartArrayList.size();
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
                            CartActivity.rlayout_cart.setVisibility(View.VISIBLE);
                            tv_cart_empty.setVisibility(View.GONE);
                            nestedscroll_cart.setVisibility(View.VISIBLE);
                        }
                        else {

                            CartActivity.rlayout_cart.setVisibility(View.GONE);
                            tv_cart_empty.setVisibility(View.VISIBLE);
                            nestedscroll_cart.setVisibility(View.GONE);
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
         tv_cart_size.setText(cart_size);
         calculateTotalPrice();

    }

    //To calculate the total
    public void calculateTotalPrice() {
        mTotalMRPprice = 0;
        mTotalPrice=0;
        if (ItemModelList != null) {
            Iterator<AddToCartOptionDetailModel> iterator = ItemModelList.iterator();
            while (iterator.hasNext()) {
                AddToCartOptionDetailModel tempObj = iterator.next();
                if (tempObj.getMrp() != null && !tempObj.getMrp().isEmpty()) {
                    try {
                        String tempMRPCost = tempObj.getMrp();
                        String tempPrice=tempObj.getPrice();
                        mTotalMRPprice = mTotalMRPprice + Integer.parseInt(tempMRPCost);
                        mTotalPrice= mTotalPrice+Integer.parseInt(tempPrice);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            CartActivity.tv_mrp_total.setText(mContext.getResources().getString(R.string.Rs)+""+mTotalMRPprice);
            int lPriceDiscount=mTotalMRPprice-mTotalPrice;
            CartActivity.tv_price_discount.setText(mContext.getResources().getString(R.string.Rs)+""+lPriceDiscount);
            CartActivity.tv_to_be_paid.setText(mContext.getResources().getString(R.string.Rs)+""+mTotalPrice);
            CartActivity.tv_total_saving.setText(mContext.getResources().getString(R.string.Rs)+""+lPriceDiscount);
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_medicine_contains)
        TextView tv_medicine_contains;
        @BindView(R.id.tv_mrp_price)
        TextView tv_mrp_price;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.view_cart_item)
        View view_cart_item;
        @BindView(R.id.tv_minus)
        TextView tv_minus;
        @BindView(R.id.tv_plus)
        TextView tv_plus;
        @BindView(R.id.tv_value)
        TextView tv_value;
        @BindView(R.id.rlayout_delete)
        RelativeLayout rlayout_delete;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
