package com.aiprous.medicobox.activity;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.adapter.SubstitutesProductAdapter;
import com.aiprous.medicobox.adapter.ViewPagerAdapter;
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

public class ProductDetailBActivity extends AppCompatActivity {



    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.SliderDots)
    LinearLayout SliderDots;
    @BindView(R.id.rv_substitute_product)
    RecyclerView rv_substitute_product;
    @BindView(R.id.tv_medicine_contains)
    TextView tv_medicine_contains;
    @BindView(R.id.spinner_count)
    Spinner spinner_count;
    ArrayList<SubstituteProductModel> substituteProductModelArrayList=new ArrayList<>();
    private Context mcontext=this;
    private int dotscount;
    private ImageView[] dots;
    String[] mValue = { "1 Strip", "2 Strip", "3 Strip", "4 Strip", "5 Strip"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_b);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        //set spinner
        //Creating the ArrayAdapter instance having the value list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,mValue);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_count.setAdapter(aa);
        //add underline to text
        tv_medicine_contains.setPaintFlags(tv_medicine_contains.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
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


        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));

        //set adapter
        rv_substitute_product.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false));
        rv_substitute_product.setHasFixedSize(true);
        rv_substitute_product.setAdapter(new SubstitutesProductAdapter(mcontext, substituteProductModelArrayList));



    }
    public class SubstituteProductModel
    {
        public String name;
        public String company;
        public int price;

        public SubstituteProductModel(String name, String company, int price) {
            this.name = name;
            this.company = company;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail()
    {
        finish();
    }
}
