package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.OrderSummaryActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {


    private ArrayList<OrderSummaryActivity.OrderSummaryModel> orderSummaryArrayList;
    private Context mContext;

    public OrderSummaryAdapter(Context mContext, ArrayList<OrderSummaryActivity.OrderSummaryModel> orderSummaryArrayList) {
        this.mContext = mContext;
        this.orderSummaryArrayList = orderSummaryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_summary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.tv_item_name.setText(orderSummaryArrayList.get(position).getProductName());
        holder.tv_medicine_contains.setText(orderSummaryArrayList.get(position).getProductcontains());
        holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs) + orderSummaryArrayList.get(position).getProduct_mrp());
        holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + orderSummaryArrayList.get(position).getProduct_price());

        if (position == getItemCount() - 1) {
            holder.view_order_summary.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (orderSummaryArrayList == null) ? 0 : orderSummaryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_mrp_price)
        TextView tv_mrp_price;
        @BindView(R.id.tv_medicine_contains)
        TextView tv_medicine_contains;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.view_order_summary)
        View view_order_summary;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
