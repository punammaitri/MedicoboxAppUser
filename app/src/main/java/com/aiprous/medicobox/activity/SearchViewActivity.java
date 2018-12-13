package com.aiprous.medicobox.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.SearchViewAdapter;
import com.aiprous.medicobox.model.ListModel;
import com.aiprous.medicobox.model.SearchModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aiprous.medicobox.utils.APIConstant.SEARCHPRODUCT;

public class SearchViewActivity extends AppCompatActivity {

    @BindView(R.id.searchview_all_medicine)
    android.support.v7.widget.SearchView searchview_all_medicine;
    @BindView(R.id.rc_common_search)
    RecyclerView rc_common_search;
    ArrayList<SearchModel> searchModelsArrayList=new ArrayList<>();
    ArrayAdapter<SearchModel> mAdapterSearch;
    private Context mContext = this;
    private RecyclerView.LayoutManager layoutManager;
    private SearchViewAdapter mSearchViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        ButterKnife.bind(this);
        init();
    }

    private void init() {


        try {
            searchview_all_medicine.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchview_all_medicine.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", newText);
                        searchAllProduct(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (searchModelsArrayList != null && !searchModelsArrayList.isEmpty()) {
                        ArrayList<SearchModel> filteredModelList = filter(searchModelsArrayList, newText);
                        mSearchViewAdapter.setFilter(filteredModelList);
                    }

                    return true;
                }

                private ArrayList<SearchModel> filter(ArrayList<SearchModel> models, String query) {
                    query = query.toLowerCase();

                    final ArrayList<SearchModel> filteredModelList = new ArrayList<>();

                    for (SearchModel model : models) {
                        final String text = model.getTitle().toLowerCase();
                        if (text.contains(query)) {
                            filteredModelList.add(model);
                        }
                    }
                    return filteredModelList;
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }




    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressList() {
        finish();
    }


    private void searchAllProduct(final JSONObject productname) {
        CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);
        AndroidNetworking.post(SEARCHPRODUCT)
                .addJSONObjectBody(productname)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        // Toast.makeText(mcontext, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            JsonObject getAllResponse = (JsonObject) new JsonParser().parse(response.toString());
                            JSONObject getAllObject = new JSONObject(getAllResponse.toString()); //first, get the jsonObject
                            JSONArray getAllProductList = getAllObject.getJSONArray("response");//get the array with the key "response"

                            for (int i = 0; i < getAllProductList.length(); i++) {
                                String id = getAllProductList.getJSONObject(i).get("id").toString();
                                String title = getAllProductList.getJSONObject(i).get("title").toString();

                                SearchModel searchModel=new SearchModel(id,title);
                                searchModel.setId(id);
                                searchModel.setTitle(title);
                                searchModelsArrayList.add(searchModel);

                            }

                          //set adapter
                            layoutManager = new LinearLayoutManager(mContext);
                            rc_common_search.setLayoutManager(new LinearLayoutManager(SearchViewActivity.this, LinearLayoutManager.VERTICAL, false));
                            rc_common_search.setHasFixedSize(true);
                            mSearchViewAdapter=new SearchViewAdapter(mContext, searchModelsArrayList);
                            rc_common_search.setAdapter(mSearchViewAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CustomProgressDialog.getInstance().dismissDialog();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        CustomProgressDialog.getInstance().dismissDialog();
                        Toast.makeText(SearchViewActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        Log.e("Error", "onError errorCode : " + error.getErrorCode());
                        Log.e("Error", "onError errorBody : " + error.getErrorBody());
                        Log.e("Error", "onError errorDetail : " + error.getErrorDetail());
                    }
                });
    }

}
