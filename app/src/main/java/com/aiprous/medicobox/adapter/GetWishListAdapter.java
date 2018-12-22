package com.aiprous.medicobox.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.model.GetWishListModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiprous.medicobox.adapter.ListAdapter.checkBoxClickEvent;


public class GetWishListAdapter extends RecyclerView.Adapter<GetWishListAdapter.ViewHolder> {

    private ArrayList<GetWishListModel> mDataArrayList;
    private Context mContext;


    public GetWishListAdapter(Context mContext, ArrayList<GetWishListModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.getwishlist_product_item_list, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.cbName.setText(mDataArrayList.get(position).getWishlist_name());

        holder.cbName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // perform logic
                    //mCheckBoxEvent.checkBoxClickEvent(mDataArrayList.get(position).getWishlist_name());
                    checkBoxClickEvent(mDataArrayList.get(position).getWishlist_name_id());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cb_name)
        AppCompatCheckBox cbName;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}