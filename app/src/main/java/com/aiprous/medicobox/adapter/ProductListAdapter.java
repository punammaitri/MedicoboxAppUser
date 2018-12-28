package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.model.ProductsModel;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private ArrayList<ProductsModel> mDataArrayList;
    private Context mContext;

    private static DecimalFormat df2 = new DecimalFormat(".##");

    public ProductListAdapter(Context mContext, ArrayList<ProductsModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv_medicine_name.setText(mDataArrayList.get(position).getName());
        holder.tv_item_contains.setText(mDataArrayList.get(position).getSku());
        holder.tv_mrp.setText("MRP ");

        //remove digit after dot & set value
        double input = Double.parseDouble(mDataArrayList.get(position).getPrice());
        holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs) + df2.format(input));
        holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + df2.format(input));

        if (position == getItemCount() - 1) {
            holder.view_order_details.setVisibility(View.GONE);
        }

        //Check for image empty or not
        if (!mDataArrayList.get(position).getImage().isEmpty()) {
            Picasso.with(mContext)
                    .load(mDataArrayList.get(position).getImage())
                    .into(holder.img_medicine, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progress_bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.progress_bar.setVisibility(View.GONE);
                        }
                    });

        }

    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_medicine_name)
        TextView tv_medicine_name;
        @BindView(R.id.tv_mrp_price)
        TextView tv_mrp_price;
        @BindView(R.id.tv_item_contains)
        TextView tv_item_contains;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.view_order_details)
        View view_order_details;
        @BindView(R.id.progress_bar)
        ProgressBar progress_bar;
        @BindView(R.id.img_medicine)
        ImageView img_medicine;
        @BindView(R.id.tv_mrp)
        TextView tv_mrp;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
