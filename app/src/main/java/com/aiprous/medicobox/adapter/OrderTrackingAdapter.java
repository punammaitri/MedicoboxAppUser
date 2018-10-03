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

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.OrderTrackingActivity;
import com.aiprous.medicobox.instaorder.InstaAddNewListActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderTrackingAdapter extends RecyclerView.Adapter<OrderTrackingAdapter.ViewHolder> {

    private ArrayList<OrderTrackingActivity.ListModel> mDataArrayList;
    private Context mContext;

    public OrderTrackingAdapter(Context mContext, ArrayList<OrderTrackingActivity.ListModel> mDataArrayList) {
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

        holder.imgProduct.setImageResource(mDataArrayList.get(position).getImage());
        holder.tvProductName.setText(mDataArrayList.get(position).getMedicineName());

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, InstaAddNewListActivity.class));
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
