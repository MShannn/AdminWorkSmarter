package com.jvidal.worksmarter.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.google.android.material.tabs.TabLayout;
import com.jvidal.worksmarter.Adapters.AnomaliesTabLayoutAdapter;
import com.jvidal.worksmarter.Adapters.TypeOfProblemListAdapter;
import com.jvidal.worksmarter.HelperMethods.BusProvider;
import com.jvidal.worksmarter.HelperMethods.Cache;
import com.jvidal.worksmarter.Interfaces.AnomalyFixedListner;
import com.jvidal.worksmarter.Models.Anomalies;
import com.jvidal.worksmarter.Models.TypeOfProblemsModel;
import com.jvidal.worksmarter.R;
import com.jvidal.worksmarter.RealmDatabase.InformationDatabase;
import com.jvidal.worksmarter.RealmDatabase.TypesOFProblemDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddAnomaliesActivity extends AppCompatActivity implements AnomalyFixedListner {


    String databaseCode;
    String databaseAddress;
    Double databaseLattitude;
    Double databaseLongitude;
    int databaseRow;
    String databaseName;
    String databaseURL;
    String structureType;

    boolean isPictureTaken = false;

    String obervationProblem;
    String currentPhotoPath;
    String adapterCode;
    String adapterRef;
    String adapterAddress;
    String client_one;
    String client_two;
    String client_three;
    // String anomaly;
    String twoDigtAdapterCOde;
    String check;
    ImageView dialogImageView;
    String workLisName;
    String workListURL;
    Realm realm;
    String client;
    String clientValue;
    int workListRow;
    int adapterPosition;
    int civilORelectric = 1;
    TabLayout tabLayout;
    ViewPager viewPager;
    boolean isProblem = false;
    Bitmap bitmap;
    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;
    TypeOfProblemListAdapter typeOfProblemListAdapter;
    ArrayList<TypeOfProblemsModel> typeOfProblemsModelsList = new ArrayList<>();
    String selectedProblemFromList = "no";
    private TextView txtCode;
    private TextView txtAddress;
    private LinearLayout llObersvationEdt;
    private LinearLayout id_ll_supervision;
    private LinearLayout ll_tab;
    private LinearLayout ll_bottom;
    private EditText edtObservaion;
    private ImageView btnSave;
    private ImageView btnBack;
    private ImageView img_setting;
    private ImageView btnBackNew;
    private ImageView btnCamera;
    private RelativeLayout progress_view;
    File photoFile = null;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String formattedDate = df.format(c.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anomalies2);
        realm = Realm.getDefaultInstance();
        initViews();


        getDataFromCache();
        setValuesInViews();


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obervationProblem = edtObservaion.getText().toString();

                if (Cache.getInstance().getLru().get("selectAnomaly") != null) {

                    if (isPictureTaken) {
                        updateExcelRecordAfterPhoto(bitmap, 1);

                    } else {
                        Toast.makeText(AddAnomaliesActivity.this, "Need to take picture", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AddAnomaliesActivity.this, "Select anomaly type", Toast.LENGTH_SHORT).show();
                }


                // addPicListner();

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnBackNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takePicture();

                dispatchTakePictureIntent();
            }
        });

        final AnomaliesTabLayoutAdapter adapter = new AnomaliesTabLayoutAdapter(AddAnomaliesActivity.this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    public void initViews() {
        txtCode = (TextView) findViewById(R.id.txt_code);
        txtAddress = (TextView) findViewById(R.id.txt_address);

        llObersvationEdt = (LinearLayout) findViewById(R.id.ll_obersvation_edt);
        id_ll_supervision = (LinearLayout) findViewById(R.id.id_ll_supervision);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_tab = (LinearLayout) findViewById(R.id.ll_tab);
        edtObservaion = (EditText) findViewById(R.id.edt_observaion);

        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnCamera = (ImageView) findViewById(R.id.btn_camera);
        btnSave = (ImageView) findViewById(R.id.btn_save);
        btnBackNew = (ImageView) findViewById(R.id.img_back);
        img_setting = (ImageView) findViewById(R.id.img_settings);
        progress_view = findViewById(R.id.progress_view);
        //llObersvationEdt.setVisibility(View.GONE);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Civil Anomalies"));
        tabLayout.addTab(tabLayout.newTab().setText("Electric Anomalies"));
        tabLayout.addTab(tabLayout.newTab().setText("Billboard Anomalies"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);


    }

    public void setValuesInViews() {
        txtCode.setText(adapterCode);
        txtAddress.setText(adapterAddress);
    }


    public void getDataFromCache() {
        adapterCode = Cache.getInstance().getLru().get("code").toString();
        adapterRef = Cache.getInstance().getLru().get("ref").toString();
        adapterAddress = Cache.getInstance().getLru().get("address").toString();
        workListURL = Cache.getInstance().getLru().get("fileURL").toString();
        workLisName = Cache.getInstance().getLru().get("fileName").toString();
        client = Cache.getInstance().getLru().get("client").toString();
        clientValue = Cache.getInstance().getLru().get("clientValue").toString();
        workListRow = (int) Cache.getInstance().getLru().get("row");
        adapterPosition = (int) Cache.getInstance().getLru().get("position");
        client_one = Cache.getInstance().getLru().get("client1").toString();
        client_two = Cache.getInstance().getLru().get("client2").toString();
        client_three = Cache.getInstance().getLru().get("client3").toString();
        twoDigtAdapterCOde = Cache.getInstance().getLru().get("twoDigitCode").toString();
        check = Cache.getInstance().getLru().get("anomaly").toString();
        structureType = Cache.getInstance().getLru().get("structureType").toString();

        // if (check.equals("Civil")||check.equals("Electric")) {
        if (check.equals("Problem")) {
            ll_bottom.setVisibility(View.GONE);
            llObersvationEdt.setVisibility(View.GONE);
            id_ll_supervision.setVisibility(View.GONE);
            ll_tab.setVisibility(View.VISIBLE);

            isProblem = true;

        } else {

            llObersvationEdt.setVisibility(View.VISIBLE);
            id_ll_supervision.setVisibility(View.VISIBLE);
            ll_tab.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.VISIBLE);
            isProblem = false;

        }


        InformationDatabase informationDatabase = realm.where(InformationDatabase.class).equalTo("code", twoDigtAdapterCOde).findFirst();
        databaseRow = informationDatabase.getRow();
        databaseName = informationDatabase.getDatabaseName();
        databaseURL = informationDatabase.getDatabaseURL();
        databaseAddress = informationDatabase.getAddress();
        databaseCode = informationDatabase.getOrignalDatabaseCode();

              /* MUPI
                PUENTE and Puente Led
                For bridge
                Vall is for billboard*/

        typeOfProblemsModelsList.clear();
        RealmResults<TypesOFProblemDatabase> typesOFProblemDatabase;


        if (structureType.equalsIgnoreCase("Mupi")) {
            structureType = "MUPI";
        } else if (structureType.equalsIgnoreCase("PUENTE") ||
                structureType.equalsIgnoreCase("Puente LED ")) {
            structureType = "BRIDGE";
        } else if (structureType.equalsIgnoreCase("Valla")) {
            structureType = "BILLBOARDS";
        }

       // Toast.makeText(getApplicationContext(), structureType, Toast.LENGTH_SHORT).show();
        if (check.equals("Civil")) {

            civilORelectric = 1;


            typesOFProblemDatabase = realm.where(TypesOFProblemDatabase.class).equalTo("anomalyType", "Anomalía Civil").equalTo("structureType", structureType).findAll();


            for (int i = 0; i < typesOFProblemDatabase.size(); i++) {


                TypeOfProblemsModel typeOfProblemsModel = new TypeOfProblemsModel();
                typeOfProblemsModel.setAnomalyType(typesOFProblemDatabase.get(i).getAnomalyType());
                typeOfProblemsModel.setProblems(typesOFProblemDatabase.get(i).getProblems());
                typeOfProblemsModel.setStructureType(typesOFProblemDatabase.get(i).getStructureType());
                typeOfProblemsModelsList.add(typeOfProblemsModel);
            }

        } else if (check.equals("Electric")) {


            civilORelectric = 2;

            typesOFProblemDatabase = realm.where(TypesOFProblemDatabase.class).equalTo("anomalyType", "Anomalía Iluminación"
            ).equalTo("structureType", structureType).findAll();


            for (int i = 0; i < typesOFProblemDatabase.size(); i++) {


                TypeOfProblemsModel typeOfProblemsModel = new TypeOfProblemsModel();
                typeOfProblemsModel.setAnomalyType(typesOFProblemDatabase.get(i).getAnomalyType());
                typeOfProblemsModel.setProblems(typesOFProblemDatabase.get(i).getProblems());
                typeOfProblemsModel.setStructureType(typesOFProblemDatabase.get(i).getStructureType());
                typeOfProblemsModelsList.add(typeOfProblemsModel);
            }

        } else if (check.equals("Billboard")) {
            civilORelectric = 3;
            String[] billboardArray = getResources().getStringArray(R.array.bilboar_anomalies);


            for (int i = 0; i < billboardArray.length; i++) {

                TypeOfProblemsModel typeOfProblemsModel = new TypeOfProblemsModel();
                typeOfProblemsModel.setAnomalyType("Anomaly Billboard");
                typeOfProblemsModel.setProblems(billboardArray[i]);
                typeOfProblemsModel.setStructureType("Billboard");
                typeOfProblemsModelsList.add(typeOfProblemsModel);
            }

        }


        typeOfProblemListAdapter = new TypeOfProblemListAdapter(typeOfProblemsModelsList, AddAnomaliesActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(typeOfProblemListAdapter);






     /*   civilProblemOld = informationDatabase.getCivilProblem();
        electricProblemOld = informationDatabase.getElectricProblem();
        obervationProblemOld = informationDatabase.getObervationProbe();*/
        //anomaly = electricProblemOld + "," + civilProblemOld + "," + obervationProblemOld;

    }


    public void takePicture() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".png";
        Cache.getInstance().getLru().remove("photopath");
        currentPhotoPath = createFolder(1) + "/" + imageFileName;
        File file = new File(currentPhotoPath);
        Cache.getInstance().getLru().put("photopath", currentPhotoPath);
        Uri outputFileUri = Uri.fromFile(file);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, 44);
    }

    public File createFolder(int a) {
        File myDirectory = null;
        if (a == 1) {
            myDirectory = new File(Environment.getExternalStorageDirectory(), "WorkSmarterPictures");
        } else {
            myDirectory = new File(Environment.getExternalStorageDirectory(), "WorkSmaterDatabase");
        }

        if (!myDirectory.exists()) {
            myDirectory.mkdir();
        }
        return myDirectory;//
    }

    private void galleryAddPic() {

        currentPhotoPath = Cache.getInstance().getLru().get("photopath").toString();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        bitmap = decodeFile(currentPhotoPath);

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


        if (isProblem) {
            updateExcelRecordAfterPhoto(bitmap, 0);

        }
    }

    public void showImage(Bitmap bitmap) {
        // final Dialog dialog = new Dialog(MainActivity.this);


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


                    if (!AddAnomaliesActivity.this.isFinishing() && dialog != null) {
                        dialog.dismiss();
                    }


                }
            }, 2000);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void updateExcelRecordAfterPhoto(Bitmap image, int a) {

        progress_view.setVisibility(View.VISIBLE);
        int count = adapterCode.length() - adapterCode.replace("-", "").length();


        String pathPhot = null;
        if (a == 1) {
            pathPhot = "Work Anomalies Photo";
            Toast.makeText(AddAnomaliesActivity.this, "Anomaly added", Toast.LENGTH_SHORT).show();
        } else {
            pathPhot = "Work Photos";

        }


        Backendless.Files.upload(photoFile, pathPhot, new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {
                response.getFileURL();
                Log.d("WWF", response.getFileURL());
                if (isProblem) {
                    progress_view.setVisibility(View.GONE);


                    //  Toast.makeText(AddAnomaliesActivity.this, "Removed sucessfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                //      Log.d("WWF", backendlessFault.getMessage());
                Toast.makeText(AddAnomaliesActivity.this,
                        backendlessFault.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
/*
        Backendless.Files.Android.upload(image,
                Bitmap.CompressFormat.PNG,
                100,
                txtCode.getText().toString() + " " + formattedDate + ".png",
                pathPhot,
                new AsyncCallback<BackendlessFile>() {
                    @Override
                    public void handleResponse(final BackendlessFile backendlessFile) {
                        backendlessFile.getFileURL();
                        Log.d("WWF", backendlessFile.getFileURL());

                        if (isProblem) {
                            progress_view.setVisibility(View.GONE);


                            //  Toast.makeText(AddAnomaliesActivity.this, "Removed sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Log.d("WWF", backendlessFault.getMessage());
                        Toast.makeText(AddAnomaliesActivity.this,
                                backendlessFault.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });*/


        if (isPictureTaken) {

            if (Cache.getInstance().getLru().get("selectAnomaly").toString() != null) {
                selectedProblemFromList = Cache.getInstance().getLru().get("selectAnomaly").toString();
                Anomalies anomalieModel = new Anomalies();
                anomalieModel.setCode(adapterCode);
           /* anomalieModel.setCivilAnomaly(civilProblem);
            anomalieModel.setElectricAnomaly(electricProblem);
            */
                if (civilORelectric == 1) {
                    anomalieModel.setCivilAnomaly(selectedProblemFromList);
                } else if (civilORelectric == 2) {
                    anomalieModel.setElectricAnomaly(selectedProblemFromList);
                }else if(civilORelectric==3){
                    anomalieModel.setBillboard(selectedProblemFromList);
                }
                anomalieModel.setObervation(obervationProblem);
                anomalieModel.setIsActive(1);
                Backendless.Data.of(Anomalies.class).save(anomalieModel, new AsyncCallback<Anomalies>() {
                    @Override
                    public void handleResponse(Anomalies response) {

                        progress_view.setVisibility(View.GONE);
                        BusProvider.getInstance().post("SHAN");
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {

                    }
                });
            } else {
                Toast.makeText(this, "Select anomaly first", Toast.LENGTH_SHORT).show();
            }

        } else {


           /* Log.d("SHAN", "Event posted");
            BusProvider.getInstance().post(1);*/
        }

        if (isProblem) {
            //     Toast.makeText(this, "Anomaly Removed", Toast.LENGTH_SHORT).show();

            BusProvider.getInstance().post("SHAN");
            Intent intent = getIntent();
            finish();
            startActivity(intent);


        } else {

        }

        //   saveClientLastPicInRealm(dbCode, currentPhotoPath);
        // showDataFromDatabase(activityResult);

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

     /*   ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int exifOrientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            int rotate = 90;

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

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == 33) {


            if (resultCode != RESULT_CANCELED) {


                galleryAddPic();
                setPic();


            }

        } else if (requestCode == 44) {
            if (resultCode != RESULT_CANCELED) {


                galleryAddPic();
                setPic();
                isPictureTaken = true;
            }

        }
        if (requestCode == 569) {
            //     Toast.makeText(this, "New pic taken" + photoFile.getName(), Toast.LENGTH_SHORT).show();
            isPictureTaken = true;
        }
    }

    @Override
    public void onAnomalyFixedListner(String objectID, int b) {

        //  Toast.makeText(this, "In Anomaly Acitygy ", Toast.LENGTH_SHORT).show();

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(txtCode.getText().toString() + " " + formattedDate,  /* prefix */".png",         /* suffix */storageDir      /* directory */);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext()
                                .getPackageName() + ".provider",
                        photoFile);/*    Uri photoURI = FileProvider.getUriForFile(this,
                        "com.jvidal.worksmarter",
                        photoFile);*/
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 569);
            }
        }
    }

}
