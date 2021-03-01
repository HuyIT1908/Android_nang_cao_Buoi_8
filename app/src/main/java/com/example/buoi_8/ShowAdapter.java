package com.example.buoi_8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowAdapter extends BaseAdapter {
    Context context;
    List<MusicList> musicLists = new ArrayList<>();

    public ShowAdapter(Context context, List<MusicList> musicLists) {
        this.context = context;
        this.musicLists = musicLists;
    }

    @Override
    public int getCount() {
        return musicLists.size();
    }

    @Override
    public Object getItem(int position) {
        return musicLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Show_Layout show_layout;
        if (convertView == null){
            show_layout = new Show_Layout();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.show_lv , null);

            show_layout.title = convertView.findViewById(R.id.textView_title);
            show_layout.image = convertView.findViewById(R.id.imageView_img_music);
            convertView.setTag(show_layout);
        }else {
            show_layout = (Show_Layout) convertView.getTag();
        }
        show_layout.title.setText(musicLists.get(position).getTitle());
        if (position % 2 == 0){
            show_layout.image.setImageResource(R.drawable.sotify);
        }else {
            show_layout.image.setImageResource(R.drawable.iconfinder___note_1904664);
        }
        return convertView;
    }

    public static class Show_Layout{
        TextView title;
        ImageView image;
    }
}
