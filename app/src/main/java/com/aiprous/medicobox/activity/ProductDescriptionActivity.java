package com.aiprous.medicobox.activity;

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
import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.fragment.HomeFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProductDescriptionActivity extends AppCompatActivity {

    @BindView(R.id.rv_people_also_viewed)
    RecyclerView rv_people_also_viewed;
    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;

    ArrayList<HomeFragment.Product> mlistModelsArray=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private Context mcontext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        //add static data
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle,"ABC","150","30%","135"));
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle,"ABC","150","30%","135"));
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle,"ABC","150","30%","135"));
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle,"ABC","150","30%","135"));
        mlistModelsArray.add(new HomeFragment.Product(R.drawable.bottle,"ABC","150","30%","135"));


        //set adapter
        rv_people_also_viewed.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false));
        rv_people_also_viewed.setHasFixedSize(true);
        rv_people_also_viewed.setAdapter(new FeatureProductAdapter(mcontext, mlistModelsArray));

    }
    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription()
    {
        finish();
    }
}
