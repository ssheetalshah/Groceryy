package com.aryanonline;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.aryanonline.Adapter.TopAdapter;
import com.aryanonline.Adapter.TopViewAdapter;
import com.aryanonline.Model.TopModel;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewDeatilsActivity extends AppCompatActivity {
    String server_url;
    RecyclerView rv_top;
    ArrayList<TopModel> top_list;
    private TopViewAdapter topViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deatils);

        rv_top = (RecyclerView)findViewById(R.id.rv_top);

        top_list = new ArrayList<>();

        if (ConnectivityReceiver.isConnected()) {
          new GetToplist().execute();
        }

    }

    class GetToplist extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ViewDeatilsActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://enlightshopping.com/api/api/getProduct";
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
                        String productname = c.getString("productname");
                        String quantity = c.getString("quantity");
                        String image = c.getString("image");
                        String stockStatus = c.getString("stockStatus");
                        String price = c.getString("price");
                        top_list.add(new TopModel(product_id, productname, quantity, image, stockStatus, price));
                    }

                    topViewAdapter = new TopViewAdapter(ViewDeatilsActivity.this, top_list);
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ViewDeatilsActivity.this, 2);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ViewDeatilsActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_top.setLayoutManager(mLayoutManager);
                    rv_top.setItemAnimator(new DefaultItemAnimator());
                    rv_top.addItemDecoration(new DividerItemDecoration(ViewDeatilsActivity.this,
                            DividerItemDecoration.VERTICAL));
                    rv_top.setAdapter(topViewAdapter);
                  //  rv_top.setNestedScrollingEnabled(false);
                    // adapter.setHasStableIds(new List(top_list.GetRange(0, 4)));


                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }
}
