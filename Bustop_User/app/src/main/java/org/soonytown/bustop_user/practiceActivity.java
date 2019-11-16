package org.soonytown.bustop_user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class practiceActivity<database> extends AppCompatActivity {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mReferenceReserves = mDatabase.getReference("reserves");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prac);

        mReferenceReserves.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int BusStop_ID = dataSnapshot.child("15qj2334").child("BusStop_ID").getValue(int.class);
                int Bus_ID = dataSnapshot.child("15qj2334").child("Bus_ID").getValue(int.class);
                int user_ID = dataSnapshot.child("15qj2334").child("user_ID").getValue(int.class);
                startToast("BusStop_ID:" + BusStop_ID + "Bus_ID:" + Bus_ID + "user_ID:" + user_ID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void mOnclick(View view) {
        String my = "99버5556"+"정류소코드";
        mReferenceReserves.child(my).child("Bus_ID").setValue(10101555);//예약기능
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    } // 토스트 메시지 하차벨용
}
