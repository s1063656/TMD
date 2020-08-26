package tw.com.tse.to_be_a_better_man;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        View view = main_RecycleView_inflater.inflate(R.layout.main_item_2,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (listOfData.get(position).toString().equals("0")){
            holder.image.setImageResource(R.drawable.ic_empty_item);
            holder.title.setText("選擇屬於你的種子");
            holder.title.setTextSize(16);
            holder.status.setText("陽性植物");
            holder.aptitude.setText("陰性植物");
            holder.days.setText("耐陰植物");
        }
    }

    @Override
    public int getItemCount() {
        if(!listOfData.contains("0")){
            listOfData.add("0");
        }
        return listOfData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView title,status,days,aptitude;
        public ViewHolder (@NonNull View itemView){
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_snapshot);
            title = (TextView) itemView.findViewById(R.id.item_title);
            status = (TextView) itemView.findViewById(R.id.item_status);
            aptitude = (TextView) itemView.findViewById(R.id.item_aptitude);
            days = (TextView) itemView.findViewById(R.id.item_days);

        }
    }

}
