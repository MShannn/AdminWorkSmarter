package com.jvidal.worksmarter.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.UploadCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jvidal.worksmarter.Adapters.WorkListAdapter;
import com.jvidal.worksmarter.HelperMethods.BusProvider;
import com.jvidal.worksmarter.HelperMethods.Cache;
import com.jvidal.worksmarter.Interfaces.MarkerClickListner;
import com.jvidal.worksmarter.Models.Anomalies;
import com.jvidal.worksmarter.Models.WorkListModel;
import com.jvidal.worksmarter.R;
import com.jvidal.worksmarter.RealmDatabase.ImagesURLDatabase;
import com.jvidal.worksmarter.RealmDatabase.InformationDatabase;
import com.squareup.otto.Subscribe;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity implements TextWatcher, MarkerClickListner, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {


    String[] anomlyCivil = {"NO ELECTRICIDAD EN SECTOR" +
            "BALASTRO QUEMADO" +
            "APAGADO" +
            "NO GIRA" +
            "LAMPARA QUEMADA PANTALLA" +
            "LAMPARAS PESTANANDO" +
            "BALASTROS PESTANANDO" +
            "GIRANDO APAGADO" +
            "PEND. INSTALACION ELECTRICA" +
            "INST. ELECTRICA AVERIADA" +
            "OTROS"};
    boolean activityResult = false;
    int r = 0;
    String code;
    String ref;
    String address;
    String client_one;
    String client_two;
    String client_three;
    String codeOne;
    String codeTwo;
    String finalCode;
    int c = 0;
    int position = 0;
    String currentPhotoPath;
    Marker marker;
    WorkListAdapter workListAdapter;
    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;
    String dbCode;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LatLng latLng;
    GoogleMap mGoogleMap;
    Marker currLocationMarker;
    RelativeLayout circularProgressView;
    ArrayList<WorkListModel> workListModels = new ArrayList<>();
    ArrayList<WorkListModel> updatedModelList = new ArrayList<>();
    MarkerOptions markerOptions;
    BottomSheetBehavior bottomSheetBehavior;
    String selectedList = "Select work list";
    String selectedURL = "dummy";
    Realm realm;
    String client;
    String clientValue;
    String first;
    String second;
    String finalResult = "";
    ImageView dialogImageView;
    LinearLayout llBottomSheet;
    Toolbar toolbar;
    boolean isSearchViewVisiable = true;
    boolean isAnomalyViewVisiable = true;
    String anomaliesFromBackendless = "";
    private ImageView imgSearch;
    private ImageView imgTick;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback mLocationCallback;
    private LinearLayout llSearachStructure;
    private ImageView imgSearchStructure;
    private ImageView img_back;
    private ImageView img_settings;
    private EditText edtSearch;
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BusProvider.getInstance().register(this);

        workListModels.clear();
        updatedModelList.clear();
        iniViews();
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchViewVisiable) {
                    llSearachStructure.setVisibility(View.VISIBLE);

                    searchStructre(updatedModelList);
                    isSearchViewVisiable = false;
                    edtSearch.setText("");
                } else {
                    isSearchViewVisiable = true;
                    llSearachStructure.setVisibility(View.GONE);
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "In process", Toast.LENGTH_SHORT).show();
            }
        });


        setSupportActionBar(toolbar);
        toolbar.setTitle("Work Smarter Point");
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor));


        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setPeekHeight(340);

        realm = Realm.getDefaultInstance();


        mLayoutManager = new LinearLayoutManager(this);
        selectedList = getIntent().getStringExtra("selectedList");
        selectedURL = getIntent().getStringExtra("selectedURL");
        try {
            downloadFile(selectedURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapsInitializer.initialize(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLastLocation();
            }
        }, 3000);


    }

    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location lastLocation = task.getResult();
                            if (lastLocation != null) {
                                LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                                markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(address);
                                marker = mGoogleMap.addMarker(markerOptions);
                                // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,
                                        17);
                                mGoogleMap.moveCamera(update);

                            }
                        }
                    }
                });
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_homee_drawer, menu);
        return true;
    }
