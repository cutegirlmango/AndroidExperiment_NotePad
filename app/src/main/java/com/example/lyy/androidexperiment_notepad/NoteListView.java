package com.example.lyy.androidexperiment_notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyy.androidexperiment_notepad.adapter.FoldersAdapter;
import com.example.lyy.androidexperiment_notepad.adapter.NoteListAdapter;

import java.util.ArrayList;
import java.util.List;


public class NoteListView extends AppCompatActivity {
    private List<Note> noteList;
    private ListView lv;
    private NoteListAdapter noteListAdapter;
    private String foldername;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lists);
        Toolbar toolbar = (Toolbar)findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        foldername = intent.getStringExtra("foldername");
        this.setTitle(R.string.kong);
        TextView textView = findViewById(R.id.the_folder_name);
        textView.setText(foldername);
    }

    private void initData() {
        noteList  = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Note note = new Note();
            note.setId(1);
            note.setFolderId(1);
            note.setContent("从前的人，如果心里有了秘密，就会跑到山上找一棵树，在树上挖一个洞，对着树洞说出全部的秘密，再用泥巴把树洞封起来。");
            note.setTitle("my secrete");
            noteList.add(note);
        }
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
                if(noteList.remove(pos)!=null){//这行代码必须有
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
        initData();
    }
}

