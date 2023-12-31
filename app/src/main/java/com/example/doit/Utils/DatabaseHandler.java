package com.example.doit.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.doit.Module.ToDoModule;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static String NAME = "toDoDB";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String DESCRIPTION = "description";
//    private static final String  CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + "TEXT, " + STATUS + " INTEGER)";
    private static final String  CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + STATUS + " INTEGER, " + DESCRIPTION + " TEXT)";
    private SQLiteDatabase db;
    public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS "+TODO_TABLE);
        //CREATE tables again
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModule task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        cv.put(DESCRIPTION, task.getTaskDescription());
        db.insert(TODO_TABLE, null, cv);
    }

    public List<ToDoModule> getAllTasks(){
        List<ToDoModule> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null,null,null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModule task = new ToDoModule();
                        task.setId(cur.getInt(cur.getColumnIndexOrThrow(ID)));
                        task.setTask(cur.getString(cur.getColumnIndexOrThrow(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndexOrThrow(STATUS)));
                        task.setTaskDescription(cur.getString(cur.getColumnIndexOrThrow(DESCRIPTION)));
                        taskList.add(task);
                    }while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            //db.close();
        }
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv= new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID+"=?", new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String task, String description){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(DESCRIPTION, description);
        db.update(TODO_TABLE, cv, ID+"=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID+"=?", new String[]{String.valueOf(id)});
    }
}
