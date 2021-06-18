package com.example.postpc_ex8;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class holderTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void creatNewCalc() {

        CalcHolder holder = new CalcHolder();
        holder.AddNewCalc(new Calculate(555));
        Calculate calc = holder.calcs.get(0);
        assertEquals(calc.status, CalcStatus.InProgg);
        assertEquals(calc.root1, 0);
        assertEquals(calc.root2, 0);
    }
    @Test
    public void checkMarkDone() {

        CalcHolder holder = new CalcHolder();
        holder.AddNewCalc(new Calculate(555));
        holder.AddNewCalc(new Calculate(33));
        holder.AddNewCalc(new Calculate(1234135));
        Calculate calc = holder.calcs.get(0);
        assertEquals(calc.status, CalcStatus.InProgg);
        holder.MarkCalcDone(calc,CalcStatus.FinishedPrime);
        assertEquals(calc.status, CalcStatus.FinishedPrime);
        holder.MarkCalcDone(calc,CalcStatus.FinishedRoots);
        assertEquals(calc.status, CalcStatus.FinishedRoots);
    }
    @Test
    public void checkSort() {

        CalcHolder holder = new CalcHolder();
        holder.AddNewCalc(new Calculate(111));
        holder.AddNewCalc(new Calculate(555));
        Calculate calc = holder.calcs.get(0);
        assertEquals(calc.numberToCalc, 111);
        holder.MarkCalcDone(calc,CalcStatus.FinishedRoots);
        Calculate testCalc = holder.calcs.get(0);
        assertEquals(testCalc.numberToCalc, 555);
    }





}