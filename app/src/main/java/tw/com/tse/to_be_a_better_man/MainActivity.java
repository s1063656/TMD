package tw.com.tse.to_be_a_better_man;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private home_page homePage;
    private info_page infoPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homePage = new home_page();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container,homePage).commitAllowingStateLoss();

    }
    public void info(View v){
        infoPage = new info_page();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,infoPage).commitAllowingStateLoss();


    }
}