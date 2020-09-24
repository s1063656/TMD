package tw.com.tse.to_be_a_better_man;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.NOTIFICATION_SERVICE;


public class info_page extends Fragment {
    Button logout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_page,container,false);
        logout = (Button) view.findViewById(R.id.button2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), login.class);
                login.standBy=true;
                MainActivity.mainAlarms.clear();
                MainActivity.mainHabitList.clear();
                MainActivity.mainHabitID.clear();
                NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                manager.cancelAll();
                startActivity(intent);
                getActivity().finish();
            }
        });

    }

}

