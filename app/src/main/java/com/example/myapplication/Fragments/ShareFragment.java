package com.example.myapplication.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.entities.video;

public class ShareFragment extends DialogFragment {

    private video currentVideo;
    private String videoLink;

    public ShareFragment(video currentVideo) {
        this.currentVideo = currentVideo;
        this.videoLink = "myapp://video?id=" + currentVideo.getId();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        ImageButton shareInstagram = view.findViewById(R.id.shareInstagram);
        ImageButton shareWhatsApp = view.findViewById(R.id.shareWhatsApp);
        ImageButton shareEmail = view.findViewById(R.id.shareEmail);
        TextView shareLinkText = view.findViewById(R.id.shareLinkText);
        Button copyLinkButton = view.findViewById(R.id.copyLinkButton);

        shareLinkText.setText(videoLink);

        shareInstagram.setOnClickListener(v -> shareOnInstagram());
        shareWhatsApp.setOnClickListener(v -> shareOnWhatsApp());
        shareEmail.setOnClickListener(v -> shareOnEmail());
        copyLinkButton.setOnClickListener(v -> copyLinkToClipboard());

        return view;
    }

    private void shareOnInstagram() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, videoLink);
        intent.setPackage("com.instagram.android");

        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Instagram is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareOnWhatsApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, videoLink);
        intent.setPackage("com.whatsapp");

        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareOnEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this video!");
        intent.putExtra(Intent.EXTRA_TEXT, videoLink);

        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Email client is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyLinkToClipboard() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Video Link", videoLink);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Link copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}
