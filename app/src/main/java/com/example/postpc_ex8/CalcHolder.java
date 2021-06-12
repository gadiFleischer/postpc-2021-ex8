package com.example.postpc_ex8;

import android.app.Activity;
import java.util.ArrayList;
import java.util.Collections;

public class CalcHolder extends Activity {

    ArrayList<Calculate> calcs;
    MyApp app;

    public CalcHolder(MyApp app) {
        calcs= new ArrayList<>();
        this.app=app;
        calcs=app.calcs;
    }

    public void AddNewCalc(Calculate calc){
        calcs.add(calc);
        Collections.sort(calcs);
        app.saveTodoList(calcs);
    }

    public void MarkCalcDone(Calculate calc,CalcStatus status){
        calc.status = status;
        calc.progress=100;
        Collections.sort(calcs);
        app.saveTodoList(calcs);
    }

    public void deleteCalc(Calculate calc)
    {
        calcs.remove(calc);
        app.saveTodoList(calcs);
    }
    public int getCalcIndex(Calculate calc)
    {
        return calcs.indexOf(calc);
    }
    public Calculate getCalcById(int id){
        for (Calculate calc : calcs) {
            if (calc.id == id) {
                return calc;
            }
        }
        return null;
    }
}
