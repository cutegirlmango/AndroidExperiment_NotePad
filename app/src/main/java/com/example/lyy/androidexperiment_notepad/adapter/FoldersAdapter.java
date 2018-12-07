package com.example.lyy.androidexperiment_notepad.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lyy.androidexperiment_notepad.Folder;
import com.example.lyy.androidexperiment_notepad.R;

import java.util.List;


public class FoldersAdapter extends ArrayAdapter<Folder> {
    private int resourceId;

    public FoldersAdapter(Context context, int resource, List<Folder> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(@NonNull int position, View convertView, @NonNull ViewGroup parent) {
        Folder folder = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.foldername= (TextView)view.findViewById(R.id.folder_name);
            viewHolder.notesnum = (TextView)view.findViewById(R.id.notes_number);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.notesnum.setText(String.valueOf(folder.getNote_num()));
        viewHolder.foldername.setText(folder.getTitle());
        return view;
    }

    class ViewHolder{
        TextView foldername;
        TextView notesnum;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
