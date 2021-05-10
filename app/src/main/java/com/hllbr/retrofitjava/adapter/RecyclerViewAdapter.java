package com.hllbr.retrofitjava.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hllbr.retrofitjava.R;
import com.hllbr.retrofitjava.model.CryptoModel;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowHolder> {

    private ArrayList<CryptoModel> cryptoList;//cryptoModels ile karışmasın diye bu şekilde ifade ediyorum

    private String[] colors = {"#196F3D","#6C3483","#F39C12","#1ABC9C","#BC44C5","#d4df22","#400080","#008080","#cbbeb5","#ff80ed","#a3ff00","#ff00aa","#b4a7d6","#a4c2f4","#8ee5ee","#cd950c","#f47932"};

    public RecyclerViewAdapter(ArrayList<CryptoModel> cryptoList) {//this area is a constructor
        this.cryptoList = cryptoList;
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//row layout ile recyclerView bağladığmız alanımız
        //return null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_layout,parent,false);//rowholder bizden bir view istediği için bir adet görünüm oluşturuyoruz bu noktada
        return new RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {//Görünümlerimizi bağladığmız alanımız
        holder.bind(cryptoList.get(position),colors,position);
    }

    @Override
    public int getItemCount() {//Kaç adet row oluşturulacağını belittiğimiz alanımız
        return cryptoList.size();
    }//View adapter
    public class RowHolder extends RecyclerView.ViewHolder {

        //TextViewlerimizi tanımladığımız alanımız

        TextView textName,textPrice ;

        public RowHolder(@NonNull View itemView) {
            super(itemView);


        }
        public void bind(CryptoModel cryptoModel, String[] colors, Integer position){
            //textNmae ve textPrice eşitlemeleri buradan almak istiyorum
            itemView.setBackgroundColor(Color.parseColor(colors[position % 17]));
            textName = itemView.findViewById(R.id.text_name);
            textPrice = itemView.findViewById(R.id.text_price);
            textName.setText(cryptoModel.currency);
            textPrice.setText(cryptoModel.price);
        }
    }

}
