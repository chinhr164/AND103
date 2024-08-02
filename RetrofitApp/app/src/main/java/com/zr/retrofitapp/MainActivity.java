package com.zr.retrofitapp;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.zr.retrofitapp.databinding.ActivityMainBinding;
import com.zr.retrofitapp.databinding.DialogAddBinding;
import com.zr.retrofitapp.databinding.DialogUpdateBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{
  private ActivityMainBinding binding;
  private HttpRequest request;

  private StudentAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    binding=ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets)->{
      Insets systemBars=insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    binding.srLayout.setOnRefreshListener(()->{
      binding.srLayout.setRefreshing(false);
      getData();
    });

    request=new HttpRequest();

    getData();
    binding.fabInsert.setOnClickListener(v->{
      addDialog();
    });
  }

  public void getData(){
    request.callAPI().getAll().enqueue(new Callback<List<Student>>(){
      @Override
      public void onResponse(Call<List<Student>> call, Response<List<Student>> response){
        if (response.isSuccessful()) {
          List<Student> list=response.body();
          Log.d("TAG Debug", "onResponse: "+list.size());
          show(list);
        } else {
          Log.e("TAG Error", "onResponse: "+response.errorBody());
        }
      }

      @Override
      public void onFailure(Call<List<Student>> call, Throwable t){
        Log.e("TAG Error", "onFailure: "+t.getMessage());
      }
    });
  }

  private void insertData(Student student){
    request.callAPI().postData(student).enqueue(new Callback<List<Student>>(){
      @Override
      public void onResponse(Call<List<Student>> call, Response<List<Student>> response){
        if (response.isSuccessful()) {
          List<Student> list=response.body();
          Log.d("TAG Debug", "onResponse: "+list.size());
          show(list);
        } else {
          Log.e("TAG Error", "onResponse: "+response.errorBody());
        }
      }

      @Override
      public void onFailure(Call<List<Student>> call, Throwable t){
        Log.e("TAG Error", "onFailure: "+t.getMessage());
      }
    });
  }

  private void updateData(Student student){
    request.callAPI().putData(student.getId(), student).enqueue(new Callback<List<Student>>(){
      @Override
      public void onResponse(Call<List<Student>> call, Response<List<Student>> response){
        if (response.isSuccessful()) {
          List<Student> list=response.body();
          Log.d("TAG Debug", "onResponse: "+list.size());
          show(list);
        } else {
          Log.e("TAG Error", "onResponse: "+response.errorBody());
        }
      }

      @Override
      public void onFailure(Call<List<Student>> call, Throwable t){
        Log.e("TAG Error", "onFailure: "+t.getMessage());
      }
    });
  }

  public void show(List<Student> list){
    adapter=new StudentAdapter(list, MainActivity.this, ((student, v)->{
      PopupMenu popup=new PopupMenu(MainActivity.this, v);
      popup.inflate(R.menu.option_menu);
      popup.setGravity(Gravity.END);
      popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
        @Override
        public boolean onMenuItemClick(MenuItem item){
          if (item.getItemId()==R.id.opt_update) {
            updateDialog(student);
          }
          if (item.getItemId()==R.id.opt_delete) {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn muốn xóa?");

            builder.setNegativeButton("Xác nhận", (dialog, which)->{
              //              deleteToFirestore(list.get(position).getId());
            });

            builder.show();
          }
          return false;
        }
      });
      popup.show();
    }));
    binding.rcvStudent.setAdapter(adapter);
  }

  private void addDialog(){
    DialogAddBinding addBinding=DialogAddBinding.inflate(getLayoutInflater());
    Dialog dialog=new Dialog(MainActivity.this);
    dialog.setContentView(addBinding.getRoot());
    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    addBinding.dialogBtnAdd.setOnClickListener(v->{
      String id=addBinding.dialogEdID.getText().toString().trim();
      String name=addBinding.dialogEdName.getText().toString().trim();
      String markString=addBinding.dialogEdMark.getText().toString().trim();

      if (id.isEmpty() || name.isEmpty() || markString.isEmpty()) {
        Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
      } else {
        double mark;
        try {
          mark=Double.parseDouble(markString);
          if (mark<0 || mark>10) {
            Toast.makeText(this, "Giá trị phải nằm trong khoảng 0-10", Toast.LENGTH_SHORT).show();
          } else {
            Student student=new Student(id, name, mark);
            insertData(student);
            dialog.dismiss();
          }
        } catch (Exception e) {
          Toast.makeText(this, "Vui lòng nhập giá trị là số nguyên", Toast.LENGTH_SHORT).show();
        }
      }
    });
    addBinding.dialogBtnCancel.setOnClickListener(v->{
      dialog.dismiss();
    });
    dialog.show();
  }

  private void updateDialog(Student student){
    DialogUpdateBinding updateBinding=DialogUpdateBinding.inflate(getLayoutInflater());
    Dialog dialog=new Dialog(MainActivity.this);
    dialog.setContentView(updateBinding.getRoot());
    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    String id=student.getId();
    updateBinding.dialogTvID.setText(id);
    updateBinding.dialogEdName.setText(student.getName());
    updateBinding.dialogEdMark.setText(student.getMark()+"");
    updateBinding.dialogBtnUpdate.setOnClickListener(v->{
      String name=updateBinding.dialogEdName.getText().toString().trim();
      String markString=updateBinding.dialogEdMark.getText().toString().trim();

      if (name.isEmpty() || markString.isEmpty()) {
        Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
      } else {
        double mark;
        try {
          mark=Double.parseDouble(markString);
          if (mark<0 || mark>10) {
            Toast.makeText(this, "Giá trị phải nằm trong khoảng 0-10", Toast.LENGTH_SHORT).show();
          } else {
            Student newStudent=new Student(id, name, mark);
            updateData(newStudent);
            dialog.dismiss();
          }
        } catch (Exception e) {
          Toast.makeText(this, "Vui lòng nhập giá trị là số nguyên", Toast.LENGTH_SHORT).show();
        }
      }
    });
    updateBinding.dialogBtnCancel.setOnClickListener(v->{
      dialog.dismiss();
    });
    dialog.show();
  }
}
