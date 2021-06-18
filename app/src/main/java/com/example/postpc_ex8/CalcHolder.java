package com.example.postpc_ex8;

import android.app.Activity;
import java.util.ArrayList;
import java.util.Collections;

public class CalcHolder extends Activity {

    ArrayList<Calculate> calcs;

    public CalcHolder() {
        calcs= new ArrayList<>();
    }

    public void AddNewCalc(Calculate calc){
        calcs.add(calc);
        Collections.sort(calcs);
    }

    public void MarkCalcDone(Calculate calc,CalcStatus status){
        calc.status = status;
        calc.progress=100;
        Collections.sort(calcs);
    }

    public void deleteCalc(Calculate calc)
    {
        calcs.remove(calc);
        Collections.sort(calcs);
    }
    public boolean findOldCalc(long numberToCalc){
        for (Calculate calc : calcs) {
            if (calc.numberToCalc == numberToCalc) {
                return true;
            }
        }
        return false;
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
