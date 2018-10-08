package com.aiprous.medicobox.pharmacist.dashboard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellerorder.SellerOrderActivity;
import com.aiprous.medicobox.pharmacist.sellerorder.SellerOrderSubListAdapter;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.SellerOrderDetailsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private ArrayList<DashboardFragment.SubListModel> mSubListModelArray;
    private Context mContext;
    private PopupWindow window;

    public DashboardAdapter(Context mContext, ArrayList<DashboardFragment.SubListModel> mSubListModel) {
        this.mContext = mContext;
        this.mSubListModelArray = mSubListModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //holder.imgProduct.setImageResource(mDataArrayList.get(position).getImage());
        holder.tv_medicine_name.setText(mSubListModelArray.get(position).getProduct_name());
    }

    @Override
    public int getItemCount() {
        return (mSubListModelArray == null) ? 0 : mSubListModelArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_medicine_name)
        TextView tv_medicine_name;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
