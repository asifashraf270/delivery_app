package com.example.khushbakht.grocerjin.adapters;

import android.content.Context;
import android.graphics.Color;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khushbakht.grocerjin.listeners.OnItemClickListener;
import com.example.khushbakht.grocerjin.R;
import com.example.khushbakht.grocerjin.model.productList.Product;
import com.example.khushbakht.grocerjin.responseentity.ProductDetailItem;
import com.example.khushbakht.grocerjin.viewHolders.ItemTouchHelperViewHolder;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by khush on 8/23/2017.
 */

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    // private ArrayList<Data> dataset;
    public List<ProductDetailItem> responseList;
    public OnItemClickListener listener;
    public Context context;
    public int rowLayout;

    public List<ProductDetailItem> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<ProductDetailItem> responseList) {
        this.responseList = responseList;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView title;
        TextView status;
        TextView quantity, price;
        TextView notes;
        ImageView productImage;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.quantity = (TextView) itemView.findViewById(R.id.quantity);
            this.price = (TextView) itemView.findViewById(R.id.price);
//            this.notes = (TextView) itemView.findViewById(R.id.notes);
            this.productImage = (ImageView) itemView.findViewById(R.id.productImage);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
            itemView.setFocusable(true);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
        // in my view holder
    }

    public ItemViewAdapter(Context context, List<ProductDetailItem> responseList) {
        this.context = context;
        this.responseList = responseList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);


        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        ProductDetailItem productList = responseList.get(position);

        holder.title.setText(productList.getOrderProductName());
        holder.price.setText(productList.getOrderProductPrice() + " Rs");
        holder.quantity.setText(productList.getOrderProductQuantity() + " (Est.weight: ");
        Picasso.with(context).load("https://dk.eleget.net/truckpart_api/uploads/product/" + productList.productImage).placeholder(R.mipmap.ic_launcher).into(holder.productImage);
//        holder.notes.setText(productList.getPNotes());
        /*  Picasso.with(context).load("http://meatonwheel.com/api/"+productList.getPImage()).placeholder(R.mipmap.ic_launcher).into(holder.productImage);
         */ //holder.productImage.setImageURI(productList.getPImage());


    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(responseList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        responseList.remove(position);
        notifyItemRemoved(position);
    }
}