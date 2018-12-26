package com.example.lyy.androidexperiment_notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyy.androidexperiment_notepad.Model.Folder;
import com.example.lyy.androidexperiment_notepad.Model.Note;
import com.example.lyy.androidexperiment_notepad.adapter.FoldersAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Folder> FolderList;
    private List<Note> notes = new ArrayList<>();
    private FoldersAdapter adapterListView;
    private ListView lv;

    private MyDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDatabaseHelper(this,"Notedb.db", null,1);

        initData();
        TextView edit = findViewById(R.id.folder_delete);
    }

    private void initData(){
        FolderList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("category",null,null,null,null,null,null,null);

        if(cursor.moveToFirst()){
            do{
                Folder folder = new Folder();
                folder.setId(cursor.getInt(cursor.getColumnIndex("id")));
                folder.setTitle(cursor.getString(cursor.getColumnIndex("name")));
                FolderList.add(folder);
            }while(cursor.moveToNext());
        }
        cursor.close();
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
                intent.putExtra("folderid","" + folder.getId());
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
       final SQLiteDatabase db = dbHelper.getWritableDatabase();
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
                            db.execSQL("update category set name = '" + newname+ "' where id = " + FolderList.get(pos).getId());
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
                int cId = FolderList.get(pos).getId();
                if(FolderList.remove(pos)!=null){//这行代码必须有
                    db.execSQL("delete from category where id = " + cId);
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

    protected void newFolder(View view){
        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建文件夹").setView(inputServer).setNegativeButton("关闭", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String inputName = inputServer.getText().toString();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("insert into category('name') values('" + inputName + "')");
                initData();
            }
        });
        builder.show();
    }
}
