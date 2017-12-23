package com.atminfotech.atmsales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ABC on 06/21/2017.
 */

public class AgeWiseDetailsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PartySubData> subDatas;

    public AgeWiseDetailsAdapter(Context mContext, ArrayList<PartySubData> subDatas) {
        this.mContext = mContext;
        this.subDatas = subDatas;
    }

    @Override
    public int getCount() {
        return subDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return subDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agewisedetails_layout,parent,false);
 TextView billno,billdate,days,amount,balance;
        billno = (TextView) view.findViewById(R.id.billno);
        billdate = (TextView) view.findViewById(R.id.date);
        days = (TextView) view.findViewById(R.id.days);
        amount = (TextView) view.findViewById(R.id.amount);
        balance = (TextView) view.findViewById(R.id.balance);




        billno.setText(subDatas.get(position).getBillNO());
        billdate.setText(subDatas.get(position).getBillDate());
        days.setText(subDatas.get(position).getDays());
        amount.setText(String.format("%.2f",Double.parseDouble(subDatas.get(position).getAmount())));
        balance.setText(String.format("%.2f",Double.parseDouble(subDatas.get(position).getBalance())));


        return view;
    }
}
