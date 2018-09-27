package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.adapter.ViewPagerAdapter;
import com.aiprous.medicobox.fragment.HomeFragment;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import medicobox.aiprous.com.medicobox.R;


public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.SliderDots)
    LinearLayout SliderDots;
    @BindView(R.id.rv_people_also_viewed)
    RecyclerView rv_people_also_viewed;
    @BindView(R.id.tv_product_mrp)
    TextView tv_product_mrp;
    @BindView(R.id.tv_product_price)
    TextView tv_product_price;
    ArrayList<HomeFragment.Product> mlistModelsArray=new ArrayList<>();
    private Context mcontext=this;
    private int dotscount;
    private ImageView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);
        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        tv_product_mrp.setText(mcontext.getResources().getString(R.string.Rs)+"150.00");
        tv_product_mrp.setPaintFlags(tv_product_mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tv_product_price.setText(mcontext.getResources().getString(R.string.Rs)+"135.00");
        //set view pager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            SliderDots.addView(dots[i], params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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
    @OnClick(R.id.llayout_product_detail)
    public void onClickProductDetail()
    {
        startActivity(new Intent(this,ProductDescriptionActivity.class));
    }
    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail()
    {
        finish();
    }
}
