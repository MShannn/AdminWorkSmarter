package com.jvidal.worksmarter.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jvidal.worksmarter.Models.Anomalies;
import com.jvidal.worksmarter.R;

import java.util.List;


public class AnomaliesExportedAdapter extends RecyclerView.Adapter<AnomaliesExportedAdapter.MyViewHolder> {

    View itemView;
    Context conn;


    private List<Anomalies> ContactList;


    public AnomaliesExportedAdapter(List<Anomalies> ContactList, Context con) {

        this.ContactList = ContactList;
        this.conn = con;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_anomalies_exported, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        // holder.setIsRecyclable(false);

        holder.txtCode.setText(ContactList.get(position).getCode());
        if (ContactList.get(position).getCivilAnomaly() != null) {
            holder.txtCivil.setText(ContactList.get(position).getCivilAnomaly());

        } else {
            holder.txtCivil.setText("N/A");

        }
        if (ContactList.get(position).getElectricAnomaly() != null) {
            holder.txtElectric.setText(ContactList.get(position).getElectricAnomaly());

        } else {
            holder.txtElectric.setText("N/A");

        }
        if (ContactList.get(position).getBillboard() != null) {
            holder.txtBillboard.setText(ContactList.get(position).getBillboard());

        } else {

            holder.txtBillboard.setText("N/A");

        }
        if (ContactList.get(position).getObervation() != null) {
            holder.txtObservation.setText(ContactList.get(position).getObervation());

        } else {
            holder.txtObservation.setText("N/A");

        }


    }


    @Override
    public int getItemCount() {
        return ContactList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCode;
        public TextView txtCivil;
        public TextView txtElectric;
        public TextView txtBillboard;
        public TextView txtObservation;


        public MyViewHolder(View view) {
            super(view);

            txtCode = view.findViewById(R.id.txtCode);
            txtCivil = view.findViewById(R.id.txtCivil);
            txtElectric = view.findViewById(R.id.txtElectric);
            txtBillboard = view.findViewById(R.id.txtBillboard);
            txtObservation = view.findViewById(R.id.txtObservation);


        }

    }
}
