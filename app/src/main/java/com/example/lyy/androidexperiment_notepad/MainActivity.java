package com.example.lyy.androidexperiment_notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Folder> FolderList;
    private List<Note> notes = new ArrayList<>();
    private FoldersAdapter adapterListView;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        TextView edit = findViewById(R.id.folder_delete);
    }

    private void initData(){
        FolderList = new ArrayList<>();
        for(int i = 0; i< 5; i++){
            Folder folder = new Folder();
            folder.setId(i);
            folder.setTitle("第"+i+"个folder");
            folder.setNote_num(7);
            FolderList.add(folder);
            notes.clear();
        }
        adapterListView = new FoldersAdapter(MainActivity.this,R.layout.folderitem,FolderList);
        lv = (ListView)findViewById(R.id.lv);
        this.registerForContextMenu(lv);
        lv.setAdapter(adapterListView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Folder folder = FolderList.get(position);
                Intent intent = new Intent();
                intent.putExtra("foldername",folder.getTitle());
                intent.setClass(MainActivity.this, NoteListView.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,1,Menu.NONE,"重命名");
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
                builder.setTitle("请输入文件夹名").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String newname = inputServer.getText().toString();
                        if(!newname.equals("")){
                            System.out.println("success");
                            FolderList.get(pos).setTitle(newname);
                            adapterListView.notifyDataSetChanged();
                        }else {
                            System.out.println("failed");
                        }
                        Toast.makeText(getBaseContext(), "重命名此文件夹", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
            case 2:
                if(FolderList.remove(pos)!=null){//这行代码必须有
                    System.out.println("success");
                }else {
                    System.out.println("failed");
                }
                Toast.makeText(getBaseContext(), "删除此文件夹", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        adapterListView.notifyDataSetChanged();
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("start");
        initData();
    }
}
