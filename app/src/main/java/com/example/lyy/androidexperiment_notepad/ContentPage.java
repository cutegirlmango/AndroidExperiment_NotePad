package com.example.lyy.androidexperiment_notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContentPage extends AppCompatActivity{
    private String foldername;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        Toolbar toolbar = (Toolbar)findViewById(R.id.content_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        foldername = intent.getStringExtra("foldername");
        this.setTitle(foldername);
        TextView titleView = findViewById(R.id.notetitle);
        titleView.setText(intent.getStringExtra("title"));
        TextView contentView = findViewById(R.id.notecontents);
        contentView.setText(intent.getStringExtra("content"));
    }
    public void onDeleteFolderClick(View view){
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //提交数据库
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
