package com.example.nouran.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myHolder> {

    private Context context;
    private ArrayList<Products> details;
    public static ArrayList<Orders> o = new ArrayList<>();
    private int count = 0;
    private DBHelper dbHelper;

    public ProductAdapter(Context context, ArrayList<Products> details) {
        this.context = context;
        this.details = details;
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
        return new myHolder(view);
    }


    @Override
    public void onBindViewHolder(myHolder holder, final int position) {
        dbHelper = new DBHelper(context);
        holder.prod_Name.setText(details.get(position).getProductName());
        if (details.get(position).getProductName().equals("shirt"))
            holder.prod_Img.setImageResource(R.drawable.shirt);
        else if (details.get(position).getProductName().equals("Laptop"))
            holder.prod_Img.setImageResource(R.drawable.labtop);
        else if (details.get(position).getProductName().equals("chocolate"))
            holder.prod_Img.setImageResource(R.drawable.choco);
        else if (details.get(position).getProductName().equals("TV"))
            holder.prod_Img.setImageResource(R.drawable.tv);
        else
            Picasso.with(context).load(details.get(position).getProductImage()).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.prod_Img);

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String[] datetime = currentDateTimeString.split(",");
        final String DatE = datetime[0] + "," + datetime[1];

        holder.cart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mQuantity = -1;
                Cursor c = dbHelper.getQuantity(String.valueOf(details.get(position).getProductID()));
                while (!c.isAfterLast()) {
                    mQuantity = c.getInt(0);
                    c.moveToNext();
                }

                if (mQuantity > 0) {
                    Log.i("KPPOOP", details.get(position).getQuantity() + "");
                    int h = 0;
//                    boolean CHECK = dbHelper.updateQuantity(String.valueOf(details.get(position).getProductID()), mQuantity - 1);
//                    if (CHECK == true)
//                        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                    Log.i("PPOOP", details.get(position).getQuantity() + "");
                    for (int i = 0; i < o.size(); i++) {
                        if (o.get(i).getProID() == details.get(position).getProductID()) {
                            if (o.get(i).getQuantity() < mQuantity) {
                                Log.i("MMMMMM", o.get(i).getProID() + "  " + details.get(position).getProductID() + "");
                                o.get(i).setQuantity((o.get(i).getQuantity()) + 1);
                                h = 1;
                            } else {
                                h = 1;
                                Toast.makeText(context, "There is no more " + details.get(position).getProductName(), Toast.LENGTH_SHORT).show();
                            }break;
                        }
                    }
                    if (h == 0)
                        o.add(new Orders(customer.getId(), 1, details.get(position).getProductID(), details.get(position).getPrice(), DatE, details.get(position).getProductName()));
                    notifyDataSetChanged();
                } else
                    Toast.makeText(context, "There is no more " + details.get(position).getProductName(), Toast.LENGTH_SHORT).show();
                //                Log.i("IIIII", "Add" + o.get(0));
//                o = OrderSingleton.getInstance().getOrdersArrayList();
//                o.add(new Orders(1,1,1,1,"kb","kg"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView prod_Name;
        ImageView prod_Img;
        ImageButton cart_img;

        public myHolder(final View itemView) {
            super(itemView);
            prod_Img = itemView.findViewById(R.id.Prod_image);
            prod_Name = itemView.findViewById(R.id.Prod_txt);
            cart_img = itemView.findViewById(R.id.cart_image_btn);
        }
    }
}