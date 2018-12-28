package com.aiprous.medicobox.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.OrderDetailsActivity;
import com.aiprous.medicobox.activity.OrderTrackingActivity;
import com.aiprous.medicobox.instaorder.InstaAddNewListActivity;
import com.aiprous.medicobox.model.OrderTrackingModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderTrackingAdapter extends RecyclerView.Adapter<OrderTrackingAdapter.ViewHolder> {

    private ArrayList<OrderTrackingModel> mDataArrayList;
    private OrderTrackingActivity mContext;

    public OrderTrackingAdapter(OrderTrackingActivity mContext, ArrayList<OrderTrackingModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_order_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Picasso.with(mContext)
                .load(mDataArrayList.get(position).getImage())
                .into(holder.imgProduct);

        holder.tvProductName.setText(mDataArrayList.get(position).getCompany_name());

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InstaAddNewListActivity.class);
                Activity activity = (Activity) mContext;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.img_product)
        ImageView imgProduct;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.txt_tracking_id)
        TextView txtTrackingId;
        @BindView(R.id.txt_sub_id)
        TextView txtSubId;
        @BindView(R.id.llMain)
        LinearLayout llMain;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
