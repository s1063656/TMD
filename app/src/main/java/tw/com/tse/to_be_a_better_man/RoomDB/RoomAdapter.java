package tw.com.tse.to_be_a_better_man.RoomDB;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private List<MyData> myData;
    private Activity activity;
    private OnItemClickListener onItemClickListener;

    public RoomAdapter(Activity activity, List<MyData> myData) {
        this.activity = activity;
        this.myData = myData;
    }
    /**建立對外接口*/
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(android.R.id.text1);
            view = itemView;
        }
    }
    /**更新資料*/
    public void refreshView() {


        new Thread(new Runnable() {

            @Override
            public void run() {

                /**延迟两秒
                try {
                    Thread.sleep( 2000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }
    /**刪除資料*/
    public void deleteData(final int position){
        final int p = position;

        new Thread(new Runnable() {

            @Override
            public void run() {

                /**延迟两秒
                 try {
                 Thread.sleep( 2000 );
                 } catch (InterruptedException e) {
                 e.printStackTrace();
                 }*/

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemRemoved(p);
                        refreshView();
                    }
                });

            }
        }).start();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final int p = position;
        holder.tvTitle.setText(myData.get(position).getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(myData.get(p));
            }
        });

    }
    @Override
    public int getItemCount() {
        return myData.size();
    }
    /**建立對外接口*/
    public interface OnItemClickListener {
        void onItemClick(MyData myData);
    }

}