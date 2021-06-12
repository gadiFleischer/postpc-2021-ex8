package com.example.postpc_ex8;

import android.app.Activity;
import java.util.ArrayList;
import java.util.Collections;

public class CalcHolder extends Activity {

    ArrayList<Calculate> calcs;
    MyApp app;

    public CalcHolder() {
        calcs= new ArrayList<>();
        app=new MyApp(this);
    }

//    public void updateCurCandidate(int pos, long curCandidate){
//        calcs.get(pos).curCandidate = curCandidate;
//        if (sp != null){
//            saveItems();
//        }
//    }

    public void CreateCalculation(long numToCalc){
        calcs.add(new Calculate(numToCalc));
        Collections.sort(calcs);
        app.saveTodoList(calcs);
    }

    public void MarkCalcDone(Calculate calc,CalcStatus status){
        calc.status = status;
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

}
