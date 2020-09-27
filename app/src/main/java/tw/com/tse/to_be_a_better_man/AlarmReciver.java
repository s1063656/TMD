package tw.com.tse.to_be_a_better_man;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import java.util.TimeZone;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReciver extends BroadcastReceiver {
    String[] arrayOfAlarmString = {"", "", "", "", "", "", "", "", "", "", "", ""};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int inInt ;
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        inInt=intent.getIntExtra("ifentify",-1);
        Log.d("intent extra", inInt + "");
        if (inInt != -1) {
            for (int i =1;i<MainActivity.mainHabitList.size();i++) {
                    Log.d("check",i+"");
                    if(Integer.parseInt(MainActivity.mainHabitList.get(i).get("time").toString())==inInt) {
                        arrayOfAlarmString[inInt / 2] += " [ " + MainActivity.mainHabitList.get(i).get("habitName").toString() + " ] ";
                        MainActivity.mainHabitList.get(i).put("safety", 0);
                        db.collection(MainActivity.user).document(MainActivity.mainHabitList.get(i).get("habitName").toString()).set(MainActivity.mainHabitList.get(i))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                    }
            }

            main_farm.main_adapter.notifyDataSetChanged();
            Notification notification = new Notification.Builder(context, MainActivity.channels[inInt])
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentTitle("該來照顧一下你的香草囉")
                    .setContentText(arrayOfAlarmString[inInt])
                    .setAutoCancel(true)
                    .build();
            manager.notify(inInt, notification);
            setAlarm(inInt,context);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MyService.class));
        } else {
            context.startService(new Intent(context, MyService.class));
        }

    }
    private void setAlarm(int identifier,Context context) {
        Intent intent = new Intent(context, AlarmReciver.class);
        intent.putExtra("identify", identifier);
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, identifier, intent, 0);
        long firstTime = SystemClock.elapsedRealtime();
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        //測試完要套用
        calendar.set(Calendar.HOUR_OF_DAY,identifier);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        //calendar.add(Calendar.SECOND, 10);

        long selectTime = calendar.getTimeInMillis();
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        long time = selectTime - systemTime;
        firstTime += time;
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, pendingIntent);
        Log.d("setExactAlarm", "set :" + identifier);
    }
}
