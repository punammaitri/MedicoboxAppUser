package com.aiprous.medicobox.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Window;
import android.view.WindowManager;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.MyOrdersAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrdersActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.searchview_order_id)
    SearchView searchview_order_id;
    @BindView(R.id.rc_my_order_list)
    RecyclerView rc_my_order_list;
    ArrayList<MyOrdersModel> myOrdersArrayList=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private Context mContext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);
        searchview_order_id.setFocusable(false);

        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        myOrdersArrayList.add(new MyOrdersModel("MB1828394829281","29/09/2018","280.00","0"));
        myOrdersArrayList.add(new MyOrdersModel("MB1828394829281","29/09/2018","280.00","1"));
        myOrdersArrayList.add(new MyOrdersModel("MB1828394829281","29/09/2018","280.00","2"));




        layoutManager = new LinearLayoutManager(mContext);
        rc_my_order_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_my_order_list.setHasFixedSize(true);
        rc_my_order_list.setAdapter(new MyOrdersAdapter(mContext, myOrdersArrayList));

    }
    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail()
    {
        finish();
    }
    public class MyOrdersModel{
        String orderId;
        String order_date;
        String order_price;
        String deliverystatus;

        public MyOrdersModel(String orderId, String order_date, String order_price, String deliverystatus) {
            this.orderId = orderId;
            this.order_date = order_date;
            this.order_price = order_price;
            this.deliverystatus = deliverystatus;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public String getOrder_price() {
            return order_price;
        }

        public void setOrder_price(String order_price) {
            this.order_price = order_price;
        }

        public String getDeliverystatus() {
            return deliverystatus;
        }

        public void setDeliverystatus(String deliverystatus) {
            this.deliverystatus = deliverystatus;
        }
    }
}
