package com.thesummitdev.ciliapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thesummitdev.ciliapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Distritos extends Fragment {


    public Distritos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_distritos, container, false);
    }

}