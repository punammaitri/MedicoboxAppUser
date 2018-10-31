package com.aiprous.medicobox.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.featuredproduct.FeaturedProductModel;
import com.aiprous.medicobox.instaorder.InstaAddNewListActivity;
import com.aiprous.medicobox.prescription.PrescriptionUploadActivity;
import com.aiprous.medicobox.sliderimages.FlipperLayout;
import com.aiprous.medicobox.sliderimages.FlipperView;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.BaseActivity;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.BANNERAPI;
import static com.aiprous.medicobox.utils.APIConstant.FEATUREDPRODUCT;
import static com.aiprous.medicobox.utils.APIConstant.GETCATEGORY;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;


public class HomeFragment extends Fragment {
    @BindView(R.id.flipper_layout)
    FlipperLayout flipperLayout;
    @BindView(R.id.slider_contactus)
    SliderLayout slider_contactus;
    @BindView(R.id.rc_product)
    RecyclerView rc_product;
    @BindView(R.id.relInstaOrder)
    RelativeLayout relInstaOrder;
    @BindView(R.id.btn_upload)
    Button btn_upload;
    View view;
    ArrayList<Integer> sliderimagesCall = new ArrayList<>();
    private PagerIndicator.IndicatorVisibility mVisibility = PagerIndicator.IndicatorVisibility.Invisible;
    private MainActivity mainActivity;
    CustomProgressDialog mAlert;
    ArrayList<FeaturedProductModel> mFeaturedProductModels = new ArrayList<FeaturedProductModel>();
    private OnFragmentInteractionListener mListener;


    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public HomeFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {


        sliderimagesCall.add(R.drawable.contactus);
        sliderimagesCall.add(R.drawable.bannerimage);

        for (int i = 0; i < sliderimagesCall.size(); i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            final int imageUrl = sliderimagesCall.get(i);
            textSliderView.image(imageUrl).setScaleType(BaseSliderView.ScaleType.Fit).empty(R.drawable.bannerimage)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView baseSliderView) {
                            //startActivity(new Intent(getActivity(), FullScreenVideoActivity.class));
                        }
                    });

            if (sliderimagesCall.size() > 1)
                slider_contactus.setPresetTransformer(SliderLayout.Transformer.Default);//Accordion
            slider_contactus.setIndicatorVisibility(mVisibility);
            slider_contactus.setCustomAnimation(new DescriptionAnimation());
            slider_contactus.setDuration(4000);

            slider_contactus.addSlider(textSliderView);
        }

        mAlert = CustomProgressDialog.getInstance();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void onFragmentTrans(Fragment framgent) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_home, framgent).commit();
    }

    public static class Product {
        int product_image;
        String product_name;
        String product_mrp;
        String product_discount;
        String product_price;

        public Product(int product_image, String product_name, String product_mrp, String product_discount, String product_price) {
            this.product_image = product_image;
            this.product_name = product_name;
            this.product_mrp = product_mrp;
            this.product_discount = product_discount;
            this.product_price = product_price;
        }

        public int getProduct_image() {
            return product_image;
        }

        public void setProduct_image(int product_image) {
            this.product_image = product_image;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getProduct_mrp() {
            return product_mrp;
        }

        public void setProduct_mrp(String product_mrp) {
            this.product_mrp = product_mrp;
        }

        public String getProduct_discount() {
            return product_discount;
        }

        public void setProduct_discount(String product_discount) {
            this.product_discount = product_discount;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isNetworkAvailable(getActivity())) {
            CustomProgressDialog.getInstance().showDialog(getActivity(), getActivity().getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            AttemptToGetFeaturedProduct();
            CustomProgressDialog.getInstance().dismissDialog();
            AttemptToGetBannerImages();
            AttemptToGetCategories();
        }
        //new GetAllProduct().execute();
    }


    @SuppressLint("StaticFieldLeak")
    class GetAllProduct extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @OnClick({R.id.btn_upload, R.id.rlayout_medicines, R.id.rlayout_lab_tests, R.id.relInstaOrder, R.id.rlayout_econsultation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_upload:
                startActivity(new Intent(getActivity(), PrescriptionUploadActivity.class));
                break;
            case R.id.rlayout_medicines:
                startActivity(new Intent(getActivity(), ListActivity.class));
                break;
            case R.id.rlayout_lab_tests:
                break;
            case R.id.relInstaOrder:
                startActivity(new Intent(getActivity(), InstaAddNewListActivity.class));
                break;
            case R.id.rlayout_econsultation:
                break;
        }
    }

    private void AttemptToGetFeaturedProduct() {
        CustomProgressDialog.getInstance().showDialog(getActivity(), "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.get(FEATUREDPRODUCT)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // do anything with response
                        JsonArray entries = (JsonArray) new JsonParser().parse(response.toString());
                        if (entries != null) {
                            mFeaturedProductModels.clear();
                            for (int i = 0; i < entries.size(); i++) {
                                String image = ((JsonObject) entries.get(i)).get("small_image").getAsString();
                                String name = ((JsonObject) entries.get(i)).get("name").getAsString();
                                String actual_price = ((JsonObject) entries.get(i)).get("price").getAsString();
                                String final_price = ((JsonObject) entries.get(i)).get("final_price").getAsString();

                                FeaturedProductModel featuredProductModel = new FeaturedProductModel(image, name, actual_price, final_price);
                                featuredProductModel.setImage(image);
                                featuredProductModel.setName(name);
                                featuredProductModel.setPrice(actual_price);
                                featuredProductModel.setFinal_price(final_price);
                                mFeaturedProductModels.add(featuredProductModel);
                            }
                        }

                        //set featured adapter
                        rc_product.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        rc_product.setHasFixedSize(true);
                        rc_product.setAdapter(new FeatureProductAdapter(getActivity(), mFeaturedProductModels));
                        BaseActivity.printLog("response-success : ", response.toString());
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    private void AttemptToGetCategories() {
        CustomProgressDialog.getInstance().showDialog(getActivity(), "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.get(GETCATEGORY)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            JsonObject entries = (JsonObject) new JsonParser().parse(response.toString());
                            JSONObject object = new JSONObject(entries.toString());

                            JSONObject getAllObject = object.getJSONObject("2").getJSONObject("child");

                            String mCategoryArrays = getAllObject.toString();
                            for (int j = 0; j < getAllObject.length(); j++) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CustomProgressDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

    private void AttemptToGetBannerImages() {
        CustomProgressDialog.getInstance().showDialog(getActivity(), "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.get(BANNERAPI)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        JsonObject entries = (JsonObject) new JsonParser().parse(response.toString());

                        try {
                            JSONObject object = new JSONObject(entries.toString()); //first, get the jsonObject
                            JSONArray array = object.getJSONArray("response");//get the array with the key "response"

                            // Access the element using for loop
                            for (int i = 0; i < array.length(); i++) {
                                FlipperView view = new FlipperView(getActivity());

                                if (array.getString(i).contains("https")) {
                                    String url = array.getString(i).replace("https", "http");
                                    view.setImageUrl(url);
                                    flipperLayout.addFlipperView(view);
                                } else {
                                    view.setImageUrl(array.getString(i));
                                    flipperLayout.addFlipperView(view);
                                }
                            }

                            CustomProgressDialog.getInstance().dismissDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

}