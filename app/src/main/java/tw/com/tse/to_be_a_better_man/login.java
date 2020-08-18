package tw.com.tse.to_be_a_better_man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void sign_in(View view) {
        EditText main_email = (EditText) findViewById(R.id.login_id);
        final EditText main_password = (EditText) findViewById(R.id.login_password);
        if (main_email.getText().toString().trim().equals("")|| main_password.getText().toString().trim().equals("")) {
            Toast.makeText(this,"帳號或密碼不能為空值",Toast.LENGTH_SHORT).show();
            Log.d("LOGIN", "帳號或密碼未輸入");
        } else {
            db.collection("users").document(main_email.getText().toString().trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if(main_password.getText().toString().trim().equals(document.get("password").toString())){
                                Toast.makeText(context,"登入成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(login.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(context,"密碼錯誤",Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.d("LOGIN", "get failed with ", task.getException());
                    }
                }
            });

        }
    }

    public void registered(View v) {
        final AlertDialog.Builder registered_page = new AlertDialog.Builder(this);
        final View view = View.inflate(this, R.layout.registered_page2, null);
        Button registered_btn = (Button) view.findViewById(R.id.button);
        final EditText registered_email = (EditText) view.findViewById(R.id.registered_email);
        final EditText registered_password = (EditText) view.findViewById(R.id.registered_password);
        registered_page.setView(view);
        final AlertDialog close = registered_page.show();


        registered_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String TAG = "registered";
                final String userID = registered_email.getText().toString().trim();
                if (isVaildEmailFormat(userID)) {
                    db.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Toast.makeText(context, "帳號已存在於資料庫", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "註冊失敗,帳號已存在");
                                } else {

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("password", registered_password.getText().toString().trim());
                                    db.collection("users").document(userID)
                                            .set(user)
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
                                    close.dismiss();
                                }

                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "信箱格式不符合", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private boolean isVaildEmailFormat(String email) {
        if (email == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}