package com.example.doit.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doit.MainActivity;
import com.example.doit.Module.ToDoModule;
import com.example.doit.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModule> todoList;
    private MainActivity activity;

    public ToDoAdapter(MainActivity activity){
        this.activity = activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        ToDoModule item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.description.setText(item.getTaskDescription());
        holder.completed.setText(item.getStatus()!=0?"Completed":"ToDo");
    }

    public int getItemCount(){
        return todoList.size();
    }

    public void setTasks(List<ToDoModule> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }
    private boolean toBoolean(int n){
        return n!=0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        TextView description;
        TextView completed;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            completed = view.findViewById(R.id.status);
        }
    }
}
