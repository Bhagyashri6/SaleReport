package com.atminfotech.atmsales;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.vistrav.ask.Ask;

public class HomeActivity extends AppCompatActivity {
    private LinearLayout saleslayout, agewiselayout, orderlayout, purchaseLayout, stocklayout, banklyout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Ask.on(this)
                .forPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.CALL_PHONE
                        )
                .withRationales("Your need to allow permission to use this app.",
                        "In order to save file you will need to grant storage permission") //optional
                .go();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        saleslayout = (LinearLayout) findViewById(R.id.saleslayout);
        agewiselayout = (LinearLayout) findViewById(R.id.agewiselayout);
        orderlayout = (LinearLayout) findViewById(R.id.orderlayout);
        purchaseLayout = (LinearLayout) findViewById(R.id.purchaseLayout);
        stocklayout = (LinearLayout) findViewById(R.id.stocklayout);
        banklyout = (LinearLayout) findViewById(R.id.banklyout);


        saleslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        agewiselayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AgeWiseActivity.class));
            }
        });
        orderlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Order.class));
            }
        });
        purchaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Purchase.class));
            }
        });
        stocklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, StockActivity.class));
            }
        });

        banklyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BankActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.close) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Exit").setTitle("Do you want Close ?").setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setIcon(R.drawable.power);

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}
