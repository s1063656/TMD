package tw.com.tse.to_be_a_better_man;

import android.app.AlarmManager;
import android.app.Dialog;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import android.content.Context;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


import tw.com.tse.to_be_a_better_man.RoomDB.DataBase;
import tw.com.tse.to_be_a_better_man.RoomDB.MyData;


import static android.content.Context.ALARM_SERVICE;
import static tw.com.tse.to_be_a_better_man.MainActivity.mainHabitID;
import static tw.com.tse.to_be_a_better_man.MainActivity.mainHabitList;

public class main_RecycleView_adapter extends RecyclerView.Adapter<main_RecycleView_adapter.ViewHolder> {
    int[][] seed = {
            {R.drawable.rosemary_0, R.drawable.rosemary_1, R.drawable.rosemary_2, R.drawable.rosemary_3, R.drawable.rosemary_4, R.drawable.rosemary_5, R.drawable.rosemary_6, R.drawable.rosemary_7},
            {R.drawable.basil_1, R.drawable.basil_2, R.drawable.basil_3, R.drawable.basil_4, R.drawable.basil_5, R.drawable.basil_6, R.drawable.basil_7, R.drawable.basil_8},
            {R.drawable.mint_1, R.drawable.mint_2, R.drawable.mint_3, R.drawable.mint_4, R.drawable.mint_5, R.drawable.mint_6, R.drawable.mint_7, R.drawable.mint_8,}};

    int[][] time = {{R.drawable.time_6, R.drawable.time_8, R.drawable.time_10, R.drawable.time_12},
            {R.drawable.time_14, R.drawable.time_16, R.drawable.time_18, R.drawable.time_20},
            {R.drawable.time_22, R.drawable.time_0, R.drawable.time_2, R.drawable.time_4}};
    int[] dialogBack = {R.drawable.dialog_back_0, R.drawable.dialog_back_1, R.drawable.dialog_back_2}, dialogSeedpack = {R.drawable.rosemary_pack, R.drawable.basil_pack, R.drawable.mint_pack};

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Map thisPositionHabitList = mainHabitList.get(position);
        if (position == 0) {
            createField(holder);
        } else {
            holder.title.setText(thisPositionHabitList.get("habitName").toString());
            try {
                holder.days.setText("天數 : " + calculateTheDay(thisPositionHabitList.get("date").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int a = Integer.parseInt(thisPositionHabitList.get("time").toString());
            //需檢查
            if (a >= 22) {
                holder.aptitude.setText("適性 : " + a + " ~ " + (a - 22));
            } else {
                holder.aptitude.setText("適性 : " + a + " ~ " + (a + 2));
            }

            try {
                grownUp(Integer.parseInt(calculateTheDay(thisPositionHabitList.get("date").toString())), a, holder);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (thisPositionHabitList.get("status").toString().equals("0")) {
                holder.status.setText("需要水份");
                holder.image.setClickable(true);
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                MyData habit = DataBase.getInstance(mcontext).getDataUao().findDataByName(thisPositionHabitList.get("habitName").toString()).get(0);
                                try {
                                    if(calculateTheDay(thisPositionHabitList.get("date").toString()).equals("21")){
                                        watering(mcontext,position,-1);
                                    }else{
                                        watering(mcontext,position,1);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                        main_farm.main_adapter.notifyItemChanged(position);
                    }
                });
            } else {
                holder.status.setText("穩定生長");
                holder.image.setClickable(false);
            }

        }

    }

    @Override
    public int getItemCount() {
        return mainHabitList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image, seedpack_1, seedpack_2, seedpack_3;
        public TextView title, status, days, aptitude;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_snapshot);
            seedpack_1 = itemView.findViewById(R.id.seedpack_1);
            seedpack_2 = itemView.findViewById(R.id.seedpack_2);
            seedpack_3 = itemView.findViewById(R.id.seedpack_3);
            title = itemView.findViewById(R.id.item_title);
            status = itemView.findViewById(R.id.item_status);
            aptitude = itemView.findViewById(R.id.item_aptitude);
            days = itemView.findViewById(R.id.item_days);
        }
    }

