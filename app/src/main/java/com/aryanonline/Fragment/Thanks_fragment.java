package com.aryanonline.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aryanonline.MainActivity;


public class Thanks_fragment extends Fragment implements View.OnClickListener {

    TextView tv_info;
    Button btn_home, btn_order;

    public Thanks_fragment() {
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
        View view = inflater.inflate(com.aryanonline.R.layout.fragment_order_thanks, container, false);

        ((MainActivity) getActivity()).setTitle(getResources().getString(com.aryanonline.R.string.thank_you));

        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    Fragment fm = new Home_fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(com.aryanonline.R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        String data = getArguments().getString("msg");

        tv_info = (TextView) view.findViewById(com.aryanonline.R.id.tv_thank_info);
        btn_home = (Button) view.findViewById(com.aryanonline.R.id.btn_thank_home);
        btn_order = (Button) view.findViewById(com.aryanonline.R.id.btn_thank_order);

        tv_info.setText(Html.fromHtml(data));

        btn_home.setOnClickListener(this);
        btn_order.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == com.aryanonline.R.id.btn_thank_home) {

            Fragment fm = new Home_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(com.aryanonline.R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        } else if (id == com.aryanonline.R.id.btn_thank_order) {

            Fragment fm = new My_order_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(com.aryanonline.R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }

    }
}
