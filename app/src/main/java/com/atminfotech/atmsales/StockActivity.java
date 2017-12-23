package com.atminfotech.atmsales;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class StockActivity extends AppCompatActivity {
    AutoCompleteTextView textView ;
    Spinner spinner;
    private Button search;
    private ListView listView;
     ArrayList<PartyData>  listdata;
    StocklistAdapter listAdapter;
    ArrayAdapter autoadapter,brancharray;
    Boolean serverissue = false;
    String comp="";
    private static final String URL = "http://atm-india.in/service.asmx";
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String Soap_ACTION = "http://tempuri.org/AtmStock";
    // specifies the action
    private static final String METHOD_NAME= "AtmStock";

    private static final String Soap_ACTIONsubgroup = "http://tempuri.org/AtmSubGroupStock";
    // specifies the action
    private static final String METHOD_NAMEsubgroup= "AtmSubGroupStock";

    private static final String Soap_ACTIONgroupname = "http://tempuri.org/AtmautoMainGroupStock";
    // specifies the action
    private static final String METHOD_NAMEgroupname= "AtmautoMainGroupStock";
    TextView data;
    String data1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        listdata =new ArrayList<>();
        textView =(AutoCompleteTextView)findViewById(R.id.autotextview);
        spinner =(Spinner)findViewById(R.id.spinner);
        spinner.setPrompt("-- Select --");
        data =(TextView)findViewById(R.id.data);
        search =(Button) findViewById(R.id.search);
        new AsyngroupData().execute();
        textView.setThreshold(1);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String data =parent.getItemAtPosition(position).toString();

                spinner.findFocus();
                spinner.setFocusable(true);
                spinner.setFocusableInTouchMode(true);

                new AsynSubgroupData(data).execute();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //asyntask
          data1 =parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      //  new AsynSubgroupData().execute();

        listView =(ListView)findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(StockActivity.this,"",Toast.LENGTH_SHORT).show();
            }
        });






        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsynStockData(data1).execute();
            }
        });
    }

    public class AsynStockData extends AsyncTask<Void,Void,Void>{
        private ArrayList<PartyData> list = new ArrayList<>();



        ProgressDialog pd = new ProgressDialog(StockActivity.this);
        String info="";
        int position=0;
        int flag =0;


        public AsynStockData(String data) {
           info=data;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.show();
            listdata =new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SoapObject request =new SoapObject(NAMESPACE,METHOD_NAME);
                request.addProperty("itemname",info);

                SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet =true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE =new HttpTransportSE(URL,60*10000);
                httpTransportSE.call(Soap_ACTION,envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if(object.getPropertyCount() >0){
                    flag =1;
                    for (int i =0;i<object.getPropertyCount(); i++){
                        SoapObject innerResponse = (SoapObject) object.getProperty(i);

                        listdata.add(new PartyData(
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S2").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S2").toString()));

                    }
                }
                else {
                    flag =0;
                }
            }catch (Exception e){
                e.printStackTrace();
                pd.dismiss();

                serverissue = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listAdapter= new StocklistAdapter(StockActivity.this,listdata);
            listView.setAdapter(listAdapter);

            pd.dismiss();
            if (flag ==0){
                data.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
            else {
                listView.setVisibility(View.VISIBLE);
                data.setVisibility(View.GONE);
            }

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(StockActivity.this)
                        .setTitle("Ooops!!!")
                        .setIcon(R.mipmap.ic_launcher)
                        .setDescription("Server is not responding...\n")
                        .withIconAnimation(true)
                        .setPositiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                Log.d("MaterialStyledDialogs", "Do something!");
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

        }
    }

    public class AsynSubgroupData extends AsyncTask<Void,Void,Void>{
        private ArrayList<PartyData> list = new ArrayList<>();
        private ArrayList<String> itemname = new ArrayList<>();


        ProgressDialog pd = new ProgressDialog(StockActivity.this);
        String info;
        int position=0;

        public AsynSubgroupData(String data) {
            info =data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SoapObject request =new SoapObject(NAMESPACE,METHOD_NAMEsubgroup);
                request.addProperty("abc",info);

                SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet =true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE =new HttpTransportSE(URL,60*10000);
                httpTransportSE.call(Soap_ACTIONsubgroup,envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if(object.getPropertyCount() >0){
                    for (int i =0;i<object.getPropertyCount(); i++){
                      String message =object.getProperty(i).toString();
                        itemname.add(message);


                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                pd.dismiss();

                serverissue = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            brancharray =new ArrayAdapter(StockActivity.this,android.R.layout.simple_list_item_1,itemname);
            brancharray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(brancharray);



            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(StockActivity.this)
                        .setTitle("Ooops!!!")
                        .setIcon(R.mipmap.ic_launcher)
                        .setDescription("Server is not responding...\n")
                        .withIconAnimation(true)
                        .setPositiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                Log.d("MaterialStyledDialogs", "Do something!");
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

        }
    }

    public class AsyngroupData extends AsyncTask<Void,Void,Void>{
        private ArrayList<String> groupname = new ArrayList<>();
        ProgressDialog pd = new ProgressDialog(StockActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SoapObject request =new SoapObject(NAMESPACE,METHOD_NAMEgroupname);

                SoapSerializationEnvelope envelope =new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet =true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE =new HttpTransportSE(URL,60*10000);
                httpTransportSE.call(Soap_ACTIONgroupname,envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if(object.getPropertyCount() >0){
                    for (int i =0;i<object.getPropertyCount(); i++){
                        String message =object.getProperty(i).toString();
                        groupname.add(message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                pd.dismiss();

                serverissue = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            autoadapter =new ArrayAdapter(StockActivity.this,android.R.layout.simple_list_item_1,groupname);
            textView.setAdapter(autoadapter);
            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(StockActivity.this)
                        .setTitle("Ooops!!!")
                        .setIcon(R.mipmap.ic_launcher)
                        .setDescription("Server is not responding...\n")
                        .withIconAnimation(true)
                        .setPositiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                Log.d("MaterialStyledDialogs", "Do something!");
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }
    @Override
    public void onBackPressed() {

    }
}
