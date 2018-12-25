package com.aiprous.medicobox.prescription;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aiprous.medicobox.R;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrescriptionUploadAdapter extends RecyclerView.Adapter<PrescriptionUploadAdapter.ViewHolder> {

    private ArrayList<ImageUrlModel> mDataArrayList;
    private PrescriptionUploadActivity mContext;
    private DeleteImageInterface mDeleteimages;

    public PrescriptionUploadAdapter(PrescriptionUploadActivity mContext, ArrayList<ImageUrlModel> mDataArrayList) {
        this.mContext = mContext;
        this.mDataArrayList = mDataArrayList;
        this.mDeleteimages = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.upload_prescription_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Picasso.with(mContext)
                .load(mDataArrayList.get(position).getImageUrl())
                .into(holder.imgOption);

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To delete data according to position
                deleteAlert(mDataArrayList.get(position).getImageUrl());
            }
        });
    }

    //To delete data according to position
    private void deleteAlert(final String imageUrl) {
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(mContext.getResources().getString(R.string.are_you_sure))
                .setContentText(mContext.getResources().getString(R.string.confirm_deleted))
                .setCancelText(mContext.getResources().getString(R.string.no))
                .setConfirmText(mContext.getResources().getString(R.string.yes))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        sDialog.setTitleText(mContext.getResources().getString(R.string.cancelled))
                                .setContentText(mContext.getResources().getString(R.string.cancelled_file))
                                .setConfirmText(mContext.getResources().getString(R.string.ok))
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText(mContext.getResources().getString(R.string.deleted))
                                .setContentText(mContext.getResources().getString(R.string.deleted_file))
                                .setConfirmText(mContext.getResources().getString(R.string.ok))
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        //To delete the item
                        mDeleteimages.DeleteImage("delete", imageUrl);
                      /*  try {
                            for (int i = 0; i < mDataArrayList.size(); i++) {
                                if (mDataArrayList.get(position).getImageUrl().equals(mDataArrayList.get(i).getImageUrl())) {
                                    mDataArrayList.remove(i);
                                    notifyDataSetChanged();
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    }
                })
                .show();
    }

    @Override
    public int getItemCount() {
        return (mDataArrayList == null) ? 0 : mDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgOption)
        ImageView imgOption;
        @BindView(R.id.img_delete)
        ImageView img_delete;


        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface DeleteImageInterface {
        public void DeleteImage(String delete, String url);
    }
}
