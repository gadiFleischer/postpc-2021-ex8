package com.example.postpc_ex8;

import java.io.Serializable;
import java.util.Random;
enum CalcStatus {
    InProgg,
    FinishedRoots,
    FinishedPrime
}

public class Calculate implements Serializable, Comparable<Calculate> {
    public long numberToCalc;
    public long currentNum;
    public long root1;
    public long root2;
    public CalcStatus status;
    public int id;
    public String workId;
    public int progress;

    public Calculate(long numberToCalc) {
        this.numberToCalc=numberToCalc;
        this.root1=0;
        this.root2=0;
        this.status=CalcStatus.InProgg;
        this.progress=0;
        this.id = new Random().nextInt(999999);
        this.workId="";
        this.currentNum=2;
    }

    @Override
    public int compareTo(Calculate otherCalc) {
        if (this.status==CalcStatus.InProgg && otherCalc.status!=CalcStatus.InProgg ){
            return -1;
        }
        else if(this.status!=CalcStatus.InProgg && otherCalc.status==CalcStatus.InProgg){
            return 1;
        }
        else if (this.numberToCalc > otherCalc.numberToCalc){
            return 1;
        }
        else{
            return -1;
        }
    }
}
