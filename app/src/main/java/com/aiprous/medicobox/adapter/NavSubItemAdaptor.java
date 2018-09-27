package com.aiprous.medicobox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.model.NavItemClicked;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.aiprous.medicobox.MainActivity.drawerLayout;


/**
 * Created by Mankesh71 on 27-01-2017.
 */
@SuppressWarnings("ALL")
public class NavSubItemAdaptor extends RecyclerView.Adapter<NavSubItemAdaptor.ViewHolder> {

    private String mNavTitles[];
    private int mIcons[];
    private Context mContext;
    private NavItemClicked navItemClicked;

    public NavSubItemAdaptor(Context mContext, NavAdaptor navItemClicked, String[] Titles, int[] Icons) {
        this.mContext = mContext;
      //  this.navItemClicked = navItemClicked;
        mNavTitles = Titles;
        mIcons = Icons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sub_menu_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvForMenuItem.setText(mNavTitles[holder.getAdapterPosition()]);
        holder.ivForMenuItem.setImageResource(mIcons[position]);
        holder.llSubNavItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0)
                {
                    Toast.makeText(mContext, "Medicine", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(mContext, "Test Lab", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
               // rvSubMenuNavigation.setVisibility(View.INVISIBLE);
            }
        });

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
        @BindView(R.id.llSubNavItem)
        LinearLayout llSubNavItem;
       // @BindView(R.id.viewForDivider)
      //  View viewForDivider;
        @BindView(R.id.cart_badge)
        TextView cartBadge;
       // @BindView(R.id.cardViewMain)
       // CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
