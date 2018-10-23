package com.aiprous.medicobox.prescription;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.CartActivity;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PrescriptionUploadOptionActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.txt_duration_of_dose)
    TextView txtDurationOfDose;
    @BindView(R.id.txt_duration_example)
    TextView txtDurationExample;
    @BindView(R.id.linear_order_everything)
    LinearLayout linearOrderEverything;
    @BindView(R.id.txt_specify_medicine)
    TextView txtSpecifyMedicine;
    @BindView(R.id.txt_specify_meds)
    TextView txtSpecifyMeds;
    @BindView(R.id.linear_specify_medicine)
    LinearLayout linearSpecifyMedicine;
    @BindView(R.id.rb_order_everything)
    RadioButton mRadioButtonOrderEverything;
    @BindView(R.id.rb_specify_medicine)
    RadioButton mRadioButtonSpecifyMedicine;
    @BindView(R.id.rb_call_me)
    RadioButton mRadioButtonCallMe;
    @BindView(R.id.btnContinue)
    Button btnContinue;
    @BindView(R.id.img_attach_arrow)
    ImageView img_attach_arrow;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;

    RecyclerView rc_medicine_list;
    ArrayList<PrescriptionUploadOptionActivity.ListModel> mlistModelsArray = new ArrayList<>();

    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_order_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);
        img_attach_arrow.setImageResource(R.drawable.arrow_yellow);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        rc_medicine_list = findViewById(R.id.rc_medicine_list);

        //add static data into List array list
        mlistModelsArray.add(new PrescriptionUploadOptionActivity.ListModel(R.drawable.bottle, "Shreya Saran", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new PrescriptionUploadOptionActivity.ListModel(R.drawable.bottle, "Shreya Saran", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new PrescriptionUploadOptionActivity.ListModel(R.drawable.bottle, "Shreya Saran", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new PrescriptionUploadOptionActivity.ListModel(R.drawable.bottle, "Shreya Saran", "Bottle of 60 tablet", "150", "30%", "135"));

        layoutManager = new LinearLayoutManager(mContext);
        rc_medicine_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rc_medicine_list.setHasFixedSize(true);
        rc_medicine_list.setAdapter(new PrescriptionUploadOptionAdapter(mContext, mlistModelsArray));
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(SingletonAddToCart.getGsonInstance().getOptionList().isEmpty())
        {
            rlayout_cart.setVisibility(View.GONE);
        }
        else {
            tv_cart_size.setText(""+SingletonAddToCart.getGsonInstance().getOptionList().size());
        }
    }
    @OnClick(R.id.rlayout_cart)
    public void ShowCart()
    {
        startActivity(new Intent(this,CartActivity.class));
    }

    @OnClick({R.id.rb_order_everything, R.id.rb_specify_medicine, R.id.rb_call_me, R.id.rlayout_back_button, R.id.btnContinue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_order_everything:
                linearOrderEverything.setVisibility(View.VISIBLE);
                rc_medicine_list.setVisibility(View.GONE);
                linearSpecifyMedicine.setVisibility(View.GONE);
                img_attach_arrow.setImageResource(R.drawable.arrow_yellow);
                break;
            case R.id.rb_specify_medicine:
                linearSpecifyMedicine.setVisibility(View.VISIBLE);
                linearOrderEverything.setVisibility(View.GONE);
                rc_medicine_list.setVisibility(View.GONE);
                img_attach_arrow.setImageResource(R.drawable.arrow_yellow);
                break;
            case R.id.rb_call_me:
                linearSpecifyMedicine.setVisibility(View.GONE);
                linearOrderEverything.setVisibility(View.GONE);
                rc_medicine_list.setVisibility(View.VISIBLE);
                img_attach_arrow.setImageResource(R.drawable.arrow_yellow_up);
                break;
            case R.id.btnContinue:
                startActivity(new Intent(this, PrescriptionChooseDeliveryAddressActivity.class));
                break;
            case R.id.rlayout_back_button:
                finish();
                break;
        }
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
}