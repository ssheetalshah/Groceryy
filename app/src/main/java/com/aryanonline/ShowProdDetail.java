package com.aryanonline;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowProdDetail extends AppCompatActivity {

    Button pro_emi;
    ImageView pro_warranty,pro_offer;
    TextView tv_waranty;
    CardView card_offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prod_detail);

        getSupportActionBar().setTitle("Product Detail");

        pro_emi=findViewById(R.id.pro_emi);
        pro_warranty=findViewById(R.id.pro_warranty);
        pro_offer=findViewById(R.id.pro_offer);
        tv_waranty=findViewById(R.id.tv_waranty);
        card_offer=findViewById(R.id.card_offer);

        pro_emi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ShowProdDetail.this,Emi_Activity.class);
                startActivity(intent);
            }
        });

        pro_warranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            tv_waranty.setVisibility(View.VISIBLE);


            }
        });

        pro_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                card_offer.setVisibility(View.VISIBLE);


            }
        });

    }
}
