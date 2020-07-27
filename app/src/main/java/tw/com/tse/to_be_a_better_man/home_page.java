package tw.com.tse.to_be_a_better_man;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class home_page extends Fragment {
    RecyclerView main_dataList;
    main_RecycleView_adapter main_adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        main_dataList = (RecyclerView) view.findViewById(R.id.main_layout_dataList);

        main_adapter = new main_RecycleView_adapter(getActivity());
        GridLayoutManager main_gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        main_dataList.setLayoutManager(main_gridLayoutManager);
        main_dataList.setAdapter(main_adapter);
    }
}
