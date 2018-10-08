package com.aiprous.medicobox.pharmacist.sellerorderdetails;

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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SellerItemOrderListAdapter extends RecyclerView.Adapter<SellerItemOrderListAdapter.ViewHolder> {

    private ArrayList<SellerItemOrderFragment.ListModel> mDataArrayList;
    private ArrayList<SellerItemOrderFragment.SubListModel> mSubListModelArray;
    private Context mContext;
    private PopupWindow window;

    public SellerItemOrderListAdapter(Context mContext, ArrayList<SellerItemOrderFragment.ListModel> mDataArrayList, ArrayList<SellerItemOrderFragment.SubListModel> mSubListModel) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
        this.mSubListModelArray = mSubListModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_seller_item_order_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //holder.imgProduct.setImageResource(mDataArrayList.get(position).getImage());
        holder.txtOrderID.setText(mDataArrayList.get(position).getOrderId());

        holder.rc_subList.setLayoutManager(new LinearLayoutManager(mContext));
        holder.rc_subList.setAdapter(new SellerItemOrderSubListAdapter(mContext, mSubListModelArray));

        holder.btn_view_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mContext.startActivity(new Intent(mContext,SellerOrderDetailsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_Order_ID)
        TextView tvOrderID;
        @BindView(R.id.txtOrderID)
        TextView txtOrderID;
        @BindView(R.id.SubList)
        RecyclerView rc_subList;
        @BindView(R.id.btn_processing)
        Button btnProcessing;
        @BindView(R.id.btn_view_detail)
        Button btn_view_detail;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
