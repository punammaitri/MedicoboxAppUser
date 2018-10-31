package com.aiprous.medicobox.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.designpattern.SingletonAddToCart;
import com.aiprous.medicobox.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DrugInformationActivity extends AppCompatActivity {
    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rlayout_cart)
    RelativeLayout rlayout_cart;
    @BindView(R.id.tv_cart_size)
    TextView tv_cart_size;
    @BindView(R.id.llusage)
    LinearLayout llusage;
    @BindView(R.id.llInteraction)
    LinearLayout llInteraction;
    @BindView(R.id.llWarning)
    LinearLayout llWarning;
    @BindView(R.id.llMoreInformation)
    LinearLayout llMoreInformation;
    @BindView(R.id.imgUsage)
    ImageView imgUsage;
    @BindView(R.id.imgInteraction)
    ImageView imgInteraction;
    @BindView(R.id.imgWarning)
    ImageView imgWarning;
    @BindView(R.id.imgMoreInfo)
    ImageView imgMoreInfo;
    @BindView(R.id.viewSeparatorMore)
    View viewSeparatorMore;
    private Context mcontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);

        //Change status bar color
        BaseActivity baseActivity = new BaseActivity();
        baseActivity.changeStatusBarColor(this);

        imgUsage.setImageResource(R.drawable.arrow_yellow);
        imgInteraction.setImageResource(R.drawable.arrow_yellow);
        imgWarning.setImageResource(R.drawable.arrow_yellow);
        imgMoreInfo.setImageResource(R.drawable.arrow_yellow);

        llusage.setVisibility(View.GONE);
        llInteraction.setVisibility(View.GONE);
        llWarning.setVisibility(View.GONE);
        llMoreInformation.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SingletonAddToCart.getGsonInstance().getOptionList().isEmpty()) {
            rlayout_cart.setVisibility(View.GONE);
        } else {
            tv_cart_size.setText("" + SingletonAddToCart.getGsonInstance().getOptionList().size());
        }
    }

    @OnClick(R.id.rlayout_cart)
    public void ShowCart() {
        startActivity(new Intent(this, CartActivity.class));
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.rlayout_back_button)
    public void BackPressSDescription() {
        finish();
    }

    @OnClick({R.id.llusage, R.id.llInteraction, R.id.llWarning, R.id.llMoreInformation,
            R.id.imgUsage, R.id.imgInteraction, R.id.imgWarning, R.id.imgMoreInfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgUsage:
                if (llusage.getVisibility() == View.GONE) {
                    llusage.setVisibility(View.VISIBLE);
                    llInteraction.setVisibility(View.GONE);
                    llWarning.setVisibility(View.GONE);
                    llMoreInformation.setVisibility(View.GONE);
                    imgUsage.setImageResource(R.drawable.arrow_yellow_up);
                    imgInteraction.setImageResource(R.drawable.arrow_yellow);
                    imgWarning.setImageResource(R.drawable.arrow_yellow);
                    imgMoreInfo.setImageResource(R.drawable.arrow_yellow);
                    viewSeparatorMore.setVisibility(View.GONE);
                } else if (llusage.getVisibility() == View.VISIBLE) {
                    llusage.setVisibility(View.GONE);
                    llInteraction.setVisibility(View.GONE);
                    llWarning.setVisibility(View.GONE);
                    llMoreInformation.setVisibility(View.GONE);
                    imgUsage.setImageResource(R.drawable.arrow_yellow);
                    imgInteraction.setImageResource(R.drawable.arrow_yellow);
                    imgWarning.setImageResource(R.drawable.arrow_yellow);
                    imgMoreInfo.setImageResource(R.drawable.arrow_yellow);
                    viewSeparatorMore.setVisibility(View.GONE);
                }
                break;
            case R.id.imgInteraction:
                if (llInteraction.getVisibility() == View.GONE) {
                    llusage.setVisibility(View.GONE);
                    llInteraction.setVisibility(View.VISIBLE);
                    llWarning.setVisibility(View.GONE);
                    llMoreInformation.setVisibility(View.GONE);
                    imgUsage.setImageResource(R.drawable.arrow_yellow);
                    imgInteraction.setImageResource(R.drawable.arrow_yellow_up);
                    imgWarning.setImageResource(R.drawable.arrow_yellow);
                    imgMoreInfo.setImageResource(R.drawable.arrow_yellow);
                    viewSeparatorMore.setVisibility(View.GONE);
                } else if (llInteraction.getVisibility() == View.VISIBLE) {
                    llusage.setVisibility(View.GONE);
                    llInteraction.setVisibility(View.GONE);
                    llWarning.setVisibility(View.GONE);
                    llMoreInformation.setVisibility(View.GONE);
                    imgUsage.setImageResource(R.drawable.arrow_yellow);
                    imgInteraction.setImageResource(R.drawable.arrow_yellow);
                    imgWarning.setImageResource(R.drawable.arrow_yellow);
                    imgMoreInfo.setImageResource(R.drawable.arrow_yellow);
                    viewSeparatorMore.setVisibility(View.GONE);
                }
                break;
            case R.id.imgWarning:
                if (llWarning.getVisibility() == View.GONE) {
                    llusage.setVisibility(View.GONE);
                    llInteraction.setVisibility(View.GONE);
                    llWarning.setVisibility(View.VISIBLE);
                    llMoreInformation.setVisibility(View.GONE);
                    imgUsage.setImageResource(R.drawable.arrow_yellow);
                    imgInteraction.setImageResource(R.drawable.arrow_yellow);
                    imgWarning.setImageResource(R.drawable.arrow_yellow_up);
                    imgMoreInfo.setImageResource(R.drawable.arrow_yellow);
                    viewSeparatorMore.setVisibility(View.GONE);
                } else if (llWarning.getVisibility() == View.VISIBLE) {
                    llusage.setVisibility(View.GONE);
                    llInteraction.setVisibility(View.GONE);
                    llWarning.setVisibility(View.GONE);
                    llMoreInformation.setVisibility(View.GONE);
                    imgUsage.setImageResource(R.drawable.arrow_yellow);
                    imgInteraction.setImageResource(R.drawable.arrow_yellow);
                    imgWarning.setImageResource(R.drawable.arrow_yellow);
                    imgMoreInfo.setImageResource(R.drawable.arrow_yellow);
                    viewSeparatorMore.setVisibility(View.GONE);
                }
                break;
            case R.id.imgMoreInfo:
                if (llMoreInformation.getVisibility() == View.GONE) {
                    llusage.setVisibility(View.GONE);
                    llInteraction.setVisibility(View.GONE);
                    llWarning.setVisibility(View.GONE);
                    llMoreInformation.setVisibility(View.VISIBLE);
                    imgUsage.setImageResource(R.drawable.arrow_yellow);
                    imgInteraction.setImageResource(R.drawable.arrow_yellow);
                    imgWarning.setImageResource(R.drawable.arrow_yellow);
                    imgMoreInfo.setImageResource(R.drawable.arrow_yellow_up);
                    viewSeparatorMore.setVisibility(View.VISIBLE);
                } else if (llMoreInformation.getVisibility() == View.VISIBLE) {
                    llusage.setVisibility(View.GONE);
                    llInteraction.setVisibility(View.GONE);
                    llWarning.setVisibility(View.GONE);
                    llMoreInformation.setVisibility(View.GONE);
                    imgUsage.setImageResource(R.drawable.arrow_yellow);
                    imgInteraction.setImageResource(R.drawable.arrow_yellow);
                    imgWarning.setImageResource(R.drawable.arrow_yellow);
                    imgMoreInfo.setImageResource(R.drawable.arrow_yellow);
                    viewSeparatorMore.setVisibility(View.GONE);
                }
                break;
        }
    }
}