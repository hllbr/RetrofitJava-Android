package com.hllbr.retrofitjava.service;

import com.hllbr.retrofitjava.model.CryptoModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CryptoAPI {

    //GET,POST,UPDATE,DELETE

    //get veriyi almak için /post sunucuya veri yazmak için kullanılınır.

    //URL BASE = www.website.com

    //GET --> price?=xxx

    //Urlsini sonradan vereceğim bu adrese bir arama(çağırma) yap ve bu şekilde bana bir liste içerisinde crypto modelleri gelecek buna ben get Data diyorum

    @GET("prices?key=1393faa0ac486549d95ce59fa7b58ace")
    //Call<List<CryptoModel>> getData();
    Observable<List<CryptoModel>> getData();

    //Tüm Structor yapısını bu kadar basit bir yapı ile kurmuş oluyorum


}
