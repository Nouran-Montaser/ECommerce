package com.example.nouran.ecommerce;

import java.util.ArrayList;

class OrderSingleton {

    private static final OrderSingleton ourInstance = new OrderSingleton();
    private ArrayList<Orders> ordersArrayList = new ArrayList<>();

    public static OrderSingleton getInstance() {
        return ourInstance;
    }

    public ArrayList<Orders> getOrdersArrayList() {
        return ordersArrayList;
    }

    public void setOrdersArrayList(ArrayList<Orders> ordersArrayList) {
        this.ordersArrayList = ordersArrayList;
    }

    private OrderSingleton() {
    }
}