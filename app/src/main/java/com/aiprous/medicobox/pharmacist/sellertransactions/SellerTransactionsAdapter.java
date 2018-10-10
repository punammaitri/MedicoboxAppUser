package com.aiprous.medicobox.pharmacist.sellertransactions;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.MyOrdersActivity;
import com.aiprous.medicobox.activity.OrderDetailsActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SellerTransactionsAdapter extends RecyclerView.Adapter<SellerTransactionsAdapter.ViewHolder> {


    private ArrayList<SellerTransactionActivity.SellerTransactionModel> msellerTransactionArrayList;
    private Context mContext;

    public SellerTransactionsAdapter(Context mContext, ArrayList<SellerTransactionActivity.SellerTransactionModel> msellerTransactionArrayList) {
        this.mContext = mContext;
        this.msellerTransactionArrayList = msellerTransactionArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_transactions_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        holder.tv_tranaction_id.setText("Transaction Id: "+msellerTransactionArrayList.get(position).getTransactionId());
        holder.tv_amount.setText(mContext.getResources().getString(R.string.Rs)+msellerTransactionArrayList.get(position).getAmount());

        holder.rlayout_seller_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,SellerTransactionDetailsActivity.class));
            }
        });
    }


    @Override
    public int getItemCount() {
        return (msellerTransactionArrayList == null) ? 0 : msellerTransactionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tranaction_id)
        TextView tv_tranaction_id;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_comment_message)
        TextView tv_comment_message;
        @BindView(R.id.tv_amount)
        TextView tv_amount;
        @BindView(R.id.tv_view_details)
        TextView tv_view_details;
        @BindView(R.id.rlayout_seller_transaction)
        LinearLayout rlayout_seller_transaction;



        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
