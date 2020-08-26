package tw.com.tse.to_be_a_better_man;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String user;
    private Fragment main_farm ;
    private info_page infoPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        user = getIntent().getExtras().toString();
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