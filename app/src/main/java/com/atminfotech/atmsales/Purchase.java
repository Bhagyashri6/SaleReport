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


public class Purchase extends AppCompatActivity {
    private TableFixHeaders tableFixHeaders;
    private TextView datetoday,hintetext;
    Calendar calendar ;
    private TextView tamount, Namount;
    String[] branch ={"All","Mumbai","Surat"};
    Spinner spinner;
    private String currentdate;
    static final int DIALOG_ID = 0;
    int month_x, day_x, year_x;
    Boolean serverissuse =false;
    private String citychoose,getData;
    static String cityspin;
    private Button search;
    static ArrayList<ModelStockReport> listPurchase = new ArrayList<>();

    private String[] tableHeaders = { "Bill No.","Client Name", "Amount"};

    Boolean serverissue = false;
    private static final String URL = "http://atm-india.in/service.asmx";
    private static final String NAMESPACE = "http://tempuri.org/";

    private static final String Brand_Soap_ACTION = "http://tempuri.org/AtmPurchase";
    // specifies the action
    private static final String METHOD_NAME = "AtmPurchase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
        tamount = (TextView) findViewById(R.id.tamount);
        hintetext=(TextView)findViewById(R.id.hintetext);
        search=(Button) findViewById(R.id.search);
        //datepicker
        datetoday =(TextView)findViewById(R.id.datetoday);
        SimpleDateFormat dateFormat =new SimpleDateFormat("MM/dd/yyyy");
        Calendar currentdate =Calendar.getInstance();
        getData =dateFormat.format(currentdate.getTime());
        datetoday.setText(getData);
        calendar =Calendar.getInstance();




        datetoday = (TextView) findViewById(R.id.datetoday);
        datetoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Purchase.this,listener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        getData =datetoday.getText().toString();

        //spinner
        spinner =(Spinner)findViewById(R.id.cityspinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citychoose =parent.getItemAtPosition(position).toString();
                if(citychoose.contentEquals("Mumbai")){
                    cityspin ="1";
                   // new asyncPurchaseReport(getData,cityspin).execute();
                }else if(citychoose.contentEquals("Surat")){
                    cityspin ="2";
                 //   new asyncPurchaseReport(getData,cityspin).execute();
                }else if(citychoose.contentEquals("All")){
                    cityspin ="All";
                    //new asyncPurchaseReport(getData,cityspin).execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter brancharray =new ArrayAdapter(this,android.R.layout.simple_spinner_item,branch);
        brancharray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(brancharray);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new asyncPurchaseReport(getData,cityspin).execute();
            }
        });






    }

    DatePickerDialog.OnDateSetListener listener =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            datetoday.setText("" + (month+1) +"/" +dayOfMonth + "/" +year );
            getData =datetoday.getText().toString();
            listPurchase.clear();
           // new asyncPurchaseReport(getData,"All").execute();
            spinner.setSelection(0);

        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(Purchase.this, dpClickListener, year_x, month_x, day_x);
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



    public class MyAdapter extends SampleTableAdapterPurchase {

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

        public int getWidth(int column) {

            switch (column) {
                case -1:
                    return widthLarge;

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
                return 1;
            }
            else if (column == -1) {
                return 3;
            }
            return 2;

        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }
    }

    public class asyncPurchaseReport extends AsyncTask<Void, Void, Void> {
      //  private ArrayList<ModelStockReport> list = new ArrayList<>();
        ModelStockReport model;
        ProgressDialog pd = new ProgressDialog(Purchase.this);
        String datee, city;
        double totalAmount = 0.00, totalNetAmount = 0.00;
        int flag =0;



        public asyncPurchaseReport(String datee,String city) {
            this.datee =datee;
            this.city =city;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listPurchase.clear();

            pd.setMessage("Please wait...");
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("sarv", datee);
                request.addProperty("company",city);
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

                        listPurchase.add(new ModelStockReport(
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S2").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S2").toString(),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S3").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S3").toString())),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S4").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S4").toString())),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S5").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S5").toString())),
                                "", "", 0.00
                               // innerResponse.getProperty("S6").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S6").toString()
                                ));

                        totalAmount += Double.valueOf(innerResponse.getProperty("S3").toString().contentEquals("anyType{}") ? "00.00" : innerResponse.getProperty("S3").toString());

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

        public void onPostExecute(Void aVoid) {
            if (flag ==0){
                hintetext.setVisibility(View.VISIBLE);
                tableFixHeaders.setVisibility(View.GONE);
            }
            else {
                hintetext.setVisibility(View.GONE);
                tableFixHeaders.setVisibility(View.VISIBLE);


            }
            pd.dismiss();
            String amountt = String.format("%.2f",totalAmount);
            tamount.setText("" + amountt);
            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(Purchase.this)
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

            tableFixHeaders.setAdapter(new MyAdapter(Purchase.this, tableHeaders, listPurchase));

        }
    }

    @Override
    public void onBackPressed() {

    }
}

