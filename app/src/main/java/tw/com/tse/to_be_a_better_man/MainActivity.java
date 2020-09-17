package tw.com.tse.to_be_a_better_man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private Fragment main_farm ;
    private info_page infoPage;
    static ArrayList<Map> mainHabitList;
    static ArrayList<String> mainHabitID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        user = this.getIntent().getStringExtra("userID");
        mainHabitList = new ArrayList();
        mainHabitID=new ArrayList();
        if(!mainHabitID.contains("System CREATE!!!")){
            mainHabitID.add("System CREATE!!!");
            Map<String, Object> habits = new HashMap<>();
            habits.put("habitName","System CREATE!!!");
            mainHabitList.add(habits);
        }
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
        main_farm = new main_farm();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,main_farm).commitAllowingStateLoss();
    }
    public void info(View v){
        infoPage = new info_page();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,infoPage).commitAllowingStateLoss();
    }
    public void logout (View view){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
        finish();
    }
}