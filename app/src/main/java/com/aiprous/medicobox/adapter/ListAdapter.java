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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<ListActivity.ListModel> mDataArrayList;
    private Context mContext;

    int number_of_item = 1;
    int setValuePosition = 1;
    private String mMedicineName;
    private String mPrice;


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

                int lItemIndex = Integer.parseInt("" + v.getTag());
                mMedicineName=mDataArrayList.get(lItemIndex).getMedicineName();
                mPrice=mDataArrayList.get(lItemIndex).getPrice();
                setValuePosition = Integer.parseInt(holder.tv_value.getText().toString()) + 1;
                holder.tv_value.setText("" + setValuePosition);
               // AddSingletonTosendAPI();
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

                    mMedicineName=mDataArrayList.get(lItemIndex).getMedicineName();
                    mPrice=mDataArrayList.get(lItemIndex).getPrice();

                    if (holder.tv_value.getText().equals("0")) {

                        holder.rlayout_number_of_item.setVisibility(View.GONE);
                        holder.rlayout_add.setVisibility(View.VISIBLE);

                    }
                   // AddSingletonTosendAPI();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

   /* private void AddSingletonTosendAPI() {
        SingletonOptionAddItemtoCart singletonOptionData = SingletonOptionAddItemtoCart.getGsonInstance();
        ItemModelList = singletonOptionData.getOptionList();

        if (ItemModelList != null && !ItemModelList.isEmpty()) {
            Iterator<AddToCartOptionDetailModel> iterator = ItemModelList.iterator();
            foundduplicateItem = false;

            while (iterator.hasNext()) {
                AddToCartOptionDetailModel tempObj = iterator.next();
                if (tempObj.getMatnr() != null && mMatnr != null && tempObj.getMatnr().equalsIgnoreCase(mMatnr)) {
                    tempObj.setPrice("" + total);
                    tempObj.setQty("" + mQty);
                    // tempObj.setUrl(image_url);
                    foundduplicateItem = true;
                }
            }
            if (!foundduplicateItem) {
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mMatnr, munique_id, "" + total, "" + mQty, mName,mUnit,munique_id);
                md.setMatnr(mMatnr);
                md.setUnique_ID(munique_id);
                md.setPrice(String.valueOf(total));
                md.setQty("" + mQty);
                md.setName(mName);
                md.setUnit(mUnit);
                md.setVendor_id(mVendorId);
                singletonOptionData.option.add(md);
            } else if (foundduplicateItem && mQty == 0 && total == 0) {

                for (int i = 0; i < ItemModelList.size(); i++) {
                    if (ItemModelList.get(i).getQty().equals("0")) {
                        ItemModelList.remove(i);
                        if(!SingletonOptionAddItemtoCart.getGsonInstance().getOptionList().isEmpty()){
                            rlayout_cart.setVisibility(View.VISIBLE);
                        }
                        else {
                            rlayout_cart.setVisibility(View.GONE);
                        }
                    }
                }
                //notifyDataSetChanged();
            }
        } else {
            if (mQty != 0) {
                AddToCartOptionDetailModel md = new AddToCartOptionDetailModel(mMatnr, munique_id, "" + total, "" + mQty, mName,mUnit,mVendorId);
                md.setMatnr(mMatnr);
                md.setUnique_ID(munique_id);
                md.setPrice("" + total);
                md.setQty("" + mQty);
                md.setName(mName);
                md.setUnit(mUnit);
                md.setVendor_id(mVendorId);
                singletonOptionData.option.add(md);
            }
        }

        String cart_size = String.valueOf(singletonOptionData.getOptionList().size());
        CustomerSideMenuTabActivity.tv_number_of_item.setText(cart_size + " Items | ");
        calculateTotalPrice();


    }*/

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
