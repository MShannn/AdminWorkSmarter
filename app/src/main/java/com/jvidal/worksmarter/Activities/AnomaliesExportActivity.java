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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
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

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_up_in,R.anim.push_down_out );
            }
        });

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
                binding.progressView.setVisibility(View.VISIBLE);
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


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Anomalies");
        HSSFRow rowA = firstSheet.createRow(0);
        // HSSFCell cellA = rowA.createCell(0);
        //cellA.setCellValue(new HSSFRichTextString("Sheet One"));
        FileOutputStream fos = null;


        for (int j = 0; j < anomaliesArrayList.size() + 1; j++) {
            HSSFRow row = firstSheet.createRow(j);


            if (j == 0) {
                HSSFCell cell = row.createCell(0);
                cell.setCellValue("Code");
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue("Civil Anomaly");
                HSSFCell cel2 = row.createCell(2);
                cel2.setCellValue("Electric Anomaly");
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue("Billboard Anomaly");
                HSSFCell cell4 = row.createCell(4);
                cell4.setCellValue("Observation");
                HSSFCell cell5 = row.createCell(5);
                cell5.setCellValue("isActive");
            } else {
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(anomaliesArrayList.get(j - 1).getCode());
                HSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(anomaliesArrayList.get(j - 1).getCivilAnomaly());
                HSSFCell cel2 = row.createCell(2);
                cel2.setCellValue(anomaliesArrayList.get(j - 1).getElectricAnomaly());
                HSSFCell cell3 = row.createCell(3);
                cell3.setCellValue(anomaliesArrayList.get(j - 1).getBillboard());
                HSSFCell cell4 = row.createCell(4);
                cell4.setCellValue(anomaliesArrayList.get(j - 1).getObervation());
                HSSFCell cell5 = row.createCell(5);
                cell5.setCellValue(anomaliesArrayList.get(j - 1).getIsActive() + "");

            }


        }


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String formattedDate = df.format(c.getTime());


        try {

            File file;
            file = new File(createFolder(), "Anomalies" + formattedDate + ".xls");
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                binding.progressView.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "File has been saved in WorkSmarterAnomalies folder", Toast.LENGTH_LONG).show();
            }
        }

    }
}