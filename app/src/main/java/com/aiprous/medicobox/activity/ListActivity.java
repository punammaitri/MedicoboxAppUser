package com.aiprous.medicobox.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.ListAdapter;
import com.aiprous.medicobox.utils.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ListActivity extends AppCompatActivity {

    // @BindView(R.id.rc_medicine_list)
    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    RecyclerView rc_medicine_list;
    ArrayList<ListActivity.ListModel> mlistModelsArray = new ArrayList<>();
    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    private ListAdapter mlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        rc_medicine_list = (RecyclerView) findViewById(R.id.rc_medicine_list);

        //add static data into array list
        mlistModelsArray.add(new ListModel(R.drawable.bottle, "ABC", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.bottle, "xyz", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.bottle, "pqr", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.bottle, "aaa", "Bottle of 60 tablet", "150", "30%", "135"));
        mlistModelsArray.add(new ListModel(R.drawable.bottle, "ccc", "Bottle of 60 tablet", "150", "30%", "135"));


        layoutManager = new LinearLayoutManager(mContext);
        rc_medicine_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_medicine_list.setHasFixedSize(true);
        mlistAdapter=new ListAdapter(mContext, mlistModelsArray);
        rc_medicine_list.setAdapter(mlistAdapter);

        try{
            searchview_medicine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchview_medicine.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    if(mlistModelsArray!=null&&!mlistModelsArray.isEmpty())
                    {
                        ArrayList<ListModel> filteredModelList = filter(mlistModelsArray, newText);
                        mlistAdapter.setFilter(filteredModelList);
                    }

                    return true;
                }

                private ArrayList<ListModel> filter(ArrayList<ListModel> models, String query) {
                    query = query.toLowerCase();

                    final ArrayList<ListModel> filteredModelList = new ArrayList<>();

                    for (ListModel model : models) {
                        final String text = model.medicineName.toLowerCase();
                        if (text.contains(query)) {
                            filteredModelList.add(model);
                        }
                    }
                    return filteredModelList;
                }
            });

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressList() {
        finish();
    }

    public class ListModel {
        int image;
        String medicineName;
        String value;
        String mrp;
        String discount;
        String price;

        public ListModel(int image, String medicineName, String value, String mrp, String discount, String price) {
            this.image = image;
            this.medicineName = medicineName;
            this.value = value;
            this.mrp = mrp;
            this.discount = discount;
            this.price = price;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
