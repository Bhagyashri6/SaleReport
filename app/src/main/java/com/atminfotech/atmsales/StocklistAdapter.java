package com.atminfotech.atmsales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ABC on 06/24/2017.
 */

public class StocklistAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PartyData> partyDatas;

    public StocklistAdapter(Context mContext, ArrayList<PartyData> partyDatas) {
        this.mContext = mContext;
        this.partyDatas = partyDatas;
    }

    @Override
    public int getCount() {
        return partyDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return partyDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1= LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_item,parent,false);

        TextView itemname = (TextView) view1.findViewById(R.id.itemname);
        TextView quantity = (TextView) view1.findViewById(R.id.quantity);
        itemname.setText(partyDatas.get(position).getPartyName());
        quantity.setText(partyDatas.get(position).getTotal());


        return view1;
    }
}
