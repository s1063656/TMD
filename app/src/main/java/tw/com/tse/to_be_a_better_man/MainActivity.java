package tw.com.tse.to_be_a_better_man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.facebook.stetho.Stetho;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.tse.to_be_a_better_man.RoomDB.DataBase;
import tw.com.tse.to_be_a_better_man.RoomDB.MyData;

public class MainActivity extends AppCompatActivity {

    static String user;
    static SharedPreferences pref;
    static List<MyData> HBlist;
    private Fragment main_farm;
    private info_page infoPage;
    private history_page hisPage;
    static String userName;
    static ArrayList<Map> mainHabitList,historyList;
    static ArrayList<String> mainHabitID;
    static String [] channels = {"Channel 0~2.","Channel 2~4.","Channel 4~6.","Channel 6~8.","Channel 8~10.",
        "Channel 10~12.","Channel 12~14.","Channel 14~16.","Channel 16~18.","Channel 18~20.","Channel 20~22.","Channel 22~0."};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Stetho.initializeWithDefaults(this);
        createNotificationChanel();
        pref = getSharedPreferences("login",MODE_PRIVATE);
        user = pref.getString("USERID","");

        userName = pref.getString("USERNAME","");

        Log.d("mainActivity",""+userName);
        mainHabitList = new ArrayList();
        mainHabitID = new ArrayList();
        historyList = new ArrayList();
        createField();
        init();
        startService();
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
        if (!mainHabitID.contains("System CREATE!!!")){
            mainHabitID.add("System CREATE!!!");
            Map<String, Object> habits = new HashMap<>();
            habits.put("habitName", "System CREATE!!!");
            mainHabitList.add(habits);
        }
    }
    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HBlist=DataBase.getInstance(getApplicationContext()).getDataUao().displayAll( );
                for(int i =0;i<HBlist.size();i++){
                    if(HBlist.get(i).getStatus()!=-1) {
                        Log.d("displayAll", HBlist.get(i).getName());
                        Map<String, Object> habit = new HashMap<>();
                        habit.put("habitName", HBlist.get(i).getName());
                        habit.put("date", HBlist.get(i).getDate());
                        habit.put("time", HBlist.get(i).getTime());
                        habit.put("status", HBlist.get(i).getStatus());
                        mainHabitList.add(habit);
                        mainHabitID.add(habit.get("habitName").toString());
                        Log.d("habitList",mainHabitList.get(mainHabitList.size()-1).get("habitName").toString() );
                    }else{
                        Map<String, Object> habit = new HashMap<>();
                        habit.put("habitName", HBlist.get(i).getName());
                        habit.put("date", HBlist.get(i).getDate());
                        habit.put("time", HBlist.get(i).getTime());
                        habit.put("status", HBlist.get(i).getStatus());
                        historyList.add(habit);
                        Log.d("historyList",historyList.get(historyList.size()-1).get("habitName").toString() );
                    }
                }
                
            }
        }).start();
    }

    private void startService() {
        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);
    }

}