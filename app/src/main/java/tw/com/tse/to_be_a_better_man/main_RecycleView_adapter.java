package tw.com.tse.to_be_a_better_man;

import android.app.ActivityManager;
import android.app.Dialog;
import android.view.WindowManager;
import android.content.Context;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class main_RecycleView_adapter extends RecyclerView.Adapter<main_RecycleView_adapter.ViewHolder> {
    ArrayList<Object> listOfData = new ArrayList();
    int [][] time_set = {{6,8,10,12},{14,16,18,20},{22,0,2,4}};

    LayoutInflater main_RecycleView_inflater;
    Context mcontext;
    public main_RecycleView_adapter(Context context){
        mcontext=context;

        this.main_RecycleView_inflater = LayoutInflater.from(context);

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = main_RecycleView_inflater.inflate(R.layout.main_item_2,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (listOfData.get(position).toString().equals("0")){
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
        }


        holder.seedpack_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,"陽性種子",Toast.LENGTH_SHORT).show();
                Dialog dialog = new Dialog(mcontext,R.style.dialogNoBg);
                dialog.setContentView(R.layout.setting);
                dialog.show();
                DisplayMetrics dm2 = mcontext.getResources().getDisplayMetrics();
                android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //獲取對話方塊當前的引數值
                int width = dm2.widthPixels;
                int height = dm2.heightPixels;
                p.height = (int) (height * 0.8);   //高度設定為螢幕的0.3
                p.width = (int) (width * 0.9);    //寬度設定為螢幕的0.5
                dialog.getWindow().setAttributes(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(!listOfData.contains("0")){
            listOfData.add("0");
        }
        return listOfData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image,seedpack_1,seedpack_2,seedpack_3;
        public TextView title,status,days,aptitude;
        public ViewHolder (@NonNull View itemView){
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.item_snapshot);
            seedpack_1 = (ImageView) itemView.findViewById(R.id.seedpack_1);
            seedpack_2 = (ImageView) itemView.findViewById(R.id.seedpack_2);
            seedpack_3 = (ImageView) itemView.findViewById(R.id.seedpack_3);
            title = (TextView) itemView.findViewById(R.id.item_title);
            status = (TextView) itemView.findViewById(R.id.item_status);
            aptitude = (TextView) itemView.findViewById(R.id.item_aptitude);
            days = (TextView) itemView.findViewById(R.id.item_days);

        }
    }

}
