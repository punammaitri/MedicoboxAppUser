package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.OrderSummaryActivity;
import com.aiprous.medicobox.model.CartOrderSummaryModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {


    private ArrayList<CartOrderSummaryModel.Response> orderSummaryArrayList;
    private OrderSummaryActivity mContext;
    private Float getMrp = 0.0f;
    private Float getDiscount = 0.0f;
    private float finalDiscount = 0.0f;
    private AddDiscountInterfce mAddDiscountInterfce;

    public OrderSummaryAdapter(OrderSummaryActivity mContext, ArrayList<CartOrderSummaryModel.Response> orderSummaryArrayList) {
        this.mContext = mContext;
        this.orderSummaryArrayList = orderSummaryArrayList;
        this.mAddDiscountInterfce = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_summary_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.tv_item_name.setText("" + orderSummaryArrayList.get(position).getName());
        holder.tv_qty.setText("Qty : " + orderSummaryArrayList.get(position).getQty());

       /* if (orderSummaryArrayList.get(position).getSale_price().isEmpty()) {
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + orderSummaryArrayList.get(position).getPrice());
            Float getPrice = Float.valueOf(orderSummaryArrayList.get(position).getPrice());
            Float getQty = Float.valueOf(orderSummaryArrayList.get(position).getQty());
            Float mDiscount = Float.valueOf(orderSummaryArrayList.get(position).getDiscount());

            //for mrp
            Float finalAmout = getQty * getPrice;
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + " " + finalAmout);
            getMrp = getMrp + finalAmout;

            //for discont
            finalDiscount = mDiscount * getQty;
            getDiscount = getDiscount + finalDiscount;

        } else {
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + orderSummaryArrayList.get(position).getSale_price());
            Float getSalePrice = Float.valueOf(orderSummaryArrayList.get(position).getSale_price());
            Float getQty = Float.valueOf(orderSummaryArrayList.get(position).getQty());
            Float mDiscount = Float.valueOf(orderSummaryArrayList.get(position).getDiscount());

            //for mrp
            Float finalSaleAmout = getQty * getSalePrice;
            holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + " " + finalSaleAmout);
            getMrp = getMrp + finalSaleAmout;

            //for discont
            finalDiscount = mDiscount * getQty;
            getDiscount = getDiscount + finalDiscount;
        }*/

        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + orderSummaryArrayList.get(position).getPrice());
        Float getPrice = Float.valueOf(orderSummaryArrayList.get(position).getPrice());
        Float getQty = Float.valueOf(orderSummaryArrayList.get(position).getQty());
        Float mDiscount = Float.valueOf(orderSummaryArrayList.get(position).getDiscount());

        //for mrp
        Float finalAmout = getQty * getPrice;
        holder.tv_price.setText(mContext.getResources().getString(R.string.Rs) + " " + finalAmout);
        getMrp = getMrp + finalAmout;

        //for discont
        finalDiscount = mDiscount * getQty;
        getDiscount = getDiscount + finalDiscount;

        Log.e("mrp", "" + getMrp);
        Log.e("discount", "" + getDiscount);

        Picasso.with(mContext)
                .load(orderSummaryArrayList.get(position).getImage())
                .into(holder.img_product, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progress_bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.progress_bar.setVisibility(View.GONE);
                    }
                });

        if (position == getItemCount() - 1) {
            holder.view_order_summary.setVisibility(View.GONE);
        }

        int getPos = position + 1;
        if (orderSummaryArrayList.size() == getPos) {
            mAddDiscountInterfce.ShowValue(getMrp, getDiscount);
        }
    }

    @Override
    public int getItemCount() {
        return (orderSummaryArrayList == null) ? 0 : orderSummaryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_product)
        ImageView img_product;
        @BindView(R.id.progress_bar)
        ProgressBar progress_bar;
        @BindView(R.id.tv_qty)
        TextView tv_qty;
        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.view_order_summary)
        View view_order_summary;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface AddDiscountInterfce {
        public void ShowValue(Float mrp, Float discount);
    }
}
