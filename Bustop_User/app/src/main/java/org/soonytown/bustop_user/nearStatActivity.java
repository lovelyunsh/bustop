package org.soonytown.bustop_user;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class nearStatActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private TextToSpeech tts; //tts 변수
    private String SpeakText; // tts로 나올 멘트
    private GpsTracker gpsTracker;
    private double latitude ;
    private double longitude ;
    private String address; //변환주소
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private int arrayIndex ;
    private int arraySize ;

    private boolean waitDouble = true;
    private static final int DOUBLE_CLICK_TIME = 400; // double click timer

    private Button btnStation1;
    private Button btnStation2;
    private Button btnStation3;

    ArrayList<arriveBus> Alist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_station);
//
//******
        Alist = new ArrayList<>();

        arrayIndex=0;

//        arriveBus arr1 = new arriveBus("1","2","3");
//        Alist.add(arr1);
        Alist.add(new arriveBus("36","5","조선대학교"));
        Alist.add(new arriveBus("54","6","대성여고"));
        Alist.add(new arriveBus("01","12","살레시오고"));
        Alist.add(new arriveBus("02","16","상무지구"));
        Alist.add(new arriveBus("06","20","광천터미널"));
        Alist.add(new arriveBus("17","26","시청"));
        Alist.add(new arriveBus("27","23","구름다리"));


        btnStation1 = (Button)findViewById(R.id.Btn_station1);
        btnStation2 = (Button)findViewById(R.id.Btn_station2);
        btnStation3 = (Button)findViewById(R.id.Btn_station3);

        arraySize = Alist.size();
        arrayIndex=0;
        //*****************************************************************

        if (arraySize>=3){
            btnStation1.setText(""+Alist.get(arrayIndex).getBusNum()+"번"+
                    "\n도착시간 : " + Alist.get(arrayIndex).getArriveTime()+"분"+
                    "\n현재정류소 : " + Alist.get(arrayIndex).getCurrentLoc());
            btnStation2.setText(""+Alist.get(arrayIndex+1).getBusNum()+"번"+
                    "\n도착시간 : " + Alist.get(arrayIndex+1).getArriveTime()+"분"+
                    "\n현재정류소 : " + Alist.get(arrayIndex+1).getCurrentLoc());
            btnStation3.setText(""+Alist.get(arrayIndex+2).getBusNum()+"번"+
                    "\n도착시간 : " + Alist.get(arrayIndex+2).getArriveTime()+"분"+
                    "\n현재정류소 : " + Alist.get(arrayIndex+2).getCurrentLoc());
        }
        else if(arraySize>=2){
            btnStation1.setText(""+Alist.get(arrayIndex).getBusNum()+"번"+
                    "\n도착시간 : " + Alist.get(arrayIndex).getArriveTime()+"분"+
                    "\n현재정류소 : " + Alist.get(arrayIndex).getCurrentLoc());
            btnStation2.setText(""+Alist.get(arrayIndex+1).getBusNum()+"번"+
                    "\n도착시간 : " + Alist.get(arrayIndex+1).getArriveTime()+"분"+
                    "\n현재정류소 : " + Alist.get(arrayIndex+1).getCurrentLoc());
            btnStation3.setText("정보없음"+
                    "\n도착시간 : 정보없음"+
                    "\n현재정류소 : 정보없음" );
        }
        else if(arraySize>=1){
            btnStation1.setText(""+Alist.get(arrayIndex).getBusNum()+"번"+
                    "\n도착시간 : " + Alist.get(arrayIndex).getArriveTime()+"분"+
                    "\n현재정류소 : " + Alist.get(arrayIndex).getCurrentLoc());
            btnStation2.setText("정보없음"+
                    "\n도착시간 : 정보없음"+
                    "\n현재정류소 : 정보없음" );
            btnStation3.setText("정보없음"+
                    "\n도착시간 : 정보없음"+
                    "\n현재정류소 : 정보없음" );
        }
        //******************************************************************
        arraySize = arraySize-3;

        tts = new TextToSpeech(this, this); //oncreate용 tts
        speakOut();                                           //oncreate용 tts

        // GPS Function -> 나중에 class 나눌 예정
        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        } else {

            checkRunTimePermission();
        }




        gpsTracker = new GpsTracker(nearStatActivity.this);

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        address = getCurrentAddress(latitude, longitude);
        //textview_address.setText(address); // *** 텍스트뷰 *** 보류
