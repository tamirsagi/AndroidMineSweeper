package com.minesweeper.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.minesweeper.BL.DB.DbManager;
import com.minesweeper.BL.DB.PlayerRecord;
import com.minesweeper.UI.Activities.DBRecordsFragmentActivity;
import com.minesweeper.UI.Activities.R;

import java.util.List;


public class RecordsTable extends Fragment {


    private static final String TAG = "RecordsTableViewFragment";
    public static final String PAGE_NUMBER = "PAGE NUMBER";

    private Spinner highScoresTables;
    private String[] spinnerDBTablesNames;
    private String mDefaultTable = DbManager.Tables.PLAYERS_RECORDS_INTERMEDIATE.toString();


    private static final int DATASET_COUNT = 10;
    private static final int SPAN_COUNT = 2;
    private int mPage;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected RecyclerView mRecyclerView;
    private RecordsRecyclerAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private List<PlayerRecord> mDataSet;
    protected LayoutManagerType mCurrentLayoutManagerType;

    private TextView tvEmptyTableMessage;

    public static RecordsTable newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(PAGE_NUMBER, page);
        RecordsTable fragment = new RecordsTable();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(PAGE_NUMBER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_record_table, container, false);
        root.setTag(TAG);
        initDataSet();
        setupSpinner(root);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.RecordsRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        tvEmptyTableMessage = (TextView) root.findViewById(R.id.emptyTable);
        if (mDataSet.size() > 0) {
            setRecyclerViewVisible(true);
            setupRecyclerListAdapter();
        } else {
            setRecyclerViewVisible(false);
        }

        return root;
    }

    private void initDataSet() {
        mDataSet = DbManager.getInstance(getContext().getApplicationContext())
                .getRecords(mDefaultTable);
    }


    /**
     * set the spinner
     */
    private void setupSpinner(View view) {
        highScoresTables = (Spinner) view.findViewById(R.id.tablePicker);
        spinnerDBTablesNames = new String[]{"Beginner", "Intermediate", "Expert"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spinnerDBTablesNames);
        highScoresTables.setAdapter(adapter);
        highScoresTables.setSelection(adapter.getPosition(spinnerDBTablesNames[1]));
        highScoresTables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        DBRecordsFragmentActivity.mDefaultTable = DbManager.Tables.PLAYERS_RECORDS_BEGINNERS.toString();
                        updateTable(DbManager.Tables.PLAYERS_RECORDS_BEGINNERS.toString());
                        break;
                    case 1:
                        DBRecordsFragmentActivity.mDefaultTable = DbManager.Tables.PLAYERS_RECORDS_INTERMEDIATE.toString();
                        updateTable(DbManager.Tables.PLAYERS_RECORDS_INTERMEDIATE.toString());
                        break;
                    case 2:
                        DBRecordsFragmentActivity.mDefaultTable = DbManager.Tables.PLAYERS_RECORDS_EXPERT.toString();
                        updateTable(DbManager.Tables.PLAYERS_RECORDS_EXPERT.toString());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void updateTable(String table) {
        mDataSet = DbManager.getInstance(getContext()).getRecords(table);
        if (mDataSet.size() > 0) {
            setRecyclerViewVisible(true);
            if(mAdapter == null)
                setupRecyclerListAdapter();
            mAdapter.updateList(mDataSet);
        }
        else
            setRecyclerViewVisible(false);
    }

    private void setupRecyclerListAdapter(){
        mAdapter = new RecordsRecyclerAdapter(getContext(), mDataSet);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setRecyclerViewVisible(Boolean state) {
        if (state) {
            tvEmptyTableMessage.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            tvEmptyTableMessage.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

}
