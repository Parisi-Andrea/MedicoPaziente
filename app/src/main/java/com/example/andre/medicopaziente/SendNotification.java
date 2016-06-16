package com.example.andre.medicopaziente;

import android.os.AsyncTask;

import com.example.damiano.myapplication.backend.messaging.Messaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;


/**
 * Created by Damiano on 15/06/2016.
 */
public class SendNotification extends AsyncTask<String, Void, Void> {
    String cFDestinatario = "CHNDMN94B11L378G";
    private Exception exception;

    protected Void doInBackground(String... message) {
        try {
            message[0] = cFDestinatario.concat(message[0]);
            Messaging.Builder builder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                    // otherwise they can be skipped
                /*.setRootUrl("http://192.168.42.217:8080/_ah/api/")
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                            throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });*/
                    .setRootUrl("https://pazientemedico.appspot.com/_ah/api/");
            Messaging messaging = builder.build();
            try {
                messaging.sendMessage(message[0]).execute();
            }catch(Exception e){
                System.out.println(e);
            }
            return null;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onPostExecute(Void feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
