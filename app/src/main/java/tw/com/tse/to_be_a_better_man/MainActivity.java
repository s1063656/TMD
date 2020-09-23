package tw.com.tse.to_be_a_better_man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String user;
    private Fragment main_farm;
    private info_page infoPage;
    static ArrayList<Map> mainHabitList;
    static ArrayList<String> mainHabitID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createNotificationChanel();
        user = this.getIntent().getStringExtra("userID");
        mainHabitList = new ArrayList();
        mainHabitID = new ArrayList();
        startService();
        createField();
        init();
        main_farm = new main_farm();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, main_farm).commitAllowingStateLoss();
    }

    public void info(View v) {
        infoPage = new info_page();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, infoPage).commitAllowingStateLoss();
    }

    public void logout(View view) {
        login.standBy = true;
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }

    public void farm(View view) {
        main_farm = new main_farm();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, main_farm).commitAllowingStateLoss();
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel vanilla = new NotificationChannel("VANILLA", "Vanilla", NotificationManager.IMPORTANCE_DEFAULT);
            vanilla.setDescription("this is VANILLA");
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(vanilla);
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> habits = new HashMap<>();
                                habits.putAll(document.getData());
                                mainHabitList.add(habits);
                                mainHabitID.add(document.getId());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startService() {
        Intent intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);
    }
}