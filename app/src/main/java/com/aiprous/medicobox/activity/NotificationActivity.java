package com.aiprous.medicobox.activity;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Window;
import android.view.WindowManager;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.adapter.ListAdapter;
import com.aiprous.medicobox.adapter.NotificationAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.searchview_medicine)
    SearchView searchview_medicine;
    @BindView(R.id.rc_notification)
    RecyclerView rc_notification;
    private Context mContext=this;
    ArrayList<NotificationModel> notificationArrayList=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        searchview_medicine.setFocusable(false);

        //set status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        notificationArrayList.add(new NotificationModel("You've successfully placed order (MB011838448382)!","29/09/2018","2:30pm"));
        notificationArrayList.add(new NotificationModel("You've successfully placed order (MB011838448382)!","29/09/2018","2:30pm"));

        layoutManager = new LinearLayoutManager(mContext);
        rc_notification.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rc_notification.setHasFixedSize(true);
        rc_notification.setAdapter(new NotificationAdapter(mContext, notificationArrayList));
    }
    @OnClick(R.id.rlayout_back_button)
    public void BackPressDetail()
    {
        finish();
    }
    public class NotificationModel{
        String notfication_text;
        String dob;
        String time;

        public NotificationModel(String notfication_text, String dob, String time) {
            this.notfication_text = notfication_text;
            this.dob = dob;
            this.time = time;
        }

        public String getNotfication_text() {
            return notfication_text;
        }

        public void setNotfication_text(String notfication_text) {
            this.notfication_text = notfication_text;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
