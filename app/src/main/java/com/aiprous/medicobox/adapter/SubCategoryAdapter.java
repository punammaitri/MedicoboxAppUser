package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.model.MainCategoryModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {


    private ArrayList<MainCategoryModel.SubCat> SubCategoryArrayList;
    private Context mContext;

    public SubCategoryAdapter(Context mContext, ArrayList<MainCategoryModel.SubCat> SubCategoryArrayList) {
        this.mContext = mContext;
        this.SubCategoryArrayList = SubCategoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

    //holder.tv_notification_text.setText(mNotificationArrayList.get(position).getNotfication_text());

       holder.tv_sub_category_name.setText(SubCategoryArrayList.get(position).getCategoryName());

        holder.llayout_subcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext,ListActivity.class).putExtra("subcategoryId",SubCategoryArrayList.get(position).getCategoryId()));
            }
        });


    }
    @Override
    public int getItemCount() {
        return (SubCategoryArrayList == null) ? 0 : SubCategoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_sub_category_name)
        TextView tv_sub_category_name;
        @BindView(R.id.llayout_subcategory)
        TextView llayout_subcategory;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
