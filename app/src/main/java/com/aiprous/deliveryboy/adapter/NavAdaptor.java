package com.aiprous.deliveryboy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.deliveryboy.R;
import com.aiprous.deliveryboy.model.NavItemClicked;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Mankesh71 on 27-01-2017.
 */
@SuppressWarnings("ALL")
public class NavAdaptor extends RecyclerView.Adapter<NavAdaptor.ViewHolder> {

    private String mNavTitles[];
    private int mIcons[];
    private Context mContext;
    private NavItemClicked navItemClicked;
    private NavSubItemAdaptor navSubItemAdaptor;

    public NavAdaptor(Context mContext, NavItemClicked navItemClicked, String Titles[], int Icons[]) {
        this.mContext = mContext;
        this.navItemClicked = navItemClicked;
        mNavTitles = Titles;
        mIcons = Icons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_menu_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tvForMenuItem.setText(mNavTitles[holder.getAdapterPosition()]);
        holder.ivForMenuItem.setImageResource(mIcons[position]);

        holder.llForNavItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemClicked.navItemClicked(mNavTitles[holder.getAdapterPosition()], holder.getAdapterPosition());
            }
        });
//
        holder.ivForMenuItem.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvForMenuItem)
        TextView tvForMenuItem;
        @BindView(R.id.ivForMenuItem)
        ImageView ivForMenuItem;
        @BindView(R.id.llForNavItem)
        LinearLayout llForNavItem;
        @BindView(R.id.tv_arrow)
        TextView tv_arrow;
        // @BindView(R.id.viewForDivider)
        //  View viewForDivider;
        //  @BindView(R.id.cart_badge)
        // TextView cartBadge;
        // @BindView(R.id.cardViewMain)
        // CardView cardView;
        @BindView(R.id.rvSubMenuNavigation)
        RecyclerView rvSubMenuNavigation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
