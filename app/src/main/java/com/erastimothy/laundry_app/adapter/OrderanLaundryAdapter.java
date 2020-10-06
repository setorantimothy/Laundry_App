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
import com.erastimothy.laundry_app.model.Laundry;

import java.util.ArrayList;
import java.util.List;

public class OrderanLaundryAdapter extends RecyclerView.Adapter<OrderanLaundryAdapter.LaundryViewHolder> implements Filterable {
    private Context context;
    private List<Laundry> laundryList;
    private List<Laundry> laundryListFull;

    public OrderanLaundryAdapter(Context context,List<Laundry> _list){
        this.laundryList = _list;
        this.laundryListFull = new ArrayList<>(laundryList);
        this.context = context;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OrderanLaundryAdapter.LaundryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_orderan, parent, false);
        return new OrderanLaundryAdapter.LaundryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderanLaundryAdapter.LaundryViewHolder holder, int position) {
        Laundry laundry = laundryList.get(position);

        holder.noOrder_tv.setText(laundry.getOrder_id()+" - "+laundry.getTanggal());
        holder.nama_tv.setText(laundry.getNama());
        holder.total_tv.setText(String.valueOf(laundry.getTotal_pembayaran()));

        if(laundry.getStatus().trim().equalsIgnoreCase("Pesanan Selesai")){
            holder.noOrder_tv.setBackgroundColor(Color.parseColor("#02c39a"));
        }else if(laundry.getStatus().trim().equalsIgnoreCase("Pesanan Batal"))
            holder.noOrder_tv.setBackgroundColor(Color.parseColor("#ff6b6b"));
        else if(laundry.getStatus().trim().equalsIgnoreCase("Menunggu Penjemputan"))
            holder.noOrder_tv.setBackgroundColor(Color.parseColor("#ffe66d"));
        else if(laundry.getStatus().trim().equalsIgnoreCase("Sedang  Diproses"))
            holder.noOrder_tv.setBackgroundColor(Color.parseColor("#00a8e8"));
    }

    @Override
    public int getItemCount() {
        return laundryList.size();
    }

    @Override
    public Filter getFilter() {
        return laundryFilter;
    }

    private Filter laundryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Laundry> filterLaundryList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0 || charSequence.toString().equalsIgnoreCase("")){
                filterLaundryList.addAll(laundryListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Laundry item : laundryList){
                    if(item.getOrder_id().toLowerCase().contains(filterPattern) || item.getStatus().toLowerCase().contains(filterPattern) || item.getNama().toLowerCase().contains(filterPattern) || item.getJenis().toLowerCase().contains(filterPattern)) {
                        filterLaundryList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterLaundryList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            laundryList.clear();
            laundryList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class LaundryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView noOrder_tv,total_tv,nama_tv;
        LaundryViewHolder(View view){
            super(view);
            noOrder_tv = view.findViewById(R.id.noOrder_tv);
            total_tv = view.findViewById(R.id.total_tv);
            nama_tv = view.findViewById(R.id.nama_tv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Laundry laundry = laundryList.get(getAdapterPosition());

            Intent intent = new Intent(view.getContext(), EditOrderLaundryActivity.class);
            Bundle bundle = new Bundle();

            bundle.putString("alamat",laundry.getAlamat());
            bundle.putString("biaya_antar",String.valueOf(laundry.getBiaya_antar()));
            bundle.putString("harga",String.valueOf(laundry.getHarga()));
            bundle.putString("total_pembayaran",String.valueOf(laundry.getTotal_pembayaran()));
            bundle.putString("jenis",laundry.getJenis());
            bundle.putString("kuantitas", String.valueOf(laundry.getKuantitas()));
            bundle.putString("order_id",laundry.getOrder_id());
            bundle.putString("nama",laundry.getNama());
            bundle.putString("tanggal",laundry.getTanggal());
            bundle.putString("uid",laundry.getUid());
            bundle.putString("status",laundry.getStatus());
            intent.putExtra("laundry",bundle);

            view.getContext().startActivity(intent);

        }
    }
}
