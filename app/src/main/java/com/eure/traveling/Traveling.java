package com.eure.traveling;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Traveling extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this).build());
    }
}
