package com.example.postpc_ex8;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;

import java.util.ArrayList;

public class MyApp extends Application {
    public ArrayList<Calculate> calcs;
    SharedPreferences sharedPref;
    Context context;
    public MyApp(Context context){
        this.context=context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        loadCalcs();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        calcs = new ArrayList<>();
    }


    public void loadCalcs() {
        calcs =  new ArrayList<>();
        String itemsJson = sharedPref.getString("calcs", "");
        if (!itemsJson.equals("")) {
            Type listType = new TypeToken<ArrayList<Calculate>>(){}.getType();
            calcs = new Gson().fromJson(itemsJson, listType);
        }
    }

    public void saveCalcs(ArrayList<Calculate> calcs) {
        this.calcs=calcs;
        String itemsJson = new Gson().toJson(calcs);
        sharedPref.edit().putString("calcs", itemsJson).apply();
    }
}
