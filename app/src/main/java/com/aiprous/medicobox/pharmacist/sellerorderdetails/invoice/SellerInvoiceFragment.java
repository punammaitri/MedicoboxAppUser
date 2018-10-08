package com.aiprous.medicobox.pharmacist.sellerorderdetails.invoice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.itemorder.SellerItemOrderAdapter;
import com.aiprous.medicobox.pharmacist.sellerorderdetails.itemorder.SellerItemOrderFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SellerInvoiceFragment extends Fragment {

    @BindView(R.id.rec_sellerInvoice)
    RecyclerView rec_sellerInvoice;

    ArrayList<SellerInvoiceFragment.ListModel> mlistModelsArray = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_invoice, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        //add static data into List array list
        mlistModelsArray.add(new SellerInvoiceFragment.ListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));
        mlistModelsArray.add(new SellerInvoiceFragment.ListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));

        layoutManager = new LinearLayoutManager(getActivity());
        rec_sellerInvoice.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rec_sellerInvoice.setHasFixedSize(true);
        rec_sellerInvoice.setAdapter(new SellerInvoiceAdapter(getActivity(), mlistModelsArray));
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