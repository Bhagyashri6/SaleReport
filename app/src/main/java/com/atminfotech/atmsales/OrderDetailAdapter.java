package com.atminfotech.atmsales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.atminfotech.atmsales.R.id.billno;
import static com.atminfotech.atmsales.R.id.days;

/**
 * Created by ABC on 06/23/2017.
 */

public class OrderDetailAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PartySubData> subDatas;

    public OrderDetailAdapter(Context context, ArrayList<PartySubData> subDatas) {
        this.context = context;
        this.subDatas = subDatas;
    }


    public int getCount() {
        return subDatas.size();
    }


    public Object getItem(int position) {
        return subDatas.get(position);
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agewisedetails_layout,parent,false);
        TextView date,product,quantity,amount;
        date = (TextView) view.findViewById(billno);
        product = (TextView) view.findViewById(R.id.date);
        quantity = (TextView) view.findViewById(days);
        amount = (TextView) view.findViewById(R.id.amount);
       // balance = (TextView) view.findViewById(balance);




        date.setText(subDatas.get(position).getBillNO());
        product.setText(subDatas.get(position).getBillDate());
        quantity.setText(subDatas.get(position).getDays());
        amount.setText(String.format("%.2f",Double.parseDouble(subDatas.get(position).getAmount())));
       // balance.setText(String.format("%.2f",Double.parseDouble(subDatas.get(position).getBalance())));


        return view;
    }
}
