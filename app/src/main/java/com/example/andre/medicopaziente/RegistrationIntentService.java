package com.example.andre.medicopaziente;


import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.damiano.myapplication.backend.registration.Registration;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;


public class RegistrationIntentService extends IntentService {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "RegIntentService";
    String codiceFiscale;

    public RegistrationIntentService() {
        super(TAG);
    }
    private void sendRegistrationToServer(String token) {
        token = codiceFiscale.concat(token);
        Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                .setRootUrl("https://pazientemedico.appspot.com/_ah/api/");
        Registration regService = builder.build();
        try {
            regService.register(token).execute();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putBoolean("GCMRegistration", true);
        }catch(Exception e){
        }
    }
    @Override
    public void onHandleIntent(Intent intent) {
        try{
            codiceFiscale = intent.getStringExtra("codiceFiscale");
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token);

        } catch (Exception e) {
        }

    }

}
