package com.aiprous.medicobox.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.adapter.SubstitutesProductAdapter;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import medicobox.aiprous.com.medicobox.R;

public class ProductDetailBActivity extends AppCompatActivity {

    @BindView(R.id.slidermedicine)
    SliderLayout slidermedicine;
    @BindView(R.id.rv_substitute_product)
    RecyclerView rv_substitute_product;
    private PagerIndicator.IndicatorVisibility mVisibility = PagerIndicator.IndicatorVisibility.Invisible;
    ArrayList<Integer> sliderimages=new ArrayList<>();
    ArrayList<SubstituteProductModel> substituteProductModelArrayList=new ArrayList<>();
    private Context mcontext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_b);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));
        substituteProductModelArrayList.add(new SubstituteProductModel("DUVADILAN 10 mg Tablets 50's","By Abbott",50));
        //add static slider images
        sliderimages.add(R.drawable.bannerimage);
        sliderimages.add(R.drawable.contactus);

        //show slider images
        for (int i = 0; i < sliderimages.size(); i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            final int imageUrl = sliderimages.get(i);
            textSliderView.image(imageUrl).setScaleType(BaseSliderView.ScaleType.Fit).empty(R.drawable.bannerimage)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView baseSliderView) {
                            //startActivity(new Intent(getActivity(), FullScreenVideoActivity.class));

                        }
                    });

            if (sliderimages.size() > 1)
                slidermedicine.setPresetTransformer(SliderLayout.Transformer.Default);//Accordion
            slidermedicine.setIndicatorVisibility(mVisibility);
            slidermedicine.setCustomAnimation(new DescriptionAnimation());
            slidermedicine.setDuration(4000);

            slidermedicine.addSlider(textSliderView);
        }


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
}
