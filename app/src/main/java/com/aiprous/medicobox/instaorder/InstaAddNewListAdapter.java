package com.aiprous.medicobox.instaorder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import medicobox.aiprous.com.medicobox.R;


public class InstaAddNewListAdapter extends RecyclerView.Adapter<InstaAddNewListAdapter.ViewHolder> {


    private ArrayList<InstaAddNewListActivity.ListModel> mDataArrayList;
    private Context mContext;
    private ArrayList<InstaAddNewListActivity.SubListModel> mSubListModelArray;
    public InstaAddNewListAdapter(Context mContext, ArrayList<InstaAddNewListActivity.ListModel> mDataArrayList, ArrayList<InstaAddNewListActivity.SubListModel> mSubListModel) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
        this.mSubListModelArray = mSubListModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insta_add_new_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //holder.imgProduct.setImageResource(mDataArrayList.get(position).getImage());
        holder.tvMedicineType.setText(mDataArrayList.get(position).getMedicineName());

        holder.list.setLayoutManager(new LinearLayoutManager(mContext));
        holder.list.setAdapter(new InstaProductSubListDetailAdapter(mContext, mSubListModelArray));

    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_medicine_type)
        TextView tvMedicineType;
        @BindView(R.id.btn_add_to_cart)
        Button btnAddToCart;
        @BindView(R.id.btn_order_checkout)
        Button btnOrderCheckout;
        @BindView(R.id.list)
        RecyclerView list;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
