package com.aryanonline;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aryanonline.Fragment.MoreFragment;
import com.aryanonline.Fragment.WishFragment;
import com.bumptech.glide.Glide;


import com.aryanonline.Config.BaseURL;
import com.aryanonline.Fragment.Home_fragment;
import com.aryanonline.Fragment.Cart_fragment;
import com.aryanonline.Fragment.Support_info_fragment;
import com.aryanonline.Fragment.Edit_profile_fragment;
import com.aryanonline.Fragment.My_order_fragment;
import com.aryanonline.Fragment.LocationFragment;

import com.aryanonline.util.ConnectivityReceiver;
import com.aryanonline.util.DatabaseHandler;
import com.aryanonline.util.Session_management;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ConnectivityReceiver.ConnectivityReceiverListener {

    private DrawerLayout mDrawerToggle2;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    RelativeLayout rll;
    FloatingActionButton fab1, fab2;
    int toggel = 0;
    BottomNavigationView navView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private TextView totalBudgetCount, tv_name, tv_number;
    private ImageView iv_profile;

    private DatabaseHandler dbcart;

    private Session_management sessionManagement;

    private Menu nav_menu;
    private ActionBarDrawerToggle mDrawerToggle;
    private View navigation_contact, navigation_contact1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rll = (RelativeLayout) findViewById(R.id.rll);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        navView = findViewById(R.id.bottom_nav_view);
        // mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation_contact = navView.findViewById(R.id.navigation_contact);
        navigation_contact1 = navView.findViewById(R.id.navigation_contact1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));


        toolbar.setTitleTextColor(getResources().getColor(R.color.darkgrey));

        rll = (RelativeLayout) findViewById(R.id.rll);

    /*    ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name);
        mDrawerToggle.getDrawerArrowDrawable().setColor(Color.RED);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
*/
        dbcart = new DatabaseHandler(this);
        Toast.makeText(this, "cart count is " + dbcart.getCartCount(), Toast.LENGTH_SHORT).show();
        checkConnection();

        sessionManagement = new Session_management(MainActivity.this);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Drawable myDrawable = MainActivity.this.getResources().getDrawable(R.drawable.menuiii);
        myDrawable.setColorFilter(new
                PorterDuffColorFilter(0xffff00, PorterDuff.Mode.MULTIPLY));

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

