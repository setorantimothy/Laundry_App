package com.erastimothy.laundry_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erastimothy.laundry_app.R;
import com.erastimothy.laundry_app.admin.EditOrderLaundryActivity;
import com.erastimothy.laundry_app.admin.LayananFormActivity;
import com.erastimothy.laundry_app.model.Laundry;
import com.erastimothy.laundry_app.model.Layanan;

import java.util.ArrayList;
import java.util.List;

public class LayananAdapter extends RecyclerView.Adapter<LayananAdapter.LayananViewHolder> implements Filterable {
    private Context context;
    private List<Layanan> layananList;
    private List<Layanan> layananListFull;

    public LayananAdapter(Context context, List<Layanan> _list){
        this.layananList = _list;
        this.layananListFull = new ArrayList<>(layananList);
        this.context = context;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LayananAdapter.LayananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layanan, parent, false);
        return new LayananAdapter.LayananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LayananAdapter.LayananViewHolder holder, int position) {
        Layanan layanan = layananList.get(position);

        holder.nama_tv.setText(layanan.getName());
        holder.harga_tv.setText(String.valueOf(layanan.getHarga()));

    }

    @Override
    public int getItemCount() {
        return layananList.size();
    }

    @Override
    public Filter getFilter() {
        return layananFilter;
    }

    private Filter layananFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Layanan> filterLayananList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0 || charSequence.toString().equalsIgnoreCase("")){
                filterLayananList.addAll(layananListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Layanan item : layananList){
                    if(String.valueOf(item.getId()).toLowerCase().contains(filterPattern) || item.getName().toLowerCase().contains(filterPattern) || String.valueOf(item.getHarga()).toLowerCase().contains(filterPattern) ) {
                        filterLayananList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterLayananList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            layananList.clear();
            layananList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class LayananViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView id_tv,harga_tv,nama_tv;
        LayananViewHolder(View view){
            super(view);
            harga_tv = view.findViewById(R.id.harga_tv);
            nama_tv = view.findViewById(R.id.nama_tv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Layanan layanan = layananList.get(getAdapterPosition());

            Intent intent = new Intent(view.getContext(), LayananFormActivity.class);
            Bundle bundle = new Bundle();

            bundle.putString("nama",layanan.getName());
            bundle.putString("harga",String.valueOf(layanan.getHarga()));
            bundle.putString("id", String.valueOf(layanan.getId()));
            intent.putExtra("layanan",bundle);

            view.getContext().startActivity(intent);

        }
    }
}
