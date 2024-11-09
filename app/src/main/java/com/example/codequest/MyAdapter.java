package com.example.codequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<QuizResult> list;

    public MyAdapter(Context context,ArrayList<QuizResult> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.recycler_view,parent,false);
        return new MyAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        QuizResult obj = list.get(position);
        holder.language.setText(obj.getLanguage());
        holder.difficulty.setText(obj.getDifficulty());
        holder.score.setText(String.valueOf(obj.getScore()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView language,difficulty,score;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            language=itemView.findViewById(R.id.language_txt);
            difficulty=itemView.findViewById(R.id.mode_txt);
            score=itemView.findViewById(R.id.score_txt);

        }
    }
}
