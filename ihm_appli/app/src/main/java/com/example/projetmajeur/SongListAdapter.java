package com.example.projetmajeur;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetmajeur.databinding.SongItemBinding;

import java.io.IOException;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {
    private List<Song> songList;
    private MediaPlayer mediaPlayer;

    public SongListAdapter(List<Song> songList,MediaPlayer mediaPlayer){
        this.songList=songList;
        this.mediaPlayer=mediaPlayer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SongItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.song_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListAdapter.ViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.viewModel.setSong(song);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SongViewModel viewModel = new SongViewModel();
        private SongItemBinding binding;

        ViewHolder(SongItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            this.binding.setSongViewModel(viewModel);

            this.binding.playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Song song = songList.get(position);
                        try {
                            mediaPlayer.setDataSource(song.getPath());
                        mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                    }
                });
            this.binding.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Song song = songList.get(position);

                    // Start a new activity to show the song details
                    Intent intent = new Intent(v.getContext(), SongDetailsActivity.class);
                    intent.putExtra("song", song);
                    v.getContext().startActivity(intent);

                }
            });
        }
    }
}
