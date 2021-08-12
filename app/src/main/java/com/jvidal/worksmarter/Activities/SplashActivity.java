package com.jvidal.worksmarter.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.FileInfo;
import com.jvidal.worksmarter.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    ArrayList arrayList = new ArrayList();
    ArrayList url = new ArrayList();
    String databaseURL;
    String databaseName;
    String[] permissions = new String[]{

            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (checkPermissions()) {
            listAllBackendlessFiles();
            listDatabaseFileBackendless();
        } else {

            Toast.makeText(getApplicationContext(), "Permission needed", Toast.LENGTH_SHORT).show();
        }
        arrayList.add(0, "Select work list");
        url.add(0, "dumy");


    }

    public void listAllBackendlessFiles() {
        Backendless.Files.listing("/Work Lists", "*.xlsx", true,
                new AsyncCallback<List<FileInfo>>() {
                    @Override
                    public void handleResponse(List<FileInfo> response) {
                        Iterator<FileInfo> filesIterator = response.iterator();
                        while (filesIterator.hasNext()) {
                            FileInfo file = filesIterator.next();
                            String URL = file.getURL();
                            String publicURL = file.getPublicUrl();
                            Date createdOn = new Date(file.getCreatedOn());
                            String fileName = file.getName();
                            arrayList.add(fileName);
                            url.add(publicURL);
                        }


                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void listDatabaseFileBackendless() {
        Backendless.Files.listing("/Work Database", "*.xlsx", true,
                new AsyncCallback<List<FileInfo>>() {
                    @Override
                    public void handleResponse(List<FileInfo> response) {
                        Iterator<FileInfo> filesIterator = response.iterator();
                        while (filesIterator.hasNext()) {
                            FileInfo file = filesIterator.next();
                            String URL = file.getURL();
                            databaseURL = file.getPublicUrl();
                            databaseName = file.getName();
                            Date createdOn = new Date(file.getCreatedOn());
                        }

                        Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                Intent intent = new Intent(getApplicationContext(), SingInActivity.class);
                                intent.putExtra("mylist", arrayList);
                                intent.putExtra("url", url);
                                intent.putExtra("databaseURL", databaseURL);
                                intent.putExtra("databaseName", databaseName);

                                startActivity(intent);
                                finish();


                            }
                        }, 1200);

                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("SHAN", "error files" + fault.getMessage());
                    }
                });

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listAllBackendlessFiles();
                listDatabaseFileBackendless();
            }

            return;
        }
    }
}
