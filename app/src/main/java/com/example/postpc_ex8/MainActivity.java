package com.example.postpc_ex8;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public EditText insertNumberEditor;
    public FloatingActionButton buttonCreateCalc;
    public RecyclerView recyclerRoots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertNumberEditor = findViewById(R.id.insertNumberEditor);
        buttonCreateCalc = findViewById(R.id.CalcButton);
        recyclerRoots = findViewById(R.id.recyclerRoots);


    }
}