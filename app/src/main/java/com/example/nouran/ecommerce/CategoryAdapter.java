package com.example.nouran.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.myHolder> {

    private Context context;
    private ArrayList<Categories> details;

    public CategoryAdapter(Context context, ArrayList<Categories> details) {
        this.context = context;
        this.details = details;
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category, parent, false);
        return new myHolder(view);
    }


    @Override
    public void onBindViewHolder(myHolder holder, final int position) {

        holder.cat_Name.setText(details.get(position).getCategoryName());

        Picasso.with(context).load(details.get(position).getCategoryImg()).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.cat_Img);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProductsActivity.class);
                intent.putExtra("categoryID",details.get(position).categoryID);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class myHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView cat_Name;
        ImageView cat_Img;

        public myHolder(final View itemView) {
            super(itemView);
            cat_Img = itemView.findViewById(R.id.Cat_image);
            cat_Name = itemView.findViewById(R.id.Cat_name);
            linearLayout = itemView.findViewById(R.id.main_layout_container);
        }
    }
}