package com.aryanonline.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aryanonline.Adapter.MyAdapterrr;
import com.aryanonline.Adapter.NewCatAdapter;
import com.aryanonline.Adapter.TopAdapter;
import com.aryanonline.Model.NewCategory;
import com.aryanonline.Model.TopModel;
import com.aryanonline.ViewDeatilsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.aryanonline.Adapter.DealsAdapter;
import com.aryanonline.Adapter.Home_adapter;
import com.aryanonline.Adapter.HorizontalAdapter;
import com.aryanonline.Adapter.OfferAdapter;
import com.aryanonline.Adapter.RecyclerViewHorizontalListAdapter;
import com.aryanonline.Config.BaseURL;
import com.aryanonline.Model.Category_model;
import com.aryanonline.Model.Data;
import com.aryanonline.Model.DealsModel;
import com.aryanonline.Model.Grocery;
import com.aryanonline.Model.OfferModel;
import com.aryanonline.AppController;
import com.aryanonline.MainActivity;
import com.aryanonline.R;
import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.CustomVolleyJsonRequest;
import com.aryanonline.util.HttpHandler;
import com.aryanonline.util.RecyclerTouchListener;

import me.relex.circleindicator.CircleIndicator;


public class Home_fragment extends Fragment {

    private static String TAG = Home_fragment.class.getSimpleName();
    private SliderLayout imgSlider;
    private RecyclerView rv_items;
    private RecyclerView rv_top;
    TextView text_marquee;
    //private RelativeLayout rl_view_all;
    private List<Category_model> category_modelList = new ArrayList<>();
    private Home_adapter adapter;
    private ImageView iv_location;
    private boolean isSubcat = false;
    EditText searchview;
    RecyclerView offerlist;
    HorizontalAdapter horizontalAdapter;
    private List<Data> data = new ArrayList<>();
    RecyclerView dealsview;
    private List<Grocery> groceryList = new ArrayList<>();
    private RecyclerView groceryRecyclerView;
    private RecyclerViewHorizontalListAdapter groceryAdapter;
    private String server_url;
    ArrayList<OfferModel> offer_list;
    private OfferAdapter offerAdapter;
    ArrayList<TopModel> top_list;
    private TopAdapter topAdapter;
    ArrayList<DealsModel> deals_list;
    private NewCatAdapter newCatAdapter;
    ArrayList<NewCategory> nCat_list;
    private DealsAdapter dealsAdapter;
    ImageView imageNew;
    String product_image;
    private static ViewPager mPager;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.pfour, R.drawable.pfive, R.drawable.psix};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    RecyclerView listPost;
    TextView bt_view;
    RecyclerView cateList;

    public Home_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        ((MainActivity) getActivity()).updateHeader();

        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    ((MainActivity) getActivity()).finish();

                    return true;
                }
                return false;
            }
        });

        //    imgSlider = (SliderLayout) view.findViewById(R.id.home_img_slider);
        rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        listPost = (RecyclerView) view.findViewById(R.id.listPost);
        rv_top = (RecyclerView) view.findViewById(R.id.rv_top);
        offerlist = (RecyclerView) view.findViewById(R.id.offerlist);
        dealsview = (RecyclerView) view.findViewById(R.id.dealsview);
        cateList = (RecyclerView) view.findViewById(R.id.cateList);
        searchview = (EditText) view.findViewById(R.id.searchview);
        imageNew = (ImageView) view.findViewById(R.id.imgNew);
        bt_view = (TextView) view.findViewById(R.id.bt_view);
        text_marquee = view.findViewById(R.id.text_marquee);

        text_marquee.setSelected(true);

        deals_list = new ArrayList<>();
        offer_list = new ArrayList<>();
        top_list = new ArrayList<>();
        nCat_list = new ArrayList<>();

        // add a divider after each item for more clarity
      /*  groceryAdapter = new RecyclerViewHorizontalListAdapter(groceryList, getActivity());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        dealsview.setLayoutManager(horizontalLayoutManager);
        dealsview.setAdapter(groceryAdapter);
        populategroceryList();
*/
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_items.setLayoutManager(gridLayoutManager);


        //rv_items.setLayoutManager(new LinearLayoutManager(getActivity()));

        // initialize a SliderLayout
      /*  imgSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imgSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imgSlider.setCustomAnimation(new DescriptionAnimation());
        imgSlider.setDuration(4000);*/


        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            //  makeGetSliderRequest();
            //  makeGetCategoryRequest("");
        }

        if (ConnectivityReceiver.isConnected()) {
            new GetToplist().execute();
        } else {
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        if (ConnectivityReceiver.isConnected()) {
            new GetBannerlist().execute();
        } else {
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
        }
        if (ConnectivityReceiver.isConnected()) {
            new GetCateeelist().execute();
        } else {
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
        }

        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String getid = category_modelList.get(position).getId();
                String getcat_title = category_modelList.get(position).getTitle();

                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                args.putString("cat_title", getcat_title);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        bt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewDeatilsActivity.class);
                startActivity(intent);
            }
        });

        searchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fm = new Search_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            }
        });

        for (int i = 0; i < XMEN.length; i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapterrr(getActivity(), XMENArray));
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        return view;
    }

    //---------------------------------------------------------------------

    class GetToplist extends AsyncTask<String, String, String> {
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
                server_url = "https://enlightshopping.com/api/api/getProduct";
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
                        String productname = c.getString("productname");
                        String quantity = c.getString("quantity");
                        String image = c.getString("image");
                        String stockStatus = c.getString("stockStatus");
                        String price = c.getString("price");
                        if(i==0 || i==1 || i==2 || i==3) {
                            top_list.add(new TopModel(product_id, productname, quantity, image, stockStatus, price));
                        }
                    }

                    topAdapter = new TopAdapter(getActivity(), top_list);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
