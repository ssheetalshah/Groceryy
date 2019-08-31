package com.aryanonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanonline.util.AppPreference;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.Session_management;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    private static String TAG = RegisterActivity.class.getSimpleName();

    private EditText et_phone, et_name, et_password, et_email, et_last_name1;
    private Button btn_register;
    private TextView tv_phone, tv_name, tv_password, tv_email;
    String Selected_product;
    String getphone, getname, getpassword, getemail, getKyc, getAccountNum, getIfsc, getBankName, getSponserId, getLast;
    Session_management sessionManagement;
    String firstname = "", telephone = "",customer_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(com.aryanonline.R.layout.activity_register);

        sessionManagement = new Session_management(RegisterActivity.this);

        et_phone = (EditText) findViewById(com.aryanonline.R.id.et_reg_phone);
        et_name = (EditText) findViewById(com.aryanonline.R.id.et_reg_name);
        et_last_name1 = (EditText) findViewById(com.aryanonline.R.id.et_last_name1);
        et_password = (EditText) findViewById(com.aryanonline.R.id.et_reg_password);
        et_email = (EditText) findViewById(com.aryanonline.R.id.et_reg_email);
        tv_password = (TextView) findViewById(com.aryanonline.R.id.tv_reg_password);
        tv_phone = (TextView) findViewById(com.aryanonline.R.id.tv_reg_phone);
        tv_name = (TextView) findViewById(com.aryanonline.R.id.tv_reg_name);
        tv_email = (TextView) findViewById(com.aryanonline.R.id.tv_reg_email);
        btn_register = (Button) findViewById(com.aryanonline.R.id.btnRegister);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getphone = et_phone.getText().toString();
                getname = et_name.getText().toString();
                getLast = et_last_name1.getText().toString();
                getpassword = et_password.getText().toString();
                getemail = et_email.getText().toString();

                new SendJsonDataToServer().execute();

                // attemptRegister();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void attemptRegister() {

        tv_phone.setText(getResources().getString(com.aryanonline.R.string.et_login_phone_hint));
        tv_email.setText(getResources().getString(com.aryanonline.R.string.tv_login_email));
        tv_name.setText(getResources().getString(com.aryanonline.R.string.tv_reg_name_hint));
        tv_password.setText(getResources().getString(com.aryanonline.R.string.tv_login_password));

        tv_name.setTextColor(getResources().getColor(com.aryanonline.R.color.color_3));
        tv_phone.setTextColor(getResources().getColor(com.aryanonline.R.color.color_3));
        tv_password.setTextColor(getResources().getColor(com.aryanonline.R.color.color_3));
        tv_email.setTextColor(getResources().getColor(com.aryanonline.R.color.color_3));

      /*  String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getpassword = et_password.getText().toString();
        String getemail = et_email.getText().toString();
*/

        getphone = et_phone.getText().toString();
        getname = et_name.getText().toString();
        getpassword = et_password.getText().toString();
        getemail = et_email.getText().toString();
       /* getKyc=et_kyc.getText().toString();
        getAccountNum=et_account_no.getText().toString();
        getBankName=et_bank_name.getText().toString();
        getIfsc=et_ifsc_code.getText().toString();
        getSponserId = et_sponser_id.getText().toString();*/
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {
            tv_phone.setTextColor(getResources().getColor(com.aryanonline.R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(com.aryanonline.R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(com.aryanonline.R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(com.aryanonline.R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpassword)) {
            tv_password.setTextColor(getResources().getColor(com.aryanonline.R.color.colorPrimary));
            focusView = et_password;
            cancel = true;
        } else if (!isPasswordValid(getpassword)) {
            tv_password.setText(getResources().getString(com.aryanonline.R.string.password_too_short));
            tv_password.setTextColor(getResources().getColor(com.aryanonline.R.color.colorPrimary));
            focusView = et_password;
            cancel = true;
        }

        if (TextUtils.isEmpty(getemail)) {
            tv_email.setTextColor(getResources().getColor(com.aryanonline.R.color.colorPrimary));
            focusView = et_email;
            cancel = true;
        } else if (!isEmailValid(getemail)) {
            tv_email.setText(getResources().getString(com.aryanonline.R.string.invalide_email_address));
            tv_email.setTextColor(getResources().getColor(com.aryanonline.R.color.colorPrimary));
            focusView = et_email;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                new SendJsonDataToServer().execute();
                // makeRegisterRequest(getname, getphone, getemail, getpassword);
            }
        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }


    //********************************************************


    class SendJsonDataToServer extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://enlightshopping.com/api/api/ragistration");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("firstname", getname);
                postDataParams.put("lastname", getLast);
                postDataParams.put("mobile", getphone);
                postDataParams.put("email", getemail);
                postDataParams.put("password", getpassword);

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
                    //String message = jsonObject.getString("message");
                    JSONObject msgObj = jsonObject.getJSONObject("massage");
                    //   user_phone = msgObj.getString("customer_id");
                     customer_id = msgObj.getString("customer_id");
                    String store_id = msgObj.getString("store_id");
                     firstname = msgObj.getString("firstname");
                    String lastname = msgObj.getString("lastname");
                    String email = msgObj.getString("email");
                     telephone = msgObj.getString("telephone");
                    String fax = msgObj.getString("fax");
                    String password = msgObj.getString("password");
                    String salt = msgObj.getString("salt");
                    String cart = msgObj.getString("cart");
                    String wishlist = msgObj.getString("wishlist");
                    String newsletter = msgObj.getString("newsletter");
                    String address_id = msgObj.getString("address_id");
                    String customer_group_id = msgObj.getString("customer_group_id");
                    String ip = msgObj.getString("ip");
                    String status = msgObj.getString("status");
                    String approved = msgObj.getString("approved");
                    String token = msgObj.getString("token");
                    String date_added = msgObj.getString("date_added");

                    Log.e(">>>>", jsonObject.toString() + " " + responce);

                    if (responce.equalsIgnoreCase("true")) {
                        AppPreference.setName(RegisterActivity.this, firstname);
                        AppPreference.setMobile(RegisterActivity.this, telephone);
                        AppPreference.setUserid(RegisterActivity.this, telephone);

                        sessionManagement.createLoginSession(customer_id, firstname, email, password);
                        et_name.setText("");
                        et_phone.setText("");
                        et_email.setText("");
                        et_phone.setText("");

                        Toast.makeText(RegisterActivity.this, responce, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
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


    /****************************************


     /**
     * Method to make json object request where json response starts wtih
     */
   /* private void makeRegisterRequest(String name, String mobile,
                                     String email, String password) {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_email", email);
        params.put("password", password);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.REGISTER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(RegisterActivity.this, "" + msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        String error = response.getString("error");
                        Toast.makeText(RegisterActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
*/
}
