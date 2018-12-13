package com.aiprous.medicobox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aiprous.medicobox.R;
import com.aiprous.medicobox.activity.NotificationActivity;
import com.aiprous.medicobox.model.ListModel;
import com.aiprous.medicobox.model.SearchModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.ViewHolder> {


    private ArrayList<SearchModel> mSearchResultArrayList;
    private Context mContext;

    public SearchViewAdapter(Context mContext, ArrayList<SearchModel> mSearchResultArrayList) {
        this.mContext = mContext;
        this.mSearchResultArrayList = mSearchResultArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searchview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

    holder.tv_medicine_name.setText(mSearchResultArrayList.get(position).getTitle());

        holder.llayout_medicine_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSearchResultArrayList.get(position).getId();

            }
        });


    }
    @Override
    public int getItemCount() {
        return (mSearchResultArrayList == null) ? 0 : mSearchResultArrayList.size();



    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_medicine_name)
        TextView tv_medicine_name;
        @BindView(R.id.llayout_medicine_name)
        LinearLayout llayout_medicine_name;



        ViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    public void setFilter(ArrayList<SearchModel> Models) {
        mSearchResultArrayList = new ArrayList<>();
        mSearchResultArrayList.addAll(Models);
        notifyDataSetChanged();
    }
}
