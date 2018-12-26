package com.example.lyy.androidexperiment_notepad.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyy.androidexperiment_notepad.Model.Note;
import com.example.lyy.androidexperiment_notepad.MyDatabaseHelper;
import com.example.lyy.androidexperiment_notepad.R;
import com.example.lyy.androidexperiment_notepad.adapter.NoteListAdapter;

import java.util.ArrayList;
import java.util.List;


public class NoteListView extends AppCompatActivity {
    private List<Note> noteList;
    private ListView lv;
    private NoteListAdapter noteListAdapter;
    private String foldername;
    private int folderid;

    private MyDatabaseHelper dbHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lists);
        dbHelper = new MyDatabaseHelper(this,"Notedb.db", null,1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        foldername = intent.getStringExtra("foldername");
        folderid = Integer.parseInt(intent.getStringExtra("folderid"));
        this.setTitle(R.string.kong);
        TextView textView = findViewById(R.id.the_folder_name);
        textView.setText(foldername);
    }

    private void initData(String where) {
        noteList  = new ArrayList<>();
        where += (where.length() == 0 ? "" : " and" ) + " cid = " + folderid;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("note",null, where,null,null,null,"id desc",null);

        if(cursor.moveToFirst()){
            do{

                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("id")));
                note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                note.setContent(cursor.getString(cursor.getColumnIndex("content")));
                note.setDate(cursor.getString(cursor.getColumnIndex("time")));
                noteList.add(note);
            }while(cursor.moveToNext());
        }

        cursor.close();

        noteListAdapter = new NoteListAdapter(NoteListView.this, R.layout.noteitem, noteList);
        lv = (ListView) findViewById(R.id.list);
        this.registerForContextMenu(lv);
        lv.setAdapter(noteListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = noteList.get(position);
                Intent intent = new Intent();
                intent.putExtra("foldername",foldername);
                intent.putExtra("folderid","" +folderid);
                intent.putExtra("note_id","" + note.getId());
                intent.putExtra("title",note.getTitle());
                intent.putExtra("content", note.getContent());
                intent.setClass(NoteListView.this, ContentPage.class);
                NoteListView.this.startActivity(intent);
            }
        });
    }
    public void onDeleteFolderClick(View view){
        finish();
    }

    public void onNewNoteClick(View view){
        final Note note = new Note();
        Intent intent = new Intent();
        intent.putExtra("foldername",foldername);
        intent.putExtra("folderid",""+folderid);
        intent.putExtra("note_id", "0");
        intent.putExtra("content","");
        intent.setClass(NoteListView.this, ContentPage.class);
        NoteListView.this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,1, Menu.NONE,"重命名");
        menu.add(0,2,Menu.NONE,"删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int pos=(int)lv.getAdapter().getItemId(menuInfo.position);
        switch (item.getItemId()) {
            case 1:
                final EditText inputServer = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请输入标题").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String newname = inputServer.getText().toString();
                        if(!newname.equals("")){
                            System.out.println("success");
                            noteList.get(pos).setTitle(newname);
                            noteListAdapter.notifyDataSetChanged();
                        }else {
                            System.out.println("failed");
                        }
                        Toast.makeText(getBaseContext(), "重命名此备忘录", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
            case 2:
                int cId = noteList.get(pos).getId();
                if(noteList.remove(pos)!=null){
                    db.execSQL("delete from note where id = " + cId);
                    System.out.println("success");
                }else {
                    System.out.println("failed");
                }
                Toast.makeText(getBaseContext(), "删除此备忘录", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        noteListAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("start");
        initData("");
    }

    protected void searchBtn(View view){
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("搜索").setView(inputServer).setNegativeButton("关闭", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String inputName = inputServer.getText().toString();
                String sql = "title like '%" + inputName + "%'" + " or " + "content like '%" + inputName + "%'";
                System.out.println(sql);
                initData(sql);
            }
        });
        builder.show();
    }
}

