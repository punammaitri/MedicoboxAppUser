package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.model.MainCategoryModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {


    private ArrayList<MainCategoryModel> mainCategoryArrayList;
    private Context mContext;

    public SubCategoryAdapter(Context mContext, ArrayList<MainCategoryModel> mainCategoryArrayList) {
        this.mContext = mContext;
        this.mainCategoryArrayList = mainCategoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

    //holder.tv_notification_text.setText(mNotificationArrayList.get(position).getNotfication_text());

       holder.tv_sub_category_name.setText(mainCategoryArrayList.get(position).getCategoryName());

    }
    @Override
    public int getItemCount() {
        return (mainCategoryArrayList == null) ? 0 : mainCategoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_sub_category_name)
        TextView tv_sub_category_name;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
