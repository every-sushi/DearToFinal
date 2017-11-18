package com.ryungna.afinal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FragMe extends Fragment {
    Button add_my_diary_btn;
    Button btn_logout;

    private ListView listView;
    List fileList = new ArrayList<>();
    ArrayAdapter adapter;
    static boolean calledAlready = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_frag_me, container, false);

        btn_logout = (Button)v.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLogin = new Intent(getActivity(),Login.class);
                Toast.makeText(getActivity(), "로그아웃 합니다",Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                startActivity(goToLogin);

            }
        });

        add_my_diary_btn = (Button) v.findViewById(R.id.add_my_diary_btn);
        add_my_diary_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getActivity(),write_mydiary.class);
                startActivity(intent);

            }
        });


        final String id=UserModel.userUid;


        listView= (ListView) v.findViewById(R.id.diary_listview);

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listitem,R.id.tv_item, fileList);
        listView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef2 = database.getReference("user");

        databaseRef2.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                fileList.clear();

                //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.
                Iterator<DataSnapshot> child = dataSnapshot.child(id).child("MyDiary").getChildren().iterator();

                while(child.hasNext())
                {
                    String key=child.next().getKey();
                    if(!key.equals("count")){
                        String weather=dataSnapshot.child(id).child("MyDiary").child(key).child("weather").getValue(String.class);
                        String subject=dataSnapshot.child(id).child("MyDiary").child(key).child("subject").getValue(String.class);
                        String contents=dataSnapshot.child(id).child("MyDiary").child(key).child("contents").getValue(String.class);
                        String date=dataSnapshot.child(id).child("MyDiary").child(key).child("date").getValue(String.class);
                        String diary=date+"\t"+weather+"\n- "+subject+" -\n"+contents;
                        fileList.add(diary);
                    }


                }

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });



        return v;
    }

}