*/

    public boolean checkFileExist(String workListFile) {
        File file = new File(createFolder(), workListFile);
        if (file.exists()) {
            file.delete();
            return true;
        } else
            return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onLocationClickListner(Double lat, Double Longi, String address) {
   /*     if (markerMap.size() > 0) {

            if (markerMap.equals(address)) {
                // Update the location.
                marker = markerMap.get(address);
                marker.remove();
            }

        }*/
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        LatLng latLng = new LatLng(lat, Longi);
      /*  markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(address);
        marker = mGoogleMap.addMarker(markerOptions);
        // mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        markerMap.put(address, marker);*/
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,
                17);
        mGoogleMap.moveCamera(update);
    }

    public void showDataFromDatabase(boolean update) {
        circularProgressView.setVisibility(View.VISIBLE);
        setAddpter(workListModels, update);
        // circularProgressView.setVisibility(View.GONE);
    }

    protected String getCellAsString(Row row, int c, FormulaEvaluator
            formulaEvaluator, XSSFSheet xssfSheet, int rownNumber) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.d("SHAN", "exceptio bn" + e.getMessage());
        }
        return value;
    }

    public void downloadFile(final String fileInfo) throws IOException {

        workListModels.clear();
        updatedModelList.clear();
        try {
            checkFileExist(selectedList);
            Uri downloadUri = Uri.parse(fileInfo);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            //  request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(createFolder().getName(), selectedList);
            long id = downloadManager.enqueue(request);
            boolean downloading = true;
            while (downloading) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(id); //filter by id which you have receieved when reqesting download from download manager
                Cursor cursor = downloadManager.query(q);
                cursor.moveToFirst();
                int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;

                }
            }

            new TestAsync().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showImage(Bitmap bitmap) {

        try {
            final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_image_shoot);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialogImageView = dialog.findViewById(R.id.img_dialog);
            dialogImageView.setImageBitmap(bitmap);
            dialog.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 6000);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public File createFolder() {
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "WorkSmarterFiles");
        if (!myDirectory.exists()) {
            myDirectory.mkdir();
        }
        return myDirectory;
    }

    public void setAddpter(final ArrayList<WorkListModel> workListModels, boolean update) {
        circularProgressView.setVisibility(View.VISIBLE);

        Backendless.Data.of(Anomalies.class).find(new AsyncCallback<List<Anomalies>>() {
            @Override
            public void handleResponse(List<Anomalies> response) {


                AfterBackendlessDataGet(workListModels, response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                AfterBackendlessDataGet(workListModels, new ArrayList<Anomalies>());

                fault.getMessage();
            }
        });


    }

    public void AfterBackendlessDataGet(ArrayList<WorkListModel> workListModels, List<Anomalies> anomalies) {

        for (int i = 0; i < workListModels.size(); i++) {

            String twoDigitCode = workListModels.get(i).getFinalCode();
            String  completeCode = workListModels.get(i).getCode();
            Log.d("SHAN", "Two digit coed=" + twoDigitCode);
            StringBuilder builder = new StringBuilder();

            if (anomalies != null || anomalies.size() > 0) {
                for (int j = 0; j < anomalies.size(); j++) {

                    String segments[] = anomalies.get(j).getCode().split("-");
                    codeOne = segments[0];
                    codeTwo = segments[1];

                    // if (twoDigitCode.equals(codeOne + "-" + codeTwo)) {
                    if (completeCode.equals(anomalies.get(j).getCode())) {
                        if (anomalies.get(j).getIsActive() == 1) {
                            if (anomalies.get(j).getCivilAnomaly() != null) {
                                builder.append(anomalies.get(j).getCivilAnomaly() + ",");
                            }
                            if (anomalies.get(j).getElectricAnomaly() != null) {
                                builder.append(anomalies.get(j).getElectricAnomaly() + ",");
                            }
                        }

                    } else {
                        //  anomaliesFromBackendless = "";
                    }

                }
            }


            final InformationDatabase informationDatabase = realm.where(InformationDatabase.class).equalTo("code", twoDigitCode).findFirst();
            if (informationDatabase != null) {


                WorkListModel workListModel = new WorkListModel();
                workListModel.setCode(this.workListModels.get(i).getCode());
                workListModel.setAddress(this.workListModels.get(i).getAddress());
                workListModel.setRef(this.workListModels.get(i).getRef());
                workListModel.setClient_one(this.workListModels.get(i).getClient_one());
                workListModel.setClient_two(this.workListModels.get(i).getClient_two());
                workListModel.setClient_three(this.workListModels.get(i).getClient_three());
                workListModel.setRow(this.workListModels.get(i).getRow());
                workListModel.setFileUrl(this.workListModels.get(i).getFileUrl());
                workListModel.setFileName(this.workListModels.get(i).getFileName());
                workListModel.setLati(informationDatabase.getLattitude());
                workListModel.setLongi(informationDatabase.getLongitude());
                workListModel.setProblem(builder.toString());
                workListModel.setFinalCode(twoDigitCode);
                workListModel.setTypeOfStructure(informationDatabase.getDatabaseStructureType());
                updatedModelList.add(workListModel);


                LatLng latLng = new LatLng(informationDatabase.getLattitude(), informationDatabase.getLongitude());
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                markerOptions.title(twoDigitCode);

                marker = mGoogleMap.addMarker(markerOptions);

                //   markerMap.put(workListModels.get(i).getAddress(), marker);


                mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        View mark = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_view, null);

                        final ImageView imageView = mark.findViewById(R.id.profile_image);
                        TextView textView = mark.findViewById(R.id.txt_address);
                        TextView problem = mark.findViewById(R.id.txt_problem);
                        try {
                            ImagesURLDatabase imagesURLDatabase = realm.where(ImagesURLDatabase.class).equalTo("code", marker.getTitle()).findFirst();

                            InformationDatabase informationDatabase1 = realm.where(InformationDatabase.class).equalTo("code", marker.getTitle()).findFirst();

                            if (imagesURLDatabase != null) {
                                Bitmap bitmap = decodeFile(imagesURLDatabase.getPhotoPath());
                                imageView.setImageBitmap(bitmap);
                            }


                            textView.setText(informationDatabase1.getAddress());
                            //  problem.setText(informationDatabase1.getObervationProbe() + "," + informationDatabase1.getCivilProblem() + "," + informationDatabase1.getElectricProblem());

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        return mark;
                    }
                });
            } else {
                //   Toast.makeText(this, "Not found" + twoDigitCode, Toast.LENGTH_SHORT).show();
            }
        }


        workListAdapter = new WorkListAdapter(updatedModelList, MainActivity.this, this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(workListAdapter);
        workListAdapter.notifyDataSetChanged();
        circularProgressView.setVisibility(View.GONE);


        mGoogleMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {


                Log.d("SHAN", "Marker lon press " + marker.getTitle() + "     ID" + marker.getTag() + "    " + marker.getId());
                Intent intent = new Intent(MainActivity.this, AllImagesActivity.class);
                intent.putExtra("structureID", marker.getTitle());
                startActivity(intent);
            }
        });
    }


    public void updateExcelRecordAfterPhoto(Bitmap image) {

        code = Cache.getInstance().getLru().get("code").toString();
        ref = Cache.getInstance().getLru().get("ref").toString();
        address = Cache.getInstance().getLru().get("address").toString();
        selectedURL = Cache.getInstance().getLru().get("fileURL").toString();
        selectedList = Cache.getInstance().getLru().get("fileName").toString();
        client = Cache.getInstance().getLru().get("client").toString();
        clientValue = Cache.getInstance().getLru().get("clientValue").toString();
        r = (int) Cache.getInstance().getLru().get("row");
        position = (int) Cache.getInstance().getLru().get("position");
        client_one = Cache.getInstance().getLru().remove("client1").toString();
        client_two = Cache.getInstance().getLru().remove("client2").toString();
        client_three = Cache.getInstance().getLru().remove("client3").toString();

        int count = code.length() - code.replace("-", "").length();

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


        Backendless.Files.upload(photoFile, "Work Photos", new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {
                response.getFileURL();
                Log.d("WWF", response.getFileURL());
                activityResult = true;

                Toast.makeText(MainActivity.this, "Data updated sucessfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.d("WWF", backendlessFault.getMessage());
                Toast.makeText(MainActivity.this,
                        backendlessFault.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });










      /*  Backendless.Files.Android.upload(image,
                Bitmap.CompressFormat.PNG,
                100,
                finalResult + " " + formattedDate + ".png",
                "Work Photos",
                new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(final BackendlessFile backendlessFile) {
                        backendlessFile.getFileURL();
                        Log.d("WWF", backendlessFile.getFileURL());
                        activityResult = true;
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Log.d("WWF", backendlessFault.getMessage());
                        Toast.makeText(MainActivity.this,
                                backendlessFault.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });*/

        try {

            File file = new File(createFolder(), selectedList);
            FileInputStream fileInputStream = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);


            Row empRow = sheet.getRow(r);
            Cell c0 = empRow.createCell(0);
            c0.setCellValue(finalResult);

            Cell c1 = empRow.createCell(1);
            c1.setCellValue(address);
            Cell c2 = empRow.createCell(2);
            c2.setCellValue(ref);
            Cell c3 = empRow.createCell(3);
            c3.setCellValue(client_one);

            Cell c4 = empRow.createCell(4);
            c4.setCellValue(client_two);

            Cell c5 = empRow.createCell(5);
            c5.setCellValue(client_three);

            FileOutputStream out = new FileOutputStream(new
                    File(createFolder(), selectedList));
            workbook.write(out);
            out.close();

            File fileUpdated = new File(createFolder(), selectedList);
            Backendless.Files.upload(fileUpdated,
                    "Work Lists",
                    true,
                    new UploadCallback() {
                        @Override
                        public void onProgressUpdate(Integer progress) {
                            Log.d("SHAN", "" + progress);
                        }
                    }, new AsyncCallback<BackendlessFile>() {
                        @Override
                        public void handleResponse(BackendlessFile response) {

                            try {
                                downloadFile(selectedURL);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("SHAN", response.getFileURL());
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Log.d("SHAN", fault.getMessage());

                        }
                    });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        saveClientLastPicInRealm(dbCode, currentPhotoPath);
        // showDataFromDatabase(activityResult);

    }

    public void saveClientLastPicInRealm(String dbCodeTwoDigits, String currentPhotoPath) {


        ImagesURLDatabase imagesURLDatabase = realm.where(ImagesURLDatabase.class).equalTo("code", dbCodeTwoDigits).findFirst();


        realm.beginTransaction();
        if (imagesURLDatabase != null) {
            imagesURLDatabase.setPhotoPath(currentPhotoPath);

        } else {
            ImagesURLDatabase imagesURLDatabase1 = realm.createObject(ImagesURLDatabase.class);
            imagesURLDatabase1.setClient(client);
            imagesURLDatabase1.setCode(dbCodeTwoDigits);
            imagesURLDatabase1.setPhotoPath(currentPhotoPath);
        }

        realm.commitTransaction();

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String uri = "google.navigation:q=%f, %f";
                Intent navIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String
                        .format(Locale.US, uri, marker.getPosition().latitude, marker.getPosition().longitude)));
                startActivity(navIntent);
            }
        });


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();


                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            // mMap.setMyLocationEnabled(true);

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void galleryAddPic() {

        currentPhotoPath = Cache.getInstance().getLru().get("photopath").toString();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        photoFile = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(photoFile);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        Bitmap bitmap = decodeFile(currentPhotoPath);


        Bitmap bitmapNew;
        bitmapNew = bitmap;


        int rotate = 90;

        if (rotate != 0) {
            int w = bitmapNew.getWidth();
            int h = bitmapNew.getHeight();
            Matrix mtx = new Matrix();
            mtx.preRotate(rotate);
            bitmapNew = Bitmap.createBitmap(bitmapNew, 0, 0, w, h, mtx, true);

        }


        showImage(bitmapNew);


        updateExcelRecordAfterPhoto(bitmap);
    }

    public Bitmap decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to

        final int REQUIRED_SIZE = 1000;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap image = BitmapFactory.decodeFile(filePath, o2);
