package com.aiprous.deliveryboy.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aiprous.deliveryboy.MainActivity;
import com.aiprous.deliveryboy.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment {

    @BindView(R.id.chart)
    BarChart mChart;

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

        //for Y axis
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setTextColor(R.color.textColor);

        mChart.setDrawGridBackground(false);

        mChart.getLegend().setEnabled(false);
        mChart.invalidate();

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