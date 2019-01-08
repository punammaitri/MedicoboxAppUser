package com.aiprous.medicobox.prescription;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.MyAccountActivity;
import com.aiprous.medicobox.instaorder.InstaAddNewListAdapter;
import com.aiprous.medicobox.model.AllCustomerAddress;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PrescriptionChooseDeliveryAddressAdapter extends RecyclerView.Adapter<PrescriptionChooseDeliveryAddressAdapter.ViewHolder> {

    private ArrayList<AllCustomerAddress> mDataArrayList;
    private PrescriptionChooseDeliveryAddressActivity mContext;
    private DeleteInterface mDeleteWishList;
    private String getFullname;

    public PrescriptionChooseDeliveryAddressAdapter(PrescriptionChooseDeliveryAddressActivity mContext, ArrayList<AllCustomerAddress> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
        this.mDeleteWishList = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_delivery_address_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        //holder.imgProduct.setImageResource(mDataArrayList.get(position).getImage());
        holder.rb_checked.setText(mDataArrayList.get(position).getFirstname() + " " + mDataArrayList.get(position).getLastname());
        holder.txtMobile.setText(mDataArrayList.get(position).getTelephone());

        final String fullAddress = mDataArrayList.get(position).getFlat() + "," + mDataArrayList.get(position).getStreet() + ","
                + mDataArrayList.get(position).getLandmark() + "," + "\n" + mDataArrayList.get(position).getCity() + ","
                + mDataArrayList.get(position).getCountry_id() + "," + mDataArrayList.get(position).getPostcode();

        holder.txtAddress.setText(fullAddress);


        holder.rb_checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getFullname = mDataArrayList.get(position).getFirstname() + " " + mDataArrayList.get(position).getLastname();
                mDeleteWishList.RadioButtonCheck(isChecked, mDataArrayList.get(position).getId(), fullAddress, getFullname, mDataArrayList.get(position).getTelephone());
            }
        });


        holder.imgOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopupWindow(view, mDataArrayList.get(position).getId(), mDataArrayList.get(position).getFirstname(), mDataArrayList.get(position).getLastname()
                        , mDataArrayList.get(position).getCity(), mDataArrayList.get(position).getCountry_id(), mDataArrayList.get(position).getRegion_id(),
                        mDataArrayList.get(position).getPostcode(), mDataArrayList.get(position).getTelephone(), mDataArrayList.get(position).getStreet(),
                        mDataArrayList.get(position).getFlat(), mDataArrayList.get(position).getLandmark());
            }
        });

    }

    private void ShowPopupWindow(View view, final String id, final String firstname, final String lastname, final String city,
                                 final String country_id, final String region_id, final String postcode, final String telephone, final String street, final String flat, final String landmark) {
        Rect r = locateView(view);
        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup_view = lInflater.inflate(R.layout.pop_up_window_delete, null);
        final PopupWindow popup = new PopupWindow(popup_view, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable());
        popup.showAtLocation(popup_view, Gravity.TOP | Gravity.LEFT, r.right, r.bottom);

        LinearLayout mLinearEdit = popup_view.findViewById(R.id.linearEdit);
        LinearLayout mLinearDelete = popup_view.findViewById(R.id.linearDelete);

        mLinearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, PrescriptionEditAddressActivity.class)
                        .putExtra("billingFlag", "true")
                        .putExtra("edit_popup", "true")
                        .putExtra("id", id)
                        .putExtra("firstname", firstname)
                        .putExtra("lastname", lastname)
                        .putExtra("city", city)
                        .putExtra("country_id", country_id)
                        .putExtra("region_id", region_id)
                        .putExtra("postcode", postcode)
                        .putExtra("telephone", telephone)
                        .putExtra("flat", flat)
                        .putExtra("street", street)
                        .putExtra("landmark", landmark)
                        .putExtra("fromchooseDelivery", "true"));
                mContext.overridePendingTransition(R.anim.right_in, R.anim.left_out);
                popup.dismiss();
            }
        });

        mLinearDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
                mDeleteWishList.Delete(id);
            }
        });
    }

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null)
            return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = loc_int[0] + v.getWidth();
        location.bottom = loc_int[1] + v.getHeight();
        return location;
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rb_checked)
        RadioButton rb_checked;
        @BindView(R.id.txtAddress)
        TextView txtAddress;
        @BindView(R.id.txtMobile)
        TextView txtMobile;
        @BindView(R.id.imgOption)
        ImageView imgOption;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface DeleteInterface {
        public void Delete(String id);

        public void RadioButtonCheck(boolean v, String id, String address, String fullname, String mobile);
    }
}
