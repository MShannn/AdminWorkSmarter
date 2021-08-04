package com.jvidal.worksmarter;

import android.app.Application;

import com.backendless.Backendless;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class WorkSmarteApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);
        Backendless.initApp(getApplicationContext(), "97B790A0-89D2-0CB9-FFD0-8641B4867D00", "6F4B12DF-9C30-45C1-82BE-E199E89EB948");
    }
}
