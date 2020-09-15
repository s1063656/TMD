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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String user;
    private Fragment main_farm ;
    private info_page infoPage;
    static ArrayList<Map> mainHabitList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        user = this.getIntent().getStringExtra("userID");
        mainHabitList = new ArrayList();
        db.collection(user).document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("here", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("here", "No such document");
                    }
                } else {
                    Log.d("here", "get failed with ", task.getException());
                }

            }   });
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