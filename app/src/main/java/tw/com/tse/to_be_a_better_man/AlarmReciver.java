package tw.com.tse.to_be_a_better_man;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Date;

public class AlarmReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "VANILLA")
                .setSmallIcon(R.drawable.ic_empty_item)
                .setContentTitle(new Date().toString())
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(3,builder.build());
        context.startForegroundService(new Intent(context,MyService.class));
    }
}
