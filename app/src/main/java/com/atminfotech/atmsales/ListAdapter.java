package com.atminfotech.atmsales;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ABC on 06/20/2017.
 */

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PartyData> partyDatas;

    public ListAdapter(Context mContext, ArrayList<PartyData> partyDatas) {
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

        View view1= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_genre,parent,false);

        TextView lblHeader = (TextView) view1.findViewById(R.id.list_item_genre_name);
        TextView totalamt = (TextView) view1.findViewById(R.id.totalamt);
        lblHeader.setTypeface(null, Typeface.BOLD);
        totalamt.setTypeface(null, Typeface.BOLD);
        lblHeader.setText(partyDatas.get(position).getPartyName());
        totalamt.setText(partyDatas.get(position).getTotal());
        return view1;
    }
}
