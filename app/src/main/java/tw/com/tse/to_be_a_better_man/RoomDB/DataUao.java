package tw.com.tse.to_be_a_better_man.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;

import androidx.room.Insert;

import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import androidx.room.OnConflictStrategy;


@Dao
public interface DataUao {

    String tableName = "MyTable";
    /**=======================================================================================*/
    /**簡易新增所有資料的方法*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)//預設萬一執行出錯怎麼辦，REPLACE為覆蓋
    void insertData(MyData myData);

    /**複雜(?)新增所有資料的方法
    @Query("INSERT INTO "+tableName+"(name,time,date,status) VALUES(:name,:time,:date,:status)")
    void insertData(String name,String time,String date,String status);*/

    /**=======================================================================================*/
    /**撈取全部資料*/
    @Query("SELECT * FROM " + tableName)
    List<MyData> displayAll();

    /**撈取某個名字的相關資料*/
    @Query("SELECT * FROM " + tableName +" WHERE name = :name")
    List<MyData> findDataByName(String name);

    /**=======================================================================================*/
    /**簡易更新資料的方法*/
    @Update
    void updateData(MyData myData);

    /**複雜(?)更新資料的方法*/
    @Query("UPDATE "+tableName+" SET name = :name,time=:time,date=:date,status = :status WHERE id = :id" )
    void updateData(int id,String name,int time,String date,int status);

    /**=======================================================================================*/
    /**簡單刪除資料的方法*/
    @Delete
    void deleteData(MyData myData);

    /**複雜(?)刪除資料的方法*/
    @Query("DELETE  FROM " + tableName + " WHERE id = :id")
    void deleteData(int id);

    @Query("DELETE FROM " + tableName)
    void deleteAll();


}
