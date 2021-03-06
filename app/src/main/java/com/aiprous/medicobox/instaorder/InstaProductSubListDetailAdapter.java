package com.aiprous.medicobox.instaorder;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InstaProductSubListDetailAdapter extends RecyclerView.Adapter<InstaProductSubListDetailAdapter.ViewHolder> {

    private JsonArray mSubListArray;
    private Context mContext;
    private Dialog dialog;
    private TextView txtOk;
    private TextView txtTabName, txt_price;
    private ImageView imgPill;

    public InstaProductSubListDetailAdapter(Context mContext, JsonArray mDataArrayList) {
        this.mContext = mContext;
        this.mSubListArray = mDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insta_add_new_sublist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        JsonArray getItemData = mSubListArray.getAsJsonArray();

        JsonObject jsonObject = (JsonObject) getItemData.get(position);
        String name = jsonObject.get("name").getAsString();
        String product_id = jsonObject.get("product_id").getAsString();

        holder.txt_tab_name.setText(name);
        holder.tv_value.setText("0");

        holder.img_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowProductInfoAlert(mContext, mSubListArray.getAsJsonArray(), position);
            }
        });

        if (position == getItemCount() - 1) {
            holder.view_sublist.setVisibility(View.GONE);
        } else {
            holder.view_sublist.setVisibility(View.VISIBLE);
        }
    }

    private void ShowProductInfoAlert(Context mContext, JsonArray asJsonArray, int pos) {
        dialog = new Dialog(mContext, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.alert_product_info);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        txtTabName = dialog.findViewById(R.id.txtTabName);
        imgPill = dialog.findViewById(R.id.imgPill);
        txt_price = dialog.findViewById(R.id.txt_price);
        txtOk = dialog.findViewById(R.id.txtOk);

        JsonArray getItemData = mSubListArray.getAsJsonArray();
        JsonObject jsonObject = (JsonObject) getItemData.get(pos);
        String name = jsonObject.get("name").getAsString();
        String product_id = jsonObject.get("product_id").getAsString();
        String price = jsonObject.get("price").getAsString();
        String image = jsonObject.get("image").getAsString();

        txtTabName.setText(name);
        txt_price.setText(" \u20B9 " + price);

        if (image.contains("https")) {
            String url = jsonObject.get("image").getAsString().replace("https", "http");
            Picasso.with(mContext)
                    .load(url)
                    .into(imgPill);
        }

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mSubListArray == null) ? 0 : mSubListArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_tab_name)
        TextView txt_tab_name;
        @BindView(R.id.tv_value)
        TextView tv_value;
        @BindView(R.id.img_Info)
        ImageView img_Info;
        @BindView(R.id.view_sublist)
        View view_sublist;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
