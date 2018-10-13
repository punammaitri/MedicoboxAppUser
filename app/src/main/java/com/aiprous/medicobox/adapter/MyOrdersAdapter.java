package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.MyOrdersActivity;
import com.aiprous.medicobox.activity.OrderDetailsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {


    private ArrayList<MyOrdersActivity.MyOrdersModel> myOrdersArrayList;
    private Context mContext;

    public MyOrdersAdapter(Context mContext, ArrayList<MyOrdersActivity.MyOrdersModel> myOrdersArrayList) {
        this.mContext = mContext;
        this.myOrdersArrayList = myOrdersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_orders_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.tv_order_id.setText("Order ID: "+myOrdersArrayList.get(position).getOrderId());
        holder.tv_order_date.setText("Order Date: "+myOrdersArrayList.get(position).getOrder_date());
        holder.tv_order_price.setText(mContext.getResources().getString(R.string.Rs)+myOrdersArrayList.get(position).getOrder_price());

        if(myOrdersArrayList.get(position).getDeliverystatus().equals("0"))
        {
            holder.tv_deliver_order_status.setTextColor(mContext.getResources().getColor(R.color.colorgreen));
            holder.tv_deliver_order_status.setText("Delivered");


        }else if(myOrdersArrayList.get(position).getDeliverystatus().equals("1")){
            holder.tv_deliver_order_status.setTextColor(mContext.getResources().getColor(R.color.coloryellow));
            holder.tv_deliver_order_status.setText("Intransit");

        }else {

            holder.tv_deliver_order_status.setTextColor(mContext.getResources().getColor(R.color.colorred));
            holder.tv_deliver_order_status.setText("Cancelled");
        }
        holder.cardview_my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,OrderDetailsActivity.class));
            }
        });



    }


    @Override
    public int getItemCount() {
        return (myOrdersArrayList == null) ? 0 : myOrdersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order_id)
        TextView tv_order_id;
        @BindView(R.id.tv_order_date)
        TextView tv_order_date;
        @BindView(R.id.tv_order_price)
        TextView tv_order_price;
        @BindView(R.id.tv_deliver_order_status)
        TextView tv_deliver_order_status;
        @BindView(R.id.cardview_my_orders)
        CardView cardview_my_orders;



        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
