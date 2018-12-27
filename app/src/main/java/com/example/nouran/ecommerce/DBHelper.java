package com.example.nouran.ecommerce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static String databaseName = "ECommerceDatabase";
    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, databaseName, null, 14);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Customers (CustID integer primary key autoincrement,CutName text not null ,CutEmail text not null , Username text not null ,Password text not null ,Gender text not null , Birthdate text/* not null*/ , Job text not null)");
        db.execSQL("create table Orders (OrdID integer primary key autoincrement,OrdDate text not null , Address text not null ,Cust_Id integer , FOREIGN KEY(Cust_Id) REFERENCES Customers(CustID))");
        db.execSQL("create table Categories (CatID integer primary key autoincrement,CatName text not null)");
        db.execSQL("create table Products (ProID integer primary key autoincrement,ProName text not null , Price integer ,Quantity integer,Cat_Id integer , FOREIGN KEY(Cat_Id) REFERENCES Categories(CatID))");
        db.execSQL("create table OrderDetails (OrdID integer not null ,ProID integer not null,Quantity integer,primary key(OrdID,ProID))");

        ContentValues contentValues = new ContentValues();

        contentValues.put("CatName", "Clothes");
        db.insert("Categories", null, contentValues);
        contentValues.put("CatName", "Food");
        db.insert("Categories", null, contentValues);
        contentValues.put("CatName", "Electronics");
        db.insert("Categories", null, contentValues);

        ContentValues values = new ContentValues();
        values.put("ProName", "Laptop");
        values.put("Price", 1000);
        values.put("Quantity", 10);
        values.put("Cat_Id", 3);
        db.insert("Products", null, values);

        values.put("ProName", "TV");
        values.put("Price", 1000);
        values.put("Quantity", 2);
        values.put("Cat_Id", 3);
        db.insert("Products", null, values);

        values.put("ProName", "shirt");
        values.put("Price", 50);
        values.put("Quantity", 2);
        values.put("Cat_Id", 1);
        db.insert("Products", null, values);

        values.put("ProName", "chocolate");
        values.put("Price", 10);
        values.put("Quantity", 2);
        values.put("Cat_Id", 2);
        Long d = db.insert("Products", null, values);
        Log.i("ins", "insert" + d);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Customers");
        db.execSQL("drop table if exists Orders");
        db.execSQL("drop table if exists OrderDetails");
        db.execSQL("drop table if exists Products");
        db.execSQL("drop table if exists Categories");
        onCreate(db);
    }

    public Boolean insertCustomer(String custname, String CutEmail, String username, String pass, String gender, String birthdate, String job) {
        ContentValues contentValues = new ContentValues();
        Log.i("PPPPPPPPPPPPPPPPPP", custname + "  " + username + "  " + pass + "  " + gender + "  " + birthdate);
        contentValues.put("CutName", custname);
        contentValues.put("CutEmail", CutEmail);
        contentValues.put("Username", username);
        contentValues.put("Password", pass);
        contentValues.put("Gender", gender);
        contentValues.put("Birthdate", birthdate);
        contentValues.put("Job", job);
        sqLiteDatabase = getWritableDatabase();
        long mcheck = sqLiteDatabase.insert("Customers", null, contentValues);
        Log.i("PPPP", mcheck + "");
        sqLiteDatabase.close();
        if (mcheck != -1) {
            return true;
        } else
            return false;
    }

    public Boolean insertOrder(String OrdDate, String Address, String Cust_Id, String OrdID, String ProID, String Quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("OrdDate", OrdDate);
        contentValues.put("Address", Address);
        contentValues.put("Cust_Id", Cust_Id);
        sqLiteDatabase = getWritableDatabase();
        long mcheck = sqLiteDatabase.insert("Orders", null, contentValues);
        sqLiteDatabase.close();
        if (mcheck != -1) {
//            return insertOrderDetail(OrdID, ProID, Quantity);
        return true;
        } else
            return false;
    }

    public Boolean insertOrderDetail(String OrdID, String ProID, String Quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("OrdID", OrdID);
        contentValues.put("ProID", ProID);
        contentValues.put("Quantity", Quantity);
        sqLiteDatabase = getWritableDatabase();
        long mcheck = sqLiteDatabase.insert("OrderDetails", null, contentValues);
        sqLiteDatabase.close();
        if (mcheck != -1) {
            return true;
        } else
            return false;
    }

    public Boolean signInCustomer(String Username, String Password) {
        sqLiteDatabase = getReadableDatabase();
        String[] args = {Username, Password};
        Cursor c = sqLiteDatabase.rawQuery("select * from Customers where Username=? AND Password =? ", args);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            customer.setId(c.getInt(0));
            return true;
        } else
            return false;
    }


    public Boolean forgetPass(String Username, String CutEmail) {
        sqLiteDatabase = getReadableDatabase();
        String[] args = {Username, CutEmail};
        Cursor c = sqLiteDatabase.rawQuery("select * from Customers where Username=? AND CutEmail =? ", args);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            return true;
        } else
            return false;
    }

    public Cursor getPass(String Username, String CutEmail) {
        sqLiteDatabase = getReadableDatabase();
        String[] args = {Username, CutEmail};
        Cursor c = sqLiteDatabase.rawQuery("select * from Customers where Username=? AND CutEmail =? ", args);
        if (c != null && c.getCount() > 0)
            c.moveToFirst();
        return c;
    }

    public Cursor getQuantity(String ProID) {
        sqLiteDatabase = getReadableDatabase();
        String[] args = {ProID};
        Cursor c = sqLiteDatabase.rawQuery("select Quantity from Products where ProID=?", args);
        if (c != null && c.getCount() > 0)
            c.moveToFirst();
        Log.i("OOPPOO", c.getInt(0) + " ll");
        return c;
    }

    public Boolean updateQuantity(String ProID, int Quantity) {
        sqLiteDatabase = getWritableDatabase();
//        String selection = ProID + " = ?";
        String[] args = {ProID};
        ContentValues contentValues = new ContentValues();
        contentValues.put("Quantity", Quantity);
        int c = sqLiteDatabase.update("Products", contentValues, "ProID = ?", args);
        if (c != 0) {
            Log.i("OOPPOO", c + "");
            return true;
        } else
            return false;
    }
