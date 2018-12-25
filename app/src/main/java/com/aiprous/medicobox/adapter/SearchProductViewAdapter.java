package com.aiprous.medicobox.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.model.SearchModel;
import com.aiprous.medicobox.prescription.PrescriptionUploadOptionActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchProductViewAdapter extends RecyclerView.Adapter<SearchProductViewAdapter.ViewHolder> {


    private ArrayList<SearchModel> mSearchResultArrayList;
    private PrescriptionUploadOptionActivity mContext;
    private DeleteCartItemInterface mDeleteCartItemInterface;

    public SearchProductViewAdapter(PrescriptionUploadOptionActivity mContext, ArrayList<SearchModel> mSearchResultArrayList) {
        this.mContext = mContext;
        this.mSearchResultArrayList = mSearchResultArrayList;
        this.mDeleteCartItemInterface=mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_product_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv_main_category_name.setText(mSearchResultArrayList.get(position).getTitle());

        Picasso.with(mContext)
                .load(mSearchResultArrayList.get(position).getImage())
                .into(holder.img_main_category);

       /* holder.tv_main_category_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              *//*  Intent intent = new Intent(mContext, ProductDetailBActivity.class);
                intent.putExtra("productId", mSearchResultArrayList.get(position).getId());
                intent.putExtra("VisibiltyFlag", "");
                intent.putExtra("SKU", mSearchResultArrayList.get(position).getSku());
                intent.putExtra("Qty","");
                intent.putExtra("imageUrl", mSearchResultArrayList.get(position).getImage());
                intent.putExtra("MedicineName", mSearchResultArrayList.get(position).getTitle());
                intent.putExtra("value", "");
                intent.putExtra("price", mSearchResultArrayList.get(position).getPrice());
                intent.putExtra("prescription", mSearchResultArrayList.get(position).getShort_description());
                intent.putExtra("MrpPrice", "");
                intent.putExtra("discount", mSearchResultArrayList.get(position).getDiscount());
                mContext.startActivity(intent);*//*
            }
        });*/

        holder.img_delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteCartItemInterface.DeleteCartItem(mSearchResultArrayList.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mSearchResultArrayList == null) ? 0 : mSearchResultArrayList.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_main_category_name)
        TextView tv_main_category_name;
        @BindView(R.id.img_main_category)
        ImageView img_main_category;
        @BindView(R.id.img_delete_item)
        ImageView img_delete_item;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

//    public void setFilter(ArrayList<SearchModel> Models) {
//        mSearchResultArrayList = new ArrayList<>();
//        mSearchResultArrayList.addAll(Models);
//        notifyDataSetChanged();
//    }
 public interface DeleteCartItemInterface {
    public void DeleteCartItem(String id);
 }


}
