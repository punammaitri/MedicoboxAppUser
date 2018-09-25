package com.aiprous.medicobox.instaorder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import medicobox.aiprous.com.medicobox.R;

public class InstaAddNewListActivity extends AppCompatActivity {

    // @BindView(R.id.rc_medicine_list)
    RecyclerView rc_medicine_list;
    ArrayList<ListModel> mlistModelsArray = new ArrayList<>();
    ArrayList<SubListModel> mSubListModelsArray = new ArrayList<>();
    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    private Dialog dialog;
    private TextView txtSave, txtCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_add_new_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        rc_medicine_list = findViewById(R.id.rc_medicine_list);

        //add static data into List array list
        mlistModelsArray.add(new ListModel(R.drawable.ic_menu_manage, "Diabetes", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.ic_menu_manage, "Monthly", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.ic_menu_manage, "Priyanka", "Bottle of 60 tablet", "150", "30%", "135"));

        //add static data into Sub List array list
        mSubListModelsArray.add(new SubListModel(R.drawable.ic_menu_manage, "Tab Evion 400mg", "1"));
        mSubListModelsArray.add(new SubListModel(R.drawable.ic_menu_manage, "Inj Emcet 4mg", "2"));
        mSubListModelsArray.add(new SubListModel(R.drawable.ic_menu_manage, "Otrivin Spray", "1"));

        layoutManager = new LinearLayoutManager(mContext);
        rc_medicine_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_medicine_list.setHasFixedSize(true);
        rc_medicine_list.setAdapter(new InstaAddNewListAdapter(mContext, mlistModelsArray,mSubListModelsArray));


    }

    @OnClick(R.id.btn_insta_list)
    public void onViewInstaListClicked() {
        ShowNewInstaListAlert(this);
    }

    private void ShowNewInstaListAlert(final InstaAddNewListActivity mActivityContext) {
        dialog = new Dialog(mActivityContext,R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.alert_new_insta_list);
        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        txtSave = dialog.findViewById(R.id.txtSave);
        txtCancel = dialog.findViewById(R.id.txtCancel);

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public class ListModel {
        int image;
        String medicineName;
        String value;
        String mrp;
        String discount;
        String price;

        public ListModel(int image, String medicineName, String value, String mrp, String discount, String price) {
            this.image = image;
            this.medicineName = medicineName;
            this.value = value;
            this.mrp = mrp;
            this.discount = discount;
            this.price = price;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public class SubListModel {
        int image;
        String medicineName;
        String price;

        public SubListModel(int image, String medicineName, String price) {
            this.image = image;
            this.medicineName = medicineName;
            this.price = price;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
