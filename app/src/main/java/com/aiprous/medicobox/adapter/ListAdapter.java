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
import android.widget.TextView;


import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.activity.ProductDetailActivity;
import com.aiprous.medicobox.activity.ProductDetailBActivity;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import medicobox.aiprous.com.medicobox.R;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<ListActivity.ListModel> mDataArrayList;
    private Context mContext;

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




    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
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


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
