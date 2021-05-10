package com.hllbr.retrofitjava.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hllbr.retrofitjava.R;
import com.hllbr.retrofitjava.adapter.RecyclerViewAdapter;
import com.hllbr.retrofitjava.model.CryptoModel;
import com.hllbr.retrofitjava.service.CryptoAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    //Öncelikli oalrak Manifest içerisinde Internet izni tanımlanmalı Uygulamamız internet kullanacak bunun kullanıcıya ve Markete bildirilmesi gerekiyor .Manifest içersinde

    //Data indirme işlemi ile başlıyorum .Bu işlem için bir ArrayList oluşturuyorum

    ArrayList<CryptoModel> cryptoModels ;

    private String BASE_URL = "https://api.nomics.com/v1/";

    Retrofit retrofit ;

    RecyclerView recyclerView ;

    RecyclerViewAdapter recyclerViewAdapter;

    //Clear Code pice :D

    CompositeDisposable compositeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://api.nomics.com/v1/prices?key=1393faa0ac486549d95ce59fa7b58ace

        recyclerView = findViewById(R.id.recyclerView);

        //CryptoModel cryptoModel = new CryptoModel();

        //Retrofit oluşturma işlemi -- Retrofit & JSON

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        loadData();

//retrofit tanımlaması yaparken yeni yöntemde sadece
// .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) kısmını eklememiz yeterli oldu


    }

    //Veriyi almak için bir method yazıyorum
    public void loadData(){
        //getDataFromAPI

       final CryptoAPI cryptoAPI = retrofit.create(CryptoAPI.class);//İnterface üzerinden bir yapı kuruyorum (Service)
       compositeDisposable = new CompositeDisposable();

       compositeDisposable.add(cryptoAPI.getData()
       .subscribeOn(Schedulers.io())
       .observeOn(AndroidSchedulers.mainThread())
       .subscribe(this::handleResponse));
        //       .subscribeOn() = hangi thread de kayıt olma işleminin yapılacağı






/*
        Call<List<CryptoModel>> call = cryptoAPI.getData();

        call.enqueue(new Callback<List<CryptoModel>>() {
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                //Bize cevabın verildiği kısım bu alandır.Burada gelen cevabın başarılı olduğunu kontrol ederek işlemlerime devam edersem kod güvenliğini sağlamış olurum
                if(response.isSuccessful()){
                    //İşlem başarılı ise gerçekleştirilecekler ...response işleme işlemlerimi burada yapıyorum

                    List<CryptoModel> responseist = response.body();//Bu yapı bana bir Crypto model listesi veriyor.

                    cryptoModels = new ArrayList<>(responseist);//Bir Listeyi bir ArrayList içine bu şekilde atabiliyorum . ****

                    //RecyclerView operations

                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    recyclerViewAdapter = new RecyclerViewAdapter(cryptoModels);

                    recyclerView.setAdapter(recyclerViewAdapter);

                    /*this area for test
                    for(CryptoModel cryptoModel : cryptoModels){
                        System.out.println(cryptoModel.price+"/"+cryptoModel.currency);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                //Eğer bir hata meydana gelirse yapılmasını isteidğim işlemleri buraya giriyiroum .Ben Şaun sadece yazdırmasını istiyorum
                t.printStackTrace();
            }
        });
Asenkronize bir şekilde gelecek cevaba göre işlemi almamız gerektiğini bize söyleyen bir yapı*/

    }
    private void handleResponse(List<CryptoModel> cryptoModelList){

        cryptoModels = new ArrayList<>(cryptoModelList);

        //RecyclerView operations

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerViewAdapter = new RecyclerViewAdapter(cryptoModels);

        recyclerView.setAdapter(recyclerViewAdapter);

    }
    //Yukarıda yazdığım yeni yapıalr sayesinde activite tamamen bittiğinde tüm eklenene API call lar için temizleme işlemini bir kerede yapabilme imkanı sağladı alt blokta olduğu gibi


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();//Critic method
    }
}
/*RxJava =
-Asyscronized işlemlerde threadlerde bize yardımcı oluyor.
İşlemlerimi Retfrofitlede yapabiliyorum fakat bir sorunum var call sayım arttıkça işler karışık hale gelebiliyor.Aynı anda aynı API den birçok call yaparsam ve hepsiyle ilgili işlemleri aynı anda yapmaya kalkarsam sıkıntı yaşayabilirim.
RxJava böyle durumları daha iyi yönetmek için hemde call(çağrıları) daha temiz bir hale getirmek için var.

Disposable yapılan çağrıların (işlemlerin) belirli bir süre sonra hafızadan temizlenmesi gerekiyor.Şuan tek bir activcity ile çalışıyorum fakat daha farklı activitylerde olursa burdan başka activity'e yönlendirilmem gerekir ve oralardada işlemler yapama gereken durumlarda olacaktır.


Böyle durumlarda temizliği Retrofit kütüphaneleriyle düzgün olarak yapamayabiliriz.Aslında RxJava burada devreye giriyor.CompositeDisposible ile birden fazla disposible ı yani birden fazla gözden çıkarılabilir(tek kullanımlık-kullan at) aynı yere koyup aynı anda temizleyebiliyoruz.

Kullan at objesi nasıl oluşturuluyor ? = API den gelen istekle call ile aynı anlama geliyor.

RxJava ile işlem yapıyorsak call ile işlem yapmamak gerekiyor. Bunun yerine Observable kullanıyoruz ve bu kelime gözlemlenebilir anlamına geliyor bu obje bize yayın yapacak .Veri setinde bir değişiklik olduğunda onu gözlemleyen objelere bildirme görevini üstleniyor diyebiliriz

Çalışma şekli call ile aynı

 */