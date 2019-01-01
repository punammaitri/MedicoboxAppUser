package com.aiprous.medicobox.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.ProductDetailBActivity;
import com.aiprous.medicobox.model.RelatedProductModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SubstitutesProductAdapter extends RecyclerView.Adapter<SubstitutesProductAdapter.ViewHolder> {
    private ArrayList<RelatedProductModel.Data> mDataArrayList;
    private ProductDetailBActivity mContext;
    private LoadProductInterface mLoadProduct;

    public SubstitutesProductAdapter(ProductDetailBActivity mContext, ArrayList<RelatedProductModel.Data> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
        this.mLoadProduct = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.substitutes_product_item, parent, false);
        return new ViewHolder(view);

       /* View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.substitutes_product_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;*/
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv_name.setText(mDataArrayList.get(position).getName());
        holder.tv_company_name.setText(mDataArrayList.get(position).getCompanyName());

        holder.tv_price.setText("" + mContext.getResources().getString(R.string.Rs) + mDataArrayList.get(position).getPrice());

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mLoadProduct.LoadProduct(mDataArrayList.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_company_name)
        TextView tv_company_name;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.layout_main)
        LinearLayout layout_main;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface LoadProductInterface {
        public void LoadProduct(String product_id);
    }

}
