package com.example.nouran.ecommerce;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.myHolder> {

    private Context context;
    private ArrayList<Orders> details;
    private DBHelper dbHelper;

    public OrderDetailAdapter(Context context, ArrayList<Orders> details) {
        this.context = context;
        this.details = details;
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order, parent, false);
        return new myHolder(view);
    }


    @Override
    public void onBindViewHolder(final myHolder holder, final int position) {
        dbHelper =  new DBHelper(context);
        Log.i("IIIII", details.get(position).getName() + "    " + details.get(position).getQuantity());
        holder.order_txt.setText(details.get(position).getName());
        holder.order_quantity.setText("" + details.get(position).getQuantity());

        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dbQuantity=-1;
                Cursor cu = dbHelper.getQuantity(String.valueOf(ProductAdapter.o.get(position).getProID()));
                while (!cu.isAfterLast()) {
                    dbQuantity = cu.getInt(0);
                    cu.moveToNext();
                }
                int quantity = (ProductAdapter.o.get(position).getQuantity()) + 1;
                Log.i("EEE",quantity +"   "+dbQuantity);
                if ( (quantity) <=dbQuantity) {
                    ProductAdapter.o.get(position).setQuantity(quantity);
//                    holder.order_quantity.setText("" + details.get(position).getQuantity());
                    notifyDataSetChanged();
                }

//                int quantity = details.get(position).getQuantity();
//                ProductAdapter.o.get(position).setQuantity((ProductAdapter.o.get(position).getQuantity()) + 1);
//                holder.order_quantity.setText("" + details.get(position).getQuantity());
            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int dbQuantity=-1;
//                Cursor cu = dbHelper.getQuantity(String.valueOf(ProductAdapter.o.get(position).getProID()));
//                while (!cu.isAfterLast()) {
//                    dbQuantity = cu.getInt(0);
//                    cu.moveToNext();
//                }
                int quantity = (ProductAdapter.o.get(position).getQuantity()) - 1;
//                Log.i("EEE",quantity +"   "+dbQuantity);
                if (quantity > 0/* && quantity <=dbQuantity*/) {
                    ProductAdapter.o.get(position).setQuantity(quantity);
//                    holder.order_quantity.setText("" + details.get(position).getQuantity());
                    notifyDataSetChanged();
                }
            }
        });

//        if(ProductAdapter.o.get(position).getQuantity()==0) {
//            details.remove(details.get(position));
//            ProductAdapter.o.remove(position);
//            notifyDataSetChanged();
//        }

        holder.orderCardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
                final android.app.AlertDialog.Builder alertt = new android.app.AlertDialog.Builder(context);
                alert.setTitle(details.get(position).getName());
                String[] items = {"Delete"};
                alert.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            alertt.setTitle("Delete").setMessage("Are you sure you want to Delete it ?");
                            alertt.setNegativeButton("No", null);
                            alertt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    deltodo(position);
                                    details.remove(details.get(position));
//                                    ProductAdapter.o.remove(position);
                                    notifyDataSetChanged();
                                }
                            });
                            alertt.show();
                        }
                    }
                });
                alert.show();
                return true;

            }
        });

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView order_txt;
        TextView order_quantity;
        Button increment;
        Button decrement;
        CardView orderCardview;

        public myHolder(final View itemView) {
            super(itemView);
            order_txt = itemView.findViewById(R.id.Order_txt);
            order_quantity = itemView.findViewById(R.id.Order_quantity);
            increment = itemView.findViewById(R.id.increment_btn);
            decrement = itemView.findViewById(R.id.decrement_btn);
            orderCardview = itemView.findViewById(R.id.order_cardview);
        }
    }
}
