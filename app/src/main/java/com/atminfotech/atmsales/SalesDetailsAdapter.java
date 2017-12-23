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
 * Created by ABC on 06/21/2017.
 */

public class SalesDetailsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PartySubData> subDatas;

    public SalesDetailsAdapter(Context mContext, ArrayList<PartySubData> subDatas) {
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_saledetail,parent,false);
 TextView itemname,quantity,itemrate,amount,balance;
        itemname = (TextView) view.findViewById(R.id.itemname);
        quantity = (TextView) view.findViewById(R.id.quantity);
        itemrate = (TextView) view.findViewById(R.id.itemrate);
        amount = (TextView) view.findViewById(R.id.amount);





        itemname.setText(subDatas.get(position).getBillNO());
        quantity.setText(subDatas.get(position).getBillDate());
        itemrate.setText(subDatas.get(position).getDays());
        amount.setText(String.format("%.2f",Double.parseDouble(subDatas.get(position).getAmount())));



        return view;
    }
}
