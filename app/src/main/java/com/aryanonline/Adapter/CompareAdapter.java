package com.aryanonline.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aryanonline.Config.BaseURL;
import com.aryanonline.Model.CompareModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.ViewHolder>  {
    private static final String TAG = "CompareAdapter";
    private ArrayList<CompareModel> CompList;
    public Context context;
    String resId = "";
    String finalStatus = "";
    String Image;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView pnme,pmodel,pPrice,pDescrip;
        //  LinearLayout card;
        ImageView imgComp;
        LinearLayout mainButton;
        int pos;

        public ViewHolder(View view) {
            super(view);

            pnme = (TextView) view.findViewById(com.aryanonline.R.id.pnme);
            pmodel = (TextView) view.findViewById(com.aryanonline.R.id.pmodel);
            pPrice = (TextView) view.findViewById(com.aryanonline.R.id.pPrice);
            pDescrip = (TextView) view.findViewById(com.aryanonline.R.id.pDescrip);
            imgComp = (ImageView) view.findViewById(com.aryanonline.R.id.imgComp);
            mainButton = (LinearLayout) view.findViewById(com.aryanonline.R.id.mainButton);
            // card = (LinearLayout) view.findViewById(R.id.card_view);
        }
    }

    public static Context mContext;

    public CompareAdapter(Context mContext, ArrayList<CompareModel> comp_list) {
        context = mContext;
        CompList = comp_list;

    }

    @Override
    public CompareAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(com.aryanonline.R.layout.compare_row, parent, false);

        return new CompareAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CompareAdapter.ViewHolder viewHolder, final int position) {
        CompareModel compareModel = CompList.get(position);
       viewHolder.pnme.setText(compareModel.getProductName());
       viewHolder.pmodel.setText(compareModel.getModel());
       viewHolder.pPrice.setText(compareModel.getPrice());
       viewHolder.pDescrip.setText(compareModel.getDescription());
        String s = compareModel.getPrice();
        Image = compareModel.getImage();
        // viewHolder.card.setTag(viewHolder);
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + Image)
                .placeholder(com.aryanonline.R.drawable.aplogo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(viewHolder.imgComp);
      /*  viewHolder.mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopModel topModel = CompList.get(position);
                Intent intent = new Intent(context, NewActivity.class);
                intent.putExtra("TopModel", topModel);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });*/
        viewHolder.pos = position;

    }

    @Override
    public int getItemCount() {
        return CompList.size();
    }


}
