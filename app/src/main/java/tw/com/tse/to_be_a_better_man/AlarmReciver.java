package tw.com.tse.to_be_a_better_man;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReciver extends BroadcastReceiver {
    String[] arrayOfAlarmString = {"", "", "", "", "", "", "", "", "", "", "", ""};
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Log.d("intent extra", intent.getIntExtra("identify", -1) + "");
        if (intent.getIntExtra("identify", -1) != -1) {
            for (int i =1;i<MainActivity.mainHabitList.size();i++) {
                    Log.d("check",i+"");
                        arrayOfAlarmString[Integer.parseInt(MainActivity.mainHabitList.get(i).get("time").toString())/2] += " [ " + MainActivity.mainHabitList.get(i).get("habitName").toString() + " ] ";
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
            /*for(Map k:MainActivity.mainAlarms.get(intent.getIntExtra("identify",-1))){
                str+= " [ "+ k.get("habitName").toString()+ " ] ";
                MainActivity.mainHabitList.get(Integer.parseInt(k.get("position").toString())).put("safety",0);
                db.collection(MainActivity.user).document(MainActivity.mainHabitList.get(Integer.parseInt(k.get("position").toString())).get("habitName").toString()).set(MainActivity.mainHabitList.get(Integer.parseInt(k.get("position").toString())))
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
                Log.d("string",k.get("habitName").toString());

            }*/
            main_farm.main_adapter.notifyDataSetChanged();
            Notification notification = new Notification.Builder(context, MainActivity.channels[intent.getIntExtra("identify", -1)])
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentTitle("該來照顧一下你的香草囉")
                    .setContentText(arrayOfAlarmString[intent.getIntExtra("identify", -1)])
                    .setAutoCancel(true)
                    .build();
            manager.notify(intent.getIntExtra("identify", -1), notification);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MyService.class));
        } else {
            context.startService(new Intent(context, MyService.class));
        }
    }
}
