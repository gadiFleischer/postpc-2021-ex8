package com.example.postpc_ex8;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import org.jetbrains.annotations.Nullable;

public class CalcRootWorker extends Worker {
    private static final String PROGRESS = "PROGRESS";
    public static final int MAX_TIME = 200000;
    private int cur_progress = 0;
    Data.Builder dataBuilder;

    public CalcRootWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
    }
    @Nullable
    private boolean TimeIsUp(long timeStartMs, int id, long numToCalc, long currentNum) {
        long nowTime = System.currentTimeMillis() - timeStartMs;
        if (nowTime >= MAX_TIME) {
            dataBuilder.putLong("numToCalc", numToCalc);
            dataBuilder.putLong("currentNum", currentNum);
            dataBuilder.putInt("id", id);
            dataBuilder.putBoolean("continueCalc", true);
            return true;
        }
        return false;
    }
    private void updateProgressBar(long numToCalc, long currentNum){
        int cur_prog = (int) ((currentNum / numToCalc) * 100);
        if (cur_progress != cur_prog) {
            cur_progress = cur_prog;
            setProgressAsync(new Data.Builder().putInt(PROGRESS, cur_progress).build());
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        dataBuilder = new Data.Builder();
        long timeStartMs = System.currentTimeMillis();
        int id = getInputData().getInt("id", -1);
        long numToCalc = getInputData().getLong("numToCalc", 0);
        long currentNum = getInputData().getLong("currentNum", 2);
        for (long i = 2; i < numToCalc / 2; i++) {
            if(TimeIsUp(timeStartMs, id, numToCalc, currentNum)){
                return Result.failure(dataBuilder.build());
            }
            updateProgressBar(numToCalc, currentNum);
            if (numToCalc % i == 0) {
                dataBuilder.putInt("id", id);
                dataBuilder.putLong("root1", i);
                dataBuilder.putLong("root2",numToCalc / i);
                dataBuilder.putLong("numToCalc", numToCalc);
                return Result.success(dataBuilder.build());
            }
        }
        dataBuilder.putInt("id", id);
        dataBuilder.putLong("numToCalc", numToCalc);
        dataBuilder.putBoolean("continueCalc", false);
        return Result.failure(dataBuilder.build());
    }

}
