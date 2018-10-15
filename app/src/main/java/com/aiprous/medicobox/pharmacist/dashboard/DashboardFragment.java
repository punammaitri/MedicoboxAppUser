package com.aiprous.medicobox.pharmacist.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aiprous.medicobox.MainActivity;
import com.aiprous.medicobox.R;
import com.aiprous.medicobox.pharmacist.sellerorder.SellerOrderActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends Fragment {

    @BindView(R.id.rec_dashboard)
    RecyclerView rec_dashboard;
    @BindView(R.id.chart)
    BarChart mChart;

    ArrayList<SubListModel> mSubListModelsArray = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.linearLatestOrder)
    LinearLayout linearLatestOrder;
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
        mSubListModelsArray.add(new SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));
        mSubListModelsArray.add(new SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));
        mSubListModelsArray.add(new SubListModel(R.drawable.ic_menu_manage, "Horlicks Lite Badam Jar 450 gm"));

        layoutManager = new LinearLayoutManager(getActivity());
        rec_dashboard.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rec_dashboard.setHasFixedSize(true);
        rec_dashboard.setAdapter(new DashboardAdapter(getActivity(), mSubListModelsArray));

        BarData data = new BarData(getXAxisValues(), getDataSet());
        mChart.setData(data);

        mChart.setDescription("");
        mChart.animateXY(2000, 2000);
        mChart.getXAxis().setEnabled(true);
        mChart.setDrawValueAboveBar(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setDrawGridLines(false);

        //for X axis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setTextColor(R.color.colorlightlightlightGray);

        //for Y axis
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);

        mChart.setDrawGridBackground(false);

        mChart.getLegend().setEnabled(false);
        mChart.invalidate();

    }

    @OnClick(R.id.linearLatestOrder)
    public void onViewClicked() {

        startActivity(new Intent(getActivity(), SellerOrderActivity.class));
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

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(90.0f, 0);
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(50.0f, 1);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(80.0f, 2);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(70.0f, 3);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(50.0f, 4);
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(85.0f, 5);
        valueSet1.add(v1e6);
        BarEntry v1e7 = new BarEntry(30.0f, 6);
        valueSet1.add(v1e7);

        BarDataSet barDataSet2 = new BarDataSet(valueSet1, "");
        barDataSet2.setColor(Color.rgb(31, 44, 76));
        barDataSet2.setDrawValues(false);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("MON");
        xAxis.add("TUE");
        xAxis.add("WED");
        xAxis.add("THU");
        xAxis.add("FRI");
        xAxis.add("SAT");
        xAxis.add("SUN");
        return xAxis;
    }
}