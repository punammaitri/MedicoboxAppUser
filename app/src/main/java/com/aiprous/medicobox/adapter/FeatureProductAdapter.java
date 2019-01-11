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
import com.aiprous.medicobox.activity.ProductDetailBActivity;
import com.aiprous.medicobox.featuredproduct.FeaturedProductModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FeatureProductAdapter extends RecyclerView.Adapter<FeatureProductAdapter.ViewHolder> {
    private ArrayList<FeaturedProductModel> mDataArrayList;
    private Context mContext;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    public FeatureProductAdapter(Context mContext, ArrayList<FeaturedProductModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_product_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        //holder.img_product.setImageResource(mDataArrayList.get(position).getProduct_image());
        holder.tv_medicine_name.setText(mDataArrayList.get(position).getName());

        double input = Double.parseDouble(mDataArrayList.get(position).getPrice());
        holder.tv_product_mrp.setText(mContext.getResources().getString(R.string.Rs) + df2.format(input));
        holder.tv_product_mrp.setPaintFlags(holder.tv_product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        double final_price = Double.parseDouble(mDataArrayList.get(position).getFinal_price());
        holder.tv_product_price.setText(mContext.getResources().getString(R.string.Rs) + df2.format(final_price));

        Float getActualPrice = Float.valueOf(mDataArrayList.get(position).getPrice());
        Float getFinalPrice = Float.valueOf(mDataArrayList.get(position).getFinal_price());

        Float finalDisAmount = getActualPrice - getFinalPrice;
        Float getTotalDiscount = (finalDisAmount / getActualPrice) * 100;
        String formattedDiscount = String.format("%,.0f", getTotalDiscount);

        holder.tv_product_discount.setText(formattedDiscount + "%" + " Off");

        if (formattedDiscount.equals("0")) {
            holder.tv_product_discount.setVisibility(View.INVISIBLE);
            holder.tv_product_mrp.setVisibility(View.GONE);
        }else {
            holder.tv_product_discount.setVisibility(View.VISIBLE);
            holder.tv_product_mrp.setVisibility(View.VISIBLE);
        }


        holder.cardRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductDetailBActivity.class);
                intent.putExtra("productId", mDataArrayList.get(position).getEntity_id());
                intent.putExtra("Qty", "1");
                mContext.startActivity(intent);
            }
        });


        //Use Picasso to load image
        Picasso.with(mContext)
                .load(mDataArrayList.get(position).getImage())
                .placeholder(R.drawable.bottle)
                .into(holder.img_product, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.GONE);
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
