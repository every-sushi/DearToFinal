package com.ryungna.afinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class addGroup extends AppCompatActivity {

    ArrayList<String> selectedItems=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        final ListView chl = (ListView) findViewById(R.id.checkable_list);
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        final String id=UserModel.userUid;

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef2 = database2.getReference("user");

        databaseRef2.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> data = new ArrayList<>();
                Iterator<DataSnapshot> child = dataSnapshot.child(id).child("Friends").getChildren().iterator();

                while (child.hasNext()) {
                    String str = child.next().getKey();
                    String str1 = dataSnapshot.child(id).child("Friends").child(str).getValue(String.class);
                    data.add(str1);
                }

                String itemlist[] = new String[data.size()];
                itemlist = data.toArray(itemlist);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listgroup, R.id.txt_lan, itemlist);
                chl.setAdapter(adapter);
                // String itemlist[]={"A","b","c"};

                chl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = ((TextView) view).getText().toString();
                        if (selectedItems.contains((selectedItem))) {
                            selectedItems.remove(selectedItem);
                        } else selectedItems.add(selectedItem);
                    }

                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });
    }


    public void PutItem(View view) {
//        String items="";
//        for(String item:selectedItems){
//            items+="-"+item+"\n";
//        }
//        Toast.makeText(this,"메일\n"+items,Toast.LENGTH_LONG).show();
        EditText g=(EditText)findViewById(R.id.g_name);
        final String group_name=g.getText().toString();

        final String id = UserModel.userUid;
        final String email= UserModel.userEmail;

        FirebaseDatabase makegroup = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = makegroup.getReference("user");

        //Toast.makeText(this, group_name, Toast.LENGTH_LONG).show();

        mRef.addValueEventListener(new ValueEventListener() {
            int flag=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> child1 = dataSnapshot.getChildren().iterator();
                Iterator<DataSnapshot> child2 = dataSnapshot.child(id).child("Groups").getChildren().iterator();
                Iterator<DataSnapshot> child3 = dataSnapshot.getChildren().iterator();
                if(flag!=1){
                    while(child2.hasNext()){
                        String str2=child2.next().getKey();
                        if(str2.equals(group_name)){
                            Toast.makeText(getApplicationContext(), "그룹이 생성되었습니다.", Toast.LENGTH_LONG).show();
                            flag=1;
                        }
                        if(flag==1)break;
                    }
                }


                if(flag!=1){
                    while(child.hasNext())
                    {
                        //Toast.makeText(addGroup.this,"2",Toast.LENGTH_SHORT).show();
                        String str=child.next().getKey();//모든 id
                        String str1=dataSnapshot.child(str).child("email").getValue(String.class); //모든 이메일
                        mRef.child(id).child("Groups").child(group_name).child("made_by").setValue(id);
                        for(String item:selectedItems){//선택한 item
                            if(item.equals(str1)){ //item과 email이 같으면
                                mRef.child(id).child("Groups").child(group_name).child("member").child(str).setValue(item); //내 아이디의 멤버에 아이디와 이메일 추가
                                mRef.child(id).child("Groups").child(group_name).child("turn").setValue("true");

                            }
                        }
                    }
                }

                if(flag!=1) {
                    while (child1.hasNext()) {

                        String str = child1.next().getKey();//모든 id
                        String str1 = dataSnapshot.child(str).child("email").getValue(String.class); //모든 이메일
                        for (String item : selectedItems) {

                            if (item.equals(str1)) {

                                while (child3.hasNext()) {


                                    String str2 = child3.next().getKey();//모든 id
                                    String str3 = dataSnapshot.child(str2).child("email").getValue(String.class); //모든 이메일

                                    for (String item2 : selectedItems) {

                                        if (!str.equals(str2) && item2.equals(str3)) {

                                            mRef.child(str).child("Groups").child(group_name).child("member").child(str2).setValue(str3);
                                            mRef.child(str).child("Groups").child(group_name).child("member").child(id).setValue(email);
                                            mRef.child(str).child("Groups").child(group_name).child("made_by").setValue(id);
                                            mRef.child(str).child("Groups").child(group_name).child("turn").setValue("true");
                                        }
                                    }
                                }

                                child3 = dataSnapshot.getChildren().iterator();
                            }
                        }//for
                        flag=1;
                    }//while
                }//if

            } //onDataChange

            //            @Override
            public void onCancelled(DatabaseError databaseError) {   }
        });//mRef.addValueEventListener
        finish();

    }//putItem

} // end of main