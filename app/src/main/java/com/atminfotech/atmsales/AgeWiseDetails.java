package com.atminfotech.atmsales;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class AgeWiseDetails extends AppCompatActivity {

    private TextView billno,billdate,days,amount,balance,title,total;
    private ListView listvv;
    private ImageView mail;
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
String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_wise_details);


        Bundle bundle=getIntent().getExtras();
       name=bundle.getString("name");

         billno = (TextView) findViewById(R.id.billno);
         billdate = (TextView) findViewById(R.id.date);
         days = (TextView) findViewById(R.id.days);
         amount = (TextView) findViewById(R.id.amount);
         balance = (TextView) findViewById(R.id.balance);
        title = (TextView) findViewById(R.id.title);
        total = (TextView) findViewById(R.id.total);
        listvv=(ListView)findViewById(R.id.listvv);
        mail=(ImageView) findViewById(R.id.mail);

        title.setText(name);

        new AsynkSubData().execute();


        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AgeWiseDetails.this);
                builder.setTitle("Mail").setIcon(R.drawable.ic_mail).setTitle("Do you want Send Mail ?").setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
                        intent.putExtra(Intent.EXTRA_TEXT, "Body of email");
                        intent.setData(Uri.parse("mailto:kshirsagarc07@gmail.com")); // or just "mailto:" for blank
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                        startActivity(intent);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

    }

    public class AsynkSubData extends AsyncTask<Void, Void, Void> {
        private ArrayList<PartySubData> listd = new ArrayList<>();

        ProgressDialog pd = new ProgressDialog(AgeWiseDetails.this);
        String comp, City;

        Double amount=0.00;

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
                request.addProperty("company",name);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60 * 10000);
                androidHttpTransport.call(Soap_ACTIONSub, envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if (object.getPropertyCount() > 0) {
                    for (int i = 0; i < object.getPropertyCount(); i++) {
                        SoapObject innerResponse = (SoapObject) object.getProperty(i);


                        listd.add(new PartySubData(innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S2").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S2").toString(),
                                innerResponse.getProperty("S3").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S3").toString(),
                                innerResponse.getProperty("S4").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S4").toString(),
                                innerResponse.getProperty("S5").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S5").toString()));

                    amount+= Double.parseDouble(innerResponse.getProperty("S5").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S5").toString());

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


            adapter=new AgeWiseDetailsAdapter(AgeWiseDetails.this,listd);
            listvv.setAdapter(adapter);

                total.setText(String.format("%.2f",amount));

            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(AgeWiseDetails.this)
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
        //super.onBackPressed();
    }
}
