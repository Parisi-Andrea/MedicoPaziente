package com.example.andre.medicopaziente;


import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.iid.InstanceIDListenerService;

public class MyInstanceIDListenerService extends InstanceIDListenerService {
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    public void onTokenRefresh() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String codiceFiscale;
        if (prefs.contains("codiceFiscale")) {
            codiceFiscale = prefs.getString("codiceFiscale", "");
            Intent intent = new Intent(this, RegistrationIntentService.class);
            intent.putExtra("codiceFiscale", codiceFiscale);
            startService(intent);
        }
    }
}
