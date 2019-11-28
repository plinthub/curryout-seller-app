package com.curryoutseller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyProductsListAdapter extends RecyclerView.Adapter<MyProductsListAdapter.ViewHolder> {
  private MyProductsDataModel[] listData;

    public MyProductsListAdapter(MyProductsDataModel[] listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public MyProductsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.my_products_item_layout, parent, false);
        ViewHolder viewHolder= new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProductsListAdapter.ViewHolder holder, int position) {
        final MyProductsDataModel myProductsDataModel= listData[position];
        holder.txtTitle.setText(listData[position].getTxtTitle());
        holder.txtSubTitle.setText(listData[position].getTxtSubTitle());
        holder.txtPrice.setText(listData[position].getTxtPrice());
        holder.imageView.setImageResource(listData[position].getImgView());
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RoundRectCornerImageView imageView;
        public TextView txtTitle, txtSubTitle, txtPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView=(RoundRectCornerImageView)itemView.findViewById(R.id.imageView);
            this.txtTitle=(TextView)itemView.findViewById(R.id.txt_title);
            this.txtSubTitle=(TextView)itemView.findViewById(R.id.txt_subTitle);
            this.txtPrice=(TextView)itemView.findViewById(R.id.txt_price);
        }
    }
}
