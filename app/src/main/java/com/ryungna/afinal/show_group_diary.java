package com.ryungna.afinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class show_group_diary extends AppCompatActivity {
    private ListView listView;
    List fileList = new ArrayList<>();
    ArrayAdapter adapter;

    Button write_group_diary;
    String myturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group_diary);
        Intent intent = getIntent();
        final String id = UserModel.userUid;
        final String group = intent.getExtras().getString("group_name");

        FirebaseDatabase my_turn=FirebaseDatabase.getInstance();
        final DatabaseReference myturn_Ref=my_turn.getReference("user");

        myturn_Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myturn=dataSnapshot.child(id).child("Groups").child(group).child("turn").getValue(String.class);

                // myturn=Boolean.valueOf(a);
                Toast.makeText(show_group_diary.this, myturn, Toast.LENGTH_SHORT).show();

            }

           @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        write_group_diary = (Button)findViewById(R.id.write_group_diary);

        write_group_diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myturn.equals("true")){
                    Intent goToWrite = new Intent(show_group_diary.this,write_group_diary.class);
                    goToWrite.putExtra("group_name",UserModel.choose_group);
                    startActivity(goToWrite);
               }
                else{
                    Toast.makeText(show_group_diary.this, "아직 차례가 되지 않았어요!!", Toast.LENGTH_SHORT).show();
                }

            }
        });



        listView= (ListView) findViewById(R.id.diary_listview);

        adapter = new ArrayAdapter<String>(this, R.layout.activity_listitem,R.id.tv_item, fileList);
        listView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef2 = database.getReference("user");

        databaseRef2.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                fileList.clear();

                String made_by=null;
                made_by = dataSnapshot.child(id).child("Groups").child(group).child("made_by").getValue(String.class);

                //users의 모든 자식들의 key값과 value 값들을 iterator로 참조합니다.
                Iterator<DataSnapshot> child = dataSnapshot.child(made_by).child("Groups").child(group).child("diary").getChildren().iterator();
                while(child.hasNext())
                {
                    String date=child.next().getKey();
                    String weather=dataSnapshot.child(made_by).child("Groups").child(group).child("diary").child(date).child("weather").getValue(String.class);
                    String subject=dataSnapshot.child(made_by).child("Groups").child(group).child("diary").child(date).child("subject").getValue(String.class);
                    String contents=dataSnapshot.child(made_by).child("Groups").child(group).child("diary").child(date).child("contents").getValue(String.class);
                    String writer=dataSnapshot.child(made_by).child("Groups").child(group).child("diary").child(date).child("writer").getValue(String.class);
                    String diary=date+"\t"+weather+"\n- "+subject+" -\n"+contents+"\n<"+writer+">";
                    fileList.add(diary);

                }

                adapter.notifyDataSetChanged();
            }//onDataChange
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });//databaseRef2.addValueEventListener

    }//onCreate
}
