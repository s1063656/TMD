package tw.com.tse.to_be_a_better_man;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void sign_in(View view){
        Intent intent = new Intent(login.this,MainActivity.class);
        startActivity(intent);
    }

    public void registered(View v){
        final AlertDialog.Builder registered_page = new AlertDialog.Builder(this);
        final View view = View.inflate(this,R.layout.registered_page,null);
        Button registered_btn = (Button) view.findViewById(R.id.registered_btn);
        registered_page.setView(view);
        final AlertDialog close = registered_page.show();

        registered_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close.dismiss();
            }
        });



    }
}