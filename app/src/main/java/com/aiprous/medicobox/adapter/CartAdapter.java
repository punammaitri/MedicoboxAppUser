package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.instaorder.InstaAddNewListActivity;
import com.aiprous.medicobox.instaorder.InstaProductSubListDetailAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {


    private ArrayList<CartActivity.CartModel> mCartArrayList;
    private Context mContext;

    public CartAdapter(Context mContext, ArrayList<CartActivity.CartModel> mCartArrayList) {
        this.mContext = mContext;
        this.mCartArrayList = mCartArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

     holder.tv_item_name.setText(mCartArrayList.get(position).getMedicineName());
     holder.tv_medicine_contains.setText(mCartArrayList.get(position).getContains());
     holder.tv_mrp_price.setText(mContext.getResources().getString(R.string.Rs)+mCartArrayList.get(position).getMrp());
     holder.tv_mrp_price.setPaintFlags(holder.tv_mrp_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
     holder.tv_price.setText(mContext.getResources().getString(R.string.Rs)+mCartArrayList.get(position).getPrice());


     if(position==getItemCount()-1)
     {
         holder.view_cart_item.setVisibility(View.GONE);
     }


    }



    @Override
    public int getItemCount() {
        return (mCartArrayList == null) ? 0 : mCartArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_medicine_contains)
        TextView tv_medicine_contains;
        @BindView(R.id.tv_mrp_price)
        TextView tv_mrp_price;
        @BindView(R.id.tv_price)
        TextView tv_price;
        @BindView(R.id.view_cart_item)
        View view_cart_item;




        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
