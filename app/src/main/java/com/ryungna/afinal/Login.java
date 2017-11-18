package com.ryungna.afinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static com.ryungna.afinal.UserModel.user;
import static com.ryungna.afinal.UserModel.userEmail;
import static com.ryungna.afinal.UserModel.userUid;


public class Login extends AppCompatActivity {

    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Button join;
    Button login;
    EditText email;
    EditText passwd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final String TAG ="EHoo";

        join = (Button)findViewById(R.id.join);
        login = (Button)findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.passwd);
        mAuth = FirebaseAuth.getInstance(); //firebaseAuh 개체의 공유 인스턴스 가져오기

        //로그인되어있는지 아닌지 확인하는거임
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                 UserModel.user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    // User is signed in
                    userEmail = user.getEmail(); //userEmail에 email들어가있음
                    userUid = user.getUid(); //userUid에 uid들어가있음

                    Toast.makeText(Login.this,user.getEmail(), Toast.LENGTH_SHORT);
                    Intent intent = new Intent(Login.this, MainFrag.class);
                    finish(); //로그인이 되면 로그인액티비티가 사라지는거임
                    startActivity(intent);


                } else {
                    // User is signed out


                }
                // ...
            }
        };//endof mAuthListener


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(email.getText().toString(),passwd.getText().toString());


            }
        });//end of join.setOnClickListener



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signInWithEmailAndPassword(email.getText().toString(), passwd.getText().toString())
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) { //로그인실패

                                    Toast.makeText(Login.this, "로그인 실패",
                                            Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Login.this, "로그인 성공",
                                            Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(Login.this,"uid:"+ UserModel.user.getEmail().toString()+"=="+userEmail,
//                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

            }
        });//end of login.setOnClickListener


    }//end of onCreate


    public void createUser(String email, String passwd){

        mAuth.createUserWithEmailAndPassword(email,passwd)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseDatabase.getInstance().getReference().child("user").child(userUid).child("email").setValue(userEmail);
                        FirebaseDatabase.getInstance().getReference().child("user").child(userUid).child("MyDiary").child("count").setValue("0");

                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "회원가입 실패",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else{//성공적으로 가입이 되면
                            Toast.makeText(Login.this, user+"++"+UserModel.userUid,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }




    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



}//end of class
