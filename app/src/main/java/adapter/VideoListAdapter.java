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

    public VideoListAdapter(List<video> videoList, Context context, user loggedInUser) {
        this.videoList = videoList;
        this.filteredVideoList = new ArrayList<>(videoList);
        this.context = context;
        this.loggedInUser = loggedInUser;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        ImageView userPhotoImageView;
        TextView titleTextView;
        TextView channelTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            userPhotoImageView = itemView.findViewById(R.id.userPhotoImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            channelTextView = itemView.findViewById(R.id.channelTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        video video = filteredVideoList.get(position);

        holder.titleTextView.setText(video.getTitle());
        holder.channelTextView.setText(video.getUser().getEmail());

        // Load the video thumbnail
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
                holder.thumbnailImageView.setImageResource(R.drawable.dog1);
            }
        } else {
            holder.thumbnailImageView.setImageResource(video.getThumbnailResId());
        }

        // Load the user photo
        if (video.getUser() != null && video.getUser().getPhotoUri() != null && !video.getUser().getPhotoUri().isEmpty()) {
            Uri uri = Uri.parse(video.getUser().getPhotoUri());
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                holder.userPhotoImageView.setImageBitmap(bitmap);
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                holder.userPhotoImageView.setImageResource(R.drawable.person);
            }
        } else {
            holder.userPhotoImageView.setImageResource(R.drawable.person);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, videowatching.class);
            intent.putExtra("title", video.getTitle());
            intent.putExtra("user", loggedInUser);
            context.startActivity(intent);
        });

        boolean isNightMode = ThemeUtil.isNightMode(context);
        ThemeUtil.changeTextColor(holder.itemView, isNightMode);
        ThemeUtil.changeBackgroundColor(holder.itemView, isNightMode);
    }

    @Override
    public int getItemCount() {
        return filteredVideoList.size();
    }

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

    public void refreshTheme() {
        notifyDataSetChanged();
    }
}
