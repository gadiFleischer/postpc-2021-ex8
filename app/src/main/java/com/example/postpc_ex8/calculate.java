package com.example.postpc_ex8;

import java.io.Serializable;

enum CalcStatus {
    InProgg,
    FinishedRoots,
    FinishedPrime
}

public class calculate implements Serializable, Comparable<calculate> {
    public long numberToCalc;
    public long root1;
    public long root2;
    public CalcStatus status;
    public int id;
    public String workId;
    int progress=0;
    static int numOfItems = 0;


//    calculating/finished-found-roots/finished-is-prime

    public calculate(long numberToCalc) {
        this.numberToCalc=numberToCalc;
        this.root1=0;
        this.root2=0;
        this.status=CalcStatus.InProgg;
        this.progress=0;
        numOfItems+=1;
    }

//
//    protected void onHandleIntent(Intent intent) {
//        if (intent == null) return;
//        long numberToCalculateRootsFor = intent.getLongExtra("number_for_service", 0);
//        if (numberToCalculateRootsFor <= 0) {
//            Log.e("CalculateRootsService", "can't calculate roots for non-positive input" + numberToCalculateRootsFor);
//            return;
//        }
//        Intent sendIntent = new Intent();
//        long timeStartMs = System.currentTimeMillis();
//        if(numberToCalculateRootsFor==1){
//            sendGood(numberToCalculateRootsFor, sendIntent,1,1,timeStartMs);
//        }else{
//            for(long i=2;i< numberToCalculateRootsFor/2; i++){
//                if (numberToCalculateRootsFor % i == 0){
//                    sendGood(numberToCalculateRootsFor, sendIntent,i,numberToCalculateRootsFor/i,timeStartMs);
//                    return;
//                }
//                long checkTime=System.currentTimeMillis() - timeStartMs;
//                if(TimeUnit.MILLISECONDS.toSeconds(checkTime) >= 20 ){
//                    sendBad(sendIntent,numberToCalculateRootsFor,checkTime);
//                    return;
//                }
//            }
//            //the number is prime
//            sendGood(numberToCalculateRootsFor, sendIntent,numberToCalculateRootsFor,1,timeStartMs);
//        }
//    }

    //    calculating/finished-found-roots/finished-is-prime
    @Override
    public int compareTo(calculate otherCalc) {
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
