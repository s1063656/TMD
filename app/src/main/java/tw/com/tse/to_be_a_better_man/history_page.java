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


public class history_page extends Fragment {
    RecyclerView history_RecycleView;
    static history_adapter his_adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_page,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        history_RecycleView = (RecyclerView) view.findViewById(R.id.history_RecycleView);
        history_RecycleView.setItemViewCacheSize(100);
        his_adapter = new history_adapter(getActivity());
        GridLayoutManager main_gridLayoutManager = new GridLayoutManager(getActivity(),4,GridLayoutManager.VERTICAL,false);
        history_RecycleView.setLayoutManager(main_gridLayoutManager);
        history_RecycleView.setAdapter(his_adapter);
    }
}