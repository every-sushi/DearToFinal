package com.ryungna.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FragGroup extends Fragment {
    Button create_group_btn;
    private ListView listView;
    List fileList = new ArrayList<>();
    ArrayAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_frag_group, container, false);

        create_group_btn = (Button)v.findViewById(R.id.create_group_btn);

        create_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),addGroup.class);
                startActivity(intent);

            }
        });


        final String id = UserModel.userUid;

        listView= v.findViewById(R.id.group_listview);

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listitem,R.id.tv_item, fileList);
        listView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef2 = database.getReference("user");

        databaseRef2.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                fileList.clear();

                Iterator<DataSnapshot> child = dataSnapshot.child(id).child("Groups").getChildren().iterator();
                //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.

                while(child.hasNext())
                {
                    String str=child.next().getKey();
                    fileList.add(str);

                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });
        listView.setOnItemClickListener(new ItemList());

        return v;
    }

    class ItemList implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ViewGroup vg = (ViewGroup)view;
            TextView tv = vg.findViewById(R.id.tv_item);

            //UserModel u=new UserModel();
            UserModel.choose_group=tv.getText().toString();

            Toast.makeText(getActivity(), UserModel.choose_group, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getActivity(),show_group_diary.class);
            intent.putExtra("group_name",UserModel.choose_group);
            startActivity(intent);
        }
    }//ItemList


}
