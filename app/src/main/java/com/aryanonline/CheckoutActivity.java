package com.aryanonline;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanonline.Adapter.Cart_adapter;
import com.aryanonline.Adapter.MyAdapter;
import com.aryanonline.Adapter.NCardAdapter;
import com.aryanonline.Fragment.Add_delivery_address_fragment;
import com.aryanonline.Fragment.Delivery_fragment;
import com.aryanonline.Model.CountryModel;
import com.aryanonline.Model.DModel;
import com.aryanonline.Model.StateModel;
import com.aryanonline.Model.TopModel;
import com.aryanonline.util.AppPreference;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.DatabaseHandler;
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
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class CheckoutActivity extends AppCompatActivity {
    RadioButton radioRegAccount, radioGuestCheckout, radioDefault, radioFlatShipping, radioFreeShipping, radioCredit;
    EditText firstName, lastName, email, confirmEmail, telephone, fax, password, confirmPassword, company, companyId, address, city,
            postCode, useCouponCode, useGiftVoucher, addComments;
    Spinner spinCountry, spinState;
    RecyclerView productDetails;
    ImageView btnRight, btRight1;
    TextView coupCode, coupAmt, subAmt, rewardCode, rewardAmt, flatShippAmt, totalAmt;
    CheckBox termsCondi;
    Button btnConfirmOrder;
    String sever_url;
    String AddressId, CustomerId, Firstname, Lastname, Company, CompanyId, TaxId, Address1, Address2, City, Postcode, CountryId, ZoneId;

    private ArrayList<CountryModel> contryList = new ArrayList<>();
    private ArrayList<String> strContryList = new ArrayList<>();
    private ArrayAdapter<String> countryAdapter;

    private ArrayList<StateModel> stateList = new ArrayList<>();
    private ArrayList<String> strStateList = new ArrayList<>();
    private ArrayAdapter<String> stateAdapter;

    public String product_id,name,model,quantity,total,tax,price;
    //fdf
    String Spin_Country;
    String Spin_state;
    String strConId;
    String strStateId;

    private DatabaseHandler db;
    private TextView tv_clear, tv_total, tv_item;
    Context context = CheckoutActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //-------------------------
        radioRegAccount = (RadioButton) findViewById(R.id.radioRegAccount);
        radioGuestCheckout = (RadioButton) findViewById(R.id.radioGuestCheckout);
        radioDefault = (RadioButton) findViewById(R.id.radioDefault);
        radioFlatShipping = (RadioButton) findViewById(R.id.radioFlatShipping);
        radioFreeShipping = (RadioButton) findViewById(R.id.radioFreeShipping);
        radioCredit = (RadioButton) findViewById(R.id.radioCredit);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        confirmEmail = (EditText) findViewById(R.id.confirmEmail);
        telephone = (EditText) findViewById(R.id.telephone);
        fax = (EditText) findViewById(R.id.fax);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        company = (EditText) findViewById(R.id.company);
        companyId = (EditText) findViewById(R.id.companyId);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        postCode = (EditText) findViewById(R.id.postCode);
        useCouponCode = (EditText) findViewById(R.id.useCouponCode);
        useGiftVoucher = (EditText) findViewById(R.id.useGiftVoucher);
        addComments = (EditText) findViewById(R.id.addComments);

        spinCountry = (Spinner) findViewById(R.id.spinCountry);
        spinState = (Spinner) findViewById(R.id.spinState);

        productDetails = (RecyclerView) findViewById(R.id.productDetails);

        btnRight = (ImageView) findViewById(R.id.btnRight);
        btRight1 = (ImageView) findViewById(R.id.btRight1);

        coupCode = (TextView) findViewById(R.id.coupCode);
        coupCode.setText(useCouponCode.getText().toString());
        coupAmt = (TextView) findViewById(R.id.coupAmt);
        subAmt = (TextView) findViewById(R.id.subAmt);
        rewardCode = (TextView) findViewById(R.id.rewardCode);
        rewardAmt = (TextView) findViewById(R.id.rewardAmt);
        flatShippAmt = (TextView) findViewById(R.id.flatShippAmt);
        totalAmt = (TextView) findViewById(R.id.totalAmt);

        termsCondi = (CheckBox) findViewById(R.id.termsCondi);
//        tv_total = (TextView) findViewById(R.id.tv_cart_total);
//        tv_item = (TextView) findViewById(R.id.tv_cart_item);

        btnConfirmOrder = (Button) findViewById(R.id.btnConfirmOrder);

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddCheckout().execute();
            }
        });
        if (getIntent() != null) {
            DModel dModel = (DModel) getIntent().getSerializableExtra("DModel");
            AddressId = dModel.getAddressId();
            CustomerId = dModel.getCustomerId();
            Firstname = dModel.getFirstname();
            Lastname = dModel.getLastname();
            Company = dModel.getCompany();
            CompanyId = dModel.getCompanyId();
            TaxId = dModel.getTaxId();
            Address1 = dModel.getAddress1();
            Address2 = dModel.getAddress2();
            City = dModel.getCity();
            Postcode = dModel.getPostcode();
            CountryId = dModel.getCountryId();
            ZoneId = dModel.getZoneId();

        }

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {
                    new AddCouponCode().execute();
                } else {
                    Toast.makeText(CheckoutActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        productDetails.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this));

        db = new DatabaseHandler(CheckoutActivity.this);

        ArrayList<HashMap<String, String>> map = db.getCartAll();
        for(int i=0;i<map.size();i++)
        {
            if(i==0) {
                product_id = map.get(i).get("product_id").toString();
                name =  map.get(i).get("product_name").toString();
                quantity = map.get(i).get("qty").toString();
                model = map.get(i).get("title").toString();

                total =  map.get(i).get("price").toString();
                 price = String.valueOf(Integer.valueOf(total)/Integer.valueOf(quantity) );
//                product_id.add(map.get(i).get("product_id").toString());
//                name.add(map.get(i).get("product_name").toString());
//                quantity.add(map.get(i).get("qty").toString());
//                product_id.add(map.get(i).get("product_name").toString());
            //    dsfsd
            }else {
                if(i<map.size()-1) {
                    product_id = product_id + map.get(i).get("product_id").toString();
                    name = name  + map.get(i).get("product_name").toString();
                    quantity = quantity  + map.get(i).get("qty").toString();
                    model = model + map.get(i).get("category_id").toString();
                    //  int price =
                    total = total  + map.get(i).get("price").toString();
                    price = price  + String.valueOf(Integer.valueOf(total) / Integer.valueOf(quantity));
                }else {
                    product_id = product_id + "," + map.get(i).get("product_id").toString();
                    name = name + "," + map.get(i).get("product_name").toString();
                    quantity = quantity + "," + map.get(i).get("qty").toString();
                    model = model + "," + map.get(i).get("category_id").toString();
                    //  int price =
                    total = total + "," + map.get(i).get("price").toString();
                    try {
                        price = price + "," + String.valueOf(Integer.valueOf(total) / Integer.valueOf(quantity));
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                
            }
            //dgdf
            Log.e("element1 is" , ""+map.get(i).get("product_name"));
            Log.e("element2 is" , ""+map.get(i).get("category_id"));
            Log.e("element3 is" , ""+map.get(i).get("price"));
            Log.e("element4 is" , ""+map.get(i).get("title"));
            Log.e("element5 is" , ""+map.get(i).get("product_id"));
        }


        NCardAdapter adapter = new NCardAdapter(CheckoutActivity.this, map);
        productDetails.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        updateData();

        if (ConnectivityReceiver.isConnected()) {
            new GetCountry().execute();
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        //-------------------------------------

        spinCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Spin_Country = countryAdapter.getItem(position).toString();
                if (!Spin_Country.equals("Select Country")) {
                    Log.e("Spin_Country", Spin_Country);
                    if (strContryList.contains(Spin_Country)) {
                        strConId = contryList.get(position).getCountryId().toString();
                        Log.e("strConId", strConId);
                        new GetState().execute();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Spin_state = stateAdapter.getItem(position).toString();
                if (strStateList.contains(Spin_state)) {
                    strStateId = stateList.get(position).getZoneId().toString();
                    Log.e("strStateId", strStateId);
                    //   new GetState().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //--------------------------------------------

   /* private void updateData() {
        tv_total.setText("" + db.getTotalAmount());
      //  tv_item.setText("" + db.getCartCount());
        ((MainActivity) context).setCartCounter("" + db.getCartCount());
    }*/

    //--------------------------------------------

    class GetCountry extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(CheckoutActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            sever_url = "https://enlightshopping.com/api/api/get_country";

            output = com.aryanonline.util.HttpHandler.makeServiceCall(sever_url);
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
                    strContryList.add("Select Country");
                    contryList.add(new CountryModel("Select", "", "", "", "", "", ""));
                    JSONObject object = new JSONObject(output);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String country_id = obj.getString("country_id");
                        String name = obj.getString("name");
                        String iso_code_2 = obj.getString("iso_code_2");
                        String iso_code_3 = obj.getString("iso_code_3");
                        String address_format = obj.getString("address_format");
                        String postcode_required = obj.getString("postcode_required");
                        String status = obj.getString("status");
                        strContryList.add(name);
                        contryList.add(new CountryModel(country_id, name, iso_code_2, iso_code_3, address_format, postcode_required, status));


                    }
                    countryAdapter = new ArrayAdapter<String>(CheckoutActivity.this, R.layout.spinner_row, strContryList);
                    countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinCountry.setAdapter(countryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }

                super.onPostExecute(output);
            }
        }
    }

    //--------------------------------------------

    class GetState extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(CheckoutActivity.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            sever_url = "https://enlightshopping.com/api/api/get_state?country_id=" + strConId;

            output = HttpHandler.makeServiceCall(sever_url);
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
                    //  strStateList.add("Select State");
                    stateList.add(new StateModel("Select", "", "", "", ""));
                    JSONObject object = new JSONObject(output);
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String zone_id = obj.getString("zone_id");
                        String country_id = obj.getString("country_id");
                        String name = obj.getString("name");
                        String code = obj.getString("code");
                        String status = obj.getString("status");
                        strStateList.add(name);
                        stateList.add(new StateModel(zone_id, country_id, name, code, status));
                    }

                    stateAdapter = new ArrayAdapter<String>(CheckoutActivity.this, R.layout.spinner_row, strStateList);
                    stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinState.setAdapter(stateAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }

                super.onPostExecute(output);
            }
        }
    }

    //----------------------------------------

    private class AddCouponCode extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(CheckoutActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://enlightshopping.com/api/api/applaycoupon");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("code", useCouponCode.getText().toString());
                postDataParams.put("order_id", "0001");
                postDataParams.put("customer_id", CustomerId);
                postDataParams.put("amount", "3000");

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
                    String massage = jsonObject.getString("massage");

                    if (responce.equals("true")) {
                        Toast.makeText(CheckoutActivity.this, massage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CheckoutActivity.this, massage, Toast.LENGTH_SHORT).show();
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

    //-----------------------------------------------------------

    private class AddCheckout extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(CheckoutActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://enlightshopping.com/api/api/add_order");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("customer_id", CustomerId);
                postDataParams.put("firstname", firstName.getText().toString());
                postDataParams.put("lastname", lastName.getText().toString());
                postDataParams.put("email", email.getText().toString());
                postDataParams.put("telephone", telephone.getText().toString());
                postDataParams.put("fax", fax.getText().toString());
                postDataParams.put("payment_firstname", firstName.getText().toString());
                postDataParams.put("payment_lastname", lastName.getText().toString());
                postDataParams.put("payment_company", company.getText().toString());
                postDataParams.put("payment_company_id", companyId.getText().toString());
                postDataParams.put("payment_tax_id","xid" );
                postDataParams.put("payment_address_1", address.getText().toString());
                postDataParams.put("payment_address_2", address.getText().toString());
                postDataParams.put("payment_city", city.getText().toString());
                postDataParams.put("payment_postcode", postCode.getText().toString());
                postDataParams.put("payment_country", spinCountry.getSelectedItemId());
                postDataParams.put("payment_country_id", spinCountry);
                postDataParams.put("payment_zone", spinState.getSelectedItemId());
                postDataParams.put("payment_zone_id", strStateId);
                postDataParams.put("payment_address_format", "Cash");
                postDataParams.put("payment_method", "Cash");
                postDataParams.put("payment_code", "INR");
                postDataParams.put("shipping_firstname", "sf");
                postDataParams.put("shipping_lastname", "sdf");
                postDataParams.put("shipping_company", "sdf");
                postDataParams.put("shipping_address_1", "sdf");
                postDataParams.put("shipping_address_2", "sdf");
                postDataParams.put("shipping_city", "sfd");
                postDataParams.put("shipping_postcode", "sfd");
                postDataParams.put("shipping_country", "fsd");
                postDataParams.put("shipping_country_id", "fsd");
                postDataParams.put("shipping_zone", "fsd");
                postDataParams.put("shipping_zone_id", "sdf");
                postDataParams.put("shipping_address_format", "sfd");
                postDataParams.put("shipping_method", "flying");
                postDataParams.put("shipping_code", "001");
                postDataParams.put("comment", "terdggdi ");
                postDataParams.put("total", total);
                postDataParams.put("order_status_id", "200");
                postDataParams.put("affiliate_id", "gsdg");
                postDataParams.put("commission", "fsd");
                postDataParams.put("language_id", "fsd");
                postDataParams.put("currency_id", "fsd");
                postDataParams.put("currency_code", "fsd");
                postDataParams.put("currency_value", "fsd");
                postDataParams.put("ip", "100.5.8.10");
                postDataParams.put("forwarded_ip", "102.43.4.56");
                postDataParams.put("user_agent", "vinod");
                postDataParams.put("accept_language", "Englishwa");
                postDataParams.put("date_added", "1996-10-6");
                postDataParams.put("user_agent", "vinowa");
                postDataParams.put("accept_language", "jijio");
                postDataParams.put("date_added", "2002-10-4");
                postDataParams.put("date_modified", "fdssdf");
                //-----------multiproduct paramiters------------
                postDataParams.put("product_id", product_id);
                postDataParams.put("name", name);

                postDataParams.put("model", model);
                postDataParams.put("quantity", quantity);
                postDataParams.put("price", total);
                postDataParams.put("total", total);

                postDataParams.put("tax", "taxwa");

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
                    String massage = jsonObject.getString("massage");

                    if (responce.equals("true")) {
                        Toast.makeText(CheckoutActivity.this, massage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CheckoutActivity.this, massage, Toast.LENGTH_SHORT).show();
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
