package com.atminfotech.atmsales;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

public class SaleDatail extends AppCompatActivity {
    private TextView itemname,quantity,itemrate,amount,title,total;
    private ListView listvv;

    private  AgeWiseDetailsAdapter adapter;
    private static final String URL = "http://atm-india.in/service.asmx";
    private static final String NAMESPACE = "http://tempuri.org/";

    private TextView totalaamunt;
    private static final String Brand_Soap_ACTION = "http://tempuri.org/AtmGetPartyData";
    // specifies the action
    private static final String METHOD_NAME = "AtmGetPartyData";

    private static final String Soap_ACTIONSub = "http://tempuri.org/ATMAgeWiseData";
    // specifies the action
    private static final String METHOD_NAMESub = "ATMAgeWiseData";

    boolean  serverissue=false;
    String billno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_datail);
        Bundle bundle=getIntent().getExtras();
        billno=bundle.getString("name");


        itemname = (TextView) findViewById(R.id.itemname);
        quantity = (TextView) findViewById(R.id.quantity);
        amount = (TextView) findViewById(R.id.amount);
        itemrate = (TextView) findViewById(R.id.itemrate);
        title = (TextView) findViewById(R.id.title);
        total = (TextView) findViewById(R.id.total);
        listvv=(ListView)findViewById(R.id.listvv);

        title.setText(billno);

    }
}
