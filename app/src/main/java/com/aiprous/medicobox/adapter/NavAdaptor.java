package com.aiprous.medicobox.adapter;

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

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.model.NavItemClicked;

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

        if (position == 1) {
            holder.tv_arrow.setVisibility(View.VISIBLE);
            String title[];
            int icon[];

            title = new String[]{
                    mContext.getResources().getString(R.string.txt_medicines),
                    mContext.getResources().getString(R.string.txt_lab_test)};

            icon = new int[]{
                    R.drawable.capsules,
                    R.drawable.syringe};


            holder.rvSubMenuNavigation.setLayoutManager(new LinearLayoutManager(mContext));
            holder.rvSubMenuNavigation.setAdapter(new NavSubItemAdaptor(mContext, this, title, icon));

        } else {
            holder.tv_arrow.setVisibility(View.INVISIBLE);
        }


        holder.tvForMenuItem.setText(mNavTitles[holder.getAdapterPosition()]);
        holder.ivForMenuItem.setImageResource(mIcons[position]);
        holder.llForNavItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 1) {

                    if (holder.rvSubMenuNavigation.getVisibility() != View.GONE) {
                        holder.rvSubMenuNavigation.setVisibility(View.GONE);
                    } else {
                        holder.rvSubMenuNavigation.setVisibility(View.VISIBLE);
                    }
                }
                navItemClicked.navItemClicked(mNavTitles[holder.getAdapterPosition()], holder.getAdapterPosition());
            }
        });
//        if (position == 3) {
//            if (EhealthCardApplication.getNotificationCount() != 0) {
//                holder.cartBadge.setText(String.valueOf(EhealthCardApplication.getNotificationCount()));
//                if (holder.cartBadge.getVisibility() != View.VISIBLE) {
//                    holder.cartBadge.setVisibility(View.VISIBLE);
//                }
//            }
//        }
        holder.ivForMenuItem.setBackgroundColor(Color.TRANSPARENT);

        if (position == 6) {
            holder.viewForDivider.setVisibility(View.GONE);
        }
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
        @BindView(R.id.viewForDivider)
        View viewForDivider;
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
