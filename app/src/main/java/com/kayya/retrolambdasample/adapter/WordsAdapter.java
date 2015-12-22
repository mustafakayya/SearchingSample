package com.kayya.retrolambdasample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kayya.retrolambdasample.R;
import com.kayya.retrolambdasample.communication.WordsAdapterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mustafakaya on 21/12/15.
 */
public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordHolder> {

    List<String> words;
    WordsAdapterListener listener;

    public WordsAdapter(List<String> words,WordsAdapterListener listener) {
        this.words = words ;
        this.listener = listener ;
    }

    @Override
    public WordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_word, parent, false);

        WordHolder vh = new WordHolder(v);
        return vh;

    }

    public void setWords(List<String> words) {
        this.words = words;

    }

    @Override
    public void onBindViewHolder(WordHolder holder, int position) {
        holder.tv_word.setText(words.get(position));
        holder.tv_word.setOnClickListener( v -> {
            if ( listener != null){
                listener.onWordSelected(words.get(position));
            }
        });
    }


    public void addWord ( String word){
        words.add(word);
    }


    public void clearList () {
        words = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    class WordHolder extends RecyclerView.ViewHolder{

        TextView tv_word;
        public WordHolder(View itemView) {
            super(itemView);
            tv_word= (TextView) itemView.findViewById(R.id.info_text);

        }
    }
}
