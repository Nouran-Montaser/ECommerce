package com.example.nouran.ecommerce;

public class Products {

    int productID , price , categoryID,quantity;
    String productName;
    String productImage;


    public Products(int productID, int price, int categoryID, String productName ,String productImage,int quantity) {
        this.productID = productID;
        this.price = price;
        this.categoryID = categoryID;
        this.productName = productName;
        this.productImage = productImage;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
