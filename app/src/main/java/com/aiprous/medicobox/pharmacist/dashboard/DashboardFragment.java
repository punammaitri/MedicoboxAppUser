package com.aiprous.medicobox.pharmacist.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellerorder.SellerOrderActivity;
import com.aiprous.medicobox.pharmacist.sellerorder.SellerOrderListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment {

    @BindView(R.id.rec_dashboard)
    RecyclerView rec_dashboard;
    ArrayList<DashboardFragment.SubListModel> mSubListModelsArray = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    private MainActivity mainActivity;

    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public DashboardFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        //add static data into Sub List array list
        mSubListModelsArray.add(new DashboardFragment.SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));
        mSubListModelsArray.add(new DashboardFragment.SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));
        mSubListModelsArray.add(new DashboardFragment.SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));

        layoutManager = new LinearLayoutManager(getActivity());
        rec_dashboard.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rec_dashboard.setHasFixedSize(true);
        rec_dashboard.setAdapter(new DashboardAdapter(getActivity(), mSubListModelsArray));
    }

    public class SubListModel {
        int image;
        String product_name;

        public SubListModel(int image, String product_name) {
            this.image = image;
            this.product_name = product_name;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void onFragmentTrans(Fragment framgent) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_home, framgent).commit();
    }
}