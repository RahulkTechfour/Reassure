package com.luminous.pdi;

import android.app.Application;
import android.os.StrictMode;

import com.luminous.pdi.core.RxBus;

public class PDIApplication extends Application {

    public static boolean isPermissionDialogShow = false;
    private static PDIApplication myApplication;

    private RxBus bus;

    @Override
    public void onCreate() {
        super.onCreate();
      /*  Fabric.with(this, new Crashlytics());
        try {
            FirebaseApp.initializeApp(this);

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        myApplication = this;
        bus = new RxBus();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    public static PDIApplication getApplication() {
        return myApplication;
    }

    public RxBus bus() {
        return bus;
    }
}