//                Toast.makeText(MainActivity.this, "현재위치 \n위도 " + latitude + "\n경도 " + longitude, Toast.LENGTH_LONG).show();
        Toast.makeText(nearStatActivity.this, "현재위치 : " + address, Toast.LENGTH_LONG).show();



    }


    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {
                //위치 값을 가져올 수 있음

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(nearStatActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(nearStatActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(nearStatActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(nearStatActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(nearStatActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(nearStatActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(nearStatActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(nearStatActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    public String getCurrentAddress(double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString() + "\n";

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(nearStatActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }




    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
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
        String text = "화면에 3개 정류소가 나와있습니다 탑승 정류소를 선택해주세요";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null, null);
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

    private void oneTouchDoubleTouchintent(final String Text1,final String Text2, Class c, String[] a){
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
            Intent intent2 = new Intent(getApplicationContext(), c);
            intent2.putExtra("array",a);
            startActivity(intent2);
        }
    }

    public void mOnclick(View v) {
        switch(v.getId()){
            case R.id.Btn_station1:
//                Intent intent2 = new Intent(this, reserveOxActivity.class);
                Toast.makeText(nearStatActivity.this, ""+Alist.get(arrayIndex).getBusNum()+"번", Toast.LENGTH_LONG).show();
//                oneTouchDoubleTouch("한번터치","두번터치",reserveOxActivity.class);
                String[] a = new String[3];
                a[0]= Alist.get(arrayIndex).getCurrentLoc();
                a[1]= Alist.get(arrayIndex).getArriveTime();
                a[2]= Alist.get(arrayIndex).getBusNum();
                oneTouchDoubleTouchintent("한번터치","두번터치",reserveOxActivity.class,a);
                break;


            case R.id.Btn_station2:
                Toast.makeText(nearStatActivity.this, "lataaa : ", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this, practiceActivity.class);
                startActivity(intent1);
//                Toast.makeText(nearStatActivity.this, "lat : "+latitude+"long : "+longitude, Toast.LENGTH_LONG).show();
                break;

            case R.id.Btn_station_next:
                Toast.makeText(nearStatActivity.this, "TEst ", Toast.LENGTH_LONG).show();
//tts
                if(arraySize<=0) { //3개 이하 버스 정류소 정보가있을때
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
                                        if(Alist.size()<=3){ //애초에 버스정보 3개 보다 작을때
                                            String SpeakText1 = "마지막 페이지"; //1번 터치시
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ttsGreater21(SpeakText1);
                                            } else {
                                                ttsUnder20(SpeakText1);
                                            }
                                        }
                                        else{ //다음페이지 마구 누르다가 늘어낫을때
                                            String SpeakText1 = "다음 페이지"; //1번 터치시
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ttsGreater21(SpeakText1);
                                            } else {
                                                ttsUnder20(SpeakText1);
                                            }
//                                            arraySize = Alist.size();
//                                            arrayIndex=0;
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
                        if(Alist.size()<=3){ //애초에 버스정보 3개 보다 작을때
                            String SpeakText1 = "마지막 페이지"; //2번 터치시
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ttsGreater21(SpeakText1);
                            } else {
                                ttsUnder20(SpeakText1);
                            }
                        }
                        else{ //다음페이지 마구 누르다가 늘어낫을때
                            String SpeakText1 = "다음페이지 선택완료"; //2번 터치시
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ttsGreater21(SpeakText1);
                            } else {
                                ttsUnder20(SpeakText1);
                            }
                                            arraySize = Alist.size();
                                            arrayIndex=0;
                            if (arraySize>=3){
                                btnStation1.setText(""+Alist.get(arrayIndex).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex).getCurrentLoc());
                                btnStation2.setText(""+Alist.get(arrayIndex+1).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex+1).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex+1).getCurrentLoc());
                                btnStation3.setText(""+Alist.get(arrayIndex+2).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex+2).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex+2).getCurrentLoc());
                            }
                            else if(arraySize>=2){
                                btnStation1.setText(""+Alist.get(arrayIndex).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex).getCurrentLoc());
                                btnStation2.setText(""+Alist.get(arrayIndex+1).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex+1).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex+1).getCurrentLoc());
                                btnStation3.setText("정보없음"+
                                        "\n도착시간 : 정보없음"+
                                        "\n현재정류소 : 정보없음" );
                            }
                            else if(arraySize>=1){
                                btnStation1.setText(""+Alist.get(arrayIndex).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex).getCurrentLoc());
                                btnStation2.setText("정보없음"+
                                        "\n도착시간 : 정보없음"+
                                        "\n현재정류소 : 정보없음" );
                                btnStation3.setText("정보없음"+
                                        "\n도착시간 : 정보없음"+
                                        "\n현재정류소 : 정보없음" );
                            }
                            arraySize = arraySize-3;

                        }

                    }
                }



                //*
                else{ //3개 이상 버스 정류소 정보가있을때
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
                                            String SpeakText1 = "다음페이지"; //1번 터치시
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
                            String SpeakText2 = "다음페이지 선택완료";
                            arrayIndex=arrayIndex+3;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ttsGreater21(SpeakText2);
                            } else {
                                ttsUnder20(SpeakText2);
                            }

                            if (arraySize>=3){
                                btnStation1.setText(""+Alist.get(arrayIndex).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex).getCurrentLoc());
                                btnStation2.setText(""+Alist.get(arrayIndex+1).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex+1).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex+1).getCurrentLoc());
                                btnStation3.setText(""+Alist.get(arrayIndex+2).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex+2).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex+2).getCurrentLoc());
                            }
                            else if(arraySize>=2){
                                btnStation1.setText(""+Alist.get(arrayIndex).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex).getCurrentLoc());
                                btnStation2.setText(""+Alist.get(arrayIndex+1).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex+1).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex+1).getCurrentLoc());
                                btnStation3.setText("정보없음"+
                                        "\n도착시간 : 정보없음"+
                                        "\n현재정류소 : 정보없음" );
                            }
                            else if(arraySize>=1){
                                btnStation1.setText(""+Alist.get(arrayIndex).getBusNum()+"번"+
                                        "\n도착시간 : " + Alist.get(arrayIndex).getArriveTime()+"분"+
                                        "\n현재정류소 : " + Alist.get(arrayIndex).getCurrentLoc());
                                btnStation2.setText("정보없음"+
                                        "\n도착시간 : 정보없음"+
                                        "\n현재정류소 : 정보없음" );
                                btnStation3.setText("정보없음"+
                                        "\n도착시간 : 정보없음"+
                                        "\n현재정류소 : 정보없음" );
                            }
                            arraySize = arraySize-3;
                        }
                }
                break;
        }
    }
}
