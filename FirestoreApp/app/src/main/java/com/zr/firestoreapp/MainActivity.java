package com.zr.firestoreapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zr.firestoreapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity{
  private ActivityMainBinding binding;
  private List<Todo> list;
  private TodoAdapter adapter;
  private FirebaseFirestore database;

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

    list=new ArrayList<>();
    database=FirebaseFirestore.getInstance();  //Khởi tạo database

    getFromFirestore();

    binding.srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
      @Override
      public void onRefresh(){
        binding.srLayout.setRefreshing(false);
        getFromFirestore();
      }
    });
  }

  public void getFromFirestore(){
    database.collection("TODO").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task){
        if (task.isSuccessful()) {
          list.clear();
          for (QueryDocumentSnapshot queryDS : task.getResult()) {
            Todo todo=queryDS.toObject(Todo.class);
            list.add(todo);
          }
        }
        try {
          adapter=new TodoAdapter(list, MainActivity.this, position->{
            Toast.makeText(MainActivity.this, list.get(position).content, Toast.LENGTH_SHORT).show();
          });
          binding.rcvTodo.setAdapter(adapter);
        } catch (Exception e) {
          Log.e("TAG Error", "getFromFirestore: ", e);
        }
      }
    }).addOnFailureListener(new OnFailureListener(){
      @Override
      public void onFailure(@NonNull Exception e){
        Toast.makeText(MainActivity.this, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show();
        Log.e("TAG Error", "onFailure: ", e);
      }
    });
  }
}