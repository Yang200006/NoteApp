package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.noteapp.adapter.MyAdapter;
import com.example.noteapp.bean.Note;
import com.example.noteapp.util.SpfUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private FloatingActionButton fab_add;
    private List<Note> mNotes;
    private MyAdapter mMyAdapter;

    private NoteDbOpenHelper mNoteDbOpenHelper;
    public static final int MODE_LINER = 0;
    public static final int MODE_GRID = 1;
    public static final String KEY_LAYOUT_MODE = "key_layout_mode";
    private int currentListLayoutMode = MODE_LINER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFromDataDB();
        setListLayout();
    }

    private void setListLayout() {
        currentListLayoutMode = SpfUtil.getIntWithDefault(this,KEY_LAYOUT_MODE,MODE_LINER);
        if (currentListLayoutMode == MODE_LINER){
            setToLinearList();
        }else{
            setToGridList();
        }
    }


    private void refreshFromDataDB() {
        mNotes = getDataFromDB();
        mMyAdapter.refreshData(mNotes);
    }

    private void initEvent() {
        mMyAdapter = new MyAdapter(this, mNotes);
        rv.setAdapter(mMyAdapter);
        setToLinearList();
    }

    private void initData() {
        mNotes = new ArrayList<>();
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);
    }

    private List<Note> getDataFromDB() {
        return mNoteDbOpenHelper.queryAllFromDB();
    }

    private String getCurrentTimeFormat(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    private void initView() {
        rv = findViewById(R.id.rv);

    }

    public void add(View view) {
        Intent it = new Intent(this, AddActivity.class);

        startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mNotes = mNoteDbOpenHelper.queryFromDbByTitle(newText);
                mMyAdapter.refreshData(mNotes);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.menu_linear:
                setToLinearList();
                currentListLayoutMode = MODE_LINER;
                SpfUtil.saveInt(this,KEY_LAYOUT_MODE,currentListLayoutMode);
                return true;
            case R.id.menu_grid:
                setToGridList();
                currentListLayoutMode = MODE_GRID;
                SpfUtil.saveInt(this,KEY_LAYOUT_MODE,currentListLayoutMode);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setToGridList() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        rv.setLayoutManager(gridLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_GRID_LAYOUT);
        mMyAdapter.notifyDataSetChanged();
    }

    private void setToLinearList() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);
        mMyAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentListLayoutMode == MODE_LINER){
            MenuItem item = menu.findItem(R.id.menu_linear);
            item.setChecked(true);
        }else{
            menu.findItem(R.id.menu_grid).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}