//        toggle.setHomeAsUpIndicator(R.drawable.indicator_corner_bg);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        // toggle.setHomeAsUpIndicator(R.drawable.arrow_yellow);
        toggle.setHomeAsUpIndicator(R.drawable.hamburg);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


       /* viewPager = (ViewPager) findViewById(R.id.viewpager);
        //setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/
//       actionBarDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
//        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

//
//        toggle = ActionBarDrawerToggle(this, toggle, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//        mDrawerToggle.setDrawerIndicatorEnabled(false);
        nav_menu = navigationView.getMenu();

        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        iv_profile = (ImageView) header.findViewById(R.id.iv_header_img);
        tv_name = (TextView) header.findViewById(R.id.tv_header_name);
        tv_number = (TextView) header.findViewById(R.id.tv_header_moblie);

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    Fragment fm = new Edit_profile_fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
            }
        });

        updateHeader();
        sideMenu();

        if (savedInstanceState == null) {
            Fragment fm = new Home_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fm, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {

                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    Fragment fr = getFragmentManager().findFragmentById(R.id.contentPanel);

                    final String fm_name = fr.getClass().getSimpleName();
                    Log.e("backstack: ", ": " + fm_name);

                    if (fm_name.contentEquals("Home_fragment")) {

                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                        toggle.setDrawerIndicatorEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();

                    } else if (fm_name.contentEquals("My_order_fragment") ||
                            fm_name.contentEquals("Thanks_fragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Fragment fm = new Home_fragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                        .addToBackStack(null).commit();
                            }
                        });
                    } else {

                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                onBackPressed();
                            }
                        });
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


        if (sessionManagement.getUserDetails().get(BaseURL.KEY_ID) != null && !sessionManagement.getUserDetails().get(BaseURL.KEY_ID).equalsIgnoreCase("")) {
            /*MyFirebaseRegister fireReg = new MyFirebaseRegister(this);
            fireReg.RegisterUser(sessionManagement.getUserDetails().get(BaseURL.KEY_ID));*/
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //   mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_contact:
                    if (toggel % 2 != 0) {
                        rll.setVisibility(View.GONE);
                        toggel++;
                    } else {
                        rll.setVisibility(View.VISIBLE);
                        toggel++;
                    } return true;
                case R.id.navigation_contact1:
                    rll.setVisibility(View.GONE);
                    navigation_contact.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_notifications:
                    //   mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_more:
                    Fragment fm = new MoreFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                    return true;

            }
            return false;
        }
    };

    public void updateHeader() {
        if (sessionManagement.isLoggedIn()) {
            String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
            String getimage = sessionManagement.getUserDetails().get(BaseURL.KEY_IMAGE);
            String getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);

            Glide.with(this)
                    .load(BaseURL.IMG_PROFILE_URL + getimage)
                    .placeholder(R.drawable.aplogo)
                    .crossFade()
                    .into(iv_profile);
            tv_name.setText(getname);
            tv_number.setText(getemail);
        }
    }

    public void sideMenu() {

        if (sessionManagement.isLoggedIn()) {
            tv_number.setVisibility(View.VISIBLE);
            nav_menu.findItem(R.id.nav_logout).setVisible(true);
            nav_menu.findItem(R.id.nav_user).setVisible(true);
        } else {
            tv_number.setVisibility(View.GONE);
            tv_name.setText(getResources().getString(R.string.btn_login));
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });
            nav_menu.findItem(R.id.nav_logout).setVisible(false);
            nav_menu.findItem(R.id.nav_user).setVisible(false);
        }
    }

    public void setFinish() {
        finish();
    }

    public void setCartCounter(String totalitem) {
        totalBudgetCount.setText(totalitem);
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_cart);
        MenuItem c_password = menu.findItem(R.id.action_change_password);
        MenuItem search = menu.findItem(R.id.action_search);

        item.setVisible(true);
        c_password.setVisible(false);
        search.setVisible(false);

        View count = item.getActionView();
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.performIdentifierAction(item.getItemId(), 0);
            }
        });

        totalBudgetCount = (TextView) count.findViewById(R.id.actionbar_notifcation_textview);

        totalBudgetCount.setText("" + dbcart.getCartCount());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_cart) {

            if (dbcart.getCartCount() > 0) {
                Fragment fm = new Cart_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();
            } else {
                Toast.makeText(MainActivity.this, "No item in cart", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fm = null;
        Bundle args = new Bundle();

        if (id == R.id.nav_home) {
            Fragment fm_home = new Home_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fm_home, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        } else if (id == R.id.nav_offer) {

        } else if (id == R.id.nav_office) {
            fm = new LocationFragment();
        } else if (id == R.id.nav_myorders) {

            fm = new My_order_fragment();
        } else if (id == R.id.nav_myprofile) {
            fm = new Edit_profile_fragment();
        } else if (id == R.id.nav_support) {

            fm = new Support_info_fragment();
            args.putString("url", BaseURL.GET_SUPPORT_URL);
            args.putString("title", getResources().getString(R.string.nav_support));
            fm.setArguments(args);
        } else if (id == R.id.nav_aboutus) {

            fm = new Support_info_fragment();
            args.putString("url", BaseURL.GET_ABOUT_URL);
            args.putString("title", getResources().getString(R.string.nav_about));
            fm.setArguments(args);
        } else if (id == R.id.nav_policy) {

            fm = new Support_info_fragment();
            args.putString("url", BaseURL.GET_TERMS_URL);
            args.putString("title", getResources().getString(R.string.nav_terms));
            fm.setArguments(args);
        } else if (id == R.id.nav_review) {
            reviewOnApp();
        } else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_wishlist) {
            fm = new WishFragment();
          /*  Intent intent = new Intent(MainActivity.this, WishlistActivity.class);
            startActivity(intent);*/
        } else if (id == R.id.nav_logout) {
            sessionManagement.logoutSession();
            finish();
        }

        if (fm != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://aryanonline.co.in/apk" + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        AppController.getInstance().setConnectivityListener(this);

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(BaseURL.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(BaseURL.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;

        if (!isConnected) {
            message = "" + getResources().getString(R.string.no_internet);
            color = Color.RED;

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.coordinatorlayout), message, Snackbar.LENGTH_LONG);
                /*.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

               }
                })*/

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    // Fetches reg id from shared preferences
    // and displays on the screen

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(BaseURL.PREFS_NAME, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {

            //txtRegId.setText("Firebase Reg Id: " + regId);

        } else {

            //txtRegId.setText("Firebase Reg Id is not received yet!");

        }
    }

}