/*
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 90;
         *//*   switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }*//*

            if (rotate != 0) {
                int w = image.getWidth();
                int h = image.getHeight();
                Matrix mtx = new Matrix();
                mtx.preRotate(rotate);
                image = Bitmap.createBitmap(image, 0, 0, w, h, mtx, true);

            }
        } catch (IOException e) {
            return null;
        }*/
        return image.copy(Bitmap.Config.ARGB_8888, true);
    }

    public void iniViews() {

        circularProgressView = findViewById(R.id.progress_view);
        circularProgressView.setVisibility(View.INVISIBLE);
        toolbar = findViewById(R.id.toolbar);
        llBottomSheet = findViewById(R.id.bottom_sheet);
        recyclerView = findViewById(R.id.recycler_view);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_settings = (ImageView) findViewById(R.id.img_settings);
        llSearachStructure = (LinearLayout) findViewById(R.id.ll_searach_structure);
        imgSearchStructure = (ImageView) findViewById(R.id.img_search_structure);
        edtSearch = (EditText) findViewById(R.id.edt_search);
        llSearachStructure.setVisibility(View.GONE);

        edtSearch.addTextChangedListener(this);


    }


    public void searchStructre(final ArrayList<WorkListModel> listModel) {


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int textlength = s.length();
        ArrayList<WorkListModel> tempArrayList = new ArrayList<WorkListModel>();
        for (WorkListModel c : updatedModelList) {
            //if (textlength <= c.getBloodGroup().length()) {
            if (c.getCode().toLowerCase().contains(s.toString().toLowerCase())
                    || c.getAddress().toLowerCase().contains(s.toString().toLowerCase())
                            /*|| c.get().toLowerCase().contains(s.toString().toLowerCase())
                            || c.getGender().toLowerCase().contains(s.toString().toLowerCase())*/
            ) {
                tempArrayList.add(c);
                // }
            }
        }


        workListAdapter = new WorkListAdapter(tempArrayList, MainActivity.this, this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(workListAdapter);
        workListAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Name");
        View view = LayoutInflater.from(this).inflate(R.layout.gps_dialog, null);
        builder.setView(view);

        TextView textView = view.findViewById(R.id.txt_file_name);
        textView.setText("Wanna  exit the app?");
        ImageView done = view.findViewById(R.id.img_done);
        ImageView cancel = view.findViewById(R.id.img_cancle);
        final AlertDialog dialog = builder.create();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.super.onBackPressed();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setCancelable(false);
        dialog.show();


    }

  /*  @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }*/

    @Override
    protected void onDestroy() {

        BusProvider.getInstance().unregister(this);

        super.onDestroy();
    }

    @Subscribe
    public void vonImageAvailable(String event) {
        Log.d("SHAN", "BUS triggred");
        try {
            workListModels.clear();
            updatedModelList.clear();
            downloadFile(selectedURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class TestAsync extends AsyncTask<Void, Integer, String> {
        String TAG = "SHAN";

        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressView.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... arg0) {
            try {
                FileInputStream file = new FileInputStream(new File(createFolder(), selectedList));
                // FileInputStream file = new FileInputStream(new File(createFolder(), "Listado 22-01-2020.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowsCount = sheet.getPhysicalNumberOfRows();
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
                for (r = 1; r < rowsCount; r++) {
                    Row row = sheet.getRow(r);
                    int cellsCount = row.getPhysicalNumberOfCells();

                    for (c = 0; c < cellsCount; c++) {
                        String value = getCellAsString(row, c, formulaEvaluator, sheet, r);
                        if (c == 0) {
                            value = value;
                            code = value;
                        } else if (c == 1) {
                            address = value;
                        } else if (c == 2) {
                            ref = value;
                        } else if (c == 3) {
                            client_one = value;
                        } else if (c == 4) {
                            client_two = value;
                        } else if (c == 5) {
                            client_three = value;
                        } /*else if (c == 6) {
                            lattitude = Double.valueOf(value);
                        } else if (c == 7) {
                            longitude = Double.valueOf(value);
                        }*/

                    }

                    if (client_one == null) {
                        client_one = "N/A";
                    }
                    if (client_three == null) {
                        client_three = "N/A";
                    }
                    if (client_two == null) {
                        client_two = "N/A";
                    }

                    if (client_one.endsWith("*") || client_one.contains("*") || client_two.endsWith("*") || client_two.contains("*") || client_three.endsWith("*") || client_three.contains("*")) {
                        int count = code.length() - code.replace("-", "").length();
                        if (count > 1) {
                            String[] segments = code.split("-");
                            codeOne = segments[0];
                            codeTwo = segments[1];
                            finalCode = codeOne + "-" + codeTwo;
                        } else {
                            finalCode = code;
                        }

                        //        InformationDatabase informationDatabase = realm.where(InformationDatabase.class).equalTo("code", finalCode).findFirst();


                        WorkListModel workListModel = new WorkListModel();
                        workListModel.setCode(code);
                        workListModel.setAddress(address);
                        workListModel.setRef(ref);
                        workListModel.setClient_one(client_one);
                        workListModel.setClient_two(client_two);
                        workListModel.setClient_three(client_three);
                        workListModel.setRow(r);
                        workListModel.setFileUrl(selectedURL);
                        workListModel.setFileName(selectedList);
                        workListModel.setFinalCode(finalCode);
                        //    workListModel.setLati(informationDatabase.getLattitude());
                        //    workListModel.setLongi(informationDatabase.getLongitude());
                        workListModels.add(workListModel);


                    }
                    /*workListModel.setFileName(selectedList);
                    workListModel.setUrl(selectedURL);*/

                    //    Log.d("BABA Realm ", client_one + "    " + client_two + "      " + client_three);

                }
            } catch (Exception e) {
                Log.d("SHAN", "exception" + e.getMessage());
            }
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);
            Log.d("SHAN" + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            circularProgressView.setVisibility(View.INVISIBLE);
            showDataFromDatabase(activityResult);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 33) {

            if (resultCode != RESULT_CANCELED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                activityResult = true;

                galleryAddPic();
                setPic();
            }

        } else if (requestCode == 55) {
            Toast.makeText(this, "video", Toast.LENGTH_SHORT).show();
        }
    }


}
