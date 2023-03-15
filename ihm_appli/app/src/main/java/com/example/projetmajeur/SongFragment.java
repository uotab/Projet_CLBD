package com.example.projetmajeur;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projetmajeur.database.DBDao;
import com.example.projetmajeur.databinding.FragmentSongBinding;
import com.example.projetmajeur.databinding.FragmentSongBindingImpl;

import java.util.Collections;
import java.util.List;



public class SongFragment extends Fragment {
    private DBDao db;
    private SongListAdapter adapter;
    private FragmentSongBinding binding;
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_song,container,false);

        binding.songList.setLayoutManager(new LinearLayoutManager(
                binding.getRoot().getContext()));

        db = DBDao.getInstance(this.getActivity().getApplicationContext());

        mediaPlayer = new MediaPlayer();



        adapter = new SongListAdapter(getRecentSongs(),mediaPlayer);
        binding.songList.setAdapter(adapter);



        return binding.getRoot();

    }




    private List<Song> getRecentSongs() {
        // get the list of recent songs from the database
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        List<Song> list = db.getSongForUser(sharedPref.getInt("id", 0));
        Collections.reverse(list);
        return list;
    }

    public void onResume() {


        super.onResume();

        adapter = new SongListAdapter(getRecentSongs(),mediaPlayer);
        binding.songList.setAdapter(adapter);
        binding.getRoot();
    }
    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

}
