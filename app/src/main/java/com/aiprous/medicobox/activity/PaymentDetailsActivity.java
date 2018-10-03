package com.aiprous.medicobox.activity;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aiprous.medicobox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentDetailsActivity extends AppCompatActivity {


    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.tv_mrp_total)
    TextView tv_mrp_total;
    @BindView(R.id.tv_price_discount)
    TextView tv_price_discount;
    @BindView(R.id.tv_to_be_paid)
    TextView tv_to_be_paid;
    @BindView(R.id.tv_total_savings)
    TextView tv_total_savings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);

        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        //set text
        tv_mrp_total.setText(this.getResources().getString(R.string.Rs)+" 350.0");
        tv_price_discount.setText("-"+this.getResources().getString(R.string.Rs)+" 30.0");
        tv_to_be_paid.setText(this.getResources().getString(R.string.Rs)+" 350.0");
        tv_total_savings.setText(this.getResources().getString(R.string.Rs)+"30.0");


    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail()
    {
        finish();
    }

}
