package com.atminfotech.atmsales;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ABC on 06/16/2017.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<PartyData> listDataHeader;
    private HashMap<String ,ArrayList<PartySubData>> listHashMap;

    public ExpandableListViewAdapter(Context mContext, ArrayList<PartyData> listDataHeader, HashMap<String, ArrayList<PartySubData>> listHashMap) {
        this.mContext = mContext;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }



    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition).getPartyName()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition).getPartyName()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            PartyData partyData = (PartyData)this.getGroup(groupPosition);
        View view1= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_genre,parent,false);

        TextView lblHeader = (TextView) view1.findViewById(R.id.list_item_genre_name);
        TextView totalamt = (TextView) view1.findViewById(R.id.totalamt);
            lblHeader.setTypeface(null, Typeface.BOLD);
        totalamt.setTypeface(null, Typeface.BOLD);
        lblHeader.setText(partyData.getPartyName());
        totalamt.setText(partyData.getTotal());
        return view1;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

                PartySubData partySubData= (PartySubData) this.getChild(groupPosition,childPosition);

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_artist,parent,false);




        TextView billno = (TextView) view.findViewById(R.id.billno);
        TextView billdate = (TextView) view.findViewById(R.id.date);
        TextView days = (TextView) view.findViewById(R.id.days);
        TextView amount = (TextView) view.findViewById(R.id.amount);
        TextView balance = (TextView) view.findViewById(R.id.balance);
//        billno.setTypeface(null, Typeface.BOLD);

/*
        billno.setText(String.valueOf(listHashMap.get(listDataHeader.get(groupPosition).getPartyName()).get(childPosition).getBillNO()));
        billdate.setText(String.valueOf(listHashMap.get(listDataHeader.get(groupPosition).getPartyName()).get(childPosition).getBillDate()));
        days.setText(String.valueOf(listHashMap.get(listDataHeader.get(groupPosition).getPartyName()).get(childPosition).getDays()));
        amount.setText(String.valueOf(listHashMap.get(listDataHeader.get(groupPosition).getPartyName()).get(childPosition).getAmount()));
        balance.setText(listHashMap.get(listDataHeader.get(groupPosition).getPartyName()).get(childPosition).getBalance());
*/
        billno.setText(partySubData.getBillNO());
        billdate.setText(partySubData.getBillDate());
        days.setText(partySubData.getDays());
        amount.setText(partySubData.getAmount());
        balance.setText(partySubData.getBalance());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
