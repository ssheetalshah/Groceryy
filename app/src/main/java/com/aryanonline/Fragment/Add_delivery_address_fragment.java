package com.aryanonline.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

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
import java.util.Map;

import com.aryanonline.Config.BaseURL;
import com.aryanonline.LoginActivity;
import com.aryanonline.Model.CountryModel;
import com.aryanonline.Model.Delivery_address_model;
import com.aryanonline.AppController;
import com.aryanonline.MainActivity;
import com.aryanonline.Model.StateModel;
import com.aryanonline.R;
import com.aryanonline.util.AppPreference;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.CustomVolleyJsonRequest;
import com.aryanonline.util.HttpHandler;
import com.aryanonline.util.Session_management;

import javax.net.ssl.HttpsURLConnection;


public class Add_delivery_address_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Add_delivery_address_fragment.class.getSimpleName();

    private EditText address_1, address_2, et_add_adres_pin, city, postcode;
    // private EditText et_phone, et_name, et_pin, et_house,postcode;
    private Button btn_update;
    String City, Postcode;
    private TextView tv_phone, tv_name, tv_pin, tv_house, tv_socity, btn_socity;
    private String getsocity = "";
    Spinner spinCountry, spinState;


    private ArrayList<CountryModel> contryList = new ArrayList<>();
    private ArrayList<String> strContryList = new ArrayList<>();
    private ArrayAdapter<String> countryAdapter;

    private ArrayList<StateModel> stateList = new ArrayList<>();
    private ArrayList<String> strStateList = new ArrayList<>();
    private ArrayAdapter<String> stateAdapter;

    private Session_management sessionManagement;
    String sever_url;
    private boolean isEdit = false;

    private String getlocation_id;
    private String getphone;
    private String getname;
    private String getpin;
    private String gethouse;
    private ArrayList<Delivery_address_model> delivery_address_modelList = new ArrayList<>();
    String Spin_Country;
    String Spin_state;
    String strConId;
    String strStateId;


    public Add_delivery_address_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_delivery_address, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.add_delivery_address));

        sessionManagement = new Session_management(getActivity());

        address_1 = (EditText) view.findViewById(R.id.address_1);
        address_2 = (EditText) view.findViewById(R.id.address_2);
        postcode = view.findViewById(R.id.postcodeed);
        et_add_adres_pin = (EditText) view.findViewById(R.id.comp);
        city = (EditText) view.findViewById(R.id.city);
        spinCountry = (Spinner) view.findViewById(R.id.spinCountry);
        spinState = (Spinner) view.findViewById(R.id.spinState);
        city = (EditText) view.findViewById(R.id.city);
        btn_update = (Button) view.findViewById(R.id.btn_add_adres_edit);
        String getsocity_name = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
        String getsocity_id = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        Bundle args = getArguments();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                City = city.getText().toString();
                Postcode = postcode.getText().toString();

                if (ConnectivityReceiver.isConnected()) {
                    new Add_Deliovery_Address().execute();
                }
            }
        });

        if (ConnectivityReceiver.isConnected()) {
            new GetCountry().execute();
        }


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

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {
            attemptEditProfile();
        }
    }

    private void attemptEditProfile() {


    }


    private class Add_Deliovery_Address extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://enlightshopping.com/api/api/add_address");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("address_1", address_1.getText().toString());
                postDataParams.put("address_2", address_2.getText().toString());
                postDataParams.put("company", et_add_adres_pin.getText().toString());
                postDataParams.put("company_id", et_add_adres_pin.getText().toString());
                postDataParams.put("customer_id", AppPreference.getUserid(getActivity()));
                postDataParams.put("city", City);
                postDataParams.put("postcode", Postcode);
                postDataParams.put("country_id", strConId);
                postDataParams.put("state_id", strStateId);


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
                    if (responce.equals("true")) {
                        JSONObject massageObj = jsonObject.getJSONObject("massage");
                        String address_id = massageObj.getString("address_id");
                        String customer_id = massageObj.getString("customer_id");
                        String firstname = massageObj.getString("firstname");
                        String lastname = massageObj.getString("lastname");
                        String company = massageObj.getString("company");
                        String company_id = massageObj.getString("company_id");
                        String tax_id = massageObj.getString("tax_id");
                        String address_1 = massageObj.getString("address_1");
                        String address_2 = massageObj.getString("address_2");
                        String city = massageObj.getString("city");
                        String postcode = massageObj.getString("postcode");
                        String country_id = massageObj.getString("country_id");
                        String zone_id = massageObj.getString("zone_id");
                        //-----------------
                        Bundle args = new Bundle();
                        Fragment fm = new Delivery_fragment();
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getActivity(), "Some Problem", Toast.LENGTH_SHORT).show();
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

    //-------------------------------------------

    class GetCountry extends AsyncTask<String, String, String> {
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

            sever_url = "https://enlightshopping.com/api/api/get_country";

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
                    countryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_row, strContryList);
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
            dialog = new ProgressDialog(getActivity());
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

                    stateAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_row, strStateList);
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

}
