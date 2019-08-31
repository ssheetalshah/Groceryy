package com.aryanonline.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.aryanonline.Adapter.Home_adapter;
import com.aryanonline.Adapter.View_time_adapter;
import com.aryanonline.AppController;
import com.aryanonline.Config.BaseURL;
import com.aryanonline.MainActivity;
import com.aryanonline.Model.Category_model;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.CustomVolleyJsonRequest;
import com.aryanonline.util.RecyclerTouchListener;
import com.aryanonline.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class View_time_fragment extends Fragment {

    private static String TAG = View_time_fragment.class.getSimpleName();

    private RecyclerView rv_time;

    private List<String> time_list = new ArrayList<>();
    private List<Category_model> category_modelList = new ArrayList<>();
    private Home_adapter adapter;

    private String getdate;

    private Session_management sessionManagement;

    public View_time_fragment() {
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
        View view = inflater.inflate(com.aryanonline.R.layout.fragment_time_list, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(com.aryanonline.R.string.delivery_time));

        sessionManagement = new Session_management(getActivity());

        rv_time = (RecyclerView) view.findViewById(com.aryanonline.R.id.rv_times);
        rv_time.setLayoutManager(new LinearLayoutManager(getActivity()));

        getdate = getArguments().getString("date");

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetTimeRequest(getdate);
        } else {
            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        rv_time.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_time, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String gettime = time_list.get(position);

                sessionManagement.cleardatetime();

                sessionManagement.creatdatetime(getdate,gettime);

                ((MainActivity) getActivity()).onBackPressed();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetTimeRequest(String date) {

        // Tag used to cancel the request
        String tag_json_obj = "json_time_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("date",date);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_TIME_SLOT, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        for(int i=0;i<response.getJSONArray("times").length();i++) {
                            time_list.add(""+response.getJSONArray("times").get(i));
                        }

                        View_time_adapter adapter = new View_time_adapter(time_list);
                        rv_time.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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
                    Toast.makeText(getActivity(), getResources().getString(com.aryanonline.R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}
