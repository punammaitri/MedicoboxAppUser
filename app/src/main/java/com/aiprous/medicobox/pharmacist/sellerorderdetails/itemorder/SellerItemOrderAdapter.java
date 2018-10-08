package com.aiprous.medicobox.pharmacist.sellerorderdetails.itemorder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiprous.medicobox.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SellerItemOrderAdapter extends RecyclerView.Adapter<SellerItemOrderAdapter.ViewHolder> {


    private ArrayList<SellerItemOrderFragment.ListModel> mDataArrayList;
    private Context mContext;
    private PopupWindow window;

    public SellerItemOrderAdapter(Context mContext, ArrayList<SellerItemOrderFragment.ListModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
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

        holder.tvProductName.setText(mDataArrayList.get(position).getOrderId());
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_product_name)
        TextView tvProductName;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
