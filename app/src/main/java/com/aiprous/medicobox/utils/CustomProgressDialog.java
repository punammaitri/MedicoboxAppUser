package com.aiprous.medicobox.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.aiprous.medicobox.R;
import com.cazaea.sweetalert.SweetAlertDialog;


public class CustomProgressDialog {

    private static CustomProgressDialog pDialog;
    @Nullable
    private SweetAlertDialog mDialog;

    private CustomProgressDialog() {
    }

    public static synchronized CustomProgressDialog getInstance() {
        if (pDialog == null) {
            pDialog = new CustomProgressDialog();
        }
        return pDialog;
    }

    public void showDialog(@NonNull Context context, @Nullable String msg, int processType) {
        String msgString;
        if (context instanceof Activity)
            if (((AppCompatActivity) context).isFinishing()) {
                return;
            }
        switch (processType) {
            case APIConstant.PROGRESS_TYPE:
                if (mDialog == null) {
                    mDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                    mDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
                    if (msg != null && !"".equals(msg)) {
                        mDialog.setTitleText(msg);
                    } else {
                        mDialog.setTitleText(context.getResources().getString(R.string.loading));
                    }
                    mDialog.setCancelable(false);
                    if (!mDialog.isShowing()) {
                        mDialog.show();
                    }
                }
                break;
            case APIConstant.ERROR_TYPE:
                if (msg != null && !"".equals(msg)) {
                    msgString = msg;
                } else {
                    msgString = APIConstant.SOME_THING_WENT_WRONG;
                }
                mDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                try {
                                    mDialog.dismiss();
                                    mDialog = null;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setContentText(msgString);
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                break;
            case APIConstant.SUCCESS_TYPE:
                if (msg != null && !"".equals(msg)) {
                    msgString = msg;
                } else {
                    msgString = "Done";
                }
                mDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                try {
                                    mDialog.dismiss();
                                    mDialog = null;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setContentText(msgString);
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                break;
            case APIConstant.WARNING_TYPE:
                if (msg != null && !"".equals(msg)) {
                    msgString = msg;
                } else {
                    msgString = APIConstant.SOME_THING_WENT_WRONG;
                }
                mDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Oops!")
                        .setContentText(msgString);
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                break;
        }
    }

    public void dismissDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void onHideKeyBoard(Activity mActivity) {
        final InputMethodManager imm = (InputMethodManager) mActivity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
    }


}