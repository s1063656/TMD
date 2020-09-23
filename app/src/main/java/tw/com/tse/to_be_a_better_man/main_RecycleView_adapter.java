package tw.com.tse.to_be_a_better_man;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;
import android.content.Context;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

public class main_RecycleView_adapter extends RecyclerView.Adapter<main_RecycleView_adapter.ViewHolder> {

    int[][] seed = {{R.drawable.rosemary_0, R.drawable.rosemary_1, R.drawable.rosemary_2, R.drawable.rosemary_3, R.drawable.rosemary_4, R.drawable.rosemary_5, R.drawable.rosemary_6, R.drawable.rosemary_7}, {14, 16, 18, 20}, {22, 0, 2, 4}};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LayoutInflater main_RecycleView_inflater;
    Context mcontext;

    public main_RecycleView_adapter(Context context) {
        mcontext = context;
        this.main_RecycleView_inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = main_RecycleView_inflater.inflate(R.layout.main_item_2, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (MainActivity.mainHabitID.get(position).equals("System CREATE!!!")) {
            createField(holder);
        } else {
            holder.title.setText(MainActivity.mainHabitList.get(position).get("habitName").toString());
            try {
                holder.days.setText("天數 : " + calculateTheDay(MainActivity.mainHabitList.get(position).get("date").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int a = Integer.parseInt(MainActivity.mainHabitList.get(position).get("time").toString());
            if (a == 22) {
                holder.aptitude.setText("適性 : " + a + " ~ " + 0);
            } else if (a >= 24) {
                holder.aptitude.setText("適性 : " + (a - 24) + " ~ " + (a - 24));
            } else {
                holder.aptitude.setText("適性 : " + a + " ~ " + (a + 2));
            }
            try {
                grownUp(Integer.parseInt(calculateTheDay(MainActivity.mainHabitList.get(position).get("date").toString())), a, holder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return MainActivity.mainHabitID.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image, seedpack_1, seedpack_2, seedpack_3;
        public TextView title, status, days, aptitude;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_snapshot);
            seedpack_1 = (ImageView) itemView.findViewById(R.id.seedpack_1);
            seedpack_2 = (ImageView) itemView.findViewById(R.id.seedpack_2);
            seedpack_3 = (ImageView) itemView.findViewById(R.id.seedpack_3);
            title = (TextView) itemView.findViewById(R.id.item_title);
            status = (TextView) itemView.findViewById(R.id.item_status);
            aptitude = (TextView) itemView.findViewById(R.id.item_aptitude);
            days = (TextView) itemView.findViewById(R.id.item_days);
        }
    }

    public void grownUp(int d, int a, ViewHolder viewHolder) {
        if (a >= 22) {
            setItemImage(d, viewHolder, 3);
        } else if (a >= 14) {
            setItemImage(d, viewHolder, 2);
        } else {
            setItemImage(d, viewHolder, 1);
        }
    }

    private void setNotivication(String Title, String Text) {
        NotificationChannel channel = new NotificationChannel("channel", Title, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(mcontext, "channel")
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle(Title)
                .setContentText(Text)
                .setAutoCancel(true)
                .build();
        manager.createNotificationChannel(channel);
        manager.notify(0, notification);
    }

    private void setAlarm() {
        AlarmManager manager = (AlarmManager) mcontext.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(mcontext, AlarmReciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mcontext, 1, intent, 0);
        long firstTime = SystemClock.elapsedRealtime();    // 開機之後到現在的執行時間(包括睡眠時間)
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 這裡時區需要設定一下，不然會有8個小時的時間差
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long selectTime = calendar.getTimeInMillis();
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        long time = selectTime - systemTime;
        firstTime += time;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime, 1000 * 120, pendingIntent);

    }

    public void setItemImage(int d, ViewHolder viewHolder, int type) {
        switch (type) {
            case 1:
                viewHolder.image.setImageResource(seed[0][(int) d / 3]);
                break;
            case 2:
                viewHolder.image.setImageResource(seed[0][(int) d / 3]);
                break;
            case 3:
                viewHolder.image.setImageResource(seed[0][(int) d / 3]);
                break;

        }
    }

    public void createSecondStep(final String habitName, final Dialog dialog, final int t) {
        db.collection(MainActivity.user).document(habitName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(mcontext, "習慣已存在於資料庫", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "新增失敗,習慣已存在");
                    } else {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = new Date(System.currentTimeMillis());
                        final Map<String, Object> habits = new HashMap<>();
                        habits.put("habitName", habitName);
                        if (t >= 24) {
                            habits.put("time", t - 24);
                        } else {
                            habits.put("time", t);
                        }
                        habits.put("date", simpleDateFormat.format(date));
                        db.collection(MainActivity.user).document(habitName).set(habits)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        MainActivity.mainHabitID.add(habitName);
                                        MainActivity.mainHabitList.add(habits);
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        main_farm.main_adapter.notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });
                        dialog.dismiss();
                        setNotivication(habitName, "趕緊來照顧你的迷迭香吧 ~");
                    }
                }
            }
        });
    }

    public String calculateTheDay(String date) throws ParseException {
        Date last = new SimpleDateFormat("yyyy/MM/dd").parse(date);
        Date now = new Date(System.currentTimeMillis());
        long l = last.getTime() - now.getTime() > 0 ? last.getTime() - now.getTime() :
                now.getTime() - last.getTime();
        return Long.toString(l / (24 * 60 * 60 * 1000));
    }

    public void createFirstStep(String type, final int t) {
        Toast.makeText(mcontext, type, Toast.LENGTH_SHORT).show();
        final Dialog dialog = new Dialog(mcontext, R.style.dialogNoBg);
        dialog.setContentView(R.layout.setting);
        dialog.show();
        DisplayMetrics dm2 = mcontext.getResources().getDisplayMetrics();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //獲取對話方塊當前的引數值
        int width = dm2.widthPixels;
        int height = dm2.heightPixels;
        p.height = (int) (height * 0.8);   //高度設定為螢幕的0.3
        p.width = (int) (width * 0.9);    //寬度設定為螢幕的0.5
        dialog.getWindow().setAttributes(p);
        final EditText habitName = (EditText) dialog.findViewById(R.id.habitName);
        Button btn1 = (Button) dialog.findViewById(R.id.btn1);
        Button btn2 = (Button) dialog.findViewById(R.id.btn2);
        Button btn3 = (Button) dialog.findViewById(R.id.btn3);
        Button btn4 = (Button) dialog.findViewById(R.id.btn4);
        if (t + 2 >= 24) {
            int t2 = 0;
            btn1.setText(t + " ~ " + t2);
            btn2.setText(t2 + " ~ " + (t2 + 2));
            btn3.setText(t2 + 2 + " ~ " + (t2 + 4));
            btn4.setText(t2 + 4 + " ~ " + (t2 + 6));
        } else {
            btn1.setText(t + " ~ " + (t + 2));
            btn2.setText(t + 2 + " ~ " + (t + 4));
            btn3.setText(t + 4 + " ~ " + (t + 6));
            btn4.setText(t + 6 + " ~ " + (t + 8));
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (habitName.getText().toString().equals("")) {
                    Toast.makeText(mcontext, "請輸入習慣名稱", Toast.LENGTH_SHORT).show();
                } else {
                    createSecondStep(habitName.getText().toString().trim(), dialog, t);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (habitName.getText().toString().equals("")) {
                    Toast.makeText(mcontext, "請輸入習慣名稱", Toast.LENGTH_SHORT).show();
                } else {
                    createSecondStep(habitName.getText().toString().trim(), dialog, t + 2);
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (habitName.getText().toString().equals("")) {
                    Toast.makeText(mcontext, "請輸入習慣名稱", Toast.LENGTH_SHORT).show();
                } else {
                    createSecondStep(habitName.getText().toString().trim(), dialog, t + 4);
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (habitName.getText().toString().equals("")) {
                    Toast.makeText(mcontext, "請輸入習慣名稱", Toast.LENGTH_SHORT).show();
                } else {
                    createSecondStep(habitName.getText().toString().trim(), dialog, t + 6);
                }
            }
        });
    }

    private void createField(ViewHolder holder) {
        holder.image.setImageResource(R.drawable.ic_empty_item);
        holder.title.setText("選擇屬於你的種子");
        holder.title.setTextSize(16);
        holder.status.setClickable(false);
        holder.status.setVisibility(View.INVISIBLE);
        holder.aptitude.setClickable(false);
        holder.aptitude.setVisibility(View.INVISIBLE);
        holder.days.setClickable(false);
        holder.days.setVisibility(View.INVISIBLE);
        holder.seedpack_1.setVisibility(View.VISIBLE);
        holder.seedpack_1.setClickable(true);
        holder.seedpack_2.setVisibility(View.VISIBLE);
        holder.seedpack_2.setClickable(true);
        holder.seedpack_3.setVisibility(View.VISIBLE);
        holder.seedpack_3.setClickable(true);
        holder.seedpack_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFirstStep("陽性種子", 6);
            }
        });
        holder.seedpack_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFirstStep("陰性種子", 14);
            }
        });
        holder.seedpack_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFirstStep("耐陰種子", 22);
            }
        });

    }
}
