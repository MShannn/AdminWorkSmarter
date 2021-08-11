package com.jvidal.worksmarter.Activities;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.jvidal.worksmarter.Adapters.AnomaliesExportedAdapter;
import com.jvidal.worksmarter.Adapters.CustomSpinnerAdapter;
import com.jvidal.worksmarter.Models.Anomalies;
import com.jvidal.worksmarter.R;
import com.jvidal.worksmarter.databinding.ActivityAnomaliesExportBinding;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnomaliesExportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityAnomaliesExportBinding binding;
    AnomaliesExportedAdapter anomaliesExportedAdapter;
    List<Anomalies> anomaliesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_anomalies_export);


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.imgNorecord.setVisibility(View.GONE);
        binding.progressView.setVisibility(View.VISIBLE);
        Backendless.Data.of("Anomalies").getObjectCount(new AsyncCallback<Integer>() {
            @Override
            public void handleResponse(Integer integer) {
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setPageSize(integer).setOffset(0);
                Backendless.Data.of(Anomalies.class).find(queryBuilder,
                        new AsyncCallback<List<Anomalies>>() {
                            @Override
                            public void handleResponse(List<Anomalies> response) {
                                anomaliesArrayList = response;

/*
                                Map<String, Anomalies> cleanMap = new LinkedHashMap<String, Anomalies>();
                                for (int i = 0; i < response.size(); i++) {
                                    cleanMap.put(response.get(i).getCode(), response.get(i));
                                }
                                List<Anomalies> list = new ArrayList<Anomalies>(cleanMap.values());*/

                                anomaliesExportedAdapter = new AnomaliesExportedAdapter(response, getApplicationContext());
                                binding.recyclerView.setAdapter(anomaliesExportedAdapter);
                                binding.progressView.setVisibility(View.GONE);
                                if (response.size() < 1) {
                                    binding.imgNorecord.setVisibility(View.VISIBLE);
                                }


                                //  setSpinnerAdapter(list);

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                binding.progressView.setVisibility(View.GONE);

                                Toast.makeText(getApplicationContext(), fault.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                binding.progressView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), backendlessFault.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Size=" + anomaliesArrayList.size(), Toast.LENGTH_SHORT).show();
                createExcelSheet();

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("SHAN", "onItemSelected=" + parent.getItemAtPosition(position).toString());

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("SHAN", "onNothingSelected=" + parent.getSelectedItem().toString());
    }

    public void setSpinnerAdapter(List<Anomalies> anomaliesArrayList) {

        CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(AnomaliesExportActivity.this, null, anomaliesArrayList, false);
        binding.spinnerAnomalies.setAdapter(customAdapter);
        binding.spinnerAnomalies.setOnItemSelectedListener(AnomaliesExportActivity.this);
    }

    public File createFolder() {
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "WorkSmarterAnomalies");
        if (!myDirectory.exists()) {
            myDirectory.mkdir();
        }
        return myDirectory;
    }

    public void createExcelSheet() {
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet("sheet1");
        XSSFRow row = sheet.createRow(0);
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("Code");


/*
        for (int j = 0; j < anomaliesArrayList.size(); j++) {
            XSSFRow row = sheet.createRow(j);


            if (j == 0) {
                XSSFCell cell = row.createCell(0);
                cell.setCellValue("Code");
                XSSFCell cell1 = row.createCell(1);
                cell1.setCellValue("Civil Anomaly");
                XSSFCell cel2 = row.createCell(2);
                cel2.setCellValue("Electric Anomaly");
                XSSFCell cell3 = row.createCell(3);
                cell3.setCellValue("Billboard Anomaly");
                XSSFCell cell4 = row.createCell(4);
                cell4.setCellValue("Observation");
                XSSFCell cell5 = row.createCell(5);
                cell5.setCellValue("isActive");
            } else {
                XSSFCell cell = row.createCell(0);
                cell.setCellValue(anomaliesArrayList.get(j).getCode());
                XSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(anomaliesArrayList.get(j).getCivilAnomaly());
                XSSFCell cel2 = row.createCell(2);
                cel2.setCellValue(anomaliesArrayList.get(j).getElectricAnomaly());
                XSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(anomaliesArrayList.get(j).getBillboard());
                XSSFCell cell4 = row.createCell(4);
                cell4.setCellValue(anomaliesArrayList.get(j).getObervation());
                XSSFCell cell5 = row.createCell(5);
                cell5.setCellValue(anomaliesArrayList.get(j).getIsActive() + "");

            }


        }*/


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = df.format(c.getTime());


        File fileUpdated = new File(createFolder(), "Anomalies.xlsx");

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileUpdated);
            workBook.write(out);
            out.close();
            Toast.makeText(getApplicationContext(), "File has been saved " + fileUpdated.getPath(), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}