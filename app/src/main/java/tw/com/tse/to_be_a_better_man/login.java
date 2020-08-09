package tw.com.tse.to_be_a_better_man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        final View view = View.inflate(this,R.layout.registered_page2,null);
        Button registered_btn = (Button) view.findViewById(R.id.button);
        final EditText registered_email = (EditText) view.findViewById(R.id.registered_email);
        final EditText registered_password = (EditText) view.findViewById(R.id.registered_password);
        registered_page.setView(view);
        final AlertDialog close = registered_page.show();



        registered_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> user = new HashMap<>();
                user.put("email", registered_email.getText().toString().trim());
                user.put("password", registered_password.getText().toString().trim());
                db.collection("user")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("registered", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("registered", "Error adding document", e);
                            }
                        });
                close.dismiss();
            }
        });



    }
}