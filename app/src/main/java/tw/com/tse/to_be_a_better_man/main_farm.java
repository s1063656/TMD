package tw.com.tse.to_be_a_better_man;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class main_farm extends Fragment {
    RecyclerView main_dataList;
    static main_RecycleView_adapter main_adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_farm, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        main_dataList = (RecyclerView) view.findViewById(R.id.farm_datalist);

        main_adapter = new main_RecycleView_adapter(getActivity());
        GridLayoutManager main_gridLayoutManager = new GridLayoutManager(getActivity(),1,GridLayoutManager.VERTICAL,false);
        main_dataList.setLayoutManager(main_gridLayoutManager);
        main_dataList.setAdapter(main_adapter);
    }
}