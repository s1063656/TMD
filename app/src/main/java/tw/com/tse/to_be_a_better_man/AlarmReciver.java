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

import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import tw.com.tse.to_be_a_better_man.RoomDB.DataBase;
import tw.com.tse.to_be_a_better_man.RoomDB.MyData;

import static android.app.Notification.FLAG_AUTO_CANCEL;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static tw.com.tse.to_be_a_better_man.MainActivity.mainHabitList;

public class AlarmReciver extends BroadcastReceiver {
    String[] arrayOfAlarmString = {"", "", "", "", "", "", "", "", "", "", "", ""};
    int identifier;
    boolean[] alarms = {false, false, false, false, false, false, false, false, false, false, false, false};

    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        identifier = intent.getIntExtra("identify", -1);
        if (identifier != -1) {
            Log.d("intent extra", identifier + "");
            for (int i = 1; i < MainActivity.mainHabitList.size(); i++) {
                if (MainActivity.mainHabitList.get(i).get("status").toString().equals("0")) {
                    end(context, i);
                } else {
                    if (Integer.parseInt(MainActivity.mainHabitList.get(i).get("time").toString()) / 2 == identifier) {
                        arrayOfAlarmString[identifier] += " [ " + MainActivity.mainHabitList.get(i).get("habitName").toString() + " ] ";
                        timeUp(context,i);
                    }
                }
            }
            main_farm.main_adapter.notifyDataSetChanged();
            if (!arrayOfAlarmString[identifier].equals("")) {
                Notification notification = new NotificationCompat.Builder(context, MainActivity.channels[identifier])
                        .setSmallIcon(R.drawable.basil_1)
                        .setContentTitle("該來照顧一下你的香草囉")
                        .setContentText(arrayOfAlarmString[identifier])
                        .setAutoCancel(true)
                        .build();
                manager.notify(identifier, notification);
                setAlarm(identifier, context);
            } else {//假設現在時間沒有任何習慣,也可以檢查是否前兩小時的習慣沒有check
                for (int i = 1; i < MainActivity.mainHabitList.size(); i++) {
                    if (MainActivity.mainHabitList.get(i).get("status").toString().equals("0")) {
                        end(context, i);
                    }
                }
            }
        } else {
            for (int i = 1; i < MainActivity.mainHabitList.size(); i++) {
                Log.d("check", i + "");
                //arrayOfAlarmString[Integer.parseInt(MainActivity.mainHabitList.get(i).get("time").toString()) / 2] += " [ " + MainActivity.mainHabitList.get(i).get("habitName").toString() + " ] ";
                alarms[Integer.parseInt(MainActivity.mainHabitList.get(i).get("time").toString()) / 2] = true;
            }
            for (int i = 0; i < 12; i++) {
                if (alarms[i]) {
                    setAlarm(i, context);
                    Log.d("set alarm", i + ".alarm");
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, MyService.class));
            } else {
                context.startService(new Intent(context, MyService.class));
            }
        }
    }


    private void setAlarm(int identifier, Context context) {
        Intent intent = new Intent(context, AlarmReciver.class);
        intent.putExtra("identify", identifier);
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, identifier, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstTime = SystemClock.elapsedRealtime();
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, identifier * 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long selectTime = calendar.getTimeInMillis();
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        long time = selectTime - systemTime;
        firstTime += time;

        manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, pendingIntent);
        Log.d("setAlarmAtAlarmReceiver", "set ALARM num : " + time);
    }

    public String calculateTheDay(String date) throws ParseException {
        Date last = new SimpleDateFormat("yyyy/MM/dd").parse(date);
        Date now = new Date(System.currentTimeMillis());
        long l = last.getTime() - now.getTime() > 0 ? last.getTime() - now.getTime() :
                now.getTime() - last.getTime();
        return Long.toString(l / (24 * 60 * 60 * 1000));
    }

    private void end(final Context context, int i) {
        MainActivity.historyList.add(mainHabitList.get(i));
        final Map<String, Object> habit = mainHabitList.remove(i);
        habit.put("status", -1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyData oldData = DataBase.getInstance(context).getDataUao().findDataByName(habit.get("habitName").toString()).get(0);
                DataBase.getInstance(context).getDataUao().updateData(oldData.getId(), habit.get("habitName").toString(),
                        Integer.parseInt(habit.get("time").toString()),
                        habit.get("date").toString(),
                        Integer.parseInt(habit.get("status").toString()));
            }
        }).start();
    }

    private void timeUp(final Context context, int i){
        MainActivity.mainHabitList.get(i).put("status", 0);
        final Map<String, Object> habit = mainHabitList.get(i);
        habit.put("status", 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyData oldData = DataBase.getInstance(context).getDataUao().findDataByName(habit.get("habitName").toString()).get(0);
                DataBase.getInstance(context).getDataUao().updateData(oldData.getId(), habit.get("habitName").toString(),
                        Integer.parseInt(habit.get("time").toString()),
                        habit.get("date").toString(),
                        Integer.parseInt(habit.get("status").toString()));
            }
        }).start();
    }
}
