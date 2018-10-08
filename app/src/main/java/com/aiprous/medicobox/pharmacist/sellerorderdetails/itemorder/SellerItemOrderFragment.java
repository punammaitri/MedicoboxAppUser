package com.aiprous.medicobox.pharmacist.sellerorderdetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellerorder.SellerOrderActivity;
import com.aiprous.medicobox.pharmacist.sellerorder.SellerOrderListAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class SellerItemOrderFragment extends Fragment {

    RecyclerView rc_seller_list;
    ArrayList<SellerItemOrderFragment.ListModel> mlistModelsArray = new ArrayList<>();
    ArrayList<SellerItemOrderFragment.SubListModel> mSubListModelsArray = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_item_order, container, false);
        ButterKnife.bind(this, view);
        init(view);
        return view;
    }

    private void init(View view) {


        rc_seller_list = view.findViewById(R.id.rc_seller_list);

        //add static data into List array list
        mlistModelsArray.add(new SellerItemOrderFragment.ListModel(R.drawable.ic_menu_manage, "12233232323"));
        mlistModelsArray.add(new SellerItemOrderFragment.ListModel(R.drawable.ic_menu_manage, "12233232323"));
        mlistModelsArray.add(new SellerItemOrderFragment.ListModel(R.drawable.ic_menu_manage, "12233232323"));

        //add static data into Sub List array list
        mSubListModelsArray.add(new SellerItemOrderFragment.SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));
        mSubListModelsArray.add(new SellerItemOrderFragment.SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));

        layoutManager = new LinearLayoutManager(getActivity());
        rc_seller_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rc_seller_list.setHasFixedSize(true);
        rc_seller_list.setAdapter(new SellerItemOrderListAdapter(getActivity(), mlistModelsArray, mSubListModelsArray));
    }

    public class ListModel {
        int image;
        String orderId;

        public ListModel(int image, String orderId) {
            this.image = image;
            this.orderId = orderId;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

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
}