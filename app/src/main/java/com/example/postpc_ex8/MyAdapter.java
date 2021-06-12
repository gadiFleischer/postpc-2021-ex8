package com.example.postpc_ex8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;
import java.util.UUID;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public WorkManager workManager;
    public CalcHolder holder;

    public MyAdapter(CalcHolder calcHolder, WorkManager workManager) {
        this.workManager = workManager;
        this.holder = calcHolder;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        Calculate calc = this.holder.calcs.get(holder.getLayoutPosition());
        holder.TextCalc.setText(holder.CalcToString(calc));
        holder.deleteButton.setOnClickListener(view -> {
            if (calc.status==CalcStatus.InProgg) {
                workManager.cancelWorkById(UUID.fromString(calc.workId));
            }
            this.holder.deleteCalc(calc);
            notifyItemRangeRemoved(holder.getLayoutPosition(), 1);
        });
        changeProgressBar(holder, calc);
    }

    @Override
    public int getItemCount() {
        return holder.calcs.size();
    }

    private void changeProgressBar(@NonNull ViewHolder holder, Calculate calc) {
        switch (calc.status) {
            case InProgg:
                holder.ProgBar.setProgress(calc.progress);
                holder.TextCalc.setText(holder.CalcToString(calc));
                break;
            default:
                holder.ProgBar.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout calculationRow;
        TextView TextCalc;
        ImageView deleteButton;
        public ProgressBar ProgBar;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.calculationRow = itemView.findViewById(R.id.calculationRow);
            this.TextCalc = itemView.findViewById(R.id.showNumberText);
            this.deleteButton = itemView.findViewById(R.id.deleteButton);
            this.ProgBar = itemView.findViewById(R.id.ShowProggView);
        }

        public void SetViewsToComplete(Calculate calc) {
            TextCalc.setText(CalcToString(calc));
            ProgBar.setVisibility(View.GONE);
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
}