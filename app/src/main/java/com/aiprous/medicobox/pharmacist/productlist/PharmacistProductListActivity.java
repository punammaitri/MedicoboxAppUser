package com.aiprous.medicobox.pharmacist.productlist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellertransaction.SellerTransactionActivity;
import com.aiprous.medicobox.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PharmacistProductListActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_product_list)
    RecyclerView rc_product_list;
    ArrayList<SellerTransactionActivity.SellerTransactionModel> transactionArraylist=new ArrayList<>();
    private Context mcontext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_product_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));
        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));
        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));
        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));
        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));


        rc_product_list.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        rc_product_list.setHasFixedSize(true);
        rc_product_list.setAdapter(new PharmacistProductListAdapter(mcontext, transactionArraylist));
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }
}
