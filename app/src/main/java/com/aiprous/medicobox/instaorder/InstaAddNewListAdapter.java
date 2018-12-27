package com.aiprous.medicobox.instaorder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.application.MedicoboxApp;
import com.aiprous.medicobox.model.GetWishListModel;
import com.aiprous.medicobox.utils.APIConstant;
import com.aiprous.medicobox.utils.CustomProgressDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aiprous.medicobox.utils.APIConstant.DELETE_WISHLIST;
import static com.aiprous.medicobox.utils.BaseActivity.isNetworkAvailable;
import static com.aiprous.medicobox.utils.BaseActivity.isValidEmailId;


public class InstaAddNewListAdapter extends RecyclerView.Adapter<InstaAddNewListAdapter.ViewHolder> {

    private ArrayList<GetWishListModel> mDataArrayList;
    private InstaAddNewListActivity mContext;
    private PopupWindow window;
    private InstaAddNewListInterface mAddNewListInterface;
    private Dialog dialog;

    public InstaAddNewListAdapter(InstaAddNewListActivity mContext, ArrayList<GetWishListModel> mGetWishListModels) {
        this.mContext = mContext;
        this.mDataArrayList = mGetWishListModels;
        this.mAddNewListInterface = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insta_add_new_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        //holder.imgProduct.setImageResource(mDataArrayList.get(position).getImage());
        holder.tvMedicineType.setText(mDataArrayList.get(position).getWishlist_name());

        holder.recyclerList.setLayoutManager(new LinearLayoutManager(mContext));

        if (!(mDataArrayList.get(position).getItems().size() == 0)) {
            holder.btn_share_wishlist.setVisibility(View.VISIBLE);
            holder.btnAddToCart.setVisibility(View.VISIBLE);
            holder.img_up_arrow.setVisibility(View.GONE);
            holder.img_down_arrow.setVisibility(View.VISIBLE);
            holder.relRecyclerList.setVisibility(View.GONE);
            holder.recyclerList.setAdapter(new InstaProductSubListDetailAdapter(mContext, mDataArrayList.get(position).getItems()));
        }

        //hide all view if item not inserted to wishlist
        if (mDataArrayList.get(position).getItems().size() == 0) {
            holder.btn_share_wishlist.setVisibility(View.GONE);
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.img_up_arrow.setVisibility(View.GONE);
            holder.img_down_arrow.setVisibility(View.GONE);
            holder.relRecyclerList.setVisibility(View.VISIBLE);
            holder.recyclerList.setVisibility(View.GONE);
            holder.txt_no_data.setVisibility(View.VISIBLE);
        }

        /*holder.relOptionDots.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                @SuppressLint("RestrictedApi") MenuBuilder menuBuilder = new MenuBuilder(mContext);
                MenuInflater inflater = new MenuInflater(mContext);
                inflater.inflate(R.menu.option_menu, menuBuilder);
                @SuppressLint("RestrictedApi") MenuPopupHelper optionsMenu = new MenuPopupHelper(mContext, menuBuilder, view);
                optionsMenu.setForceShowIcon(true);

               *//* // Set Item Click Listener
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.opt1: // Handle option1 Click
                                return true;
                            case R.id.opt2: // Handle option2 Click
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onMenuModeChange(MenuBuilder menu) {}
                });*//*


                // Display the menu
                optionsMenu.show();
            }
        });*/

        holder.img_down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_down_arrow.setVisibility(View.GONE);
                holder.img_up_arrow.setVisibility(View.VISIBLE);
                holder.relSelectAll.setVisibility(View.GONE);
                holder.relRecyclerList.setVisibility(View.VISIBLE);
                holder.txt_no_data.setVisibility(View.GONE);
            }
        });

        holder.img_up_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.img_up_arrow.setVisibility(View.GONE);
                holder.img_down_arrow.setVisibility(View.VISIBLE);
                holder.relSelectAll.setVisibility(View.GONE);
                holder.relRecyclerList.setVisibility(View.GONE);
            }
        });

        //btn to share wishlist
        holder.btn_share_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowShareWishListPopUp(mDataArrayList.get(position).getWishlist_name_id());
            }
        });

        //btn to add to cart  wishlist
        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddNewListInterface.addToCartWishList(mDataArrayList.get(position).getWishlist_name_id());
            }
        });

        holder.relOptionDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopupWindow(view, position);
            }
        });
    }

    //for share wishlist alert dialog
    private void ShowShareWishListPopUp(final String wishlist_name_id) {
        dialog = new Dialog(mContext, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.alert_for_share_wiselist);

        dialog.show();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        ImageView imgCancel = dialog.findViewById(R.id.imgCancel);
        final EditText edt_email = dialog.findViewById(R.id.edt_email);
        final EditText edt_message = dialog.findViewById(R.id.edt_message);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edt_emails = edt_email.getText().toString().trim();
                String edt_messages = edt_message.getText().toString().trim();
                if (!isValidEmailId(edt_email)) {
                    Toast.makeText(mContext, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (edt_message.getText().length() == 0) {
                    Toast.makeText(mContext, "please add message", Toast.LENGTH_SHORT).show();
                } else {
                    mAddNewListInterface.shareWishList(MedicoboxApp.onGetId(), wishlist_name_id, edt_emails, edt_messages, dialog);
                }
            }
        });

    }

    private void ShowPopupWindow(View view, final int position) {
        Rect r = locateView(view);
        LayoutInflater lInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup_view = lInflater.inflate(R.layout.popupwindow, null);
        final PopupWindow popup = new PopupWindow(popup_view, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable());
        popup.showAtLocation(popup_view, Gravity.TOP | Gravity.LEFT, r.right, r.bottom);

        LinearLayout mlinearMoveAllToWishlist = popup_view.findViewById(R.id.linearMoveAllToWishlist);
        LinearLayout mlinearDeleteWishlist = popup_view.findViewById(R.id.linearDeleteWishlist);

        mlinearMoveAllToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });

        mlinearDeleteWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mContext.startActivity(new Intent(mContext, InstaProductDetailActivity.class));
                //popup.dismiss();
                CallGetAllWishListAPI(mDataArrayList.get(position).getWishlist_name_id(), popup);
            }
        });
    }

    private void CallGetAllWishListAPI(String wishlistNameId, final PopupWindow popup) {
        if (!isNetworkAvailable(mContext)) {
            CustomProgressDialog.getInstance().showDialog(mContext, mContext.getResources().getString(R.string.check_your_network), APIConstant.ERROR_TYPE);
        } else {
            CustomProgressDialog.getInstance().showDialog(mContext, "", APIConstant.PROGRESS_TYPE);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("user_id", MedicoboxApp.onGetId());
                jsonObject.put("wishlist_name_id", "" + wishlistNameId);
                Log.e("url", "" + jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(DELETE_WISHLIST)
                    .addJSONObjectBody(jsonObject) // posting json
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsonResponse = new JSONObject(jsonObject.get("response").toString());
                                String getStatus = String.valueOf(jsonResponse.get("status"));

                                if (getStatus.equals("success")) {
                                    String msg = String.valueOf(jsonResponse.get("msg"));
                                    Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
                                    mAddNewListInterface.Delete();
                                } else {
                                    Toast.makeText(mContext, "Wishlist not deleted", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            popup.dismiss();
                            CustomProgressDialog.getInstance().dismissDialog();
                        }

                        @Override
                        public void onError(ANError anError) {
                            CustomProgressDialog.getInstance().dismissDialog();
                            //  Toast.makeText(ListActivity.this, "Check login credentials", Toast.LENGTH_SHORT).show();
                            Log.e("Error", "onError errorCode : " + anError.getErrorCode());
                            Log.e("Error", "onError errorBody : " + anError.getErrorBody());
                            Log.e("Error", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    });
        }
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

        @BindView(R.id.tv_medicine_type)
        TextView tvMedicineType;
        @BindView(R.id.btn_add_to_cart)
        Button btnAddToCart;
        @BindView(R.id.btn_share_wishlist)
        Button btn_share_wishlist;
        @BindView(R.id.recyclerList)
        RecyclerView recyclerList;
        @BindView(R.id.img_up_arrow)
        ImageView img_up_arrow;
        @BindView(R.id.img_down_arrow)
        ImageView img_down_arrow;
        @BindView(R.id.relSelectAll)
        RelativeLayout relSelectAll;
        @BindView(R.id.relOptionDots)
        RelativeLayout relOptionDots;
        @BindView(R.id.relRecyclerList)
        RelativeLayout relRecyclerList;
        @BindView(R.id.txt_no_data)
        TextView txt_no_data;

        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface InstaAddNewListInterface {
        public void Delete();

        public void shareWishList(String s, String wishlist_name_id, String edt_emails, String edt_messages, Dialog dialog);

        public void addToCartWishList(String wishlist_name_id);
    }
}
