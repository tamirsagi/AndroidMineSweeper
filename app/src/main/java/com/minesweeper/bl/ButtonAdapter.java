package com.minesweeper.BL;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.minesweeper.app.R;

import java.util.ArrayList;

public class ButtonAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<ImageButton> board;
    int layoutResourceId;


    public ButtonAdapter(Context context, int layoutResourceId,ArrayList<ImageButton> content) {
        super(context, layoutResourceId, content);
        this.context = context;
        board = content;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return board.size();
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        RecordHolder holder = null;
        View row = convertView;
        if (row == null) {
                // if it's not recycled, initialize some attributes
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new RecordHolder();
                holder.imageButton = (ImageView)row.findViewById(R.id.BTH_Cell);
                row.setTag(holder);
        }
        else {
            holder = (RecordHolder) row.getTag();
        }
        holder.imageButton = board.get(position);
        return row;
    }

    static class RecordHolder {
        ImageView imageButton;
    }

}