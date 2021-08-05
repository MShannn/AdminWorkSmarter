package com.jvidal.worksmarter.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jvidal.worksmarter.HelperMethods.Cache;
import com.jvidal.worksmarter.Models.TypeOfProblemsModel;
import com.jvidal.worksmarter.R;

import java.util.List;


public class TypeOfProblemListAdapter extends RecyclerView.Adapter<TypeOfProblemListAdapter.MyViewHolder> {

    View itemView;
    Context conn;

    private List<TypeOfProblemsModel> ContactList;

    private int selectedItem = 0;
    private int lastSelected = 0;

    public TypeOfProblemListAdapter(List<TypeOfProblemsModel> ContactList, Context con) {

        this.ContactList = ContactList;
        this.conn = con;
        Log.d("SHAN", "size" + ContactList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_civil_electric_observations, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // holder.setIsRecyclable(false);
        holder.txt_problem.setText(ContactList.get(position).getProblems());
        int backgroundColor = (position == selectedItem) ? R.color.sky : R.color.thora_km_dark;
        holder.parent_ll.setBackgroundColor(ContextCompat.getColor(conn, backgroundColor));
        holder.txt_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelected = selectedItem;
                selectedItem = position;
                notifyItemChanged(lastSelected);
                notifyItemChanged(selectedItem);

                Cache.getInstance().getLru().remove("selectAnomaly");
                Cache.getInstance().getLru().put("selectAnomaly", ContactList.get(position).getProblems());

                //Toast.makeText(conn, "Anomaly=" + ContactList.get(position).getProblems(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return ContactList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView txt_problem;
        public LinearLayout parent_ll;


        public MyViewHolder(View view) {
            super(view);


            txt_problem = view.findViewById(R.id.txt_anomaly);
            parent_ll = view.findViewById(R.id.parent_ll);


        }

    }
}
