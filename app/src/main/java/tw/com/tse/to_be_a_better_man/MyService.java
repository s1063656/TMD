package tw.com.tse.to_be_a_better_man;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

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
        //这里是定时的,这里设置的是每隔两秒打印一次时间
        int durtime = 10*60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + durtime;

        Intent i = new Intent(this,AlarmReciver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service","onDestroy()");
    }
}
