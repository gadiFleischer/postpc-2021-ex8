package com.example.postpc_ex8;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class calcRootWorker extends Worker {
    private static final String PROGRESS = "PROGRESS";
    private static final long DELAY = 1000L;

    public calcRootWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Result doWork() {
        return null;
    }
}
