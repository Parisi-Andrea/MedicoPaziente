package com.example.andre.medicopaziente;

/**
 * Created by Damiano on 07/06/2016.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    String TAG = getClass().getName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String msg = data.getString("message");
        notifyUser(getApplicationContext(),msg);
    }

    public void notifyUser(Context context,String message){
        String type = message.substring(0,3); // either "p2m" or "m2p"
        message = message.substring(3);
        Intent intent = new Intent(this, MainActivity.class);
        if(type.equals("p2m")){
            intent.putExtra("richiesta", "");
        } else {
            String idRichiesta = message.substring(0,9);
            message = message.substring(9);
            intent.putExtra("richiesta", idRichiesta);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.logo)
                .setContentTitle("MedicoPaziente")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }
}
