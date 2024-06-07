package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.entities.Comment;
import com.example.myapplication.entities.video;
import adapter.CommentsAdapter;
import java.util.List;

public class Comments extends DialogFragment {

    private static final String TAG = "CommentsFragment";

    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comment> commentList;
    private video currentVideo;

    public Comments(video currentVideo) {
        this.currentVideo = currentVideo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments, container, false);

        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            Log.d(TAG, "Close button clicked");
            dismiss();
        });

        // Initialize with comments from the video
        commentList = currentVideo.getComments();

        commentsAdapter = new CommentsAdapter(commentList);
        commentsRecyclerView.setAdapter(commentsAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set the dialog to appear in a specific area of the screen
        if (getDialog() != null && getDialog().getWindow() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Adjust this as needed
            getDialog().getWindow().setAttributes(params);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
