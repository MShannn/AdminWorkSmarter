package com.jvidal.worksmarter.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jvidal.worksmarter.Adapters.ImagesStructureTabLayoutAdapter;
import com.jvidal.worksmarter.R;
import com.jvidal.worksmarter.RealmDatabase.InformationDatabase;

import io.realm.Realm;

public class AllImagesActivity extends AppCompatActivity {


    String structureIdTwoDigit;
    Realm realm;
    private ImageView imgBack;
    private ImageView imgSettings;
    private TextView txtCode;
    private TextView txtAddress;
    private LinearLayout llTab;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_images);
        initViews();
        realm = Realm.getDefaultInstance();
        settingData();


        final ImagesStructureTabLayoutAdapter adapter = new ImagesStructureTabLayoutAdapter(AllImagesActivity.this, getSupportFragmentManager(), tabLayout.getTabCount(), structureIdTwoDigit);
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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AllImagesActivity.this, "In process", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void initViews() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgSettings = (ImageView) findViewById(R.id.img_settings);
        txtCode = (TextView) findViewById(R.id.txt_code);
        txtAddress = (TextView) findViewById(R.id.txt_address);
        llTab = (LinearLayout) findViewById(R.id.ll_tab);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Structures"));
        tabLayout.addTab(tabLayout.newTab().setText("Structure Anomalies"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    public void settingData() {
        structureIdTwoDigit = getIntent().getStringExtra("structureID");
        InformationDatabase informationDatabase = realm.where(InformationDatabase.class).equalTo("code", structureIdTwoDigit).findFirst();
        if (informationDatabase != null) {

            txtCode.setText(informationDatabase.getOrignalDatabaseCode());
            txtAddress.setText(informationDatabase.getAddress());

        }
    }
}
