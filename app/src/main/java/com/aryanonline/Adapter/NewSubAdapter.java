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

import com.aryanonline.AllCateActivity;
import com.aryanonline.Config.BaseURL;
import com.aryanonline.Model.SubCatModel;
import com.aryanonline.util.AppPreference;
import com.aryanonline.util.ConnectivityReceiver;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class NewSubAdapter extends RecyclerView.Adapter<NewSubAdapter.ViewHolder> {

    private static final String TAG = "NewSubAdapter";
    private ArrayList<SubCatModel> newSubList;
    public Context context;
    String resId = "";
    String finalStatus = "";
    String Image;
    String prId, prName, prModel, prStock, prPrice;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView idProductprice;
        LinearLayout card, prodd;
        ImageView imagSub, wish, wishred;
        int pos;

        public ViewHolder(View view) {
            super(view);

            idProductprice = (TextView) view.findViewById(com.aryanonline.R.id.idProductprice);
            imagSub = (ImageView) view.findViewById(com.aryanonline.R.id.imagSub);
            wish = (ImageView) view.findViewById(com.aryanonline.R.id.wish);
            wishred = (ImageView) view.findViewById(com.aryanonline.R.id.wishred);
            prodd = (LinearLayout) view.findViewById(com.aryanonline.R.id.prodd);
        }
    }

    public static Context mContext;

    public NewSubAdapter(Context mContext, ArrayList<SubCatModel> subC_list) {
        context = mContext;
        newSubList = subC_list;

    }

    @Override
    public NewSubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(com.aryanonline.R.layout.new_sub_cat, parent, false);

        return new NewSubAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewSubAdapter.ViewHolder viewHolder, final int position) {
        SubCatModel subCatModel = newSubList.get(position);
        // viewHolder.idProductprice.setText(subCatModel.getPrice());

        String s = subCatModel.getPrice();
        String ss = s.indexOf(".") < 0 ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
        viewHolder.idProductprice.setText(ss);
        Image = subCatModel.getImage();
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + Image)
                .placeholder(com.aryanonline.R.drawable.aplogo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(viewHolder.imagSub);

        viewHolder.prodd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubCatModel subCatModel = newSubList.get(position);
                Intent intent = new Intent(context, AllCateActivity.class);
                intent.putExtra("SubCatModel", subCatModel);
                context.startActivity(intent);
            }
        });

        viewHolder.wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.wish.setVisibility(View.GONE);
                viewHolder.wishred.setVisibility(View.VISIBLE);

                if (ConnectivityReceiver.isConnected()) {
                    new SendJsonData().execute();
                }
               /* SubCatModel subCatModel1 = newSubList.get(position);
                Intent intent = new Intent(context, NewActivity.class);
                intent.putExtra("NewCategory", newCategory);
                context.startActivity(intent);
                ((Activity)context).finish();*/
            }
        });
        viewHolder.wishred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.wishred.setVisibility(View.GONE);
                viewHolder.wish.setVisibility(View.VISIBLE);
            }
        });

        prId = subCatModel.getProductId();
        prName = subCatModel.getProductname();
        prModel = subCatModel.getModel();
        prStock = subCatModel.getStockStatus();
        prPrice = subCatModel.getPrice();
        viewHolder.pos = position;

    }

    @Override
    public int getItemCount() {
        return newSubList.size();
    }

    //------------------------------------------------

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


}
