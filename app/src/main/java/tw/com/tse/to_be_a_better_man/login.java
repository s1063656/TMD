package tw.com.tse.to_be_a_better_man;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Display;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.tse.to_be_a_better_man.RoomDB.DataBase;
import tw.com.tse.to_be_a_better_man.RoomDB.MyData;

import static tw.com.tse.to_be_a_better_man.MainActivity.mainHabitID;
import static tw.com.tse.to_be_a_better_man.MainActivity.mainHabitList;
import static tw.com.tse.to_be_a_better_man.MainActivity.user;

public class login extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context = this;
    String userid,password;
    SharedPreferences pref ;
    EditText main_email,main_password;
    TextView title ;
    ConstraintLayout loginBack;
    static Boolean standBy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_2);
        init();

    }

    public void init(){
        pref = getSharedPreferences("login",MODE_PRIVATE);
        userid = getSharedPreferences("login", MODE_PRIVATE)
                .getString("USERID", "");
        password = getSharedPreferences("login",MODE_PRIVATE)
                .getString("PASSWORD","");

        main_email = (EditText) findViewById(R.id.login_id);
        main_password = (EditText) findViewById(R.id.login_password);
        title = (TextView) findViewById(R.id.loginTitle);
        title.setText("香草習慣");
        title.setTextSize(48f);
        title.setTextColor(Color.rgb(0, 0, 0));
        title.setTypeface(Typeface.DEFAULT_BOLD);
        loginBack = (ConstraintLayout) findViewById(R.id.loginBack);
        loginBack.setBackgroundResource(R.drawable.login_background);
        if(!userid.equals("")&&!password.equals("")) {
            main_email.setText(userid);
            main_password.setText(password);
            if (!standBy) {
                login(null);
            }
        }
    }

    public void login(View view) {
        if (main_email.getText().toString().trim().equals("") || main_password.getText().toString().trim().equals("")) {
            Toast.makeText(this, "帳號或密碼未輸入", Toast.LENGTH_SHORT).show();
            Log.d("LOGIN", "帳號或密碼未輸入");
        } else {
            if (isVaildEmailFormat(main_email.getText().toString().trim())) {
                db.collection("users").document(main_email.getText().toString().trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                if (main_password.getText().toString().trim().equals(document.get("password").toString())) {
                                    final Dialog ud = new Dialog(context,R.style.dialogNoBg);
                                    final View view = View.inflate(context, R.layout.upload_download, null);
                                    Button up = (Button) view.findViewById(R.id.upload);
                                    Button down = (Button) view.findViewById(R.id.download);
                                    ud.setContentView(view);
                                    ud.show();
                                    android.view.WindowManager.LayoutParams p = ud.getWindow().getAttributes();  //獲取對話方塊當前的引數值
                                    DisplayMetrics dm = new DisplayMetrics();
                                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                                    Display display = wm.getDefaultDisplay();
                                    //Display display = wm.getDefaultDisplay();getWindowManager().getDefaultDisplay().getMetrics(dm);
                                    int width = display.getWidth();
                                    int height = display.getHeight();
                                    p.height = (int) (height * 0.2);
                                    p.width = (int) (width * 0.8);
                                    ud.getWindow().setAttributes(p);//設定生效

                                    up.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    final List<MyData> myDataList = DataBase.getInstance(context).getDataUao().displayAll();
                                                    final ArrayList<String> fireData = new ArrayList();
                                                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                                    db.collection(userid)
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                                            fireData.add(document.get("habitName").toString());
                                                                            Log.d("fireData_",document.get("habitName").toString());
                                                                        }
                                                                        try {
                                                                            Thread.sleep(3000);
                                                                        } catch (InterruptedException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        while(!fireData.isEmpty()){
                                                                            Log.d("fireData",fireData.size()+"");
                                                                            db.collection(userid).document(fireData.remove(0)).delete();
                                                                        }
                                                                        Log.d("upClean","SUCCESSFUL");
                                                                        for(int i=0;i<myDataList.size();i++){
                                                                            Map<String, Object> habits = new HashMap<>();
                                                                            habits.put("habitName",myDataList.get(i).getName());
                                                                            habits.put("date",myDataList.get(i).getDate());
                                                                            habits.put("time",myDataList.get(i).getTime());
                                                                            habits.put("status",1);
                                                                            db.collection(userid).document(myDataList.get(i).getName()).set(habits)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Log.d("upload", "DocumentSnapshot successfully written!");
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.w("upload", "Error adding document", e);
                                                                                        }
                                                                                    });
                                                                    }} else {
                                                                        Log.d("upClean", "Error getting documents: ", task.getException());
                                                                    }
                                                                }
                                                            });

                                                    }

                                            }).start();
                                        }
                                    });


                                    down.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });

                                }else {
                                    Log.d("LOGIN","密碼錯誤");
                                    Toast.makeText(context, "密碼錯誤", Toast.LENGTH_SHORT).show();
                                }


                                /*
                                if (main_password.getText().toString().trim().equals(document.get("password").toString())) {
                                    pref.edit()
                                            .putString("USERID",main_email.getText().toString().trim())
                                            .putString("PASSWORD",main_password.getText().toString().trim())
                                            .apply();
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    intent.putExtra("userID", main_email.getText().toString().trim());
                                    intent.putExtra("userName",document.get("name").toString());
                                    Toast.makeText(context, "登入成功", Toast.LENGTH_SHORT).show();
                                    Log.d("LOGIN","登入成功");
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Log.d("LOGIN","密碼錯誤");
                                    Toast.makeText(context, "密碼錯誤", Toast.LENGTH_SHORT).show();
                                }*/
                            } else {
                                Toast.makeText(context, "請檢查輸入的帳號密碼是否有誤\n並確實註冊帳號密碼", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Log.d("LOGIN", "get failed with ", task.getException());
                        }
                    }
                });
            } else {
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
        final EditText registered_name = view.findViewById(R.id.res_name);
        registered_page.setContentView(view);
        registered_page.show();
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
                if(registered_name.getText().toString().equals("")){
                    Toast.makeText(context,"名稱欄位不能空白",Toast.LENGTH_SHORT).show();
                }else{
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
                                    user.put("name",registered_name.getText().toString().trim());
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
                                    Toast.makeText(context,"恭喜你註冊成功",Toast.LENGTH_LONG).show();
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
            }}
        });

    }

    private boolean isVaildEmailFormat(String email) {
        if (email == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void dbSync(){
        db.collection(main_email.getText().toString().trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> habits = new HashMap<>();
                                habits.putAll(document.getData());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                }).start();
                            }
                        } else {
                            Log.d("sync", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}