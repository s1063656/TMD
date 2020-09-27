package tw.com.tse.to_be_a_better_man;

import android.app.AlarmManager;
import android.app.NotificationManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.NOTIFICATION_SERVICE;


public class info_page extends Fragment {
    Button logout;
    TextView name,email;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_page,container,false);
        logout = (Button) view.findViewById(R.id.button2);
        name = view.findViewById(R.id.info_name);
        email = view.findViewById(R.id.info_email);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name.setText(MainActivity.userName);
        email.setText(MainActivity.user);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), login.class);
                login.standBy=true;
                MainActivity.mainHabitList.clear();
                MainActivity.mainHabitID.clear();
                NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                for(int i=0;i<12;i++){
                    Intent alarmIntent  = new Intent(getActivity(), AlarmReciver.class);
                    intent.setAction("SomeAction");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, alarmIntent, PendingIntent.FLAG_NO_CREATE);
                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                    if(pendingIntent != null) {
                        alarmManager.cancel(pendingIntent);
                        Log.d("cancel alarm","cancel alarm : "+i);
                    }
                }
                startActivity(intent);
                getActivity().finish();
            }
        });

    }

}

