package com.hoshizora.ossstb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JLTeruel on 01/03/2017.
 */

public class ProductItemsAdapter extends RecyclerView.Adapter<ProductItemsAdapter.MyViewHolder> {
    private List<ProductItem> productItemList;
    private  Context currentContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, price;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.item_name);
            price = (TextView) view.findViewById(R.id.price);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public ProductItemsAdapter(Context context, List<ProductItem> productItemList) {
        this.productItemList = productItemList;
        this.currentContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductItem productItem = productItemList.get(position);
        holder.itemName.setText(productItem.getItemName());
        holder.price.setText(String.valueOf(productItem.getPrice()));
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(!productItem.getImageurl().isEmpty()){
            final String fullUrl = productItem.getImageurl();
            Log.e("URL IS: ", fullUrl);
            Picasso.with(currentContext).load(fullUrl).into(holder.imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    Log.e("DEBUG", "SUCCESS");
                }

                @Override
                public void onError() {
                    Log.e("FAIL TO LOAD", fullUrl);
                    holder.imageView.setImageResource(R.mipmap.placeholder_img);
                }
            });
        }else{
            holder.imageView.setImageResource(R.mipmap.placeholder_img);
        }
    }

    @Override
    public int getItemCount() {
        return productItemList.size();
    }
}
