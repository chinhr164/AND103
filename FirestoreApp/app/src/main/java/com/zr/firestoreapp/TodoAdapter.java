package com.zr.firestoreapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zr.firestoreapp.databinding.LayoutItemTodoBinding;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoHolder>{
  public interface OnClickListener{
    void onItemClick(int position);
  }

  private List<Todo> list;
  private Context context;
  private OnClickListener onClickListener;

  public TodoAdapter(List<Todo> list, Context context, OnClickListener onClickListener){
    this.list=list;
    this.context=context;
    this.onClickListener=onClickListener;
  }

  @NonNull
  @Override
  public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
    LayoutItemTodoBinding binding=LayoutItemTodoBinding.inflate(LayoutInflater.from(context), parent, false);
    TodoHolder holder=new TodoHolder(binding);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull TodoHolder holder, int position){
    holder.bind(list.get(position), onClickListener, position);
  }

  @Override
  public int getItemCount(){
    return list.size();
  }

  // Interface for the click listener

  class TodoHolder extends RecyclerView.ViewHolder{
    LayoutItemTodoBinding binding;

    public TodoHolder(@NonNull LayoutItemTodoBinding binding){
      super(binding.getRoot());
      this.binding=binding;
    }

    public void bind(Todo todo, OnClickListener listener, int pos){
      binding.tvTitle.setText(todo.getTitle());
      binding.tvContent.setText(todo.getContent());
      itemView.setOnClickListener(v->{
        listener.onItemClick(pos);
      });
    }
  }
}


