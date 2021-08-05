package com.jvidal.worksmarter.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.jvidal.worksmarter.HelperMethods.BusProvider;
import com.jvidal.worksmarter.HelperMethods.Cache;
import com.jvidal.worksmarter.Interfaces.AnomalyFixedListner;
import com.jvidal.worksmarter.Models.Anomalies;
import com.jvidal.worksmarter.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CivilElectricObservationsAdapter extends RecyclerView.Adapter<CivilElectricObservationsAdapter.MyViewHolder> {

    View itemView;
    Context conn;
    int civilOrElectric = 1;
    String currentPhotoPath;
    AnomalyFixedListner anomalyFixedListnerA;

    private List<Anomalies> ContactList;


    public CivilElectricObservationsAdapter(List<Anomalies> ContactList, Context con, int check) {

        this.ContactList = ContactList;
        this.conn = con;
        this.civilOrElectric = check;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_civil_electric_observations, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // holder.setIsRecyclable(false);

        holder.txt_code.setText(ContactList.get(position).getCode());
        if (civilOrElectric == 1) {
            holder.txt_anomaly.setText(ContactList.get(position).getCivilAnomaly());
        } else {
            holder.txt_anomaly.setText(ContactList.get(position).getElectricAnomaly());
        }


        holder.txt_anomaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                /*TODO: Need to fix to avoid update when photo not taken*/
                Toast.makeText(conn, "Please wait...", Toast.LENGTH_SHORT).show();
                Backendless.Data.of(Anomalies.class).findById(ContactList.get(position).getObjectId(), new AsyncCallback<Anomalies>() {
                    @Override
                    public void handleResponse(Anomalies response) {

                        response.setIsActive(0);

                        Backendless.Persistence.save(response, new AsyncCallback<Anomalies>() {
                            @Override
                            public void handleResponse(Anomalies response) {


                                setValueInCache();



                                //  anomalyFixedListnerA.onAnomalyFixedListner("", 1);

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                            }
                        });
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });


            }
        });


    }

    public void setValueInCache() {
        // public void setValueInCache(String client, String code, String clientValue, int row) {

        Cache.getInstance().getLru().remove("photopath");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".png";
        currentPhotoPath = createFolder() + "/" + imageFileName;
        File file = new File(currentPhotoPath);
        Cache.getInstance().getLru().put("photopath", currentPhotoPath);



        Uri outputFileUri = Uri.fromFile(file);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        ((Activity) conn).startActivityForResult(cameraIntent, 33);


    }

    public File createFolder() {
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "WorkSmarterPictures");
        if (!myDirectory.exists()) {
            myDirectory.mkdir();
        }
        return myDirectory;
    }


    @Override
    public int getItemCount() {
        return ContactList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_code;
        public TextView txt_anomaly;


        public MyViewHolder(View view) {
            super(view);

            txt_code = view.findViewById(R.id.txt_code);
            txt_anomaly = view.findViewById(R.id.txt_anomaly);


        }

    }
}