    public void grownUp(int d, int a, ViewHolder viewHolder) {
        if (a >= 6 && a < 14) {
            viewHolder.image.setImageResource(seed[0][d / 3]);
        } else if (a >= 14 && a < 22) {
            viewHolder.image.setImageResource(seed[1][d / 3]);
        } else {
            viewHolder.image.setImageResource(seed[2][d / 3]);
        }
    }


    public String calculateTheDay(String date) throws ParseException {
        Date last = new SimpleDateFormat("yyyy/MM/dd").parse(date);
        Date now = new Date(System.currentTimeMillis());
        long l = last.getTime() - now.getTime() > 0 ? last.getTime() - now.getTime() :
                now.getTime() - last.getTime();
        return Long.toString(l / (24 * 60 * 60 * 1000));
    }

    private void createFirstStep(String type, final int t) {
        Toast.makeText(mcontext, type, Toast.LENGTH_SHORT).show();
        final Dialog dialog = new Dialog(mcontext, R.style.dialogNoBg);
        dialog.setContentView(R.layout.setting);
        ImageView seedpack = dialog.findViewById(R.id.imageView3);
        Button[] buttons = {dialog.findViewById(R.id.btn1), dialog.findViewById(R.id.btn2), dialog.findViewById(R.id.btn3), dialog.findViewById(R.id.btn4)};
        dialog.show();
        DisplayMetrics dm2 = mcontext.getResources().getDisplayMetrics();
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //獲取對話方塊當前的引數值
        int width = dm2.widthPixels;
        int height = dm2.heightPixels;
        p.height = (int) (height * 0.8);   //高度設定為螢幕的0.3
        p.width = (int) (width * 0.9);    //寬度設定為螢幕的0.5
        dialog.getWindow().setAttributes(p);
        final EditText habitName = dialog.findViewById(R.id.habitName);

        int timeInterval;
        if (t >= 6 && t < 14) {
            timeInterval = 0;
        } else if (t >= 14 && t < 22) {
            timeInterval = 1;
        } else {
            timeInterval = 2;
        }
        seedpack.setImageResource(dialogSeedpack[timeInterval]);
        dialog.findViewById(R.id.dialog_background).setBackgroundResource(dialogBack[timeInterval]);
        for (int i = 0; i < 4; i++) {
            final int temp = i;
            buttons[i].setBackgroundResource(time[timeInterval][i]);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (habitName.getText().toString().equals("")) {
                        Toast.makeText(mcontext, "請輸入習慣名稱", Toast.LENGTH_SHORT).show();
                    } else if (MainActivity.mainHabitID.contains(habitName.getText().toString())) {
                        Toast.makeText(mcontext, "習慣名稱重複", Toast.LENGTH_SHORT).show();
                    } else {

                        createSecondStep(habitName.getText().toString().trim(), dialog, t + (temp * 2));
                        Log.d("IDlist", "" + mainHabitID);
                    }
                }
            });
        }
    }

    private void createSecondStep(final String habitName, final Dialog dialog, final int t) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        final Date date = new Date(System.currentTimeMillis());
        final int tempTime;
        if (t >= 24) {
            tempTime = t - 24;
        } else {
            tempTime = t;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> habits = new HashMap<>();
                habits.put("habitName", habitName);
                habits.put("date", simpleDateFormat.format(date));
                habits.put("time", tempTime);
                habits.put("status", 1);

                MainActivity.mainHabitID.add(habitName);
                MainActivity.mainHabitList.add(habits);
                insertData(new MyData(habitName, tempTime, simpleDateFormat.format(date), 1));
            }
        }).start();
        dialog.dismiss();
        notifyDataSetChanged();
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

    private void insertData(MyData myData) {
        DataBase.getInstance(mcontext).getDataUao().insertData(myData);
        Log.d("sqlite", "insert " + myData.getName());
    }

    private void watering(final Context context, int i,int status) {
        MainActivity.mainHabitList.get(i).put("status", status);
        final Map<String, Object> habit = mainHabitList.get(i);
        habit.put("status", status);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyData oldData = DataBase.getInstance(context).getDataUao().findDataByName(habit.get("habitName").toString()).get(0);
                DataBase.getInstance(context).getDataUao().updateData(oldData.getId(), habit.get("habitName").toString(),
                        Integer.parseInt(habit.get("time").toString()),
                        habit.get("date").toString(),
                        Integer.parseInt(habit.get("status").toString()));
            }
        }).start();
    }
}
