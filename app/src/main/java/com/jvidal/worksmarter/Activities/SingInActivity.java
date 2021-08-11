package com.jvidal.worksmarter.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.jvidal.worksmarter.Adapters.CustomSpinnerAdapter;
import com.jvidal.worksmarter.Models.Anomalies;
import com.jvidal.worksmarter.Models.InformationDatabaseModel;
import com.jvidal.worksmarter.R;
import com.jvidal.worksmarter.RealmDatabase.InformationDatabase;
import com.jvidal.worksmarter.RealmDatabase.TypesOFProblemDatabase;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class SingInActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String selectedList = "Select work list";
    String selectedURL = "dummy";
    String databaseURL;
    String databaseName;
    String databaseCode;
    String databaseAddress;
    String databaseCordinates;
    String codeOne;
    String codeTwo;
    String finalCode;
    String databaseStructureType;
    String structureType;
    String anomalyType;
    String problems;
    Double lati;
    Double longi;
    //int r = 0;
    int c = 0;
    int rowDatabase = 0;
    ArrayList<InformationDatabaseModel> informationDatabaseModels = new ArrayList<>();
    Realm realm;
    ArrayList<String> urls;
    String[] permissions = new String[]{

            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
    };
    private LinearLayout ll_new_account;
    private Button btn_login;
    private TextInputEditText edtPassword;
    private TextView txtForgotPassowrd;
    private RelativeLayout progressView;
    private EditText edt_email;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
        btn_login = findViewById(R.id.btn_login);
        ll_new_account = findViewById(R.id.ll_new_account);
        edtPassword = (TextInputEditText) findViewById(R.id.edt_password);
        txtForgotPassowrd = (TextView) findViewById(R.id.txt_forgot_passowrd);
        progressView = (RelativeLayout) findViewById(R.id.progress_view);
        edt_email = (EditText) findViewById(R.id.edt_email);
        progressView.setVisibility(View.GONE);


        txtForgotPassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "in process", Toast.LENGTH_SHORT).show();
            }
        });

        databaseURL = getIntent().getStringExtra("databaseURL");
        databaseName = getIntent().getStringExtra("databaseName");
        Toast.makeText(getApplicationContext(), "Database url=" + databaseURL, Toast.LENGTH_SHORT).show();
        realm = Realm.getDefaultInstance();

        TypesOFProblemDatabase typesOFProblemDatabase = realm.where(TypesOFProblemDatabase.class).findFirst();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        if (checkPermissions()) {
            try {
                downloadFile(databaseURL);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                downloadFile(databaseURL);

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        statusCheck();
        urls = (ArrayList<String>) getIntent().getSerializableExtra("url");
        ll_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "in process", Toast.LENGTH_SHORT).show();

                // Intent intent = new Intent(SingInActivity.this, SignupActivity.class);
                // startActivity(intent);
            }

        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressView.setVisibility(View.VISIBLE);
                if (selectedList.equals("Select work list")) {
                    Toast.makeText(SingInActivity.this, "Select work list first", Toast.LENGTH_SHORT).show();
                    progressView.setVisibility(View.GONE);
                } else {

                    progressView.setVisibility(View.VISIBLE);
                    Backendless.UserService.login(
                            edt_email.getText().toString(),
                            edtPassword.getText().toString(),
                            new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse(BackendlessUser response) {


                                    Intent intent = new Intent(SingInActivity.this, MainActivity.class);
                                    intent.putExtra("selectedList", selectedList);
                                    intent.putExtra("selectedURL", selectedURL);
                                    startActivity(intent);
                                    progressView.setVisibility(View.GONE);
                                    finish();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    progressView.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SingInActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }

            }
        });


        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("mylist");
        spinner = (Spinner) findViewById(R.id.spinner);


        CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(SingInActivity.this, myList, null, true);
        spinner.setAdapter(customAdapter);
        spinner.setOnItemSelectedListener(SingInActivity.this);



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


      /*  TextView textVie = view.findViewById(R.id.txt_spinner);
        textVie.setText(parent.getItemAtPosition(position).toString());
*/
        selectedList = parent.getItemAtPosition(position).toString();
        selectedURL = urls.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                try {
                    downloadFile(databaseURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return;
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Name");
        View view = LayoutInflater.from(this).inflate(R.layout.gps_dialog, null);
        builder.setView(view);

        ImageView done = view.findViewById(R.id.img_done);
        ImageView cancel = view.findViewById(R.id.img_cancle);
        final AlertDialog dialog = builder.create();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.cancel();
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

                case Cell.CELL_TYPE_BLANK:

                default:
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
            Log.d("SHAN", "exceptio bn" + e.getMessage());
        }
        return value;
    }

    public void downloadFile(final String fileInfo) throws IOException {
        try {

            progressView.setVisibility(View.VISIBLE);
            checkFileExist(databaseName);
            Uri downloadUri = Uri.parse(fileInfo);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            //  request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(createFolder().getName(), databaseName);
            long id = downloadManager.enqueue(request);
            boolean downloading = true;
            while (downloading) {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(id); //filter by id which you have receieved when reqesting download from download manager
                Cursor cursor = downloadManager.query(q);
                cursor.moveToFirst();

                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false;

                }
            }


            Log.d("SHAN", "DOWNOLOAD DONE");
            progressView.setVisibility(View.GONE);
            InformationDatabase workListDatabasecheck = realm.where(InformationDatabase.class).findFirst();

            if (workListDatabasecheck != null) {

                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
            }


            new ReadDatabaseInformationAsyncTash().execute();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public boolean checkFileExist(String workListFile) {
        File file = new File(createFolder(), workListFile);
        if (file.exists()) {
            file.delete();
            return true;
        } else
            return false;
    }

    public File createFolder() {
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "WorkSmaterDatabase");
        // File myDirectory = new File(getApplicationContext().getFilesDir(), "WorkSmaterDatabase");
        if (!myDirectory.exists()) {
            myDirectory.mkdir();
        }
        return myDirectory;
    }

    class ReadDatabaseInformationAsyncTash extends AsyncTask<Void, Integer, String> {
        String TAG = "SHAN";

        protected void onPreExecute() {
            super.onPreExecute();

            ll_new_account.setClickable(false);
            btn_login.setClickable(false);
            progressView.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... arg0) {
            try {


                FileInputStream file = new FileInputStream(new File(createFolder(), databaseName));
                // FileInputStream file = new FileInputStream(new File(createFolder(), "Listado 22-01-2020.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(file);


                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowsCount = sheet.getPhysicalNumberOfRows();
                FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();


                for (rowDatabase = 1; rowDatabase < rowsCount; rowDatabase++) {
                    Row row = sheet.getRow(rowDatabase);
                    int cellsCount = row.getPhysicalNumberOfCells();
                    for (c = 0; c < cellsCount; c++) {
                        String value = getCellAsString(row, c, formulaEvaluator, sheet, rowDatabase);
                        if (c == 0) {
                            value = value.substring(0, value.length());
                            databaseCode = value;
                        } else if (c == 1) {
                            databaseAddress = value;
                        } else if (c == 3) {
                            databaseCordinates = value;
                        } else if (c == 4) {
                            databaseStructureType = value;
                        }

                    }

                    int count = databaseCode.length() - databaseCode.replace("-", "").length();

                    if (count > 1) {
                        String segments[] = databaseCode.split("-");
                        codeOne = segments[0];
                        codeTwo = segments[1];
                        finalCode = codeOne + "-" + codeTwo;
                    } else {
                        finalCode = databaseCode;
                    }

                    String segments[] = databaseCordinates.split(",");
                    lati = Double.valueOf(segments[0]);
                    longi = Double.valueOf(segments[1]);
                    InformationDatabaseModel informationDatabaseModel = new InformationDatabaseModel();
                    informationDatabaseModel.setRow(rowDatabase);
                    informationDatabaseModel.setAddress(databaseAddress);
                    informationDatabaseModel.setCode(finalCode);
                    informationDatabaseModel.setLattitude(lati);
                    informationDatabaseModel.setLongitude(longi);
                    informationDatabaseModel.setOrignalDatabasecode(databaseCode);
                    informationDatabaseModel.setDatabaseName(databaseName);
                    informationDatabaseModel.setDatabaseURL(databaseURL);
                    informationDatabaseModel.setDatabaseStructureType(databaseStructureType);
                    informationDatabaseModels.add(informationDatabaseModel);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressView.setVisibility(View.GONE);
                    }
                });


            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

               /* MUPI
                PUENTE and Puente Led
                For bridge
                Vall is for billboard*/


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
            ll_new_account.setClickable(true);
            btn_login.setClickable(true);


            for (int i = 0; i < informationDatabaseModels.size(); i++) {
                realm.beginTransaction();
                InformationDatabase informationDatabase = realm.createObject(InformationDatabase.class);
                informationDatabase.setRow(informationDatabaseModels.get(i).getRow());
                informationDatabase.setAddress(informationDatabaseModels.get(i).getAddress());
                informationDatabase.setCode(informationDatabaseModels.get(i).getCode());
                informationDatabase.setLattitude(informationDatabaseModels.get(i).getLattitude());
                informationDatabase.setLongitude(informationDatabaseModels.get(i).getLongitude());
                informationDatabase.setOrignalDatabaseCode(informationDatabaseModels.get(i).getOrignalDatabasecode());
                informationDatabase.setDatabaseName(informationDatabaseModels.get(i).getDatabaseName());
                informationDatabase.setDatabaseURL(informationDatabaseModels.get(i).getDatabaseURL());
                informationDatabase.setDatabaseStructureType(informationDatabaseModels.get(i).getDatabaseStructureType());
              /*  informationDatabase.setCivilProblem(informationDatabaseModels.get(i).getCivilProblem());
                informationDatabase.setElectricProblem(informationDatabaseModels.get(i).getElectricProblem());
                informationDatabase.setObervationProbe(informationDatabaseModels.get(i).getObervationProbe());*/


                realm.commitTransaction();
            }
            //  realm.close();


            new AnomaliesAsyncTaske().execute();
            //progressView.setVisibility(View.GONE);
        }
    }

    class AnomaliesAsyncTaske extends AsyncTask<Void, Integer, String> {
        String TAG = "SHAN";

        protected void onPreExecute() {
            super.onPreExecute();

            ll_new_account.setClickable(false);
            btn_login.setClickable(false);
            progressView.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... arg0) {
            try {

                realm = Realm.getDefaultInstance();

                FileInputStream file = new FileInputStream(new File(createFolder(), databaseName));
                // FileInputStream file = new FileInputStream(new File(createFolder(), "Listado 22-01-2020.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(file);


                XSSFSheet sheetAnomaly = workbook.getSheetAt(1);
                int rowsCountAnomaly = sheetAnomaly.getPhysicalNumberOfRows();
                FormulaEvaluator formulaEvaluatorAnomaly = workbook.getCreationHelper().createFormulaEvaluator();


                //  Log.d("SHAN", "Second sheet" + rowsCountAnomaly);
                for (int dataRow = 1; dataRow < rowsCountAnomaly; dataRow++) {
                    Row rowAnomaly = sheetAnomaly.getRow(dataRow);
                    int cellsCount = rowAnomaly.getPhysicalNumberOfCells();


                    for (c = 0; c < cellsCount; c++) {
                        String value = getCellAsString(rowAnomaly, c, formulaEvaluatorAnomaly, sheetAnomaly, dataRow);
                        if (c == 0) {
                            value = value.substring(0, value.length());
                            structureType = value;
                        } else if (c == 1) {
                            anomalyType = value;
                        } else if (c == 2) {
                            problems = value;
                        }
                        //   Log.d("SHAN", "Second sheet" + rowsCountAnomaly + "   " + problems + "   " + structureType + "    " + anomalyType);

                    }


                    realm.beginTransaction();
                    TypesOFProblemDatabase typesOFProblemDatabase = realm.createObject(TypesOFProblemDatabase.class);
                    typesOFProblemDatabase.setStructureType(structureType);

                    //  Log.d("SHAN", "Anomalies=" + structureType + "    " + anomalyType + "       " + problems);
                    typesOFProblemDatabase.setAnomalyType(anomalyType);
                    typesOFProblemDatabase.setProblems(problems);
                    realm.commitTransaction();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressView.setVisibility(View.GONE);
                        }
                    });
                }


            } catch (final Exception e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

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
            ll_new_account.setClickable(true);
            btn_login.setClickable(true);


            progressView.setVisibility(View.GONE);
        }
    }

}
