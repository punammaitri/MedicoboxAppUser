package com.aiprous.medicobox.pharmacist.sellerorder;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiprous.medicobox.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SellerOrderSubListAdapter extends RecyclerView.Adapter<SellerOrderSubListAdapter.ViewHolder> {


    private ArrayList<SellerOrderActivity.SubListModel> mSubListArray;
    private Context mContext;
    private Dialog dialog;
    private TextView txtOk;

    public SellerOrderSubListAdapter(Context mContext, ArrayList<SellerOrderActivity.SubListModel> mDataArrayList) {
        this.mContext = mContext;
        this.mSubListArray = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_order_sublist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.tvProductName.setText(mSubListArray.get(position).getProduct_name());
    }


    @Override
    public int getItemCount() {
        return (mSubListArray == null) ? 0 : mSubListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.txtQty)
        TextView txtQty;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
