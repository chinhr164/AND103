package com.zr.retrofitapp;

public class Student{
  private String id, name;
  private double mark;

  public Student(){
  }

  public Student(String id, String name, Double mark){
    this.id=id;
    this.name=name;
    this.mark=mark;
  }

  public String getId(){
    return id;
  }

  public void setId(String id){
    this.id=id;
  }

  public String getName(){
    return name;
  }

  public void setName(String name){
    this.name=name;
  }

  public double getMark(){
    return mark;
  }

  public void setMark(double mark){
    this.mark=mark;
  }
}
