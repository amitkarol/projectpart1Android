package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment; // Import the Fragment class from AndroidX

import com.example.myapplication.R;

public class Comments extends Fragment {

    public static Comments newInstance() {
        return new Comments(); // Return an instance of Comments, not android.app.Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.comments, container, false);
    }
}
