package com.zr.retrofitapp;

import static com.zr.retrofitapp.ApiServices.BASE_URL;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zr.retrofitapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    binding.srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
      @Override
      public void onRefresh(){
        binding.srLayout.setRefreshing(false);
      }
    });

    request=new HttpRequest();
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

  public void show(List<Student> list){
    adapter=new StudentAdapter(list, MainActivity.this, ((position, v)->{
      PopupMenu popup=new PopupMenu(MainActivity.this, v);
      popup.inflate(R.menu.option_menu);
      popup.setGravity(Gravity.END);
      popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
        @Override
        public boolean onMenuItemClick(MenuItem item){
          if (item.getItemId()==R.id.opt_update) {
            //            updateDialog(position);
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
}