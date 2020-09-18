package tw.com.tse.to_be_a_better_man;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
    String userid,password;
    SharedPreferences pref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pref = getSharedPreferences("login",MODE_PRIVATE);
        userid = getSharedPreferences("login", MODE_PRIVATE)
                .getString("USERID", "");
        password = getSharedPreferences("login",MODE_PRIVATE)
                .getString("PASSWORD","");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_2);
    }

    public void login(View view) {
        final EditText main_email = (EditText) findViewById(R.id.login_id);
        final EditText main_password = (EditText) findViewById(R.id.login_password);
        if(!userid.equals("")&&!password.equals("")){
            main_email.setText(userid);
            main_password.setText(password);
        }
        if (main_email.getText().toString().trim().equals("")|| main_password.getText().toString().trim().equals("")) {
            Toast.makeText(this,"帳號或密碼不能為空值",Toast.LENGTH_SHORT).show();
            Log.d("LOGIN", "帳號或密碼未輸入");
        } else {
            if(isVaildEmailFormat(main_email.getText().toString().trim())) {
                db.collection("users").document(main_email.getText().toString().trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (main_password.getText().toString().trim().equals(document.get("password").toString())) {
                                    pref.edit()
                                            .putString("USERID",main_email.getText().toString().trim())
                                            .putString("PASSWORD",main_password.getText().toString().trim())
                                            .apply();
                                    Toast.makeText(context, "登入成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    intent.putExtra("userID", main_email.getText().toString().trim());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(context, "密碼錯誤", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d("LOGIN", "get failed with ", task.getException());
                        }
                    }
                });
            }else{
                Toast.makeText(context, "信箱格式錯誤", Toast.LENGTH_SHORT).show();
                Log.d("LOGIN", "信箱格式錯誤");
            }
        }
    }

    public void registered(View v) {
        final Dialog registered_page = new Dialog(this,R.style.dialogNoBg);
        final View view = View.inflate(this, R.layout.registered_page_3, null);
        Button registered_btn = (Button) view.findViewById(R.id.button5);
        final EditText registered_email = (EditText) view.findViewById(R.id.res_email);
        final EditText registered_password = (EditText) view.findViewById(R.id.res_password);
        registered_page.setContentView(view);
        registered_page.show();
        WindowManager m = getWindowManager();
        android.view.WindowManager.LayoutParams p = registered_page.getWindow().getAttributes();  //獲取對話方塊當前的引數值
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        p.height = (int) (height * 0.8);   //高度設定為螢幕的0.3
        p.width = (int) (width * 0.9);    //寬度設定為螢幕的0.5
        registered_page.getWindow().setAttributes(p);     //設定生效
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
                                    registered_page.dismiss();
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