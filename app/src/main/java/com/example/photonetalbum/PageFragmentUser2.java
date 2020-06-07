package com.example.photonetalbum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public class PageFragmentUser2 extends Fragment {

    private ViewGroup root;
    private open_dir a = new open_dir();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_page, container, false);
        a.onCreate(root.getContext(), root, 2);
        return root;
    }
}