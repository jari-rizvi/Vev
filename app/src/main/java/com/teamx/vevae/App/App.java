package com.teamx.vevae.App;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

import com.stripe.android.PaymentConfiguration;
import com.stripe.stripeterminal.TerminalApplicationDelegate;
import com.teamx.vevae.Utils.localization.LocaleManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pixplicity.easyprefs.library.Prefs;

public class App extends Application {

    // for the sake of simplicity. use DI in real apps instead
    private static Context context;
    public static LocaleManager localeManager = null;

    @Override
    protected void attachBaseContext(Context base) {
        localeManager = new LocaleManager(base); 
        super.attachBaseContext(localeManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        localeManager.setLocale(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = this;
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        FirebaseMessaging.getInstance().subscribeToTopic("vev_notification");

        TerminalApplicationDelegate.onCreate(this);

        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51DxDBcACqLSpVnkChYNe1cMPpjZ5G6KObaoyO451c5lOIHu16rH6eTiZ2LBROAwhH2III8JbIKExHjdHhtTW6D6M007PQu4Aks"
        );
    }

    public static Context getContext(){
        return context;
    }



}