package tw.com.tse.to_be_a_better_man;

import android.app.AlarmManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.HashMap;
import java.util.Map;
import tw.com.tse.to_be_a_better_man.RoomDB.DataBase;

public class info_page extends Fragment {
    Button logout,sync;
    TextView name,email;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_page,container,false);
        logout = (Button) view.findViewById(R.id.button2);
        sync = view.findViewById(R.id.info_sync);
        name = view.findViewById(R.id.info_name);
        email = view.findViewById(R.id.info_email);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name.setText(MainActivity.pref.getString("USERNAME",""));
        email.setText(MainActivity.pref.getString("USERID",""));
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),login.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), login.class);
                login.standBy=true;
                MainActivity.mainHabitList.clear();
                MainActivity.mainHabitID.clear();
                name.setText("");
                email.setText("");
                MainActivity.pref.edit().clear().commit();
                createField();
                Toast.makeText(getActivity(),"登出",Toast.LENGTH_LONG).show();
                for(int i=0;i<12;i++){
                    Intent alarmIntent  = new Intent(getActivity(), AlarmReciver.class);
                    //intent.setAction("SomeAction");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, alarmIntent, PendingIntent.FLAG_NO_CREATE);
                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                    if(pendingIntent != null) {
                        alarmManager.cancel(pendingIntent);
                        Log.d("cancel alarm","cancel alarm : "+i);
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataBase.getInstance(getContext()).getDataUao().deleteAll();
                        DbHelper.resetAutoIncrement(new DbHelper(getContext()),"MyTable");
                        Log.d("room","ok");
                    }
                }).start();

                //startActivity(intent);
                //getActivity().finish();
                main_farm main_farm = new main_farm();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, main_farm).commitAllowingStateLoss();
            }
        });
    }
    private void createField() {
        if (!MainActivity.mainHabitID.contains("System CREATE!!!")) {
            MainActivity.mainHabitID.add("System CREATE!!!");
            Map<String, Object> habits = new HashMap<>();
            habits.put("habitName", "System CREATE!!!");
            MainActivity.mainHabitList.add(habits);
        }
    }


}

