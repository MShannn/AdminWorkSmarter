package com.jvidal.worksmarter.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.jvidal.worksmarter.Activities.AddAnomaliesActivity;
import com.jvidal.worksmarter.HelperMethods.Cache;
import com.jvidal.worksmarter.Interfaces.MarkerClickListner;
import com.jvidal.worksmarter.Models.WorkListModel;
import com.jvidal.worksmarter.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.MyViewHolder> {

    public String myCheck = "un";
    View itemView;
    Context conn;
    String c_one;
    String c_two;
    String c_three;
    String currentPhotoPath;
    boolean viewCheck = false;
    private MarkerClickListner mListener;
    private List<WorkListModel> ContactList;
    String code;
    String client_one;
    String client_two;
    String client_three;
    String codeOne;
    String codeTwo;
    String finalCode;
    String dbCode;
    String client;
    String first;
    String second;
    String finalResult = "";

    public WorkListAdapter(List<WorkListModel> ContactList, Context con, MarkerClickListner markerClickListner) {
        this.ContactList = ContactList;
        this.conn = con;
        this.mListener = markerClickListner;
        Log.d("SHAN", "size work adapter" + ContactList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.wrok_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        c_one = ContactList.get(position).getClient_one();
        c_two = ContactList.get(position).getClient_two();
        c_three = ContactList.get(position).getClient_three();
        if (c_three.isEmpty() || c_three.equals("") || c_three.length() <= 0 || c_three == null) {
            c_three = "N/A";
        }
        if (c_two.equals("") || c_two.isEmpty() || c_two.length() <= 0 || c_two == null) {
            c_two = "N/A";
        }
        if (c_one.equals("") || c_one.isEmpty() || c_one.length() <= 0 || c_one == null) {
            c_one = "N/A";
        }


        if (c_one.endsWith("*") || c_one.contains("*")) {

            holder.txt_client_one.setBackgroundResource(R.drawable.client_shape_list);
            c_one = c_one.replace("*", "");
            holder.txt_client_one.setText(c_one);
            viewCheck = false;

        } else {
            viewCheck = true;
            holder.txt_client_one.setText(c_one);
        }

        if (c_two.endsWith("*") || c_two.contains("*")) {

            holder.txt_client_two.setBackgroundResource(R.drawable.client_shape_list);
            c_two = c_two.replace("*", "");
            holder.txt_client_two.setText(c_two);
            viewCheck = false;
        } else {
            holder.txt_client_two.setText(c_two);
            viewCheck = true;
        }

        if (c_three.endsWith("*") || c_three.contains("*")) {
            holder.txt_client_three.setBackgroundResource(R.drawable.client_shape_list);
            c_three = c_three.replace("*", "");
            holder.txt_client_three.setText(c_three);
            viewCheck = false;
        } else {
            holder.txt_client_three.setText(c_three);
            viewCheck = true;
        }
        holder.txt_code.setText(ContactList.get(position).getCode() + " " + ContactList.get(position).getAddress()
                + " " + ContactList.get(position).getRef()
        );
        Log.d("SHAN", "ADAPAET PROBLEM" + ContactList.get(position).getProblem());
        if (ContactList.get(position).getProblem().length() > 4) {
            holder.txt_problem.setText(ContactList.get(position).getProblem());

        } else {
            holder.txt_problem.setVisibility(View.GONE);
        }

        holder.txt_client_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValueInCache("Client1", ContactList.get(position).getClient_one(), ContactList.get(position), position);
            }
        });

        holder.txt_client_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValueInCache("Client2", ContactList.get(position).getClient_two(), ContactList.get(position), position);
            }
        });
        holder.txt_client_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValueInCache("Client3", ContactList.get(position).getClient_three(), ContactList.get(position), position);
            }
        });

        holder.txt_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLocationClickListner(ContactList.get(position).getLati(), ContactList.get(position).getLongi(), ContactList.get(position).getAddress());
            }
        });
        holder.txt_civil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataSaveinCache("Client3", ContactList.get(position).getClient_three(), ContactList.get(position), position, "", "Civil", ContactList.get(position).getTypeOfStructure());
                ((Activity) conn).startActivity(new Intent(conn, AddAnomaliesActivity.class));
            }
        });
        holder.txt_electric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSaveinCache("Client3", ContactList.get(position).getClient_three(), ContactList.get(position), position, "", "Electric", ContactList.get(position).getTypeOfStructure());
                ((Activity) conn).startActivity(new Intent(conn, AddAnomaliesActivity.class));
            }
        });

        holder.txt_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSaveinCache("Client3", ContactList.get(position).getClient_three(), ContactList.get(position), position, "", "Problem", "");
                ((Activity) conn).startActivity(new Intent(conn, AddAnomaliesActivity.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return ContactList.size();
    }

    public void setValueInCache(String clinets, String clientValue, WorkListModel workListModel, int pos) {
        StrictMode.VmPolicy.Builder builderr = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builderr.build());


        code = workListModel.getCode();
        client = clinets;
        client_one = workListModel.getClient_one();
        client_two = workListModel.getClient_two();
        client_three = workListModel.getClient_three();


        String code = workListModel.getCode();
        int count = workListModel.getCode().length() - code.replace("-", "").length();

        if (count > 1) {
            String[] segments = code.split("-");
            first = segments[0];
            second = segments[1];
            if (client.equals("Client1")) {
                finalResult = first + "-" + second + "-01";
                client_one = client_one.replace("*", "");
            } else if (client.equals("Client2")) {
                finalResult = first + "-" + second + "-02";
                client_two = client_two.replace("*", "");
            } else if (client.equals("Client3")) {
                finalResult = first + "-" + second + "-03";
                client_three = client_three.replace("*", "");
            }
            dbCode = first + "-" + second;
        } else {
            if (client.equals("Client1")) {
                client_one = client_one.replace("*", "");
            } else if (client.equals("Client2")) {
                client_two = client_two.replace("*", "");
            } else if (client.equals("Client3")) {
                client_three = client_three.replace("*", "");
            }
            dbCode = code;
            finalResult = code;
        }


        String timeStamp = new SimpleDateFormat("EEEE, dd MMMM yyyy hh-mm a").format(new Date());
        String nameOfFile = finalResult + " " + timeStamp + ".png";

        // String imageFileName = timeStamp + ".jpg";
        currentPhotoPath = createFolder() + "/" + nameOfFile;
        File file = new File(currentPhotoPath);
        dataSaveinCache(clinets, clientValue, workListModel, pos, currentPhotoPath, "", "");
        final Uri outputFileUri = Uri.fromFile(file);
        final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        ((Activity) conn).startActivityForResult(cameraIntent, 33);



/*        AlertDialog.Builder builder = new AlertDialog.Builder(conn);
        //builder.setTitle("Name");
        View view = LayoutInflater.from(conn).inflate(R.layout.dialog_image_video, null);
        builder.setView(view);

        ImageView cam = view.findViewById(R.id.img_cam);
        ImageView vid = view.findViewById(R.id.img_vid);
        final AlertDialog dialog = builder.create();


        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                ((Activity) conn).startActivityForResult(cameraIntent, 33);
            }
        });

        vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                ((Activity) conn).startActivityForResult(cameraIntent, 55);
            }
        });
        dialog.setCancelable(true);
        dialog.show();*/

    }

    public void dataSaveinCache(String clinet, String clientValue, WorkListModel workListModel, int pos, String currentPhotoPath, String anomaly, String structureType) {
        Cache.getInstance().getLru().remove("code");
        Cache.getInstance().getLru().remove("ref");
        Cache.getInstance().getLru().remove("address");
        Cache.getInstance().getLru().remove("fileURL");
        Cache.getInstance().getLru().remove("fileName");
        Cache.getInstance().getLru().remove("client");
        Cache.getInstance().getLru().remove("clientValue");
        Cache.getInstance().getLru().remove("row");
        Cache.getInstance().getLru().remove("client1");
        Cache.getInstance().getLru().remove("client2");
        Cache.getInstance().getLru().remove("client3");
        Cache.getInstance().getLru().remove("position");
        Cache.getInstance().getLru().remove("photopath");
        Cache.getInstance().getLru().remove("anomaly");
        Cache.getInstance().getLru().remove("twoDigitCode");
        Cache.getInstance().getLru().remove("structureType");


        Cache.getInstance().getLru().put("code", workListModel.getCode());
        Cache.getInstance().getLru().put("twoDigitCode", workListModel.getFinalCode());
        Cache.getInstance().getLru().put("ref", workListModel.getRef());
        Cache.getInstance().getLru().put("address", workListModel.getAddress());
        Cache.getInstance().getLru().put("fileURL", workListModel.getFileUrl());
        Cache.getInstance().getLru().put("fileName", workListModel.getFileName());
        Cache.getInstance().getLru().put("client", clinet);
        Cache.getInstance().getLru().put("clientValue", clientValue);
        Cache.getInstance().getLru().put("row", workListModel.getRow());
        Cache.getInstance().getLru().put("client1", workListModel.getClient_one());
        Cache.getInstance().getLru().put("client2", workListModel.getClient_two());
        Cache.getInstance().getLru().put("client3", workListModel.getClient_three());
        Cache.getInstance().getLru().put("position", pos);
        Cache.getInstance().getLru().put("photopath", currentPhotoPath);
        Cache.getInstance().getLru().put("anomaly", anomaly);
        Cache.getInstance().getLru().put("structureType", structureType);
    }


    public File createFolder() {
        File myDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "WorkSmarterPictures");
        if (!myDirectory.exists()) {
            myDirectory.mkdir();
            Toast.makeText(conn, "Directorty created", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(conn, "D already", Toast.LENGTH_SHORT).show();
        return myDirectory;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_code;
        public TextView txt_ref;
        public TextView txt_address;
        public TextView txt_client_one;
        public TextView txt_client_two;
        public TextView txt_client_three;
        public TextView txt_electric;
        public TextView txt_civil;
        public TextView txt_problem;


        public MyViewHolder(View view) {
            super(view);

            txt_code = view.findViewById(R.id.txt_code);
            txt_ref = view.findViewById(R.id.txt_ref);
            txt_address = view.findViewById(R.id.txt_address);
            txt_client_one = view.findViewById(R.id.txt_client_one);
            txt_client_two = view.findViewById(R.id.txt_client_two);
            txt_client_three = view.findViewById(R.id.txt_client_three);
            txt_electric = view.findViewById(R.id.txt_electric);
            txt_civil = view.findViewById(R.id.txt_civil);
            txt_problem = view.findViewById(R.id.txt_problem);


        }

    }
}
//WorkListAdapter before updation