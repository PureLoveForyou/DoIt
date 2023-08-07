package com.example.doit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.doit.Adapter.ToDoAdapter;
import com.example.doit.Module.ToDoModule;
import com.example.doit.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private DatabaseHandler db;
    private RecyclerView taskRecyclerView;
    private ToDoAdapter taskAdapter;
    private List<ToDoModule> taskList;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.d(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", "check");

//        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏actionbar
        //数据库定义与读取
        db = new DatabaseHandler(this);
        db.openDatabase();
        taskList = new ArrayList<>();

        taskRecyclerView = findViewById(R.id.taskRecycler);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new ToDoAdapter(db, this);
        taskRecyclerView.setAdapter(taskAdapter);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);

        //浮动添加按钮的监听
        fab = findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        //把左右滑动添加到recvcleView中
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTasks(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}