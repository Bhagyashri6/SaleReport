package com.atminfotech.atmsales;

import java.util.ArrayList;

/**
 * Created by ABC on 06/20/2017.
 */

public class GlobalList {
    public static ArrayList<PartySubData> list = new ArrayList<>();

    public static   ArrayList<PartySubData> getList(){
        return list;
    }

    public static void setList(String billNO, String billDate, String days, String amount, String balance){
        PartySubData partySubData = new PartySubData( billNO, billDate, days, amount, balance);
        list.add(partySubData);

    }


    public static ArrayList<ModelStockReport> listmodel = new ArrayList<>();

    public static   ArrayList<ModelStockReport> getListmodel(){
        return listmodel;
    }

    public static void setListmodel(String itemcode, String itemname, String model, String brand, String subBrand, String gender, String catogery, double amount){
        ModelStockReport modelStockReport = new ModelStockReport(itemcode ,itemname, model, brand, subBrand, gender, catogery, amount);
        listmodel.add(modelStockReport);

    }
}
