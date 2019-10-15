package com.example.vehicleassistance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Spare_Parts_Adapter extends RecyclerView.Adapter<Spare_Parts_Adapter.ExampleViewHolder>  {
    private ArrayList<ExampleSpareParts> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
//        public TextView mTextView2;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView_sparepart);
            mTextView1 = itemView.findViewById(R.id.textView_spare);
//          mTextView2 = itemView.findViewById(R.id.textView_sparepart_price);


        }
    }

    public Spare_Parts_Adapter(ArrayList<ExampleSpareParts> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_sparepart,
                parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleSpareParts currentItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());


        holder.mTextView1.setText(currentItem.getText1());
//        holder.mTextView2.setText(currentItem.getText2());

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
