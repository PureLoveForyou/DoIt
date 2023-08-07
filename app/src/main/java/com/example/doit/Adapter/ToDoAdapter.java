package com.example.doit.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.AddNewTask;
import com.example.doit.MainActivity;
import com.example.doit.Module.ToDoModule;
import com.example.doit.R;
import com.example.doit.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModule> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity) {
        this.activity = activity;
        this.db = db;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDatabase();
        ToDoModule item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.description.setText(item.getTaskDescription());
        holder.completed.setText(item.getStatus() != 0 ? "Completed" : "ToDo");

        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                    holder.completed.setText("Completed");
                } else {
                    db.updateStatus(item.getId(), 0);
                    holder.completed.setText("TODO");
                }
            }
        });
    }

    public int getItemCount() {
        return todoList.size();
    }

    public void setTasks(List<ToDoModule> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public Context getContext() {
        return activity;
    }

    public void editItem(int position) {
        Log.d("Test: ", "start edit in adapter>>>>>>>>>>>>>>>>>>>>>");
        ToDoModule item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("description", item.getTaskDescription());//把元素放入编辑页面
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);//更新recvcleView中的视图
//         Log.d("Test: ", "end edit in adapter√>√√√VVV√VVVVVVVVVV√√√");
//        item.setTask();
        //todoListset(position, item);
        // notifyltemChanged(position)://刷新recycleView的单个item
    }

    public void deleteltem(int position) {
        ToDoModule item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        TextView description;
        TextView completed;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            completed = view.findViewById(R.id.status);
        }
    }
}
