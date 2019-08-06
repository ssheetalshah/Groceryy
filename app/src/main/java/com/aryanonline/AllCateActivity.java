package com.aryanonline;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.aryanonline.Adapter.DetailsAdapter;
import com.aryanonline.Model.DetailsModel;
import com.aryanonline.Model.SubCatModel;
import com.aryanonline.Model.TopModel;
import com.aryanonline.util.DatabaseHandler;
import com.aryanonline.util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllCateActivity extends AppCompatActivity {
    RecyclerView getDetlist;
    String server_url;
    ArrayList<DetailsModel> det_list;
    private DatabaseHandler db;
    private DetailsAdapter detailsAdapter;
    String prId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cate);


        getDetlist = (RecyclerView)findViewById(R.id.getDetlist);

        Toast.makeText(this, "New activity", Toast.LENGTH_SHORT).show();
        det_list = new ArrayList<>();
        if (getIntent() != null) {
            SubCatModel subCatModel = (SubCatModel) getIntent().getSerializableExtra("SubCatModel");
            prId = subCatModel.getProductId();
//            provId = reviewsModel1.getProviderId();
//            prId = reviewsModel1.getId();

        }
        new GetDetails().execute();

    }

    //-------------------------------------------------

    public  class GetDetails extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(AllCateActivity.this);
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
                    db = new DatabaseHandler(AllCateActivity.this);

                    ArrayList<HashMap<String, String>> map = db.getCartAll();
                    detailsAdapter = new DetailsAdapter(AllCateActivity.this, det_list,map);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AllCateActivity.this);
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
