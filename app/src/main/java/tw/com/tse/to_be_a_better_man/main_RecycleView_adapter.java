package tw.com.tse.to_be_a_better_man;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class main_RecycleView_adapter extends RecyclerView.Adapter<main_RecycleView_adapter.ViewHolder> {

    List<String> main_RecycleView_titles;
    List<Integer> main_RecycleView_images;
    LayoutInflater main_RecycleView_inflater;

    public main_RecycleView_adapter(Context context/*, List<String> titles ,List<Integer> images*/){
        /*this.main_RecycleView_titles = titles;
        this.main_RecycleView_images = images;*/
        this.main_RecycleView_inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = main_RecycleView_inflater.inflate(R.layout.main_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder (@NonNull View itemView){
            super(itemView);
        }
    }

}
