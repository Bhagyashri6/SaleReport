package com.atminfotech.atmsales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Locale;

public class OrderDetail extends AppCompatActivity {

    TableFixHeaders tableFixHeaders;
    private TextView data,title,total,titlenew,hintetext;
    private ImageView back;

    private String[] tableHeaders = {"Product Name", "Quantity", "Rate","Amount"};

    private static final String URL = "http://atm-india.in/service.asmx";
    private static final String NAMESPACE = "http://tempuri.org/";

    private static final String Brand_Soap_ACTION = "http://tempuri.org/AtmOrderProductDetail";
    // specifies the action
    private static final String METHOD_NAME = "AtmOrderProductDetail";

    private boolean serverissue=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);


        title=(TextView)findViewById(R.id.title);
        total=(TextView)findViewById(R.id.total);
        hintetext=(TextView)findViewById(R.id.hintetext);
        tableFixHeaders =(TableFixHeaders)findViewById(R.id.table);


        Bundle bundle = getIntent().getExtras();
        String dd =bundle.getString("Bill");
        new AsyncOrderDetails(dd).execute();
        title.setText(dd);



        titlenew=(TextView)findViewById(R.id.titlenew);
        titlenew.setText("Sales Details");
        back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    public class MyAdapter extends SampleTableAdapterBlank {

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
                    return widthXLarge;
                case 0:
                    return widthXSmall;
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
            } else if (column == -1) {
                return 1;
            }else if (column == 2) {
                return 2;
            }
            else if (column == 1) {
                return 2;
            }
            return 2;

        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }
    }

    public class AsyncOrderDetails extends AsyncTask<Void,Void,Void> {
ArrayList<ModelStockReport> orderlistData = new ArrayList<>();
        ModelStockReport model;
        ProgressDialog pd = new ProgressDialog(OrderDetail.this);
        String datee, City;
        double totalAmount = 0.00, totalNetAmount = 0.00;

        int flag=0;

        public AsyncOrderDetails(String dd) {
            datee=dd;
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

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("billno",datee);
                //  request.addProperty("company", City);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60 * 10000);
                androidHttpTransport.call(Brand_Soap_ACTION, envelope);

                SoapObject object = (SoapObject) envelope.getResponse();

                if (object.getPropertyCount() > 0) {
                    flag=1;
                    for (int i = 0; i < object.getPropertyCount(); i++) {
                        SoapObject innerResponse = (SoapObject) object.getProperty(i);

                        orderlistData.add(new ModelStockReport(
                                innerResponse.getProperty("S1").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S1").toString(),
                                innerResponse.getProperty("S2").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S2").toString(),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S3").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S3").toString())),
                                String.format("%.2f", Double.valueOf(innerResponse.getProperty("S4").toString().contentEquals("anyType{}") ? " " : innerResponse.getProperty("S4").toString())),
                               "",
                                "", "", 0.00));

                        totalAmount += Double.valueOf(innerResponse.getProperty("S4").toString().contentEquals("anyType{}") ? "00.00" : innerResponse.getProperty("S4").toString());


                    }
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
            if(flag==0)
            {
                hintetext.setVisibility(View.VISIBLE);
                tableFixHeaders.setVisibility(View.GONE);
            }
            else {
                hintetext.setVisibility(View.GONE);
                tableFixHeaders.setVisibility(View.VISIBLE);
            }
            total.setText(String.format(Locale.US,"%.2f",totalAmount));
            pd.dismiss();

            super.onPostExecute(aVoid);
            if (serverissue) {
                MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(OrderDetail.this)
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

            tableFixHeaders.setAdapter(new MyAdapter(OrderDetail.this, tableHeaders, orderlistData));

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
