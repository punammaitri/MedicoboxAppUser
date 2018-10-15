package com.aiprous.medicobox.pharmacist.sellertransaction;

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
import com.aiprous.medicobox.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SellerTransactionActivity extends AppCompatActivity {
    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_seller_transactions)
    RecyclerView rc_seller_transactions;
    private Context mcontext = this;
    ArrayList<SellerTransactionModel> transactionArraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_transaction2);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);
        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        transactionArraylist.add(new SellerTransactionModel("TR18392832835", "28/09/2018", "200"));
        transactionArraylist.add(new SellerTransactionModel("TR18392832835", "28/09/2018", "200"));
        transactionArraylist.add(new SellerTransactionModel("TR18392832835", "28/09/2018", "200"));
        transactionArraylist.add(new SellerTransactionModel("TR18392832835", "28/09/2018", "200"));
        transactionArraylist.add(new SellerTransactionModel("TR18392832835", "28/09/2018", "200"));

        rc_seller_transactions.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        rc_seller_transactions.setHasFixedSize(true);
        rc_seller_transactions.setAdapter(new SellerTransactionsAdapter(mcontext, transactionArraylist));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

    public static class SellerTransactionModel {
        String transactionId;
        String Date;
        String Amount;


        public SellerTransactionModel(String transactionId, String date, String amount) {
            this.transactionId = transactionId;
            Date = date;
            Amount = amount;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String date) {
            Date = date;
        }

        public String getAmount() {
            return Amount;
        }

        public void setAmount(String amount) {
            Amount = amount;
        }
    }
}
