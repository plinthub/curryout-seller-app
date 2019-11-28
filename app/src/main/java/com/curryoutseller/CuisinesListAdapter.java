package com.curryoutseller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curryoutseller.model.CuisinesDataModel;

import java.util.ArrayList;

public class CuisinesListAdapter extends RecyclerView.Adapter<CuisinesListAdapter.ViewHolder> {
    private ArrayList<CuisinesDataModel> listdata;
    Context context;

    public CuisinesListAdapter(ArrayList<CuisinesDataModel> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public CuisinesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.select_cuisines_listitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CuisinesListAdapter.ViewHolder holder, int position) {
        final CuisinesDataModel cuisinesDataModel = listdata.get(position);
        holder.txtFoodName.setText(cuisinesDataModel.getName());
        //holder.imageView.setImageResource(searchListData.getImg());
        final String cuisine_id = cuisinesDataModel.getCuisineID();
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFoodName;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtFoodName = (TextView) itemView.findViewById(R.id.txt_cuisines);
        }
    }
}
