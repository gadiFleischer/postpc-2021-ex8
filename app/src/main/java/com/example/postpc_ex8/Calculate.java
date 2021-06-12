package com.example.postpc_ex8;

import java.io.Serializable;

enum CalcStatus {
    InProgg,
    FinishedRoots,
    FinishedPrime
}

public class Calculate implements Serializable, Comparable<Calculate> {
    public long numberToCalc;
    public long root1;
    public long root2;
    public CalcStatus status;
    public int id;
    public String workId;
    public int progress=0;
    static int numOfItems = 0;

    public Calculate(long numberToCalc) {
        this.numberToCalc=numberToCalc;
        this.root1=0;
        this.root2=0;
        this.status=CalcStatus.InProgg;
        this.progress=0;
        numOfItems+=1;
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
