package com.aiprous.medicobox.pharmacist.product_list;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Window;
import android.view.WindowManager;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellertransactions.SellerTransactionActivity;
import com.aiprous.medicobox.pharmacist.sellertransactions.SellerTransactionsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));
        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));
        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));
        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));
        transactionArraylist.add(new SellerTransactionActivity.SellerTransactionModel("TR18392832835","28/09/2018","200"));


        rc_product_list.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        rc_product_list.setHasFixedSize(true);
        rc_product_list.setAdapter(new PharmacistProductListAdapter(mcontext, transactionArraylist));
    }
}
