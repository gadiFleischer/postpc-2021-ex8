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
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.widget.LinearLayout.VERTICAL;
import static java.util.jar.Pack200.Unpacker.PROGRESS;

public class MainActivity extends AppCompatActivity {
    EditText insertNumberEditor;
    FloatingActionButton buttonCreateCalc;
    RecyclerView recyclerRoots;
    MyAdapter adapter;
    CalcHolder holder;
    MyApp app;
    Data.Builder dataBuilder;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        app = new MyApp(this);

        //views
        insertNumberEditor = findViewById(R.id.insertNumberEditor);
        buttonCreateCalc = findViewById(R.id.CalcButton);
        recyclerRoots = findViewById(R.id.recyclerRoots);

        //init recycler stuff
        holder = new CalcHolder();
        holder.calcs=app.calcs;
        if(context!=null){
            adapter = new MyAdapter(holder, WorkManager.getInstance(context),app);
        }
        recyclerRoots.setAdapter(adapter);
        recyclerRoots.setLayoutManager(new LinearLayoutManager(this));
        recyclerRoots.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        dataBuilder = new Data.Builder();

        buttonCreateCalc.setOnClickListener(view -> {
            try {
                long numToCalc = Long.parseLong(insertNumberEditor.getText().toString());
                if(!holder.findOldCalc(numToCalc)){
                    StartCalc(new Calculate(numToCalc), true);
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        //run in async with all other tasks
        for (Calculate calc : holder.calcs) {
            if (calc.status == CalcStatus.InProgg) {
                StartCalc(calc, false);
            }
        }
    }

    private void StartCalc(Calculate curCalc, boolean isNew){
        if (isNew){
            holder.AddNewCalc(curCalc);
            app.saveCalcs(holder.calcs);
            adapter.notifyItemInserted(holder.getCalcIndex(curCalc));
        }

        dataBuilder.putInt("id", curCalc.id);
        dataBuilder.putLong("numToCalc", curCalc.numberToCalc);
        dataBuilder.putLong("currentNum", curCalc.currentNum);
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
                    Data output = workInfo1.getOutputData();
                    if(GotFail(output)){
                        curCalc.currentNum= output.getLong("currentNum", 2);
                        curCalc.progress=output.getInt("prog", 0);
                        holder.calcs.set(holder.getCalcIndex(curCalc),curCalc);
                        updateProgress(workInfo1.getId().toString(), output.getInt("prog", 0));
                        app.saveCalcs(holder.calcs);
                        StartCalc(curCalc,false);
                    }
                }
//                updateProgress(workInfo1.getId().toString(), workInfo1.getOutputData().getInt("prog", 0));
            }
        });
    }
    private void GotSuccess(Data output){
        Calculate calc = holder.getCalcById(output.getInt("id", -1));
        if(calc!=null){
            calc.root1 =  output.getLong("root1", 0);
            calc.root2 = output.getLong("root2", 0);
            holder.MarkCalcDone(calc,CalcStatus.FinishedRoots);
            app.saveCalcs(holder.calcs);
            adapter.notifyDataSetChanged();
            MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) recyclerRoots.
                    findViewHolderForLayoutPosition(holder.getCalcIndex(calc));
            if(viewHolder!=null){
                viewHolder.SetViewsToComplete(calc);
            }
        }

    }
    private boolean GotFail(Data output){
        if(output.getBoolean("continueCalc", true)){
            return true;
        }
        Calculate calc = holder.getCalcById(output.getInt("id", -1));
        calc.status = CalcStatus.FinishedPrime;
        adapter.notifyDataSetChanged();
        MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder)
                recyclerRoots.findViewHolderForLayoutPosition(holder.getCalcIndex(calc));
        if (viewHolder != null) {
            viewHolder.SetViewsToComplete(calc);
        }
        holder.MarkCalcDone(calc,CalcStatus.FinishedPrime);
        app.saveCalcs(holder.calcs);
        return false;
    }
    private void updateProgress(String workId, int progress){
        for (int i = 0; i < holder.calcs.size(); i++) {
            Calculate calc = holder.calcs.get(i);
            if (calc.workId.equals(workId)) {
                calc.progress = progress;
                MyAdapter.ViewHolder viewHolder =
                        (MyAdapter.ViewHolder) recyclerRoots.findViewHolderForLayoutPosition(i);
                if (viewHolder != null) {
                    if (progress <= 1) {
                        viewHolder.ProgBar.setProgress(0);
                    }
                    else if (progress >= 99) {
                        viewHolder.ProgBar.setProgress(100);
                    }
                    viewHolder.ProgBar.setProgress(progress);
                    viewHolder.TextCalc.setText(CalcToString(calc));
                }
            }
        }
    }
    public String CalcToString(Calculate calc) {
        String res;
        switch(calc.status) {
            case InProgg:
                res="Calculating roots for "+ calc.numberToCalc+" at :"+calc.progress +"% percent";
                break;
            case FinishedRoots:
                res="Roots for "+calc.numberToCalc+":   "+calc.root1+"x"+calc.root2;
                break;
            case FinishedPrime:
                res="Roots for "+calc.numberToCalc+":   number is prime";
                break;
            default:
                res="error";
        }
        return res;
    }
}