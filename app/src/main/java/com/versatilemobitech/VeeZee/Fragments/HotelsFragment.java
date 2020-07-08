package com.versatilemobitech.VeeZee.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.versatilemobitech.VeeZee.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelsFragment extends Fragment {
    private View view;

    public HotelsFragment newInstance() {
        HotelsFragment fragment = new HotelsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hotels, container, false);

        initComponents();
        return view;

    }

    private void initComponents() {

    }

}
