package com.aryanonline.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanonline.CompareActivity;
import com.aryanonline.Config.BaseURL;
import com.aryanonline.Model.DetailsModel;
import com.aryanonline.R;
import com.aryanonline.util.AppPreference;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.DatabaseHandler;
import com.aryanonline.util.Session_management;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private static final String TAG = "DetailsAdapter";
    private ArrayList<DetailsModel> DetList;
    ArrayList<HashMap<String, String>> list;
    public Context context;
    String resId = "";
    HashMap<Integer, DetailsModel> map = new HashMap<>();
    String finalStatus = "";
    DatabaseHandler dbHandler;
    String prId, prName, prModel, prStock, prPrice, prDescrption;

    private Session_management sessionManagement;
    String Image;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_title, tv_price, tv_total, tv_contetiy, tv_add, mrpPrice, tv_subcat_model;
        public ImageView iv_logo, iv_plus, iv_minus, iv_remove;
        ImageView wwish, wwishred, compare;
        LinearLayout mainButton;
        DatabaseHandler dbHandler;
        int pos;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
            tv_subcat_model = (TextView) view.findViewById(R.id.tv_subcat_model);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
            tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
            iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);
            mrpPrice = (TextView) view.findViewById(R.id.mrpPrice);
            mainButton = (LinearLayout) view.findViewById(R.id.mainButton);
            wwish = (ImageView) view.findViewById(R.id.wwish);
            wwishred = (ImageView) view.findViewById(R.id.wwishred);
            compare = (ImageView) view.findViewById(R.id.compare);

            // card = (LinearLayout) view.findViewById(R.id.card_view);
        }

    }

    public static Context mContext;


    public DetailsAdapter(Context mContext, ArrayList<DetailsModel> det_list, ArrayList<HashMap<String, String>> list) {
        context = mContext;
        this.DetList = det_list;
        this.list = list;


        dbHandler = new DatabaseHandler(mContext);

    }

    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_row, parent, false);

        return new DetailsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DetailsAdapter.ViewHolder viewHolder, final int position) {
        DetailsModel detailsModel = DetList.get(position);

//        for (int p=0;p<DetList.size();p++) {
//            map.put(p,DetList.get(p).getDescription());
//
//        }
//        final HashMap<String, String> map = list.get(position);

        //    final Lis map = DetList.get(position);
        viewHolder.tv_title.setText(detailsModel.getProductname());
        viewHolder.tv_price.setText(detailsModel.getStockStatus());
        viewHolder.tv_subcat_model.setText(detailsModel.getModel());
        String s = detailsModel.getPrice();
        Image = detailsModel.getImage();
        String ss = s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
        viewHolder.tv_total.setText(ss);
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + Image)
                .placeholder(R.drawable.aplogo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(viewHolder.iv_logo);

        viewHolder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(viewHolder.tv_contetiy.getText().toString());
                qty = qty + 1;

                viewHolder.tv_contetiy.setText(String.valueOf(qty));

//
//                dbHandler.setCart(map, Float.valueOf(viewHolder.tv_contetiy.getText().toString()));
//                Toast.makeText(context, "at cart value "+ dbHandler.setCart(map, Float.valueOf(viewHolder.tv_contetiy.getText().toString())), Toast.LENGTH_SHORT).show();
//                Double items = Double.parseDouble(dbHandler.getInCartItemQty(map.get("product_id")));
//                Double price = Double.parseDouble(map.get("price"));
//
//                viewHolder.tv_total.setText("" + price * items );
                //holder.tv_total.setText(activity.getResources().getString(R.string.tv_cart_total) + price * items + " " +activity.getResources().getString(R.string.currency));
                updateintent();
            }
        });

        viewHolder.wwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.wwish.setVisibility(View.GONE);
                viewHolder.wwishred.setVisibility(View.VISIBLE);

                if (ConnectivityReceiver.isConnected()) {
                    new SendJsonData().execute();
                } else {
                    Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.wwishred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.wwishred.setVisibility(View.GONE);
                viewHolder.wwish.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = 1;
                if (!viewHolder.tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(viewHolder.tv_contetiy.getText().toString());

                if (qty > 1) {
                    qty = qty - 1;
                    viewHolder.tv_contetiy.setText(String.valueOf(qty));
                }
            }
        });

        viewHolder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.valueOf(viewHolder.tv_contetiy.getText().toString());
