package com.example.lyy.androidexperiment_notepad.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lyy.androidexperiment_notepad.Folder;
import com.example.lyy.androidexperiment_notepad.Note;
import com.example.lyy.androidexperiment_notepad.R;

import java.util.List;

public class NoteListAdapter extends ArrayAdapter<Note>{
    private int resourceId;

    public NoteListAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(@NonNull int position, View convertView, @NonNull ViewGroup parent) {
        Note note = getItem(position);
        View view;
        NoteListAdapter.ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new NoteListAdapter.ViewHolder();
            viewHolder.content=(TextView)view.findViewById(R.id.note_content);
            viewHolder.noteTitle = (TextView)view.findViewById(R.id.note_title);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (NoteListAdapter.ViewHolder)view.getTag();
        }
        viewHolder.content.setText(String.valueOf(note.getContent()));
        viewHolder.noteTitle.setText(note.getTitle());
        return view;
    }

    class ViewHolder{
        TextView content;
        TextView noteTitle;
    }
}
