package com.jvidal.worksmarter.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.jvidal.worksmarter.Adapters.CivilElectricObservationsAdapter;
import com.jvidal.worksmarter.HelperMethods.BusProvider;
import com.jvidal.worksmarter.HelperMethods.Cache;
import com.jvidal.worksmarter.Interfaces.AnomalyFixedListner;
import com.jvidal.worksmarter.Models.Anomalies;
import com.jvidal.worksmarter.R;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class BillboardAnomalyFragment extends Fragment implements AnomalyFixedListner {
    String s = "";
    CivilElectricObservationsAdapter anomalyFixedListnerAdapter;
    ArrayList<Anomalies> anomaliesTemp = new ArrayList<>();
    RecyclerView recyclerView;
    LayoutInflater layoutInflater;
    LinearLayoutManager mLayoutManager;
    String codeAdapater;
    CivilElectricObservationsAdapter civilElectricObservationsAdapter;
    String check;
    RelativeLayout progress;
    ImageView img_noRecord;
    private boolean isRegistered = false;

    public BillboardAnomalyFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.billboard_anomaly_fragment, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());


        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progress = (RelativeLayout) root.findViewById(R.id.progress_view);
        img_noRecord = (ImageView) root.findViewById(R.id.img_norecord);
        img_noRecord.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);


        setDataInList();


        return root;
    }

    @Override
    public void onAnomalyFixedListner(String objectID, int b) {
        setDataInList();

    }

    public void setDataInList() {

        anomaliesTemp.clear();
        String whereClause = "code ='" + Cache.getInstance().getLru().get("code").toString() + "'";
        DataQueryBuilder dataQueryBuilder = DataQueryBuilder.create();
        dataQueryBuilder.setWhereClause(whereClause);
        Backendless.Data.of(Anomalies.class).find(dataQueryBuilder, new AsyncCallback<List<Anomalies>>() {
            @Override
            public void handleResponse(List<Anomalies> response) {

                for (int i = 0; i < response.size(); i++) {

                    if (response.get(i).getIsActive() == 1) {
                        if (response.get(i).getBillboard() != null) {
                            anomaliesTemp.add(response.get(i));
                        }
                    }
                }

                civilElectricObservationsAdapter = new CivilElectricObservationsAdapter(anomaliesTemp, getActivity(), 3);
                recyclerView.setAdapter(civilElectricObservationsAdapter);
                progress.setVisibility(View.GONE);
                if (anomaliesTemp.size() < 1) {
                    img_noRecord.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                fault.getMessage();
                progress.setVisibility(View.GONE);

            }
        });


    }



}
