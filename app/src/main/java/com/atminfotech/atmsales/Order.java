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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

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

public class Order extends AppCompatActivity {
     static ArrayList<ModelStockReport> orderlist = new ArrayList<>();
    private TableFixHeaders tableFixHeaders;
    Calendar calendar;
    private Button search;
    private TextView datetoday,hintetext;
    //ArrayList<ModelStockReport> list = new ArrayList<>();
    static final int DIALOG_ID = 0;
    int month_x, day_x, year_x;
    private Spinner cityspinner;
    private String[] tableHeaders = {"Order No.", "Client Name", "Amount"};
    private TextView tamount, Namount;
    Boolean serverissue = false;
    private String citychoose, cityspin, getDate;

    private static final String URL = "http://atm-india.in/service.asmx";
    private static final String NAMESPACE = "http://tempuri.org/";

    private static final String Brand_Soap_ACTION = "http://tempuri.org/ATMOrderDetail";
    // specifies the action
    private static final String METHOD_NAME = "ATMOrderDetail";

    private String[] City = {"All", "Mumbai", "Surat"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        tableFixHeaders =(TableFixHeaders)findViewById(R.id.table);
        datetoday =(TextView)findViewById(R.id.datetoday);
        tamount =(TextView)findViewById(R.id.tamount);
        search =(Button) findViewById(R.id.search);
        cityspinner =(Spinner)findViewById(R.id.cityspinner);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Calendar currentCal =Calendar.getInstance();
        getDate =dateFormat.format(currentCal.getTime());
        hintetext=(TextView)findViewById(R.id.hintetext);
        datetoday.setText(getDate);
        calendar =Calendar.getInstance();

        datetoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Order.this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });

        getDate =datetoday.getText().toString();









        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncOrderData().execute();
            }
        });






    }
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            datetoday.setText("" + (month+1) + "/" + dayOfMonth + "/" + year);
            getDate=datetoday.getText().toString();
            orderlist.clear();
          //  new AsyncOrderData().execute();
           // cityspinner.setSelection(0);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(Order.this, dpClickListener, year_x, month_x, day_x);
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

    public class MyAdapter extends SampleTableAdapterOrder {

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
                    return widthMedium;
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

            return getColumnString(list.get(row), column + 1);
        }

        private String getColumnString(ModelStockReport modelSalesReport, int column) {
            switch (column) {
                case 0:
                    return modelSalesReport.getItemcode();
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
                    layoutResource = R.layout.item_table_center_even;
                    break;
                case 2:
                    layoutResource = R.layout.item_table_center_odd;
                    break;
                case 3:
                    layoutResource = R.layout.item_table_center;
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
            } else if (column == 1) {
                return 2;
            }else if (column == 0) {
                return 3;
            }
            else if (column == 3) {
                return 3;
            }
            return 2;

        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }
    }

    public class AsyncOrderData extends AsyncTask<Void,Void,Void>{

        ModelStockReport model;
        ProgressDialog pd = new ProgressDialog(Order.this);
        String datee, City;
        int flag =0;

        double totalAmount = 0.00, totalNetAmount = 0.00;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("sarv",getDate);
              //  request.addProperty("company", City);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60 * 10000);
                androidHttpTransport.call(Brand_Soap_ACTION, envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if (object.getPropertyCount() > 0) {
                    flag =1;
                    for (int i = 0; i < object.getPropertyCount(); i++) {
                        SoapObject innerResponse = (SoapObject) object.getProperty(i);

                        orderlist.add(new ModelStockReport(
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S3").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S3").toString(),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S5").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S5").toString())),
                                innerResponse.getProperty("S5").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S5").toString(),
                                innerResponse.getProperty("S5").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S5").toString(),
                                "", "", 0.00));

                        totalAmount += Double.valueOf(innerResponse.getProperty("S5").toString().contentEquals("anyType{}") ? "00.00" : innerResponse.getProperty("S5").toString());


                    }
                }
                else {
                    flag =0;
                }
            } catch (Exception e) {
                e.printStackTrace();

                pd.dismiss();

                serverissue = true;
            }
            return null;
        }

        @Override
        public void onPostExecute(Void aVoid) {


            tamount.setText(String.format(Locale.US,"%.2f",totalAmount));
            pd.dismiss();
            if (flag ==0){
                hintetext.setVisibility(View.VISIBLE);
                tableFixHeaders.setVisibility(View.GONE);
            }
            else {
                hintetext.setVisibility(View.GONE);
                tableFixHeaders.setVisibility(View.VISIBLE);

                
            }
            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(Order.this)
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

            tableFixHeaders.setAdapter(new MyAdapter(Order.this, tableHeaders, orderlist));

        }
    }

    @Override
    public void onBackPressed() {

    }
}
