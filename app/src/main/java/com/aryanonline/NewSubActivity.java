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

import com.aryanonline.Adapter.NewSubAdapter;
import com.aryanonline.Adapter.TopViewAdapter;
import com.aryanonline.Model.NewCategory;
import com.aryanonline.Model.SubCatModel;
import com.aryanonline.Model.TopModel;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewSubActivity extends AppCompatActivity {
    RecyclerView subCateList;
    String server_url;
    ArrayList<SubCatModel> subC_list;
    private NewSubAdapter newSubAdapter;
    String prId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sub);

        if (getIntent() != null) {
            NewCategory newCategory = (NewCategory) getIntent().getSerializableExtra("NewCategory");
            prId = newCategory.getCategoryId();
//            provId = reviewsModel1.getProviderId();
//            prId = reviewsModel1.getId();

        }

        subCateList = (RecyclerView)findViewById(R.id.subCateList);

        subC_list = new ArrayList<>();

        if (ConnectivityReceiver.isConnected()) {
            new GetSublist().execute();
        }
    }

    class GetSublist extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(NewSubActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://enlightshopping.com/api/api/getproduct_category?category_id="+prId;
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
                        subC_list.add(new SubCatModel(product_id, model, quantity, image, stockStatus, price,productname,description));
                    }

                    newSubAdapter = new NewSubAdapter(NewSubActivity.this, subC_list);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(NewSubActivity.this, 2);
//                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(NewSubActivity.this, LinearLayoutManager.VERTICAL, false);
                    subCateList.setLayoutManager(gridLayoutManager);
                    subCateList.setItemAnimator(new DefaultItemAnimator());
                   /* subCateList.addItemDecoration(new DividerItemDecoration(NewSubActivity.this,
                            DividerItemDecoration.VERTICAL));*/
                    subCateList.setAdapter(newSubAdapter);
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
