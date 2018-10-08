package com.aiprous.medicobox.pharmacist.sellerorderdetails.invoice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.itemorder.SellerItemOrderFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SellerInvoiceAdapter extends RecyclerView.Adapter<SellerInvoiceAdapter.ViewHolder> {


    private ArrayList<SellerInvoiceFragment.ListModel> mDataArrayList;
    private Context mContext;

    public SellerInvoiceAdapter(Context mContext, ArrayList<SellerInvoiceFragment.ListModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_seller_invoice_item, parent, false);
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
