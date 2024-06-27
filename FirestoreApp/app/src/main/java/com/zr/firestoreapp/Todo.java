package com.zr.firestoreapp;

import java.util.HashMap;

public class Todo{
  String id, title, content;

  public Todo(){
  }

  public Todo(String id, String title, String content){
    this.id=id;
    this.title=title;
    this.content=content;
  }

  public HashMap<String, Object> convertHashMap(){
    HashMap<String ,Object> hashMap =  new HashMap<>();
    hashMap.put("id",id);
    hashMap.put("title",title);
    hashMap.put("content",content);
    return hashMap;
  }

  public String getId(){
    return id;
  }

  public void setId(String id){
    this.id=id;
  }

  public String getTitle(){
    return title;
  }

  public void setTitle(String title){
    this.title=title;
  }

  public String getContent(){
    return content;
  }

  public void setContent(String content){
    this.content=content;
  }
}
