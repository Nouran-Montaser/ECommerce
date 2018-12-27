package com.example.nouran.ecommerce;

public class Orders {

    int OrdID, Cust_Id, Quantity, ProID, price ;
    String OrdDate, Address, name;

    public Orders(int cust_Id, int quantity, int proID, int price, String ordDate, String name) {
        Cust_Id = cust_Id;
        Quantity = quantity;
        ProID = proID;
        this.price = price;
        OrdDate = ordDate;
        this.name = name;
    }

    public Orders(int ordID, int cust_Id, int quantity, int proID, String ordDate, String address, String name) {
        OrdID = ordID;
        Cust_Id = cust_Id;
        Quantity = quantity;
        ProID = proID;
        OrdDate = ordDate;
        Address = address;
        this.name = name;
    }

    public int getOrdID() {
        return OrdID;
    }

    public void setOrdID(int ordID) {
        OrdID = ordID;
    }

    public int getCust_Id() {
        return Cust_Id;
    }

    public void setCust_Id(int cust_Id) {
        Cust_Id = cust_Id;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getProID() {
        return ProID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.ProID == ((Orders) obj).ProID)
            return true;
        else
            return false;
    }

    public void setProID(int proID) {
        ProID = proID;
    }

    public String getOrdDate() {
        return OrdDate;
    }

    public void setOrdDate(String ordDate) {
        OrdDate = ordDate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
