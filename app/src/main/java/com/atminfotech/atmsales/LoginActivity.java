package com.atminfotech.atmsales;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.vistrav.ask.Ask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText username, password;
    private String uname, pwd,IMEI_no;


    private static final String URL = "http://atm-india.in/service.asmx";
    private static final String NAMESPACE = "http://tempuri.org/";

    private static final String METHOD_NAMEimemi = "ATMimei";
    private static final String SOAP_ACTIONimei = "http://tempuri.org/ATMimei";
    private static final String METHOD_NAMELogin = "ATMLogin";
    private static final String SOAP_ACTIONLogin= "http://tempuri.org/ATMLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ask.on(this)
                .forPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE

                     )
                .withRationales("Location permission need for map to work properly",
                        "In order to save file you will need to grant storage permission") //optional
                .go();



        if(!isNetworkAvailable())
        {
            MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(this)
                    .setTitle("Alert!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setDescription("Netwrok Not Availible..\n")
                    .withIconAnimation(true)
                    .setPositiveText("Retry")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {



                            startActivity(new Intent(Settings.ACTION_SETTINGS));

                        }}) .setNegativeText("Close") .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Log.d("MaterialStyledDialogs", "Do something!");

                            dialog.dismiss();
                            finishAffinity();
                        }})
                    .show();
        }

        TelephonyManager telephonyManager= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI_no=  telephonyManager.getDeviceId();

        new AsynkImei(IMEI_no).execute();
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                uname = username.getText().toString();
                pwd = password.getText().toString();

                    new AsynkLogin(uname,pwd).execute();

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager  connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    class  AsynkImei extends AsyncTask<Void,Void,Void>{

        ProgressDialog pd = new ProgressDialog(LoginActivity.this);

        String result= "false";
        String imei="";

        public AsynkImei(String imei_no) {
            imei=imei_no;
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
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAMEimemi);
                request.addProperty("IMEI",imei.trim());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE transportSE = new HttpTransportSE(URL);
                transportSE.call(SOAP_ACTIONimei,envelope);
                SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

                result=soapPrimitive.toString();

            } catch (Exception e) {
                e.printStackTrace();
                pd.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            if(result.contentEquals("true"))
            {
                pd.dismiss();
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            }
            else {
                pd.dismiss();
                Toast.makeText(LoginActivity.this,"Use username and password..!",Toast.LENGTH_SHORT).show();
            }
        }
    }
    class  AsynkLogin extends AsyncTask<Void,Void,Void>{

        ProgressDialog pd = new ProgressDialog(LoginActivity.this);

        String result= "false";
        String uname="",pwd="";


        public AsynkLogin(String uname, String pwd) {
            this.uname=uname;
            this.pwd=pwd;
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
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAMELogin);
                request.addProperty("username",uname.trim());
                request.addProperty("password",pwd);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet=true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE transportSE = new HttpTransportSE(URL);
                transportSE.call(SOAP_ACTIONLogin,envelope);
                SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

                result=soapPrimitive.toString();

            } catch (Exception e) {
                e.printStackTrace();
                pd.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

            if(result.contentEquals("true"))
            {
                pd.dismiss();
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            }
            else {
                    pd.dismiss();
                Toast.makeText(LoginActivity.this," username and password Incorrect..!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
