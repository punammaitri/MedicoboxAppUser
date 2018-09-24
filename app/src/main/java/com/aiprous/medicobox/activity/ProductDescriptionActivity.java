package com.aiprous.medicobox.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.fragment.HomeFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import medicobox.aiprous.com.medicobox.R;

public class ProductDescriptionActivity extends AppCompatActivity {

    @BindView(R.id.rv_people_also_viewed)
    RecyclerView rv_people_also_viewed;

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
}
