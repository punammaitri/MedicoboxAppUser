package com.aiprous.medicobox.instaorder;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import medicobox.aiprous.com.medicobox.R;


public class InstaProductSubListDetailAdapter extends RecyclerView.Adapter<InstaProductSubListDetailAdapter.ViewHolder> {

    private ArrayList<InstaAddNewListActivity.SubListModel> mSubListArray;
    private Context mContext;

    public InstaProductSubListDetailAdapter(Context mContext, ArrayList<InstaAddNewListActivity.SubListModel> mDataArrayList) {
        this.mContext = mContext;
        this.mSubListArray = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insta_add_new_sublist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.txt_tab_name.setText(mSubListArray.get(position).medicineName);
        holder.tv_value.setText(mSubListArray.get(position).price);
    }

    @Override
    public int getItemCount() {
        return (mSubListArray == null) ? 0 : mSubListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_tab_name)
        TextView txt_tab_name;
        @BindView(R.id.tv_value)
        TextView tv_value;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
