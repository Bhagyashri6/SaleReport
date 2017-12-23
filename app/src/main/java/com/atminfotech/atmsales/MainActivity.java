package com.atminfotech.atmsales;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private TableFixHeaders tableFixHeaders;
    Calendar calendar;
    private TextView datetoday, tilltoday, totalmonth;
    ArrayList<ModelStockReport> list = new ArrayList<>();
    static final int DIALOG_ID = 0;
    static int month_x, day_x, year_x;

    private Spinner cityspinner;
    private Button search;
    private String[] tableHeaders = {"Bill No.", "Client Name", "Amount"};
    private TextView tamount, hintetext;
    Boolean serverissue = false;
    private String citychoose, cityspin, getDate;
    static ArrayList<ModelStockReport> listSale = new ArrayList<>();
    public static int tableRow;
    private static final String URL = "http://atm-india.in/service.asmx";
    private static final String NAMESPACE = "http://tempuri.org/";

    private static final String Brand_Soap_ACTION = "http://tempuri.org/AtmSales";
    private static final String SOAP_ACTIONtilltoday = "http://tempuri.org/ATMFinancial";
    private static final String SOAP_ACTIONmonthlyAmount = "http://tempuri.org/ATMMonthlyAmount";
    // specifies the action
    private static final String METHOD_NAME = "AtmSales";
    private static final String METHOD_NAMEtilltoday = "ATMFinancial";
    private static final String METHOD_NAMEmonthlyAmount = "ATMMonthlyAmount";

    private String[] City = {"All", "Mumbai", "Surat"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
        datetoday = (TextView) findViewById(R.id.datetoday);
        tamount = (TextView) findViewById(R.id.tamount);
        hintetext = (TextView) findViewById(R.id.hintetext);
        tilltoday = (TextView) findViewById(R.id.tilltoday);
        totalmonth = (TextView) findViewById(R.id.totalmonth);
        //Namount = (TextView) findViewById(R.id.Namount);
        cityspinner = (Spinner) findViewById(R.id.cityspinner);
        search = (Button) findViewById(R.id.search);


        SimpleDateFormat dateFormat = new SimpleDateFormat(" MM/dd/yyyy");
        Calendar currentCal = Calendar.getInstance();
        getDate = dateFormat.format(currentCal.getTime());

        datetoday.setText(getDate);
        calendar = Calendar.getInstance();

        datetoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        tableFixHeaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, tableRow, Toast.LENGTH_SHORT).show();
      /*  Intent intent = new Intent(MainActivity.this,SalesDetailsActivity.class);
        intent.putExtra("Bill", GlobalList.listmodel.get(tableRow).getItemcode());
        startActivity(intent);*/


            }
        });


        getDate = datetoday.getText().toString();


        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, City);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityspinner.setAdapter(adapter);


        cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citychoose = parent.getItemAtPosition(position).toString();

                if (citychoose.contentEquals("Mumbai")) {
                    cityspin = "1";

                    // new AsyncStockReportData(getDate, cityspin).execute();

                } else if (citychoose.contentEquals("Surat")) {
                    cityspin = "2";

                    // new AsyncStockReportData(getDate, cityspin).execute();
                } else {
                    cityspin = "All";

                    // new AsyncStockReportData(getDate, cityspin).execute();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSale.clear();
                new AsyncStockReportData(getDate, cityspin).execute();
                new AsynkTilltoday(getDate,cityspin).execute();
                new AsynkMonthlyAmount().execute();
            }
        });


    }


    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            datetoday.setText("" + (month + 1) + "/" + dayOfMonth + "/" + year);
            getDate = datetoday.getText().toString();
            listSale.clear();
            //  new AsyncStockReportData(getDate,"All").execute();
           /* totalmonth.setText("0.00");
            tilltoday.setText("0.00");*/
            cityspinner.setSelection(0);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(MainActivity.this, dpClickListener, year_x, month_x, day_x);
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener dpClickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;
            datetoday.setText(month_x + "/" + day_x + "/" + year_x);
        }
    };

    public class MyAdapter extends SampleTableAdapter {

        private String[] tableHeaderList;
        private final int widthXSmall;
        private final int widthSmall;
        private final int widthMedium;
        private final int widthLarge;
        private final int widthXLarge;
        private final int height;
        private ArrayList<ModelStockReport> list = new ArrayList<>();

        public MyAdapter(Context context, String[] tableHeaderList, ArrayList<ModelStockReport> list) {
            super(context);

            this.tableHeaderList = tableHeaderList;
            this.list = list;

            Resources resources = context.getResources();

            widthXSmall = resources.getDimensionPixelSize(R.dimen.table_width_xsmall);
            widthSmall = resources.getDimensionPixelSize(R.dimen.table_width_small);
            widthMedium = resources.getDimensionPixelSize(R.dimen.table_width_medium);
            widthLarge = resources.getDimensionPixelSize(R.dimen.table_width_large);
            widthXLarge = resources.getDimensionPixelSize(R.dimen.table_width_xlarge);
            height = resources.getDimensionPixelSize(R.dimen.table_height);


        }

        @Override
        public int getRowCount() {
            return list.size();
        }

        @Override
        public int getColumnCount() {
            return tableHeaderList.length - 1;
        }

        @Override
        public int getWidth(int column) {

            switch (column) {
                case -1:
                    return widthSmall;

                case 0:
                    return widthXLarge;

                case 1:
                    return widthXSmall;
                case 2:
                    return widthXSmall;
                case 3:
                    return widthXSmall;
                case 5:
                    return widthLarge;

            }
            return widthLarge;
        }

        @Override
        public int getHeight(int row) {
            return height;
        }

        @Override
        public String getCellString(int row, int column) {
            if (getItemViewType(row, column) == 0) {
                return tableHeaderList[column + 1];
            }
            tableRow = row;
            return getColumnString(list.get(row), column + 1);
        }


        private String getColumnString(ModelStockReport modelSalesReport, int column) {
            switch (column) {
                case 0:

                    String dd = modelSalesReport.getItemcode();
                    String df = dd.substring(13);
                    return df;
                case 1:
                    return modelSalesReport.getItemname();
                case 2:
                    return modelSalesReport.getModel();
                case 3:
                    return modelSalesReport.getBrand();
                case 4:
                    return modelSalesReport.getSubBrand();
                case 5:
                    return modelSalesReport.getGender();
                case 6:
                    return modelSalesReport.getCatogery();
                case 7:
                    return String.valueOf(modelSalesReport.getAmount());

            }
            return null;
        }

        @Override
        public int getLayoutResource(int row, int column) {
            final int layoutResource;
            switch (getItemViewType(row, column)) {
                case 0:
                    layoutResource = R.layout.item_table1_header;
                    break;
                case 1:
                    layoutResource = R.layout.item_table_center;
                    break;
                case 2:
                    layoutResource = R.layout.item_table_center_even;
                    break;
                case 3:
                    layoutResource = R.layout.item_table_center_odd;
                    break;
                default:
                    throw new RuntimeException("wtf?");
            }
            return layoutResource;
        }

        @Override
        public int getItemViewType(int row, int column) {

            if (row < 0) {
                return 0;
            } else if (column == -1) {
                return 1;
            } else if (column == 1) {
                return 3;
            } else if (column == 3) {
                return 3;
            }
            return 2;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }
    }


    public class AsyncStockReportData extends AsyncTask<Void, Void, Void> {
        private ArrayList<ModelStockReport> list = new ArrayList<>();
        ModelStockReport model;
        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        String datee, City;
        double totalAmount = 0.00, totalNetAmount = 0.00;
        int flag = 0;

        public AsyncStockReportData(String getDate, String cityspin) {
            datee = getDate;
            City = cityspin;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.show();
            GlobalList.listmodel.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("sarv", datee);
                request.addProperty("company", City);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60 * 10000);
                androidHttpTransport.call(Brand_Soap_ACTION, envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if (object.getPropertyCount() > 0) {
                    flag = 1;
                    for (int i = 0; i < object.getPropertyCount(); i++) {
                        SoapObject innerResponse = (SoapObject) object.getProperty(i);

                        listSale.add(new ModelStockReport(
                                innerResponse.getProperty("S6").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S6").toString(),
                                innerResponse.getProperty("S2").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S2").toString(),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S3").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S3").toString())),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S4").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S4").toString())),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S5").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S5").toString())),
                                "", "", 0.00));

                        totalAmount += Double.valueOf(innerResponse.getProperty("S3").toString().contentEquals("anyType{}") ? "00.00" : innerResponse.getProperty("S3").toString());


                    }
                } else {
                    flag = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();

                pd.dismiss();

                serverissue = true;
            }
            return null;
        }

        public void onPostExecute(Void aVoid) {
            if (flag == 0) {
                hintetext.setVisibility(View.VISIBLE);
                tableFixHeaders.setVisibility(View.GONE);
            } else {
                hintetext.setVisibility(View.GONE);
                tableFixHeaders.setVisibility(View.VISIBLE);


            }
            tamount.setText(String.format(Locale.US, "%.2f", totalAmount));
            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(MainActivity.this)
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

            tableFixHeaders.setAdapter(new MyAdapter(MainActivity.this, tableHeaders, listSale));

        }
    }


    public class AsynkTilltoday extends AsyncTask<Void, Void, Void> {
        private ArrayList<ModelStockReport> list = new ArrayList<>();
        ModelStockReport model;
        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        String datee, City;
        double totalAmount = 0.00, totalNetAmount = 0.00;
        int flag = 0;
        String getDate,cityspin;
        Double till_today;

        public AsynkTilltoday(String getDate, String cityspin) {
            this.getDate=getDate;
            this.cityspin=cityspin;
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

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAMEtilltoday);
                request.addProperty("date", getDate);
                request.addProperty("company", cityspin);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60 * 10000);
                androidHttpTransport.call(SOAP_ACTIONtilltoday, envelope);
                flag = 1;
                SoapPrimitive primitive = (SoapPrimitive) envelope.getResponse();
                till_today = Double.valueOf(primitive.toString());
                /*if (object.getPropertyCount() > 0) {

                    for (int i = 0; i < object.getPropertyCount(); i++) {



                    }
                } */
            } catch (Exception e) {
                e.printStackTrace();

                pd.dismiss();

                serverissue = true;
            }
            return null;
        }

        public void onPostExecute(Void aVoid) {
            if (flag == 0) {

            } else {
                tilltoday.setText(String.format("%.2f", till_today));
            }
            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(MainActivity.this)
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

            tableFixHeaders.setAdapter(new MyAdapter(MainActivity.this, tableHeaders, listSale));

        }
    }

    public class AsynkMonthlyAmount extends AsyncTask<Void, Void, Void> {
        private ArrayList<ModelStockReport> list = new ArrayList<>();
        ModelStockReport model;
        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        String monthe, yeare;
        double totalAmount = 0.00, totalNetAmount = 0.00;
        int flag = 0;

        Double till_month = 0.00;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.show();
          /*  monthe=getDate.substring(0,1);
            yeare=getDate.substring(5);*/

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAMEmonthlyAmount);
                request.addProperty("datee", getDate);
                request.addProperty("company", cityspin);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60 * 10000);
                androidHttpTransport.call(SOAP_ACTIONmonthlyAmount, envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if (object.getPropertyCount() > 0) {
                    flag = 1;
                    for (int i = 0; i < object.getPropertyCount(); i++) {

                        till_month += Double.valueOf(object.getProperty(i).toString());

                    }
                } else {
                    flag = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();

                pd.dismiss();

                serverissue = true;
            }
            return null;
        }

        public void onPostExecute(Void aVoid) {
            if (flag == 0) {

            } else {
                totalmonth.setText(String.format("%.2f", till_month));
            }
            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(MainActivity.this)
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

            tableFixHeaders.setAdapter(new MyAdapter(MainActivity.this, tableHeaders, listSale));

        }
    }

    @Override
    public void onBackPressed() {

    }
}

