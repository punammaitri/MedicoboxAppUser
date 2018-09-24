package com.aiprous.medicobox.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.fragment.HomeFragment;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import medicobox.aiprous.com.medicobox.R;


public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.rv_people_also_viewed)
    RecyclerView rv_people_also_viewed;
    @BindView(R.id.slidermedicine)
    SliderLayout slidermedicine;
    ArrayList<Integer> sliderimages=new ArrayList<>();
    ArrayList<HomeFragment.Product> mlistModelsArray=new ArrayList<>();
    private PagerIndicator.IndicatorVisibility mVisibility = PagerIndicator.IndicatorVisibility.Invisible;
    private Context mcontext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
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
        rv_people_also_viewed.setLayoutManager(new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false));
        rv_people_also_viewed.setHasFixedSize(true);
        rv_people_also_viewed.setAdapter(new FeatureProductAdapter(mcontext, mlistModelsArray));

    }
}
