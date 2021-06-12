package com.example.postpc_ex8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.widget.LinearLayout.VERTICAL;
import static java.util.jar.Pack200.Unpacker.PROGRESS;

public class MainActivity extends AppCompatActivity {
    public EditText insertNumberEditor;
    public FloatingActionButton buttonCreateCalc;
    public RecyclerView recyclerRoots;
    public MyAdapter adapter;
    public CalcHolder holder;
    public MyApp app;
    public Data.Builder dataBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNumberEditor = findViewById(R.id.insertNumberEditor);
        buttonCreateCalc = findViewById(R.id.CalcButton);
        recyclerRoots = findViewById(R.id.recyclerRoots);

        app = new MyApp(this);
        holder = new CalcHolder(app);
        dataBuilder = new Data.Builder();
        adapter = new MyAdapter(this, holder, WorkManager.getInstance(this));
        recyclerRoots.setAdapter(adapter);
        recyclerRoots.setLayoutManager(new LinearLayoutManager(this));
        recyclerRoots.addItemDecoration(new DividerItemDecoration(this, VERTICAL));

        buttonCreateCalc.setOnClickListener(view -> {
            try {
                long numToCalc = Long.parseLong(insertNumberEditor.getText().toString());
                Calculate curCalc = new Calculate(numToCalc);
                StartCalc(curCalc, true);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        for (Calculate calc : holder.calcs) {
            if (calc.status == CalcStatus.InProgg) {
                StartCalc(calc, false);
            }
        }
    }

    private void StartCalc(Calculate curCalc, boolean isNew){
        if (isNew){
            holder.AddNewCalc(curCalc);
            adapter.notifyItemInserted(holder.getCalcIndex(curCalc));
        }

        dataBuilder.putInt("id", curCalc.id);
        dataBuilder.putLong("numToCalc", curCalc.numberToCalc);
//        dataBuilder.putLong("current", curCalc.curCandidate);
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CalcRootWorker.class).setInputData(dataBuilder.build()).build();
        WorkManager.getInstance(this).enqueue(workRequest);
        curCalc.workId=workRequest.getId().toString();
        LiveData<WorkInfo> workInfo = WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(workRequest.getId());
        workInfo.observeForever(workInfo1 -> {
            if (workInfo1 != null) {
                WorkInfo.State state = workInfo1.getState();
                if (state == WorkInfo.State.SUCCEEDED){
                    GotSuccess(workInfo1.getOutputData());
                }
                else if (state == WorkInfo.State.FAILED){
                    boolean tryAgain = GotFail(workInfo1.getOutputData());
                    if(tryAgain){
                        StartCalc(curCalc,false);
                    }
                }
                Data progress = workInfo1.getProgress();
                int value = progress.getInt(PROGRESS, 0);
                updateProgress(workInfo1.getId().toString(), value);
            }
        });
    }
    private void GotSuccess(Data output){
        int id = output.getInt("id", -1);
        long root1 = output.getLong("root1", 0);
        long root2 = output.getLong("root2", 0);

        Calculate calc = holder.getCalcById(id);
        if(calc!=null){
            calc.root1 = root1;
            calc.root2 = root2;
            holder.MarkCalcDone(calc,CalcStatus.FinishedRoots);
            adapter.notifyDataSetChanged();
        }
    }
    private boolean GotFail(Data output){
        if(output.getBoolean("retry", true)){
            return true;
        }
        int id = output.getInt("id", -1);
        Calculate calc = holder.getCalcById(id);
        calc.status = CalcStatus.FinishedPrime;
        adapter.notifyDataSetChanged();
        holder.MarkCalcDone(calc,CalcStatus.FinishedPrime);
        return false;
    }
    private void updateProgress(String workId, int progress){
        for (Calculate calc : holder.calcs) {
            if(calc.workId.equals(workId)){
                calc.progress=progress;
            }
        }


//        for (int i = 0 ; i < holder.calcs.size(); i++){
//            if (holder.calcs.get(i).workId != null && calcHolder.calculations.get(i).workId.equals(workId)){
//                calcHolder.calculations.get(i).progress = progress;
//                CalculationHolder.CalculationAdapter.ViewHolder viewHolder = (CalculationHolder.CalculationAdapter.ViewHolder) calcRecyclerView.findViewHolderForLayoutPosition(i);
//                if (viewHolder != null){
//                    viewHolder.setCalculationRowProgress(progress);
//                }
//            }
//        }
    }
}