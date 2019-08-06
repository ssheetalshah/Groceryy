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
import android.widget.Toast;

import com.aryanonline.Adapter.CompareAdapter;
import com.aryanonline.Adapter.TopViewAdapter;
import com.aryanonline.Model.CompareModel;
import com.aryanonline.Model.TopModel;
import com.aryanonline.util.AppPreference;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompareActivity extends AppCompatActivity {
    RecyclerView compareList;
    String server_url;
    ArrayList<CompareModel> comp_list;
    private CompareAdapter compareAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        compareList = (RecyclerView) findViewById(R.id.compareList);

        comp_list = new ArrayList<>();

        if (ConnectivityReceiver.isConnected()) {
            new GetCompare().execute();
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    //-------------------------------------

    class GetCompare extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(CompareActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://enlightshopping.com/api/api/get_compare?user_id="+ AppPreference.getUserid(CompareActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
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
                        String id = c.getString("id");
                        String user_id = c.getString("user_id");
                        String product_id = c.getString("product_id");
                        String product_name = c.getString("product_name");
                        String model = c.getString("model");
                        String price = c.getString("price");
                        String description = c.getString("description");
                        String image = c.getString("image");
                        comp_list.add(new CompareModel(id, user_id, product_id, product_name, model, price,description,image));
                    }

                    compareAdapter = new CompareAdapter(CompareActivity.this, comp_list);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(CompareActivity.this, 2);
                    //LinearLayoutManager mLayoutManager = new LinearLayoutManager(CompareActivity.this, LinearLayoutManager.VERTICAL, false);
                    compareList.setLayoutManager(gridLayoutManager);
                    compareList.setItemAnimator(new DefaultItemAnimator());
                    compareList.setAdapter(compareAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }
}