//                qty = qty + 1;
                for (int p = 0; p < DetList.size(); p++) {
                    map.put(p, new DetailsModel(DetList.get(position).getProductId(), DetList.get(position).getModel(), DetList.get(position).getQuantity(), DetList.get(position).getImage(), DetList.get(position).getStockStatus(), DetList.get(position).getPrice(), DetList.get(position).getProductname(), DetList.get(position).getDescription()));

                }
                viewHolder.tv_contetiy.setText(String.valueOf(qty));

                dbHandler.setCartSimple(map, Float.valueOf(viewHolder.tv_contetiy.getText().toString()), position);
                // Toast.makeText(context, "at cart value "+ dbHandler.setCartSimple(map, Float.valueOf(viewHolder.tv_contetiy.getText().toString()) ,position), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show();
                viewHolder.tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
               /* Double items = Double.parseDouble(dbHandler.getInCartItemQty(map.get("product_id").toString()));
                Double price = Double.parseDouble(map.get("price").toString());*/

                //   viewHolder.tv_total.setText("" + price * items );
            }
        });

        viewHolder.compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendAddCompare().execute();
            }
        });

        prId = detailsModel.getProductId();
        prName = detailsModel.getProductname();
        prModel = detailsModel.getModel();
        prStock = detailsModel.getStockStatus();
        prPrice = detailsModel.getPrice();
        prDescrption = detailsModel.getDescription();

        viewHolder.pos = position;

    }

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
    }

    @Override
    public int getItemCount() {
        return DetList.size();
    }

    //------------------------------------

    class SendJsonData extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://enlightshopping.com/api/api/addwishlist");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", AppPreference.getUserid(context));
                postDataParams.put("product_id", prId);
                postDataParams.put("product_name", prName);
                postDataParams.put("model", prModel);
                postDataParams.put("stock", prStock);
                postDataParams.put("price", prPrice);


                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    /*BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString(); */

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                // JSONObject jsonObject = null;
                Log.e("SendJsonDataToServer>>>", result.toString());
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String responce = jsonObject.getString("responce");
                    if (responce.equalsIgnoreCase("true")) {
                        JSONObject massageobject = jsonObject.getJSONObject("massage");
                        String id = massageobject.getString("id");
                        String user_id = massageobject.getString("user_id");
                        String product_id = massageobject.getString("product_id");
                        String product_name = massageobject.getString("product_name");
                        String model = massageobject.getString("model");
                        String stock = massageobject.getString("stock");
                        String price = massageobject.getString("price");
                        Toast.makeText(context, "Product in wishlist.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Not in wishlist.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }
    }

    //--------------------------------------------------------

    class SendAddCompare extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://enlightshopping.com/api/api/addcompare");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", AppPreference.getUserid(context));
                postDataParams.put("product_id", prId);
                postDataParams.put("product_name", prName);
                postDataParams.put("model", prModel);
                postDataParams.put("price", prPrice);
                postDataParams.put("description", prDescrption);


                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    /*BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString(); */

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                // JSONObject jsonObject = null;
                Log.e("SendJsonDataToServer>>>", result.toString());
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String responce = jsonObject.getString("responce");
                    if (responce.equalsIgnoreCase("true")) {
                        JSONObject massageobject = jsonObject.getJSONObject("massage");
                        String id = massageobject.getString("id");
                        String user_id = massageobject.getString("user_id");
                        String product_id = massageobject.getString("product_id");
                        String product_name = massageobject.getString("product_name");
                        String model = massageobject.getString("model");
                        String price = massageobject.getString("price");
                        String description = massageobject.getString("description");
                        Toast.makeText(context, "Product in Compare", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, CompareActivity.class);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Not in Compare.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }


    }


}
