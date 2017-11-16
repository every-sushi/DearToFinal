package com.ryungna.afinal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragMe extends Fragment {
    Button add_my_diary_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_frag_me, container, false);

        add_my_diary_btn = (Button) v.findViewById(R.id.add_my_diary_btn);

        add_my_diary_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new (getActivity(),WriteMyDiary.class);
                //startActivity(intent);




            }
        });

        return v;
    }

}
