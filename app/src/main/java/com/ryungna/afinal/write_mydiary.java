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

public class write_mydiary extends AppCompatActivity {
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd ");
    EditText editText;
    String weather;
    String writer=UserModel.userEmail;
    String date;
    String turn="true";
    String id =UserModel.userUid;
    String madeby;
    int flag=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_mydiary);
        //데이터베이스 저장
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference myRef=database.getReference("user");


        //시간 가져오기
        editText = (EditText)findViewById(R.id.DateText);
        date=getTime();
        editText.setText(date);

        final Button btn = (Button)findViewById(R.id.save_my_diary);//확인 버튼값 btn에 넣기
        final Button btn2 = (Button)findViewById(R.id.cancel_my_diary);//확인 버튼값 btn에 넣기

        final EditText et1,et2,et3;
        et1 = (EditText)findViewById(R.id.titleText);//일기제목
        et2 = (EditText)findViewById(R.id.contentbox);//일기내용
        et3 = (EditText)findViewById(R.id.DateText);//일기
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToList = new Intent(write_mydiary.this,FragMe.class);
                finish();

            }
        });

        //확인 버튼 누르면 다음 화면으로 넘어감
        btn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                myRef.addValueEventListener(new ValueEventListener() {

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(flag==0){
                            String a=dataSnapshot.child(id).child("MyDiary").child("count").getValue(String.class);

                            int b= Integer.parseInt(a); //여기서 오류난다 NumberFormatException
                            ++b;
                            a=Integer.toString(b);

                            myRef.child(id).child("MyDiary").child(a).child("date").setValue(date);
                            myRef.child(id).child("MyDiary").child(a).child("weather").setValue(weather);
                            myRef.child(id).child("MyDiary").child(a).child("subject").setValue(et1.getText().toString());
                            myRef.child(id).child("MyDiary").child(a).child("contents").setValue(et2.getText().toString());
                            myRef.child(id).child("MyDiary").child("count").setValue(a);

                            flag=1;

                        }
                        finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG: ", "Failed to read value", databaseError.toException());
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