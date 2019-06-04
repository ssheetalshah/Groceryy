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
import android.widget.Toast;

import com.aryanonline.Config.BaseURL;
import com.aryanonline.Model.Product_model;
import com.aryanonline.util.DatabaseHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;

public class ShowProdDetail extends AppCompatActivity {

    Button pro_emi;
    ImageView pro_warranty,pro_offer,prod_img;
    TextView tv_waranty,add_to_cart,tv_prod_price,tv_prod_desc;
    CardView card_offer;
    Product_model product_model;
    private DatabaseHandler dbcart;

    HashMap<String, String> map = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_prod_detail);

        getSupportActionBar().setTitle("Product Detail");

        dbcart = new DatabaseHandler(ShowProdDetail.this);

        pro_emi=findViewById(R.id.pro_emi);
        pro_warranty=findViewById(R.id.pro_warranty);
        pro_offer=findViewById(R.id.pro_offer);
        tv_waranty=findViewById(R.id.tv_waranty);
        card_offer=findViewById(R.id.card_offer);
        add_to_cart=findViewById(R.id.add_to_cart);
        prod_img=findViewById(R.id.prod_img);
        tv_prod_price=findViewById(R.id.tv_prod_price);
        tv_prod_desc=findViewById(R.id.tv_prod_desc);

        product_model=(Product_model)getIntent().getSerializableExtra("Product_Model");

            try {

                map.put("product_id", getIntent().getStringExtra("product_id"));
                map.put("category_id", getIntent().getStringExtra("category_id"));
                map.put("product_image", getIntent().getStringExtra("product_image"));
                map.put("increament", getIntent().getStringExtra("increament"));
                map.put("product_name", getIntent().getStringExtra("product_name"));
                map.put("price", getIntent().getStringExtra("price"));
                map.put("stock", getIntent().getStringExtra("stock"));
                map.put("title", getIntent().getStringExtra("title"));
                map.put("unit", getIntent().getStringExtra("unit"));
                map.put("Mrp", getIntent().getStringExtra("Mrp"));
                map.put("unit_value", getIntent().getStringExtra("unit_value"));
            }catch (Exception e){

            }
        Toast.makeText(this, "ppI "+map.get("product_id"), Toast.LENGTH_SHORT).show();
        tv_prod_price.setText("Rs. "+map.get("price"));
        tv_prod_desc.setText(map.get("product_name"));

        Glide.with(ShowProdDetail.this)
                .load(BaseURL.IMG_PRODUCT_URL + map.get("product_image"))
                .centerCrop()
                .placeholder(R.drawable.logoimg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(prod_img);



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

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ShowProdDetail.this, "Add to cart Successfully", Toast.LENGTH_SHORT).show();


                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf("1"));

                    } else {
                        dbcart.setCart(map, Float.valueOf("1"));
                    }

            }
        });



    }
}
