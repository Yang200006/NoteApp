package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteapp.bean.Note;
import com.example.noteapp.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_title;
    private EditText et_content;
    private NoteDbOpenHelper mNoteDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        findViewById(R.id.btn_add).setOnClickListener(this);
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);

    }

    @Override
    public void onClick(View view) {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();
        if (TextUtils.isEmpty(title)){
            ToastUtil.toastShort(this, "標題不能為空!");
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedTime(getCurrentTimeFormat());
        long row = mNoteDbOpenHelper.insertData(note);
        if (row != -1){
            ToastUtil.toastShort(this, "添加成功!");
            this.finish();
        }else {
            ToastUtil.toastShort(this, "添加失敗!");

        }
    }

    private String getCurrentTimeFormat(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
}
