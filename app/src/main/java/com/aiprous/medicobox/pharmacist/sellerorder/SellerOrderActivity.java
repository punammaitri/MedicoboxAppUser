package com.aiprous.medicobox.pharmacist.sellerorder;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.instaorder.InstaAddNewListActivity;
import com.aiprous.medicobox.instaorder.InstaAddNewListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SellerOrderActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;

    RecyclerView rc_seller_list;
    ArrayList<SellerOrderActivity.ListModel> mlistModelsArray = new ArrayList<>();
    ArrayList<SellerOrderActivity.SubListModel> mSubListModelsArray = new ArrayList<>();
    private Context mContext = this;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);
        searchview_medicine.setQueryHint("Search Orders");
        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        rc_seller_list = findViewById(R.id.rc_seller_list);

        //add static data into List array list
        mlistModelsArray.add(new SellerOrderActivity.ListModel(R.drawable.ic_menu_manage, "12233232323"));
        mlistModelsArray.add(new SellerOrderActivity.ListModel(R.drawable.ic_menu_manage, "12233232323"));
        mlistModelsArray.add(new SellerOrderActivity.ListModel(R.drawable.ic_menu_manage, "12233232323"));

        //add static data into Sub List array list
        mSubListModelsArray.add(new SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));
        mSubListModelsArray.add(new SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));
        mSubListModelsArray.add(new SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));

        layoutManager = new LinearLayoutManager(mContext);
        rc_seller_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_seller_list.setHasFixedSize(true);
        rc_seller_list.setAdapter(new SellerOrderListAdapter(mContext, mlistModelsArray, mSubListModelsArray));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

    public class ListModel {
        int image;
        String orderId;

        public ListModel(int image, String orderId) {
            this.image = image;
            this.orderId = orderId;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

    }

    public class SubListModel {
        int image;
        String product_name;

        public SubListModel(int image, String product_name) {
            this.image = image;
            this.product_name = product_name;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }
    }
}
