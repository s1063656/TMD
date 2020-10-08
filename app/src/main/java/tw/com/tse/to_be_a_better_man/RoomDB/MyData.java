package tw.com.tse.to_be_a_better_man.RoomDB;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "MyTable")//這邊要先取好table的名字，稍後的table設置必須與他相同
public class MyData {
    @PrimaryKey(autoGenerate = true)//設置是否使ID自動累加
    private int id;
    private String name;
    private String time;
    private String date;
    private String status;

    public MyData(String name, String time, String date, String status) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.status = status;
    }
    @Ignore//如果要使用多形的建構子，必須加入@Ignore
    public MyData(int id,String name, String time, String date, String status) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setPhone(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
