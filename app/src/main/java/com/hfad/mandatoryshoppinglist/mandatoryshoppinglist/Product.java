package com.hfad.mandatoryshoppinglist.mandatoryshoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jesper on 14-Mar-16.
 */
public class Product implements Parcelable{
    String name;
    int quantity;
    int price;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public Product()
    {
    }

    public Product(String name, int quantity, int price)
    {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    //De-parcel
    public Product(Parcel in){
        name = in.readString();
        quantity = in.readInt();
        price = in.readInt();
    }

    @Override
    public String toString() {
        return quantity+" x "+name+" - "+price+"kr/stk";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeInt(price);

    }

    // Creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };




}