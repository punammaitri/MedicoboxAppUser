package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.NotificationActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {


    private ArrayList<NotificationActivity.NotificationModel> mNotificationArrayList;
    private Context mContext;

    public NotificationAdapter(Context mContext, ArrayList<NotificationActivity.NotificationModel> mNotificationArrayList) {
        this.mContext = mContext;
        this.mNotificationArrayList = mNotificationArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

    holder.tv_notification_text.setText(mNotificationArrayList.get(position).getNotfication_text());
    holder.tv_dob.setText(mNotificationArrayList.get(position).getDob()+",  ");
    holder.tv_time.setText(mNotificationArrayList.get(position).getTime());

    }
    @Override
    public int getItemCount() {
        return (mNotificationArrayList == null) ? 0 : mNotificationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_notification_text)
        TextView tv_notification_text;
        @BindView(R.id.tv_dob)
        TextView tv_dob;
        @BindView(R.id.tv_time)
        TextView tv_time;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
