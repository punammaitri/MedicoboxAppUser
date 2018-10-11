package com.aiprous.medicobox.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.ListActivity;
import com.aiprous.medicobox.activity.OrderTrackingActivity;
import com.aiprous.medicobox.adapter.FeatureProductAdapter;
import com.aiprous.medicobox.instaorder.InstaAddNewListActivity;
import com.aiprous.medicobox.instaorder.InstaProductDetailActivity;
import com.aiprous.medicobox.prescription.PrescriptionUploadActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends Fragment {
    @BindView(R.id.sliderAdvertise)
    SliderLayout sliderAdvertise;
    @BindView(R.id.slider_contactus)
    SliderLayout slider_contactus;
    @BindView(R.id.rc_product)
    RecyclerView rc_product;
    @BindView(R.id.relInstaOrder)
    RelativeLayout relInstaOrder;
    @BindView(R.id.btn_upload)
    Button btn_upload;
    View view;
    ArrayList<Integer> sliderimages = new ArrayList<>();
    ArrayList<Integer> sliderimagesCall = new ArrayList<>();
    private PagerIndicator.IndicatorVisibility mVisibility = PagerIndicator.IndicatorVisibility.Invisible;
    ArrayList<HomeFragment.Product> mlistModelsArray = new ArrayList<>();
    private MainActivity mainActivity;

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

    // URL of object to be parsed
    String JsonURL = "http://user8.itsindev.com/medibox/featured-products.php";
    // This string will hold the results
    String data = "";
    // Defining the Volley request queue that handles the URL request concurrently
    RequestQueue requestQueue;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        sliderimages.add(R.drawable.bannerimage);
        sliderimages.add(R.drawable.contactus);

        sliderimagesCall.add(R.drawable.contactus);
        sliderimagesCall.add(R.drawable.bannerimage);


        //add static data
        mlistModelsArray.add(new Product(R.drawable.bottle, "ABC", "150", "30%", "135"));
        mlistModelsArray.add(new Product(R.drawable.bottle, "ABC", "150", "30%", "135"));
        mlistModelsArray.add(new Product(R.drawable.bottle, "ABC", "150", "30%", "135"));
        mlistModelsArray.add(new Product(R.drawable.bottle, "ABC", "150", "30%", "135"));
        mlistModelsArray.add(new Product(R.drawable.bottle, "ABC", "150", "30%", "135"));


        //set adapter
        rc_product.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rc_product.setHasFixedSize(true);
        rc_product.setAdapter(new FeatureProductAdapter(getActivity(), mlistModelsArray));

        //show slider images
        for (int i = 0; i < sliderimages.size(); i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            final int imageUrl = sliderimages.get(i);
            textSliderView.image(imageUrl).setScaleType(BaseSliderView.ScaleType.Fit).empty(R.drawable.bannerimage)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView baseSliderView) {
                            //startActivity(new Intent(getActivity(), FullScreenVideoActivity.class));

                        }
                    });

            if (sliderimages.size() > 1)
                sliderAdvertise.setPresetTransformer(SliderLayout.Transformer.Default);//Accordion
            sliderAdvertise.setIndicatorVisibility(mVisibility);
            sliderAdvertise.setCustomAnimation(new DescriptionAnimation());
            sliderAdvertise.setDuration(4000);

            sliderAdvertise.addSlider(textSliderView);
        }

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
                sliderAdvertise.setPresetTransformer(SliderLayout.Transformer.Default);//Accordion
            slider_contactus.setIndicatorVisibility(mVisibility);
            slider_contactus.setCustomAnimation(new DescriptionAnimation());
            slider_contactus.setDuration(4000);

            slider_contactus.addSlider(textSliderView);
        }

        requestQueue = Volley.newRequestQueue(getActivity());

        // Creating the JsonObjectRequest class called obreq, passing required parameters:
        //GET is used to fetch data from the server, JsonURL is the URL to be fetched from.
        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, JsonURL,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("colorObject");
                            // Retrieves the string labeled "colorName" and "description" from
                            //the response JSON Object
                            //and converts them into javascript objects
                            String color = obj.getString("colorName");
                            String desc = obj.getString("description");

                            // Adds strings from object to the "data" string
                            data += "Color Name: " + color +
                                    "nDescription : " + desc;

                            // Adds the data string to the TextView "results"
                            //results.setText(data);
                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        // Adds the JSON object request "obreq" to the request queue
        requestQueue.add(obreq);
    }

    @OnClick(R.id.rlayout_medicines)
    public void onClickMedicines() {
        startActivity(new Intent(getActivity(), ListActivity.class));
    }


    @OnClick(R.id.relInstaOrder)
    public void onViewInstaOrderClicked() {
        startActivity(new Intent(getActivity(), InstaAddNewListActivity.class));

    }

    @OnClick(R.id.btn_upload)
    public void onViewUploadPresc() {
        startActivity(new Intent(getActivity(), PrescriptionUploadActivity.class));

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

}
