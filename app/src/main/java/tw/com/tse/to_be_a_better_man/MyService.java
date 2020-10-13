package tw.com.tse.to_be_a_better_man;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1, setNotification());
        Log.d("service","onCreate()");
    }
    @Override
    public int onStartCommand(Intent intent , int flags, int startId){
        Log.d("service","onStartCommand()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("BackService", new Date().toString());
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int durtime = 10*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + durtime;
        Intent i = new Intent(this,AlarmReciver.class);
        i.putExtra("identify",-1);
        PendingIntent pi = PendingIntent.getBroadcast(this, -1, i, 0);
        manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service","onDestroy()");
    }

    public Notification setNotification(){
        NotificationChannel channel = new NotificationChannel("service",
                "SERVICE",
                NotificationManager.IMPORTANCE_DEFAULT);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this,"service")
                .setContentTitle("")
                .setContentText("")
                .build();

        return notification;

    }


}
