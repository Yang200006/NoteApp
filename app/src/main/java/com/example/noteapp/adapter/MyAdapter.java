package com.example.noteapp.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.EditActivity;
import com.example.noteapp.NoteDbOpenHelper;
import com.example.noteapp.R;
import com.example.noteapp.bean.Note;
import com.example.noteapp.util.ToastUtil;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Note> mBeanList;
    private LayoutInflater mLayoutInflater;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    private int viewType;

    public static int TYPE_LINEAR_LAYOUT = 0;
    public static int TYPE_GRID_LAYOUT = 1;

    public MyAdapter(Context context, List<Note> mBeanList) {
        this.mContext = context;
        this.mBeanList = mBeanList;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LINEAR_LAYOUT){
            View view = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }else if (viewType == TYPE_GRID_LAYOUT){
            View view = mLayoutInflater.inflate(R.layout.list_item_grid_layout, parent, false);
            MyGridViewHolder myGridViewHolder = new MyGridViewHolder(view);
            return myGridViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (holder == null){
            return;
        }

        if (holder instanceof MyViewHolder){
            bindMyViewHolder((MyViewHolder) holder, position);
        }else if (holder instanceof MyGridViewHolder){
            bindGridViewHolder((MyGridViewHolder) holder, position);
        }

    }

    private void bindMyViewHolder(MyViewHolder holder, int position){
        Note note = mBeanList.get(position);
        holder.tv_title.setText(note.getTitle());
        holder.tv_content.setText(note.getContent());
        holder.tv_time.setText(note.getCreatedTime());
        holder.rl_item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, EditActivity.class);
                it.putExtra("note",note);
                mContext.startActivity(it);
            }
        });
        holder.rl_item_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //長按彈出談窗 刪除或者編輯
                Dialog dialog = new Dialog(mContext);
                View view1 = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
                TextView tv_delete = view1.findViewById(R.id.tv_delete);
                TextView tv_edit = view1.findViewById(R.id.tv_edit);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
                        if (row > 0){
                            removeData(position);
                            ToastUtil.toastShort(mContext, "刪除成功");
                        }else {
                            ToastUtil.toastShort(mContext, "刪除失敗");
                        }
                        dialog.dismiss();
                    }
                });
                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent(mContext, EditActivity.class);
                        it.putExtra("note",note);
                        mContext.startActivity(it);
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view1);
                dialog.show();
                return false;
            }
        });
    }

    private void bindGridViewHolder(MyGridViewHolder holder, int position){
        Note note = mBeanList.get(position);
        holder.tv_title.setText(note.getTitle());
        holder.tv_content.setText(note.getContent());
        holder.tv_time.setText(note.getCreatedTime());
        holder.rl_item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, EditActivity.class);
                it.putExtra("note",note);
                mContext.startActivity(it);
            }
        });
        holder.rl_item_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //長按彈出談窗 刪除或者編輯
                Dialog dialog = new Dialog(mContext);
                View view1 = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
                TextView tv_delete = view1.findViewById(R.id.tv_delete);
                TextView tv_edit = view1.findViewById(R.id.tv_edit);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
                        if (row > 0){
                            removeData(position);
                            ToastUtil.toastShort(mContext, "刪除成功");
                        }else {
                            ToastUtil.toastShort(mContext, "刪除失敗");
                        }
                        dialog.dismiss();
                    }
                });
                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent(mContext, EditActivity.class);
                        it.putExtra("note",note);
                        mContext.startActivity(it);
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view1);
                dialog.show();
                return false;
            }
        });
    }



    @Override
    public int getItemCount() {
        return mBeanList.size();
    }

    public void refreshData(List<Note> notes){
        this.mBeanList = notes;
        notifyDataSetChanged();
    }

    public void removeData(int pos){
        mBeanList.remove(pos);
        notifyItemRemoved(pos);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_content;
        TextView tv_time;
        ViewGroup rl_item_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_content = itemView.findViewById(R.id.tv_content);
            this.tv_time = itemView.findViewById(R.id.tv_time);
            this.rl_item_container = itemView.findViewById(R.id.rl_item_container);
        }
    }

    class MyGridViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        TextView tv_content;
        TextView tv_time;
        ViewGroup rl_item_container;

        public MyGridViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_content = itemView.findViewById(R.id.tv_content);
            this.tv_time = itemView.findViewById(R.id.tv_time);
            this.rl_item_container = itemView.findViewById(R.id.rl_item_container);
        }
    }


}
