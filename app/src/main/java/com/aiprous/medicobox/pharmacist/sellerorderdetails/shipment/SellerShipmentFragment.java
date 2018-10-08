package com.aiprous.medicobox.pharmacist.sellerorderdetails;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiprous.medicobox.R;

import butterknife.ButterKnife;

public class SellerShipmentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_shipment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

    }
}

