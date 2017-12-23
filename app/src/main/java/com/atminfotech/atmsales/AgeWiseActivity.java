package com.atminfotech.atmsales;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

public class AgeWiseActivity extends AppCompatActivity {


    private ListView expandableListView;
    private ExpandableListViewAdapter expandableListViewAdapter;
    private ArrayList<PartyData> listDataHeader;
    ListAdapter listAdapter;
    private HashMap<String, ArrayList<PartySubData>> listHash;
    Boolean serverissue = false;
    String comp="";
    private String citychoose, cityspin, getDate;
    private Spinner cityspinner;
    ArrayList<PartySubData> subDatas;
    private static final String URL = "http://atm-india.in/service.asmx";
    private static final String NAMESPACE = "http://tempuri.org/";

    private TextView totalaamunt;
    private static final String Brand_Soap_ACTION = "http://tempuri.org/AtmGetPartyData";
    // specifies the action
    private static final String METHOD_NAME = "AtmGetPartyData";

    private static final String Soap_ACTIONSub = "http://tempuri.org/ATMAgeWiseData";
    // specifies the action
    private static final String METHOD_NAMESub = "ATMAgeWiseData";

    private String[] City = {"All", "Mumbai", "Surat"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_wise);


        totalaamunt=(TextView)findViewById(R.id.totalaamunt);

       // new AsyncStockReportData("All").execute();
      //  new AsynkSubData().execute();

        listHash = new HashMap<>();
        subDatas = new ArrayList<>();
       /* subDatas.add(new PartySubData("sdf","sdfs","sdfsf","df","sdf"));
        subDatas.add(new PartySubData("umesh","sdfs","sdfsf","df","sdf"));
        subDatas.add(new PartySubData("yogesh","sdfs","sdfsf","df","sdf"));*/

        expandableListView=(ListView) findViewById(R.id.expandlistView);
        cityspinner = (Spinner) findViewById(R.id.cityspinner);



      /*  ArrayAdapter adapter = new ArrayAdapter(AgeWiseActivity.this, android.R.layout.simple_spinner_dropdown_item, City);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityspinner.setAdapter(adapter);*/


     /*   cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citychoose = parent.getItemAtPosition(position).toString();

                if (citychoose.contentEquals("Mumbai")) {
                    cityspin = "1";

                    new AsyncStockReportData(cityspin).execute();

                } else if (citychoose.contentEquals("Surat")) {
                    cityspin = "2";

                    new AsyncStockReportData(cityspin).execute();
                } else {
                    cityspin = "All";

                 new AsyncStockReportData(cityspin).execute();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
            expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String dd=listDataHeader.get(position).getPartyName().toString();
                    Intent intent = new Intent(AgeWiseActivity.this,AgeWiseDetails.class);
                    intent.putExtra("name",dd);
                    startActivity(intent);
                }
            });
    }

    public class AsyncStockReportData extends AsyncTask<Void, Void, Void> {
        private ArrayList<PartyData> list = new ArrayList<>();

        ProgressDialog pd = new ProgressDialog(AgeWiseActivity.this);
        String datee, City;

        int position=0;
        Double Total=0.00;

        public AsyncStockReportData(String all) {
            datee=all;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listDataHeader= new ArrayList<>();
            pd.setMessage("Please wait...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("company",datee.trim());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60 * 10000);
                androidHttpTransport.call(Brand_Soap_ACTION, envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if (object.getPropertyCount() > 0) {
                    for (int i = 0; i < object.getPropertyCount(); i++) {
                        SoapObject innerResponse = (SoapObject) object.getProperty(i);

                        listDataHeader.add(new PartyData(
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S2").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S2").toString()))));
                        Total+=Double.valueOf(innerResponse.getProperty("S2").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S2").toString());
                        comp=listDataHeader.get(i).getPartyName();

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();

                pd.dismiss();

                serverissue = true;
            }
            return null;
        }

        public void onPostExecute(Void aVoid) {
            listAdapter= new ListAdapter(AgeWiseActivity.this,listDataHeader);
            expandableListView.setAdapter(listAdapter);
            totalaamunt.setText(String.format("%.2f",Total));
            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(AgeWiseActivity.this)
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner cityspinner = (Spinner) MenuItemCompat.getActionView(item); // get the spinner
        ArrayAdapter adapter = new ArrayAdapter(AgeWiseActivity.this, android.R.layout.simple_spinner_dropdown_item, City);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityspinner.setAdapter(adapter);


        cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citychoose = parent.getItemAtPosition(position).toString();

                if (citychoose.contentEquals("Mumbai")) {
                    cityspin = "1";

                    new AsyncStockReportData(cityspin).execute();

                } else if (citychoose.contentEquals("Surat")) {
                    cityspin = "2";

                    new AsyncStockReportData(cityspin).execute();
                } else {
                    cityspin = "All";

                    new AsyncStockReportData(cityspin).execute();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }
        @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id =item.getItemId();
        if(id==android.R.id.home)
        {
            this.finish();
        }


        return false;
    }




   /* public class AsynkSubData extends AsyncTask<Void, Void, Void> {
        private ArrayList<PartyData> list = new ArrayList<>();

        ProgressDialog pd = new ProgressDialog(AgeWiseActivity.this);
        String comp, City;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAMESub);
                 request.addProperty("company","DIAGRAPH IMPEX PVT LTD");
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60 * 10000);
                androidHttpTransport.call(Soap_ACTIONSub, envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if (object.getPropertyCount() > 0) {
                    for (int i = 0; i < object.getPropertyCount(); i++) {
                        SoapObject innerResponse = (SoapObject) object.getProperty(i);


                        GlobalList.setList(innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString());

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

                pd.dismiss();

                serverissue = true;
            }
            return null;
        }

        public void onPostExecute(Void aVoid) {
            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(AgeWiseActivity.this)
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
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
