package tw.com.tse.to_be_a_better_man;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Date;

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
        /*switch (intent.getIntExtra("identify",-1)){
            case -1 :
                NotificationCompat.Builder Cdefault = new NotificationCompat.Builder(context, "VANILLA")
                        .setSmallIcon(R.drawable.ic_empty_item)
                        .setContentTitle(new Date().toString())
                        .setContentText("");
                manager.notify(3,Cdefault.build());
                break;
            case 0:
                break;
            case 2:
                break;
            case 4:
                break;
            case 6:
                Notification c6 = new Notification.Builder(context, "ChannelOf6")
                        .setSmallIcon(R.drawable.notification_icon_background)
                        .setContentTitle("C6")
                        .setContentText("C6")
                        .setAutoCancel(true)
                        .build();
                manager.notify(3,c6);
                Log.d("C6","done");
                break;
            case 8:
                break;
            case 10:
                break;
            case 12:
                break;
            case 14:
                break;
            case 16:
                break;
            case 18:
                break;
            case 20:
                break;
            case 22:
                break;
            default:
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context,MyService.class));
        } else {
            context.startService(new Intent(context,MyService.class));
        }
    }
}
