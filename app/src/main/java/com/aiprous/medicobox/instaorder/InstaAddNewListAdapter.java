package com.aiprous.medicobox.instaorder;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class InstaAddNewListAdapter extends RecyclerView.Adapter<InstaAddNewListAdapter.ViewHolder> {


    private ArrayList<InstaAddNewListActivity.ListModel> mDataArrayList;
    private Context mContext;
    private ArrayList<InstaAddNewListActivity.SubListModel> mSubListModelArray;
    private PopupWindow window;

    public InstaAddNewListAdapter(Context mContext, ArrayList<InstaAddNewListActivity.ListModel> mDataArrayList, ArrayList<InstaAddNewListActivity.SubListModel> mSubListModel) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
        this.mSubListModelArray = mSubListModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insta_add_new_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //holder.imgProduct.setImageResource(mDataArrayList.get(position).getImage());
        holder.tvMedicineType.setText(mDataArrayList.get(position).getMedicineName());

        holder.list.setLayoutManager(new LinearLayoutManager(mContext));
        holder.list.setAdapter(new InstaProductSubListDetailAdapter(mContext, mSubListModelArray));

        /*holder.relOptionDots.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(mContext);
                MenuInflater inflater = new MenuInflater(mContext);
                inflater.inflate(R.menu.option_menu, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(mContext, menuBuilder, view);
                optionsMenu.setForceShowIcon(true);

               *//* // Set Item Click Listener
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.opt1: // Handle option1 Click
                                return true;
                            case R.id.opt2: // Handle option2 Click
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onMenuModeChange(MenuBuilder menu) {}
                });*//*


                // Display the menu
                optionsMenu.show();
            }
        });*/

        holder.relOptionDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopupWindow(view);
            }
        });
    }

    private void ShowPopupWindow(View view) {
        Rect r = locateView(view);
        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup_view = lInflater.inflate(R.layout.popupwindow, null);
        final PopupWindow popup = new PopupWindow(popup_view, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable());
        popup.showAtLocation(popup_view, Gravity.TOP | Gravity.LEFT, r.right, r.bottom);

        LinearLayout mLinearEdit = popup_view.findViewById(R.id.linearEdit);
        LinearLayout mLinearAdd = popup_view.findViewById(R.id.linearAdd);

        mLinearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });

        mLinearAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext,InstaProductDetailActivity.class));

                popup.dismiss();
            }
        });


    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null)
            return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = loc_int[0] + v.getWidth();
        location.bottom = loc_int[1] + v.getHeight();
        return location;
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_medicine_type)
        TextView tvMedicineType;
        @BindView(R.id.btn_add_to_cart)
        Button btnAddToCart;
        @BindView(R.id.btn_order_checkout)
        Button btnOrderCheckout;
        @BindView(R.id.list)
        RecyclerView list;
        @BindView(R.id.relOptionDots)
        RelativeLayout relOptionDots;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
