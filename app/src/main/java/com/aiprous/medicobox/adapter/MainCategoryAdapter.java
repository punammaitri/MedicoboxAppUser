package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.NotificationActivity;
import com.aiprous.medicobox.activity.SubCategoryActivity;
import com.aiprous.medicobox.model.MainCategoryModel;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {


    private ArrayList<MainCategoryModel> mainCategoryArrayList;
    ArrayList<MainCategoryModel.SubCat> subCatArrayList= new ArrayList<>();
    private Context mContext;

    public MainCategoryAdapter(Context mContext, ArrayList<MainCategoryModel> mainCategoryArrayList) {
        this.mContext = mContext;
        this.mainCategoryArrayList = mainCategoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


       holder.tv_main_category_name.setText(mainCategoryArrayList.get(position).getCategoryName());
       holder.llayout_subcategory.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Gson gson = new Gson();
               subCatArrayList= mainCategoryArrayList.get(position).getSubCats();
               String dataModel = gson.toJson(subCatArrayList);
               mContext.startActivity(new Intent(mContext,SubCategoryActivity.class).putExtra("SubCategoryArray",dataModel).putExtra("MainCategoryName",mainCategoryArrayList.get(position).getCategoryName()));
           }
       });

    }
    @Override
    public int getItemCount() {
        return (mainCategoryArrayList == null) ? 0 : mainCategoryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_main_category)
        ImageView img_main_category;
        @BindView(R.id.tv_main_category_name)
        TextView tv_main_category_name;
        @BindView(R.id.llayout_subcategory)
        LinearLayout llayout_subcategory;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
