package com.example.myapplication.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.entities.Comment;
import com.example.myapplication.entities.user;
import com.example.myapplication.entities.video;

public class AddCommentDialog extends Dialog {
    private video currentVideo;
    private user loggedInUser;
    private AddCommentListener listener;

    public AddCommentDialog(Context context, video currentVideo, user loggedInUser, AddCommentListener listener) {
        super(context);
        this.currentVideo = currentVideo;
        this.loggedInUser = loggedInUser;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_comment);

        EditText commentEditText = findViewById(R.id.commentEditText);
        Button addCommentButton = findViewById(R.id.addCommentButton);

        addCommentButton.setOnClickListener(v -> {
            String commentText = commentEditText.getText().toString().trim();
            if (!commentText.isEmpty()) {
                // Add the comment to the video
                Comment newComment = new Comment(loggedInUser.getDisplayName(), commentText, loggedInUser.getPhotoUri());
                currentVideo.addComment(newComment);
                listener.onCommentAdded(newComment);
                dismiss();
            }
        });
    }

    public interface AddCommentListener {
        void onCommentAdded(Comment newComment);
    }
}
