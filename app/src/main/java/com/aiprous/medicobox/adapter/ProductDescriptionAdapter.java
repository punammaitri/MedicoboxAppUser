package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.fragment.HomeFragment;
import com.aiprous.medicobox.pharmacist.sellertransaction.SellerTransactionActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductDescriptionAdapter extends RecyclerView.Adapter<ProductDescriptionAdapter.ViewHolder> {
    private ArrayList<HomeFragment.Product> mDataArrayList;
    private Context mContext;

    public ProductDescriptionAdapter(Context mContext, ArrayList<HomeFragment.Product> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext())
        //        .inflate(R.layout.feature_product_item, parent, false);
        // return new ViewHolder(view);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_product_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.img_product.setImageResource(mDataArrayList.get(position).getProduct_image());
        holder.tv_medicine_name.setText(mDataArrayList.get(position).getProduct_name());
        holder.tv_product_mrp.setText(mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getProduct_price());
        holder.tv_product_mrp.setPaintFlags(holder.tv_product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        /*holder.tv_product_discount.setText(mDataArrayList.get(position).getProduct_discount() + "off");
        holder.tv_product_price.setText(mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getProduct_price());*/

       /* //Use Picasso to load image
        Picasso.with(mContext)
                .load()
                .into(holder.img_product, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.GONE);
                    }
                });*/


        holder.cardRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, SellerTransactionActivity.class));
                //((Activity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_product)
        ImageView img_product;
        @BindView(R.id.tv_product_name)
        TextView tv_medicine_name;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.tv_product_mrp)
        TextView tv_product_mrp;
        @BindView(R.id.tv_product_price)
        TextView tv_product_price;
        @BindView(R.id.cardRating)
        CardView cardRating;
        @BindView(R.id.tv_product_discount)
        TextView tv_product_discount;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
