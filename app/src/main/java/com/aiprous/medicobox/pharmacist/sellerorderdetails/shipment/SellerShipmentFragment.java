package com.aiprous.medicobox.pharmacist.sellerorderdetails.shipment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.invoice.SellerInvoiceAdapter;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.invoice.SellerInvoiceFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SellerShipmentFragment extends Fragment {

    @BindView(R.id.rec_sellerShipment)
    RecyclerView rec_sellerShipment;
    ArrayList<SellerShipmentFragment.ListModel> mlistModelsArray = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_shipment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        //add static data into List array list
        mlistModelsArray.add(new SellerShipmentFragment.ListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));
        mlistModelsArray.add(new SellerShipmentFragment.ListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));

        layoutManager = new LinearLayoutManager(getActivity());
        rec_sellerShipment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rec_sellerShipment.setHasFixedSize(true);
        rec_sellerShipment.setAdapter(new SellerShipmentAdapter(getActivity(), mlistModelsArray));
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

}