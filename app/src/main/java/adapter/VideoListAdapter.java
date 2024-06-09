package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ThemeUtil;
import com.example.myapplication.entities.video;
import com.example.myapplication.entities.user;
import com.example.myapplication.videowatching;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private List<video> videoList;
    private List<video> filteredVideoList;
    private Context context;
    private user loggedInUser;

    // Constructor with Context parameter
    public VideoListAdapter(List<video> videoList, Context context, user loggedInUser) {
        this.videoList = videoList;
        this.filteredVideoList = new ArrayList<>(videoList);
        this.context = context;
        this.loggedInUser = loggedInUser;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView channelTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            channelTextView = itemView.findViewById(R.id.channelTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and return a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the views in each item
        video video = filteredVideoList.get(position);

        holder.titleTextView.setText(video.getTitle());
        holder.channelTextView.setText(video.getChannelName());

        // Check if the thumbnail URL is set and load it, otherwise use the resource ID
        if (video.getThumbnailUrl() != null && !video.getThumbnailUrl().isEmpty()) {
            Uri uri = Uri.parse(video.getThumbnailUrl());
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                holder.thumbnailImageView.setImageBitmap(bitmap);
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Set a default image or handle the error case
                holder.thumbnailImageView.setImageResource(R.drawable.dog1);
            }
        } else {
            holder.thumbnailImageView.setImageResource(video.getThumbnailResId());
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, videowatching.class);
            intent.putExtra("title", video.getTitle());
            intent.putExtra("user", loggedInUser);
            context.startActivity(intent);
        });

        // Apply the theme to each item view
        boolean isNightMode = ThemeUtil.isNightMode(context);
        ThemeUtil.changeTextColor(holder.itemView, isNightMode);
    }

    @Override
    public int getItemCount() {
        // Return the size of your dataset
        return filteredVideoList.size();
    }

    // Method to filter the list
    public void filter(String query) {
        filteredVideoList.clear();
        if (query.isEmpty()) {
            filteredVideoList.addAll(videoList);
        } else {
            for (video video : videoList) {
                if (video.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredVideoList.add(video);
                }
            }
        }
        notifyDataSetChanged();
    }
}