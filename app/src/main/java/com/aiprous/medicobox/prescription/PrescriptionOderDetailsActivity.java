package com.aiprous.medicobox.prescription;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aiprous.medicobox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PrescriptionOderDetailsActivity extends AppCompatActivity {

    @BindView(R.id.rb_order_everything)
    RadioButton mRadioButtonOrderEverything;
    @BindView(R.id.txt_duration_of_dose)
    TextView txtDurationOfDose;
    @BindView(R.id.txt_duration_example)
    TextView txtDurationExample;
    @BindView(R.id.linear_order_everything)
    LinearLayout linearOrderEverything;
    @BindView(R.id.rb_specify_medicine)
    RadioButton mRadioButtonSpecifyMedicine;
    @BindView(R.id.txt_specify_medicine)
    TextView txtSpecifyMedicine;
    @BindView(R.id.txt_specify_meds)
    TextView txtSpecifyMeds;
    @BindView(R.id.linear_specify_medicine)
    LinearLayout linearSpecifyMedicine;
    @BindView(R.id.rb_call_me)
    RadioButton mRadioButtonCallMe;
    @BindView(R.id.img_attachment)
    ImageView imgAttachment;
    @BindView(R.id.btnContinue)
    Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_order_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

    }

    @OnClick({R.id.rb_order_everything, R.id.rb_specify_medicine, R.id.rb_call_me, R.id.rlayout_back_button, R.id.btnContinue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_order_everything:
                linearOrderEverything.setVisibility(View.VISIBLE);
                imgAttachment.setVisibility(View.GONE);
                linearSpecifyMedicine.setVisibility(View.GONE);
                break;
            case R.id.rb_specify_medicine:
                linearSpecifyMedicine.setVisibility(View.VISIBLE);
                linearOrderEverything.setVisibility(View.GONE);
                imgAttachment.setVisibility(View.GONE);
                break;
            case R.id.rb_call_me:
                linearSpecifyMedicine.setVisibility(View.GONE);
                linearOrderEverything.setVisibility(View.GONE);
                imgAttachment.setVisibility(View.VISIBLE);
                break;
            case R.id.btnContinue:
                break;
            case R.id.rlayout_back_button:
                finish();
                break;
        }
    }
}