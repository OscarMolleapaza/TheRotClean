package com.thesummitdev.ciliapp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thesummitdev.ciliapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class creditos extends Fragment {

    TextView txtPolitica;

    public creditos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate( R.layout.fragment_creditos, container, false );


        txtPolitica = v.findViewById(R.id.lblPoliticas);

        if(txtPolitica != null){
            txtPolitica.setMovementMethod(LinkMovementMethod.getInstance());
        }


        return v;
    }

}
