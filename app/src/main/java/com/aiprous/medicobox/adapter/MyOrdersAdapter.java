package com.aiprous.medicobox.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.MyOrdersActivity;
import com.aiprous.medicobox.activity.OrderDetailsActivity;
import com.aiprous.medicobox.model.MyOrdersModel;
import com.aiprous.medicobox.model.SearchModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private ArrayList<MyOrdersModel> myOrdersArrayList;
    private MyOrdersActivity mContext;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    public MyOrdersAdapter(MyOrdersActivity mContext, ArrayList<MyOrdersModel> myOrdersArrayList) {
        this.mContext = mContext;
        this.myOrdersArrayList = myOrdersArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_orders_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tv_order_id.setText("Order ID: " + myOrdersArrayList.get(position).getOrder_inc_id());

        //for showing date
        String created_at =myOrdersArrayList.get(position).getCreated_at();
        StringTokenizer splitDate = new StringTokenizer(created_at, " ");
        String date = splitDate.nextToken();
        String time = splitDate.nextToken();

        holder.tv_order_date.setText("Order Date: " + date);

        //for price
        double input = Double.parseDouble(myOrdersArrayList.get(position).getGrand_total());
        holder.tv_order_price.setText(mContext.getResources().getString(R.string.Rs) + df2.format(input));

        if (myOrdersArrayList.get(position).getStatus().equals("delivered")) {
            holder.tv_deliver_order_status.setTextColor(mContext.getResources().getColor(R.color.colorgreen));
            holder.tv_deliver_order_status.setText("Delivered");

        } else if (myOrdersArrayList.get(position).getStatus().equals("processing")) {
            holder.tv_deliver_order_status.setTextColor(mContext.getResources().getColor(R.color.coloryellow));
            holder.tv_deliver_order_status.setText("Processing");
        } else {
            holder.tv_deliver_order_status.setTextColor(mContext.getResources().getColor(R.color.colorred));
            holder.tv_deliver_order_status.setText("Cancelled");
        }
        holder.cardview_my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mContext.startActivity(new Intent(mContext, OrderDetailsActivity.class));
                Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                intent.putExtra("order_id", "" + myOrdersArrayList.get(position).getEntity_id());
                Activity activity = (Activity) mContext;
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (myOrdersArrayList == null) ? 0 : myOrdersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order_id)
        TextView tv_order_id;
        @BindView(R.id.tv_order_date)
        TextView tv_order_date;
        @BindView(R.id.tv_order_price)
        TextView tv_order_price;
        @BindView(R.id.tv_deliver_order_status)
        TextView tv_deliver_order_status;
        @BindView(R.id.cardview_my_orders)
        CardView cardview_my_orders;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setFilter(ArrayList<MyOrdersModel> Models) {
        myOrdersArrayList = new ArrayList<>();
        myOrdersArrayList.addAll(Models);
        notifyDataSetChanged();
    }
}
