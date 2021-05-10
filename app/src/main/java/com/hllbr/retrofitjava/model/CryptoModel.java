package com.hllbr.retrofitjava.model;

import com.google.gson.annotations.SerializedName;

public class CryptoModel {//Default java class
    @SerializedName("currency")
    public String currency;
    @SerializedName("price")
    public String price;

}
