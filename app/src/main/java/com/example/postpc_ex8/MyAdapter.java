package com.example.postpc_ex8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import java.util.UUID;

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView calculationRowRoot;
    ImageView calculationRowDelete;
//    ConstraintLayout calculationRow;
    ProgressBar calculationRowProgress;
    View view;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        this.calculationRowRoot = itemView.findViewById(R.id.calculationRowRoot);
        this.calculationRowDelete = itemView.findViewById(R.id.calculationRowDelete);
//        this.calculationRow = itemView.findViewById(R.id.calculationRow);
        this.calculationRowProgress = itemView.findViewById(R.id.calculationRowProgress);
//        this.context = itemView.getContext();
    }
    public void SetCalcProg(int progress){
        calculationRowProgress.setProgress(progress);
    }

    public void turnOffProgBar(){
        calculationRowProgress.setVisibility(View.GONE);
    }

    public void turnOffDeleteButton(){
//        calculationRowDelete.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_delete_24));
    }

    public void setRowText(String message){
        calculationRowRoot.setText(message);
    }
}

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    public CalcHolder calcHolder;
    LayoutInflater inflater;
    private final WorkManager workManager;
    MyViewHolder viewHolder;

    public MyAdapter(Context context, CalcHolder holder, WorkManager workManager){
        calcHolder = holder;
        this.inflater = LayoutInflater.from(context);
        this.workManager = workManager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_view, parent, false);
        viewHolder= new MyViewHolder(view);
        return this.viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int pos = holder.getLayoutPosition();
        Calculate curCalc = calcHolder.calcs.get(pos);
        holder.calculationRowRoot.setText(CalcToString(curCalc));

        if(curCalc.status!=CalcStatus.InProgg){
            holder.turnOffProgBar();
//            holder.turnOffDeleteButton();
        }
        holder.calculationRowDelete.setOnClickListener(view -> {
            if (curCalc.status==CalcStatus.InProgg){
                workManager.cancelWorkById(UUID.fromString(curCalc.workId));
            }
            calcHolder.deleteCalc(curCalc);
            notifyItemRangeRemoved(pos, 1);
        });
        if (curCalc.status==CalcStatus.InProgg){
            holder.calculationRowProgress.setProgress(curCalc.progress);
        }
        else {
            holder.calculationRowProgress.setVisibility(View.INVISIBLE);
//            holder.calculationRowDelete.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_baseline_delete_24));
        }
    }

    @Override
    public int getItemCount() {
        return calcHolder.calcs.size();
    }

    private String CalcToString(Calculate calc) {
        String res="";
        switch(calc.status) {
            case InProgg:
                res="Calculating roots for"+ calc.numberToCalc;
                break;
            case FinishedRoots:
                 res="Roots for "+calc.numberToCalc+":   "+calc.root1+"x"+calc.root2;
                break;
            case FinishedPrime:
                res="Roots for "+calc.numberToCalc+":   numer is prime";
                break;
            default:
                res="defualt";
        }
        return res;
    }
}
