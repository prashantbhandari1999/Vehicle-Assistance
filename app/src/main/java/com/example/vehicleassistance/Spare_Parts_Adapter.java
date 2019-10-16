package com.example.vehicleassistance;

import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Spare_Parts_Adapter extends RecyclerView.Adapter<Spare_Parts_Adapter.ExampleViewHolder>  {
    private ArrayList<ExampleSpareParts> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public RelativeLayout mrelativelayout;



        public ExampleViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView_sparepart);
            mTextView1 = itemView.findViewById(R.id.textView_spare);
            mrelativelayout = itemView.findViewById(R.id.relativelayout_spare);
        }
    }

    public Spare_Parts_Adapter(ArrayList<ExampleSpareParts> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_sparepart,
                parent, false);
        final ExampleViewHolder evh = new ExampleViewHolder(v);
        evh.mrelativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               switch (evh.getAdapterPosition()) {
                   case 0:
                       TextView tv = v.findViewById(R.id.textView_spare);
                       Spanned html = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.airbagcenter.com'>Airbagcenter</a>");
                       tv.setMovementMethod(LinkMovementMethod.getInstance());
                       tv.setText(html);
                        break;
                   case 1:
                       TextView tv_1 = v.findViewById(R.id.textView_spare);
                       Spanned html_1 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.amazon.in/s?k=battery+for+cars+exide&crid=2CEQ2X8YICVM8&sprefix=battery+for+cars+%2Caps%2C399&ref=nb_sb_ss_i_4_17'>Battery</a>");
                       tv_1.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_1.setText(html_1);
                       break;
                   case 2:
                       TextView tv_2 = v.findViewById(R.id.textView_spare);
                       Spanned html_2 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.amazon.in/Brakes/b?ie=UTF8&node=5257529031'>Brakes</a>");
                       tv_2.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_2.setText(html_2);
                       break;
                   case 3:
                       TextView tv_3 = v.findViewById(R.id.textView_spare);
                       Spanned html_3 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.amazon.in/s?k=shock+absorber+for+car&crid=1NPZFYZTPI5WL&sprefix=shock+absor%2Caps%2C377&ref=nb_sb_ss_i_7_11'>Shock Absorbers</a>");
                       tv_3.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_3.setText(html_3);
                       break;
                   case 4:
                       TextView tv_4 = v.findViewById(R.id.textView_spare);
                       Spanned html_4 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.alibaba.com/premium/fuel_injectors.html?src=sem_ggl&cmpgn=897408147&adgrp=58601962924&fditm=&tgt=kwd-20664484150&locintrst=&locphyscl=1007788&mtchtyp=b&ntwrk=g&device=c&dvcmdl=&creative=280539033110&plcmnt=&plcmntcat=&p1=&p2=&aceid=&position=1t1&gclid=EAIaIQobChMI99v0or2e5QIVmCQrCh3eBA48EAAYASAAEgILEvD_BwE'>Fuel Injectors</a>");
                       tv_4.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_4.setText(html_4);
                       break;
                   case 5:
                       TextView tv_5 = v.findViewById(R.id.textView_spare);
                       Spanned html_5 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href=' https://www.alibaba.com/trade/search?fsb=y&IndexArea=product_en&CatId=&SearchText=clucth+disc+for+cars&viewtype=&tab='>Clutch</a>");
                       tv_5.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_5.setText(html_5);
                       break;
                   case 6:
                       TextView tv_6 = v.findViewById(R.id.textView_spare);
                       Spanned html_6 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.alibaba.com/trade/search?fsb=y&IndexArea=product_en&CatId=&SearchText=mirrors+of+cars&viewtype=&tab='>Mirrors</a>");
                       tv_6.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_6.setText(html_6);
                       break;
                   case 7:
                       TextView tv_7 = v.findViewById(R.id.textView_spare);
                       Spanned html_7 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.amazon.in/s?k=horns+of+vehicles&ref=nb_sb_noss_2'>Horns</a>");
                       tv_7.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_7.setText(html_7);
                       break;
                   case 8:
                       TextView tv_8 = v.findViewById(R.id.textView_spare);
                       Spanned html_8 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href=' https://www.amazon.in/s?k=oil+filters+for+bike&crid=2OUED49W8NJ18&sprefix=oil+filters+%2Caps%2C585&ref=nb_sb_ss_i_1_12'>Oil Filter</a>");
                       tv_8.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_8.setText(html_8);
                       break;
                   case 9:
                       TextView tv_9 = v.findViewById(R.id.textView_spare);
                       Spanned html_9 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href=' https://www.amazon.in/s?k=audio+player+for+cars&ref=nb_sb_noss_1'>Audio/Video Player</a>");
                       tv_9.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_9.setText(html_9);
                       break;
                   case 10:
                       TextView tv_10 = v.findViewById(R.id.textView_spare);
                       Spanned html_10 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.amazon.in/s?k=spark+plugs+for+bike&crid=UB7TJ6NULZCJ&sprefix=spark+plugs%2Caps%2C347&ref=nb_sb_ss_i_3_11'>Spark plugs</a>");
                       tv_10.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_10.setText(html_10);
                       break;
                   case 11:
                       TextView tv_11 = v.findViewById(R.id.textView_spare);
                       Spanned html_11 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href=' https://www.amazon.in/s?k=interior+lights+for+car&crid=2SDFBQIF29878&sprefix=interior+%2Caps%2C346&ref=nb_sb_ss_i_3_9'>Interior Lights</a>");
                       tv_11.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_11.setText(html_11);
                       break;
                   case 12:
                       TextView tv_12 = v.findViewById(R.id.textView_spare);
                       Spanned html_12 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.amazon.in/s?k=fuel+indicators&ref=nb_sb_noss_1'>Fuel indicators</a>");
                       tv_12.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_12.setText(html_12);
                       break;
                   case 13:
                       TextView tv_13= v.findViewById(R.id.textView_spare);
                       Spanned html_13 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.amazon.in/s?k=speedometer+for+motorcycle&crid=2HAEFBHNV0C5K&sprefix=speedome%2Caps%2C393&ref=nb_sb_ss_i_3_8'>Speedometers</a>");
                       tv_13.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_13.setText(html_13);
                       break;
                   case 14:
                       TextView tv_14= v.findViewById(R.id.textView_spare);
                       Spanned html_14 = Html.fromHtml("Click the link to visit <br />" +
                               "<a href='https://www.amazon.in/s?k=tyres&ref=nb_sb_noss_2'>Tyres</a>");
                       tv_14.setMovementMethod(LinkMovementMethod.getInstance());
                       // Set TextView text from html
                       tv_14.setText(html_14);
                       break;
               }



            }
        });
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleSpareParts currentItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());


        holder.mTextView1.setText(currentItem.getText1());
//       holder.mTextView2.setText(currentItem.getText2());

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<ExampleSpareParts> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }
}
