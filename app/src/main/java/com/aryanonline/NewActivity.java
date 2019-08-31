package com.aryanonline;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.aryanonline.Adapter.DetailsAdapter;
import com.aryanonline.Model.DetailsModel;
import com.aryanonline.Model.TopModel;
import com.aryanonline.util.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewActivity extends AppCompatActivity {
    RecyclerView getDetlist;
    String server_url;
    ArrayList<DetailsModel> det_list;
    private DatabaseHandler db;
    private DetailsAdapter detailsAdapter;
    String prId;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.aryanonline.R.layout.activity_new);

        activity = this;

        getDetlist = (RecyclerView)findViewById(com.aryanonline.R.id.getDetlist);

        Toast.makeText(this, "New activity", Toast.LENGTH_SHORT).show();
        det_list = new ArrayList<>();
        if (getIntent() != null) {
            TopModel topModel = (TopModel) getIntent().getSerializableExtra("TopModel");
            prId = topModel.getProductId();
//            provId = reviewsModel1.getProviderId();
//            prId = reviewsModel1.getId();

        }
      new GetDetails().execute();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
        Log.e("Attach Base Context","----------");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewActivity.this , MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
//-------------------------------------------------

    public  class GetDetails extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(NewActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "http://enlightshopping.com/api/api/getProductdetails?product_id="+prId;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            //   Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();
                    JSONObject obj = new JSONObject(output);
                    String responce = obj.getString("responce");
                    JSONArray Data_array = obj.getJSONArray("data");
                    for (int i = 0; i < Data_array.length(); i++) {
                        JSONObject c = Data_array.getJSONObject(i);
                        String product_id = c.getString("product_id");
                        String model = c.getString("model");
                        String quantity = c.getString("quantity");
                        String image = c.getString("image");
                        String stockStatus = c.getString("stockStatus");
                        String price = c.getString("price");
                        String productname = c.getString("productname");
                        String description = c.getString("description");
                        det_list.add(new DetailsModel(product_id, model, quantity, image, stockStatus, price,productname,description));
                    }
                    db = new DatabaseHandler(NewActivity.this);

                    ArrayList<HashMap<String, String>> map = db.getCartAll();
                    detailsAdapter = new DetailsAdapter(activity, det_list,map);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewActivity.this);
//                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    getDetlist.setLayoutManager(mLayoutManager);
                    getDetlist.setItemAnimator(new DefaultItemAnimator());
                    getDetlist.setAdapter(detailsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }

}
