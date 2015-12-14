package com.minesweeper.UI.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.minesweeper.BL.DB.PlayerRecord;
import com.minesweeper.UI.Activities.R;

import java.util.List;

/**
 * @author Tamir Sagi
 *         This class manages the rcycler list's  adapter inside records fragment.
 */

public class RecordsRecyclerAdapter extends RecyclerView.Adapter<RecordsRecyclerAdapter.RecordHolder> {

    private Context context;
    private List<PlayerRecord> records;

    public RecordsRecyclerAdapter(Context context, List<PlayerRecord> records) {
        this.records = records;
        this.context = context;
    }


    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.record_row, parent, false);
        return new RecordHolder(itemView);          //return custom item
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        PlayerRecord record = records.get(position);
        holder.tv_place.setText(record.getId());
        holder.tv_player_name.setText(record.getFullName());
        holder.tv_round_time.setText(record.getRoundTime());
        String location = "Location Not Found";
        boolean cityExist = false;
        if (record.getCity() != null && !record.getCity().isEmpty()) {
            location = record.getCity();
            cityExist = true;
        }

        if (record.getCountry() != null && !record.getCountry().isEmpty()) {
            if (cityExist)
                location += "," + record.getCountry();
            else
                location = record.getCountry();
        }

        holder.tv_location.setText(location);
        holder.tv_date.setText(record.getDate());
    }


    @Override
    public int getItemCount() {
        return records.size();
    }


    public void updateList(List<PlayerRecord> records) {
        this.records = records;
        notifyDataSetChanged();
    }


    public void addItem(int position, PlayerRecord data) {
        records.add(position, data);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        records.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * this class represents an item within the recycler list.
     */
    class RecordHolder extends RecyclerView.ViewHolder {
        TextView tv_place;
        TextView tv_player_name;
        TextView tv_round_time;
        TextView tv_location;
        TextView tv_date;

        public RecordHolder(View itemView) {
            super(itemView);
            tv_place = (TextView) itemView.findViewById(R.id.ColumnId);
            tv_player_name = (TextView) itemView.findViewById(R.id.ColumnName);
            tv_round_time = (TextView) itemView.findViewById(R.id.ColumnRoundTime);
            tv_location = (TextView) itemView.findViewById(R.id.ColumnLocation);
            tv_date = (TextView) itemView.findViewById(R.id.ColumnDate);
        }
    }


}