//    public Boolean insertCategory( String name) {
//        ContentValues contentValues = new ContentValues();
////        contentValues.put("CatID", id);
//        contentValues.put("CatName", name);
////        sqLiteDatabase = getWritableDatabase();
//        long mcheck = sqLiteDatabase.insert("Categories", null, contentValues);
//        sqLiteDatabase.close();
//        if (mcheck != -1)
//            return true;
//        else
//            return false;
//    }

    public Cursor getAllCategories() {
        sqLiteDatabase = getReadableDatabase();
        String[] args = {"CatID", "CatName"};
        Cursor c = sqLiteDatabase.query("Categories", args, null, null, null, null, null);
        if (c != null && c.getCount() > 0)
            c.moveToFirst();
        sqLiteDatabase.close();
        Log.i("PPP", c.getString(1));
        return c;
    }

//    public int getCategoryID(String CatName) {
//        sqLiteDatabase = getReadableDatabase();
//        String[] arg = {CatName};
//        Cursor cursor = sqLiteDatabase.rawQuery("Select CatID from Categories where CatName = ?", arg);
//        if (cursor!=null)
//            cursor.moveToFirst();
//        sqLiteDatabase.close();
//        return cursor.getInt(0);
//    }

//    public Boolean insertProduct(String ProName, int Price, int Quantity, int Cat_Id) {
//        ContentValues contentValues = new ContentValues();
////        contentValues.put("ProID", ProID);
//        contentValues.put("ProName", ProName);
//        contentValues.put("Price", Price);
//        contentValues.put("Quantity", Quantity);
//        contentValues.put("Cat_Id", Cat_Id);
//        sqLiteDatabase = getWritableDatabase();
//        long mcheck = sqLiteDatabase.insert("Products", null, contentValues);
//        sqLiteDatabase.close();
//        Log.i("LLLLLLLLLLLLo", mcheck + "");
//        if (mcheck != -1)
//            return true;
//        else
//            return false;
//    }

    public Cursor getAllProducts(String Cat_Id) {
        sqLiteDatabase = getReadableDatabase();
        String[] arg = {Cat_Id};
        Log.i("KKKKKKppKK", Cat_Id + "");

//        String[] args = {"ProID", "ProName", "Price", "Quantity", "Cat_Id"};
        Cursor cursor = sqLiteDatabase.rawQuery("Select *  from Products where Cat_Id = ?", arg);
        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        Log.i("KKKKKKKK", cursor.getCount() + "NN");
        sqLiteDatabase.close();
        return cursor;
    }

    public Cursor getMatchedProducts(String ProName) {
        sqLiteDatabase = getReadableDatabase();
        String[] arg = {"%" + ProName + "%"};
        Cursor cursor = sqLiteDatabase.rawQuery("Select ProName from Products where Lower(ProName) like ?", arg);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Log.i("PLPLPL", cursor.getInt(0) + "");
        }
        sqLiteDatabase.close();
        return cursor;
    }
}
