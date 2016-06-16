package com.example.andre.medicopaziente;


import android.app.IntentService;
import android.content.Intent;

import com.example.damiano.myapplication.backend.registration.Registration;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;


public class RegistrationIntentService extends IntentService {
    String codiceFiscale="CHNDMN94B11L378G";

    private static final String TAG = "RegIntentService";

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
        }catch(Exception e){
        }
    }
    @Override
    public void onHandleIntent(Intent intent) {
        try{
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token);

        } catch (Exception e) {
        }

    }

}
