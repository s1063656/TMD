package tw.com.tse.to_be_a_better_man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String user;
    private Fragment main_farm;
    private info_page infoPage;
    private history_page hisPage;
    static String userName;
    static ArrayList<Map> mainHabitList;
    static ArrayList<String> mainHabitID;
    //static int[] checkBeforeHand={0,0,0,0,0,0,0,0,0,0,0,0};
    static String [] channels = {"Channel 0~2.","Channel 2~4.","Channel 4~6.","Channel 6~8.","Channel 8~10.",
        "Channel 10~12.","Channel 12~14.","Channel 14~16.","Channel 16~18.","Channel 18~20.","Channel 20~22.","Channel 22~0."};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createNotificationChanel();
        user = this.getIntent().getStringExtra("userID");
        db.collection("users").document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userName = document.get("name").toString();
                    }else{
                        Log.d("info","null");
                    }
                } else {
                    Log.d("info", "get failed with ", task.getException());
                }
            }
        });
        mainHabitList = new ArrayList();
        mainHabitID = new ArrayList();

        init();
        startService();
        createField();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        main_farm = new main_farm();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, main_farm).commitAllowingStateLoss();
    }

    public void info(View v) {
        infoPage = new info_page();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, infoPage).commitAllowingStateLoss();
    }

    public void his(View view) {
        hisPage = new history_page();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, hisPage).commitAllowingStateLoss();
    }

    public void farm(View view) {
        main_farm = new main_farm();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, main_farm).commitAllowingStateLoss();
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            for(int i = 0;i<channels.length;i++){
                manager.createNotificationChannel(new NotificationChannel(channels[i], channels[i], NotificationManager.IMPORTANCE_DEFAULT));
            }
        }
    }

    private void createField() {
        if (!mainHabitID.contains("System CREATE!!!")) {
            mainHabitID.add("System CREATE!!!");
            Map<String, Object> habits = new HashMap<>();
            habits.put("habitName", "System CREATE!!!");
            mainHabitList.add(habits);
        }
    }
    private void init() {
        db.collection(user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //int p =1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> habits = new HashMap<>();
                                habits.putAll(document.getData());
                                mainHabitList.add(habits);
                                mainHabitID.add(document.getId());
                                Log.d("num",Integer.parseInt(document.get("time").toString())/2+"");
                                //checkBeforeHand[Integer.parseInt(document.get("time").toString())/2]=1;
                                //habits.put("position",p++);
                                setAlarm(Integer.parseInt(document.get("time").toString()));
                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });




    }

    private void startService() {
        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);
    }
    private void setAlarm(int identifier) {
        Intent intent = new Intent(this, AlarmReciver.class);
        intent.putExtra("identify",identifier);
        AlarmManager manager = (AlarmManager)this.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, identifier, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstTime = SystemClock.elapsedRealtime();
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY,identifier);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        long selectTime = calendar.getTimeInMillis();
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        long time = selectTime - systemTime;
        firstTime += time;
        manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, pendingIntent);
        Log.d("setAlarm","ALARM num : "+time);
    }
}