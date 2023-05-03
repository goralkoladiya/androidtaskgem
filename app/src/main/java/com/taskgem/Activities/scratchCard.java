package com.taskgem.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anupkumarpanwar.scratchview.ScratchView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.taskgem.MainActivity;
import com.taskgem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class scratchCard extends AppCompatActivity {
    Toolbar toolbar;
    TextView scratchcount,scratchcredit,timer,scratchtext;
    int count=3;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit;
    ScratchView scratchView ;
    int[] valueArray = {20, 2, 5, 8, 11, 14, 16, 18};
    int rewards=0,scratchcoin=0;
    List<Integer> al;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);
        toolbar=findViewById(R.id.toolbar);
        cardView=findViewById(R.id.cardview);
        timer=findViewById(R.id.timer);
        scratchcount=findViewById(R.id.scratchcount);
        scratchtext=findViewById(R.id.scratchtext);
        scratchView =findViewById(R.id.scratch_view);
        scratchcredit=findViewById(R.id.scratchcredit);
        toolbar.setContentInsetsAbsolute(0,toolbar.getContentInsetStartWithNavigation());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(scratchCard.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        al= Arrays.stream(valueArray).boxed().collect(Collectors.toList());;
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        rewards=sharedPreferences.getInt("rewards",0);
        count= sharedPreferences.getInt("scratchcount",3);
        System.out.println(sharedPreferences.getInt("scratchcount",11));
        System.out.println(""+count);
        scratchcredit.setVisibility(View.GONE);
        scratchcount.setText("Scratch Left : "+count);
        Collections.shuffle(al);
        scratchcoin=al.get(0);
        scratchtext.setText("You've won\n"+scratchcoin);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
//                Toast.makeText(getApplicationContext(), "Reveled", Toast.LENGTH_LONG).show();
                if(count!=0)
                {
                    scratchcredit.setVisibility(View.GONE);

//                    play.setEnabled(false);
                    count=count-1;
                    myEdit.putInt("scratchcount",count);
                    myEdit.commit();
                    scratchcount.setText("Scratch Left : "+count);
                    scratchcredit.setVisibility(View.VISIBLE);
                    scratchcredit.setText("+"+scratchcoin+" Coins Credited");
                    rewards=rewards+scratchcoin;
                    myEdit.putInt("rewards",rewards);
                    myEdit.commit();
                    if(count==0)
                    {
                        scratchcredit.setVisibility(View.VISIBLE);
//                        timer.setVisibility(View.VISIBLE);
                        Date currentTime = new Date();
                        String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(currentTime);
                        System.out.println(currentDateandTime);
                        getFutureDate(currentTime,2);
                    }
                    scratchView.setVisibility(View.GONE);
                    RequestQueue queue = Volley.newRequestQueue(scratchCard.this);
                    String url = "http://taskgem.in/taskgem/admin/rewards-insert-api.php";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);
                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        if(jsonObject.getBoolean("status"))
                                        {

                                           Thread.sleep(1000);
                                            Intent intent=new Intent(scratchCard.this,scratchCard.class);
                                            startActivity(intent);
                                            finish();
//                                        play.setEnabled(true);
                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println(error.getMessage());
                                }
                            }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("uid", sharedPreferences.getString("uid","1"));
                            params.put("rewards",""+scratchcoin);
                            params.put("reason", "credited via scratch");
                            return params;
                        }
                    };

// Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
                else {
                    Toast.makeText(scratchCard.this, "You have not enough coin please wait", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                System.out.println("==="+percent);
            }
        });
        startTimer();
    }

    void startTimer()
    {
        if(getTimeDifference()<=0)
        {
            timer.setVisibility(View.GONE);
//            play.setEnabled(true);
//            scratchView.mask();
            myEdit.putString("scratcfuturedate","").commit();
            if(count==0)
            {
                myEdit.putInt("scratchcount",3).commit();
                count=3;
            }
            scratchcount.setText("Scratch Left : "+count);
            timer.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
        }

        new CountDownTimer(getTimeDifference(), 1000) {
            public void onTick(long millisUntilFinished) {

                long minute = millisUntilFinished / (60 * 1000) % 60;
                long second = millisUntilFinished / 1000 % 60;
                timer.setText(String.format("%02d:%02d", minute, second));
                cardView.setVisibility(View.GONE);
            }

            public void onFinish() {
//                scratchView.mask();
//                play.setEnabled(true);
                cardView.setVisibility(View.VISIBLE);
                if(count==0)
                {
                    myEdit.putInt("scratchcount",3).commit();
                    count=3;

                }
                scratchcount.setText("Scratch Left : "+count);
                timer.setVisibility(View.GONE);
            }
        }.start();
    }
    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.MILLISECONDS.convert(format.parse(oldDate).getTime() - format.parse(newDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    int getTimeDifference()
    {
        Date currentTime = new Date();
        String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(currentTime);
        String futuredate=sharedPreferences.getString("scratcfuturedate","0");
        int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"), futuredate,currentDateandTime);
        System.out.println("dateDifference: " + dateDifference);
        return dateDifference;
    }
    public void getFutureDate(Date currentDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
//        cal.add(Calendar.MINUTE, days);
        cal.add(Calendar.MINUTE, 1);
        Date futureDate = cal.getTime();
        String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(futureDate);
        myEdit.putString("scratcfuturedate",currentDateandTime);
        myEdit.commit();
        System.out.println(currentDateandTime);

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(scratchCard.this,MainActivity.class);
        startActivity(intent);
        finish();
//        super.onBackPressed();
    }
}