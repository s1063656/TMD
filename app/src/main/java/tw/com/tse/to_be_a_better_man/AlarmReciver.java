package tw.com.tse.to_be_a_better_man;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Log.d("intent extra",intent.getIntExtra("identify",-1)+"");
        if(intent.getIntExtra("identify",-1)!=-1) {
            String str = "";
            for (String k : MainActivity.mainAlarms.get(intent.getIntExtra("identify", -1))) {
                str +="  [ "+ k + " ]";
            }
            Notification notification = new Notification.Builder(context, MainActivity.channels[intent.getIntExtra("identify", -1)])
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentTitle(MainActivity.channels[intent.getIntExtra("identify", -1)])
                    .setContentText(str)
                    .setAutoCancel(true)
                    .build();
            manager.notify(3, notification);
            Log.d(MainActivity.channels[intent.getIntExtra("identify", -1)], "done");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context,MyService.class));
        } else {
            context.startService(new Intent(context,MyService.class));
        }
    }
}