//                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    rv_top.setLayoutManager(gridLayoutManager);
                    rv_top.setItemAnimator(new DefaultItemAnimator());
                    rv_top.setAdapter(topAdapter);
                    rv_top.setNestedScrollingEnabled(false);
                    // adapter.setHasStableIds(new List(top_list.GetRange(0, 4)));


                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }

    class GetBannerlist extends AsyncTask<String, String, String> {
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
                server_url = "https://enlightshopping.com/api/api/get_banner_image";
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
                        String id = c.getString("id");
                        String title = c.getString("title");
                        String parent_id = c.getString("parent_id");
                        String group_id = c.getString("group_id");
                        String params = c.getString("params");
                        String layersparams = c.getString("layersparams");
                        String image = c.getString("image");
                        String status = c.getString("status");
                        String position = c.getString("position");
                        offer_list.add(new OfferModel(id, title, parent_id, group_id, params, layersparams, image, status, position));
                    }

                    offerAdapter = new OfferAdapter(getActivity(), offer_list);
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    listPost.setLayoutManager(mLayoutManager);
                    listPost.setItemAnimator(new DefaultItemAnimator());
                    listPost.setAdapter(offerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }

    //--------------------------------------------------------------

    class GetCateeelist extends AsyncTask<String, String, String> {
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
                server_url = "https://enlightshopping.com/api/api/getcategory";
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
                        String category_id = c.getString("category_id");
                        String image = c.getString("image");
                        String parent_id = c.getString("parent_id");
                        String top = c.getString("top");
                        String column = c.getString("column");
                        String sort_order = c.getString("sort_order");
                        String status = c.getString("status");
                        String date_added = c.getString("date_added");
                        String date_modified = c.getString("date_modified");
                        String language_id = c.getString("language_id");
                        String name = c.getString("name");
                        String description = c.getString("description");
                        String meta_description = c.getString("meta_description");
                        String meta_keyword = c.getString("meta_keyword");
                        nCat_list.add(new NewCategory(category_id, image, parent_id, top, column, sort_order, status, date_added, date_modified,
                                language_id,name,description,meta_description,meta_keyword));

                    }

                    newCatAdapter = new NewCatAdapter(getActivity(), nCat_list);
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    cateList.setLayoutManager(mLayoutManager);
                    cateList.setItemAnimator(new DefaultItemAnimator());
                    cateList.setAdapter(newCatAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //  dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }
}
