package com.jvidal.worksmarter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.jvidal.worksmarter.PDFReport.PdfCreatorExampleActivity;
import com.jvidal.worksmarter.R;
import com.jvidal.worksmarter.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);


        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right );
            }
        });

        binding.cardGetAnomaliesRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AnomaliesExportActivity.class));
                overridePendingTransition(R.anim.push_up_in,R.anim.push_down_out );
            }
        });
        binding.cardGenerateStructuresReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PdfCreatorExampleActivity.class));

            }
        });
    }
}