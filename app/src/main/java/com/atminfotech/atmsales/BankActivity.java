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
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BankActivity extends AppCompatActivity {

    private TableFixHeaders tableFixHeaders;
    private String currentdate;
    Calendar calendar;
    private Button search;
    private TextView datetoday;
    static ArrayList<ModelStockReport> listbank = new ArrayList<>();
    static final int DIALOG_ID = 0;
    int month_x, day_x, year_x;
    private Spinner cityspinner;
    private String[] tableHeaders = {"V No.", "Party Name", "Payment", "Receipt"};
    private TextView tamount, Namount, hintetext,ramount;
    Boolean serverissue = false;
    private String citychoose, cityspin, getDate;
    public static int tableRow;
    private static final String URL = "http://atm-india.in/service.asmx";
    private static final String NAMESPACE = "http://tempuri.org/";

    private static final String Brand_Soap_ACTION = "http://tempuri.org/ATMBankData";
    // specifies the action
    private static final String METHOD_NAME = "ATMBankData";


    private static final String Soap_ACTIONbank = "http://tempuri.org/ATMBanknames";
    // specifies the action
    private static final String METHOD_NAMEbank = "ATMBanknames";
    private String[] City = {"All", "Mumbai", "Surat"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);


        tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
        datetoday = (TextView) findViewById(R.id.datetoday);
        tamount = (TextView) findViewById(R.id.tamount);
        ramount = (TextView) findViewById(R.id.ramount);
        hintetext = (TextView) findViewById(R.id.hintetext);
        //Namount = (TextView) findViewById(R.id.Namount);
        cityspinner = (Spinner) findViewById(R.id.cityspinner);
        search = (Button) findViewById(R.id.search);


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar currentCal = Calendar.getInstance();
        getDate = dateFormat.format(currentCal.getTime());

        datetoday.setText(getDate);
        calendar = Calendar.getInstance();

        datetoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BankActivity.this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        tableFixHeaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(BankActivity.this, tableRow, Toast.LENGTH_SHORT).show();
            }
        });


        getDate = datetoday.getText().toString();
        new AsynkBank().execute();

       /* ArrayAdapter adapter = new ArrayAdapter(BankActivity.this, android.R.layout.simple_spinner_dropdown_item, City);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityspinner.setAdapter(adapter);
*/

        cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citychoose = parent.getItemAtPosition(position).toString();

               // new AsyncStockReportData(getDate, citychoose).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncStockReportData(getDate, citychoose).execute();
            }
        });


    }


    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            datetoday.setText("" + year + "/" + (month + 1) + "/" + dayOfMonth);
            getDate = datetoday.getText().toString();
            listbank.clear();
            //new AsyncStockReportData(getDate).execute();
         //   new AsynkBank().execute();
            //  cityspinner.setSelection(0);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(BankActivity.this, dpClickListener, year_x, month_x, day_x);
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

    public class MyAdapter extends SampleTableAdapterBank {

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
                    return widthXLarge;
                case 2:
                    return widthMedium;
                case 3:
                    return widthMedium;
                case 5:
                    return widthMedium;

                case 6:
                    return widthMedium;

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
                    String df = dd.substring(11);
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
                return 1;
            } else if (column == 2) {
                return 1;
            } else if (column == 3) {
                return 1;
            } else if (column == 4) {
                return 1;
            } else if (column == 5) {
                return 1;
            }
            return 2;
        }

        @Override
        public int getViewTypeCount() {
            return 7;
        }
    }


    public class AsynkBank extends AsyncTask<Void, Void, Void> {

        ArrayList<String> banklist = new ArrayList<>();

        ProgressDialog pd = new ProgressDialog(BankActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please Wait...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAMEbank);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE transportSE = new HttpTransportSE(URL);

                transportSE.call(Soap_ACTIONbank, envelope);

                SoapObject object = (SoapObject) envelope.getResponse();
                if (object.getPropertyCount() > 0) {

                    for (int i = 0; i < object.getPropertyCount(); i++) {
                        banklist.add(object.getProperty(i).toString().trim());

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                pd.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter adapter = new ArrayAdapter(BankActivity.this, android.R.layout.simple_spinner_dropdown_item, banklist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cityspinner.setAdapter(adapter);
            super.onPostExecute(aVoid);
            pd.dismiss();
        }
    }


    public class AsyncStockReportData extends AsyncTask<Void, Void, Void> {
        private ArrayList<ModelStockReport> list = new ArrayList<>();
        ModelStockReport model;
        ProgressDialog pd = new ProgressDialog(BankActivity.this);
        String datee, City;
        double totalAmount = 0.00, RecAmount = 0.00;
        int flag = 0;

        public AsyncStockReportData(String getDate, String CityChooser) {
            datee = getDate;
            City = CityChooser;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.show();
            listbank.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("date", datee);
                request.addProperty("bank", City);
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
                        String ss = innerResponse.getProperty("S7").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S7").toString();
                      String  Recpt="0.00", paymnt="0.00";
                        if (ss.contentEquals("Receipt")) {
                            Recpt = String.format("%.2f",Double.parseDouble(innerResponse.getProperty("S3").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S3").toString()));
                          //  paymnt = "-";
                            RecAmount += Double.valueOf(Recpt);
                        }
                        if (ss.contentEquals("Payment")) {
                            paymnt = String.format("%.2f",Double.parseDouble(innerResponse.getProperty("S3").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S3").toString()));
                           // Recpt = "-";
                            totalAmount += Double.valueOf(paymnt);
                        }
                        listbank.add(new ModelStockReport(
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S2").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S2").toString(),
                                paymnt,
                                Recpt,
                                innerResponse.getProperty("S5").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S5").toString(),
                                "",
                                "", 0.00
                        ));





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
            ramount.setText(String.format(Locale.US, "%.2f", RecAmount));
            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(BankActivity.this)
                        .setTitle("Ooops!!!")
                        .setIcon(R.mipmap.ic_launcher)
                        .setDescription("Server is not responding...\n")
                        .withIconAnimation(true)
                        .setPositiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //    startActivity(new Intent(BankActivity.this, HomeActivity.class));
                                Log.d("MaterialStyledDialogs", "Do something!");
                                dialog.dismiss();
                            }
                        })
                        .show();
            }

            tableFixHeaders.setAdapter(new BankActivity.MyAdapter(BankActivity.this, tableHeaders, listbank));

        }
    }

    @Override
    public void onBackPressed() {

    }
}

