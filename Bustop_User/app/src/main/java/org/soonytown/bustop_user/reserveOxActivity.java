package org.soonytown.bustop_user;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class reserveOxActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mReferenceReserves = mDatabase.getReference("bus");

    private TextToSpeech tts; //tts 변수

    private String led ; //firebase용 변수
    private String reserve; //firebase용 변수
    private String user; //firebase용 변수

    private boolean waitDouble = true;
    private static final int DOUBLE_CLICK_TIME = 400; // double click timer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_ox);
//
//        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status != TextToSpeech.ERROR) {
//                    tts.setLanguage(Locale.KOREAN);
//                }
//            }
//        });


//        mReferenceReserves.addValueEventListener(new ValueEventListener() {
        mReferenceReserves.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                startToast("1");
                led = dataSnapshot.child("22211").child("led").getValue(String.class);
                reserve= dataSnapshot.child("22211").child("reserve").getValue(String.class);
                user = dataSnapshot.child("22211").child("user").getValue(String.class);
                startToast("led:" + led + "reserve:" + reserve+ "user:" + user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        tts = new TextToSpeech(this, this); //oncreate용 tts
        speakOut();                                           //oncreate용 tts


        Intent intent = getIntent();

        String array[] = intent.getExtras().getStringArray("array"); /*배열*/
        String add_array="";
        for(int i=0;i<array.length;i++){
            add_array+=array[i]+",";
        }

//        Toast.makeText(reserveOxActivity.this, ""+add_array, Toast.LENGTH_LONG).show();




    }
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    } // 토스트 메시지 하차벨용


    @Override //oncreate용 tts
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }




    @Override //oncreate용 tts
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.KOREA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "지원되지 않는 언어입니다.");
            } else {
//                btnSpeak.setEnabled(true);
                speakOut();
            }
        } else {
            Log.e("TTS", "출력 실패!");
        }
    }

    private void speakOut(){ //oncreate용 tts
        String text = "머";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null, null);
    }


    public void mOnclick(View v) {
        switch(v.getId()) {

            case R.id.Btn_reserve_yes:

//                mReferenceReserves.addListenerForSingleValueEvent(new V);

//                mReferenceReserves.addValueEventListener(new ValueEventListener() {
//
//
////                    @Override
////                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                        startToast("3");
////                        led = dataSnapshot.child("22211").child("led").getValue(String.class);
////                        reserve= dataSnapshot.child("22211").child("reserve").getValue(String.class);
////                        user = dataSnapshot.child("22211").child("user").getValue(String.class);
////                        startToast("led:" + led + "\nreserve:" + reserve+ "\nuser:" + user);
////                }
////
////                @Override
////                public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                }
////
////            });
                startToast("2");
                reserve = reserve + ",010101";
                mReferenceReserves.child("22211").child("reserve").setValue(reserve);//예약기능

                break;
            case R.id.Btn_reserve_no: //예약 취소

                oneTouchDoubleTouch("예약 취소","예약 취소되었습니다.",nearStatActivity.class);
                break;
        }
    }



    //tts용 함수
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    //tts용 함수
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }


    private void oneTouchDoubleTouch(final String Text1,final String Text2, Class c){
        final String SpeakText1 = Text1;//한번클릭시 나오는 음성
        final String SpeakText2 = Text2;//더블클릭시 나오는 음성
        //Class c는 엑티비티 이동
        if (waitDouble == true) {
            waitDouble = false;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(DOUBLE_CLICK_TIME);
                        if (waitDouble == false) {
                            waitDouble = true;
                            //single click event
                            //SpeakText1 = "조선대 버스정류소";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ttsGreater21(SpeakText1);
                            } else {
                                ttsUnder20(SpeakText1);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        } else {
            //double click event
            waitDouble = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ttsGreater21(SpeakText2);
            } else {
                ttsUnder20(SpeakText2);
            }
            Intent intent2 = new Intent(this, c);
            startActivity(intent2);
        }
    }
}
