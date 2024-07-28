package com.zr.retrofitapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zr.retrofitapp.databinding.LayoutItemStudentBinding;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentHolder>{
  public interface OnClickListener{
    void onItemClick(int position, View v);
  }

  private List<Student> list;
  private Context context;
  private OnClickListener onClickListener;

  public StudentAdapter(List<Student> list, Context context, OnClickListener onClickListener){
    this.list=list;
    this.context=context;
    this.onClickListener=onClickListener;
  }

  @NonNull
  @Override
  public StudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
    LayoutItemStudentBinding binding=LayoutItemStudentBinding.inflate(LayoutInflater.from(context), parent, false);
    return new StudentHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull StudentHolder holder, int position){
    holder.bind(list.get(position), onClickListener, position);
  }

  @Override
  public int getItemCount(){
    return list.size();
  }

  class StudentHolder extends RecyclerView.ViewHolder{
    LayoutItemStudentBinding binding;

    public StudentHolder(@NonNull LayoutItemStudentBinding binding){
      super(binding.getRoot());
      this.binding=binding;
    }

    @SuppressLint("SetTextI18n")
    public void bind(Student student, OnClickListener listener, int pos){
      binding.tvID.setText(student.getId());
      binding.tvName.setText(student.getName());
      binding.tvMark.setText(student.getMark()+"");
    }
  }
}
