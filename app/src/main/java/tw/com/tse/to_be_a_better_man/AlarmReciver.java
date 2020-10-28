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

import java.util.ArrayList;
import java.util.Calendar;


import java.util.Map;
import java.util.TimeZone;

import tw.com.tse.to_be_a_better_man.RoomDB.DataBase;
import tw.com.tse.to_be_a_better_man.RoomDB.MyData;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static tw.com.tse.to_be_a_better_man.MainActivity.historyList;
import static tw.com.tse.to_be_a_better_man.MainActivity.mainHabitList;

public class AlarmReciver extends BroadcastReceiver {

    int identifier;
    ArrayList<String> gotoHistory = new ArrayList();
    @Override
    public void onReceive(final Context context, Intent intent) {
        String[] arrayOfAlarmString = {"", "", "", "", "", "", "", "", "", "", "", ""};
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        identifier = intent.getIntExtra("identify", -1);

        Log.d("identifier",identifier+"");
        if (identifier != -1) {
            for (int i = 1; i < MainActivity.mainHabitList.size(); i++) {
                Log.d("identifier",i+"");
                Log.d("status",i+ mainHabitList.get(i).get("habitName").toString()+" status :"+Integer.parseInt(MainActivity.mainHabitList.get(i).get("status").toString()));
                if (Integer.parseInt(MainActivity.mainHabitList.get(i).get("status").toString())==0) {
                    gotoHistory.add(mainHabitList.get(i).get("habitName").toString());
                } else {
                    if (Integer.parseInt(MainActivity.mainHabitList.get(i).get("time").toString()) / 2 == identifier) {
                        arrayOfAlarmString[identifier] += " [ " + MainActivity.mainHabitList.get(i).get("habitName").toString() + " ] ";
                        timeUp(context, i);
                    }
                }
            }
            if(!arrayOfAlarmString[identifier].equals("")){
                main_farm.main_adapter.notifyDataSetChanged();
                Notification notification = new NotificationCompat.Builder(context, MainActivity.channels[identifier])
                        .setSmallIcon(R.drawable.basil_1)
                        .setContentTitle("該來照顧一下你的香草囉")
                        .setContentText(arrayOfAlarmString[identifier])
                        .setAutoCancel(true)
                        .build();
                manager.notify(identifier, notification);
            }

        } else {
            for (int i = 0; i < 12; i++) {
                setAlarm(i, context);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, MyService.class));
            } else {
                context.startService(new Intent(context, MyService.class));
            }
        }

        end(context,gotoHistory);
        gotoHistory.clear();
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
        Log.d("setAlarmAtAlarmReceiver", "" + time);
    }

    private void end(final Context context, ArrayList<String> i) {
        while(!i.isEmpty()){
            String p = i.remove(0);
            for(int j=1;j< mainHabitList.size();j++){
                if(mainHabitList.get(j).get("habitName").toString().equals(p)){
                    MainActivity.historyList.add(mainHabitList.remove(j));
                    break;
                }
            }

            final Map<String, Object> habit =historyList.get(historyList.size()-1);
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
            Log.d("gotoHistory",habit.get("habitName").toString());
        }
    }

    private void timeUp(final Context context, int i) {
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
