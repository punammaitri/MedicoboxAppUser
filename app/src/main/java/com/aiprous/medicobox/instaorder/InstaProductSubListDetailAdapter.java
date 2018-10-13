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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiprous.medicobox.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InstaProductSubListDetailAdapter extends RecyclerView.Adapter<InstaProductSubListDetailAdapter.ViewHolder> {

    private ArrayList<InstaAddNewListActivity.SubListModel> mSubListArray;
    private Context mContext;
    private Dialog dialog;
    private TextView txtOk;

    public InstaProductSubListDetailAdapter(Context mContext, ArrayList<InstaAddNewListActivity.SubListModel> mDataArrayList) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.cb_tab_name.setText(mSubListArray.get(position).medicineName);
        holder.tv_value.setText(mSubListArray.get(position).price);

        holder.img_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowProductInfoAlert(mContext);
            }
        });

    }

    private void ShowProductInfoAlert(Context mContext) {
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
        txtOk = dialog.findViewById(R.id.txtOk);

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

        @BindView(R.id.cb_tab_name)
        CheckBox cb_tab_name;
        @BindView(R.id.tv_value)
        TextView tv_value;
        @BindView(R.id.img_Info)
        ImageView img_Info;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
