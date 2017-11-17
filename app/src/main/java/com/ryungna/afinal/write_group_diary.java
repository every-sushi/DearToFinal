package com.ryungna.afinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class write_group_diary extends AppCompatActivity {

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd ");
    EditText editText;
    String weather;
    String writer=UserModel.userEmail;
    String date;
    String turn="true";
    String id = UserModel.userUid;
    String madeby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_group_diary);
        //데이터베이스 저장
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=database.getReference("user");
        Intent intent = getIntent();
        final String group = intent.getExtras().getString("group_name");


        //시간 가져오기
        editText = (EditText)findViewById(R.id.DateText);
        date=getTime();
        editText.setText(date);

        final Button btn = (Button)findViewById(R.id.inputfirebasebt);//확인 버튼값 btn에 넣기
        final EditText et1,et2,et3;
        et1 = (EditText)findViewById(R.id.titleText);//일기제목
        et2 = (EditText)findViewById(R.id.contentbox);//일기내용
        et3 = (EditText)findViewById(R.id.DateText);//일기

        FirebaseDatabase made=FirebaseDatabase.getInstance();
        final DatabaseReference mRef=made.getReference("user");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                madeby=dataSnapshot.child(id).child("Groups").child(group).child("made_by").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //확인 버튼 누르면 다음 화면으로 넘어감
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                    Map<String, Object> map = new HashMap<String, Object>();


                    Log.e("날짜", String.valueOf(mDate));
                    //map.put(String.valueOf(date), new Diary(String.valueOf(et2.getText()),String.valueOf(et1.getText()),weather,writer,turn)); //넣기
                    map.put(String.valueOf(date), new Diary(String.valueOf(et2.getText()), String.valueOf(et1.getText()), weather, writer)); //넣기
                    myRef.child(madeby).child("Groups").child(group).child("diary").updateChildren(map);
                    //작성자가 친구목록 중 한명이라면 false로 바꾸기
                    myRef.child(id).child("Groups").child(group).child("turn").setValue("false");


                    FirebaseDatabase member = FirebaseDatabase.getInstance();
                    final DatabaseReference member_Ref = member.getReference("user");


                    member_Ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> child = dataSnapshot.child(id).child("Groups").child(group).child("member").getChildren().iterator();
                            while (child.hasNext()) {
                                String member = child.next().getKey();
                                member_Ref.child(member).child("Groups").child(group).child("turn").setValue("true");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

        });
        //날씨맑음->파베넘기기
        final Button sunny = (Button)findViewById(R.id.sunnybt);
        sunny.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                weather="맑음";
            }
        });
        final Button cloud = (Button)findViewById(R.id.cloudbt);
        cloud.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                weather="구름";
            }
        });
        final Button fog = (Button)findViewById(R.id.fogbt);
        fog.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                weather="안개";
            }
        });
        final Button rain = (Button)findViewById(R.id.rainbt);
        rain.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                weather="비";
            }
        });
        final Button snow = (Button)findViewById(R.id.snowbt);
        snow.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                weather="눈";
            }
        });
    }
    //시간 설정 메소드
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}
