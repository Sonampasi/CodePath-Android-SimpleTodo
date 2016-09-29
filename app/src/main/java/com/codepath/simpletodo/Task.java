package com.codepath.simpletodo;

import java.util.ArrayList;

/**
 * Created by Sonam on 9/24/2016.
 */

public class Task {
    int id;
    String name;
    String priority;
    String due_date;

    public Task(){

    }
    public Task(int id, String name, String priority,String due_date){
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.due_date = due_date;
    }
    public Task(String name, String priority,String due_date){
        this.name = name;
        this.priority = priority;
        this.due_date = due_date;
    }

    // getting ID
    public int getID(){
        return this.id;
    }

    // setting id
    public void setID(int id){
        this.id = id;
    }

    // getting name
    public String getName(){
        return this.name;
    }

    // setting name
    public void setName(String name){
        this.name = name;
    }

    // getting priority
    public String getPriority(){
        return this.name;
    }

    // setting priority
    public void setPriority(String priority){
        this.priority = priority;
    }

    // getting due date
    public String getDueDate(){
        return this.name;
    }

    // setting due date
    public void setDueDate(String priority){
        this.priority = priority;
    }

}
