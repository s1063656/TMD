package tw.com.tse.to_be_a_better_man;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class main_RecycleView_adapter extends RecyclerView.Adapter<main_RecycleView_adapter.ViewHolder> {
    ArrayList<Object> listOfData = new ArrayList();

    LayoutInflater main_RecycleView_inflater;

    public main_RecycleView_adapter(Context context){

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
        if (listOfData.get(position).toString().equals("0")){
            holder.image.setImageResource(R.drawable.button_shape);
            holder.project.setText("NEW");

        }
    }

    @Override
    public int getItemCount() {
        if(listOfData.size()==0){
            listOfData.add(0);
            listOfData.add(1);
        }
        return listOfData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView project;
        public ViewHolder (@NonNull View itemView){
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            project = (TextView) itemView.findViewById(R.id.textView);
        }
    }

}
