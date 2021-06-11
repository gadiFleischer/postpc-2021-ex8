package com.example.postpc_ex8;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MyApp extends Application {
//    public List<TodoItem> todoItems;
    SharedPreferences sharedPref;
    Context context;
    public MyApp(Context context){
        this.context=context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        loadTodoList();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    public void loadTodoList() {
//        todoItems =  new ArrayList<>();
        String itemsJson = sharedPref.getString("todoItems", "");
        if (!itemsJson.equals("")) {
//            Type listType = new TypeToken<ArrayList<TodoItem>>(){}.getType();
//            todoItems = new Gson().fromJson(itemsJson, listType);
        }
    }

    public void saveTodoList() {
//        String itemsJson = new Gson().toJson(todoItems);
        sharedPref.edit().putString("todoItems", "gadi").apply();
    }
}
