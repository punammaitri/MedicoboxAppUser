package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.OrderDetailsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    private ArrayList<OrderDetailsActivity.ProductModel> mDataArrayList;
    private Context mContext;

    public OrderDetailsAdapter(Context mContext, ArrayList<OrderDetailsActivity.ProductModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv_medicine_name.setText(mDataArrayList.get(position).getMedicine_name());
        holder.tv_item_contains.setText(mDataArrayList.get(position).getItem_contains());
        holder.tv_mrp_price.setText("MRP " + mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getMrp_price());
        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getMedicine_price());

        if (position == getItemCount() - 1) {
            holder.view_order_details.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_medicine_name)
        TextView tv_medicine_name;
        @BindView(R.id.tv_mrp_price)
        TextView tv_mrp_price;
        @BindView(R.id.tv_item_contains)
        TextView tv_item_contains;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.view_order_details)
        View view_order_details;
        @BindView(R.id.img_medicine)
        ImageView img_medicine;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
