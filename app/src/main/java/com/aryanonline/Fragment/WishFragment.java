package com.aryanonline.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanonline.Adapter.WishAdapter;
import com.aryanonline.MainActivity;
import com.aryanonline.Model.WishModel;
import com.aryanonline.R;
import com.aryanonline.util.AppPreference;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;


public class WishFragment extends Fragment {
    RecyclerView wishList_re;
    ArrayList<WishModel> wish_list;
    private WishAdapter wishAdapter;
    String server_url;

    public WishFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wish, container, false);

        wishList_re = (RecyclerView) view.findViewById(R.id.wishList_re);
        wish_list = new ArrayList<>();

        if (ConnectivityReceiver.isConnected()) {
            new GetWishlist().execute();
        }else {
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    //---------------------------------------

    class GetWishlist extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://enlightshopping.com/api/api/get_wishlist?user_id=" + AppPreference.getUserid(getActivity());
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
                    JSONArray data_array = obj.getJSONArray("data");
                    for (int i = 0; i < data_array.length(); i++) {
                        JSONObject c = data_array.getJSONObject(i);
                        String id = c.getString("id");
                        String user_id = c.getString("user_id");
                        String product_id = c.getString("product_id");
                        String product_name = c.getString("product_name");
                        String model = c.getString("model");
                        String stock = c.getString("stock");
                        String price = c.getString("price");
                        wish_list.add(new WishModel(id, user_id, product_id, product_name, model, stock, price));
                    }

                    wishAdapter = new WishAdapter(getActivity(), wish_list);
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ListPriceActivity.this, 2);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    wishList_re.setLayoutManager(mLayoutManager);
                    wishList_re.setItemAnimator(new DefaultItemAnimator());
                    wishList_re.setAdapter(wishAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }
}
