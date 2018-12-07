package com.example.lyy.androidexperiment_notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ContentPage extends AppCompatActivity{
    private String foldername;
    private MyDatabaseHelper dbHelper;

    private TextView titleView;
    private TextView contentView;

    private String title;
    private String content;
    private Integer note_id;
    private Integer cid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        dbHelper = new MyDatabaseHelper(this,"Notedb.db", null,1);

        Toolbar toolbar = (Toolbar)findViewById(R.id.content_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        foldername = intent.getStringExtra("foldername");
        note_id = Integer.parseInt(intent.getStringExtra("note_id"));
        cid = Integer.parseInt(intent.getStringExtra("folderid"));
        this.setTitle(foldername);
        titleView = findViewById(R.id.notetitle);
        title = intent.getStringExtra("title");
        titleView.setText(title);
        contentView = findViewById(R.id.notecontents);
        content = intent.getStringExtra("content");
        contentView.setText(content);
    }
    public void onDeleteFolderClick(View view){
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //提交数据库
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String sql = null;

            if (title == null || content == null || !title.equals(titleView.getText().toString()) || !content.equals(contentView.getText().toString())){
                if (note_id == null || note_id == 0) {
                    Calendar calendar = Calendar.getInstance();

                    String date = "" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "  " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

                    sql = "INSERT INTO note('id','title','content', 'cid', 'time') VALUES(NULL,'" + titleView.getText().toString() + "','" + contentView.getText().toString() + "', " + cid + ", '" + date + "')";
                }else{
                    sql = "UPDATE note SET title='" + titleView.getText().toString() + "',content='" + contentView.getText().toString() + "' WHERE id = " + note_id;
                }
                db.execSQL(sql);
            }
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
