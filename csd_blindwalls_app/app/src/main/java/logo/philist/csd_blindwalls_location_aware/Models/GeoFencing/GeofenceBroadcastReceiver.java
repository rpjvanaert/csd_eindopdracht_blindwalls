package logo.philist.csd_blindwalls_location_aware.Models.GeoFencing;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import logo.philist.csd_blindwalls_location_aware.R;


public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private final String CHANNEL_ID = "10019";

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
      this.context = context;
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);


        List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();

//        geofencingEvents.forEach((i)->{
//            if (com.example.dewegwijzer.logic.GeoFenceManager.getInstance()!= null) {
//                com.example.dewegwijzer.logic.GeoFenceManager.getInstance().alert(i.getRequestId());
//            }else {
//                createNotificationChannel();
//                popup();
//            }
//
//        });

        for (Geofence event : geofences){
            if (GeoFenceManager.getInstance() != null){
                GeoFenceManager.getInstance().alert(event.getRequestId());
            } else {
                createNotificationChannel();
                notification();
            }
        }
    }

    private void notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_location_on_24)
                .setContentTitle(context.getString(R.string.location_reached))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(10019,builder.build());
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "logo.philist.csd_blindwalls_location_aware.popups";
            String description = context.getString(R.string.notification_title);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
