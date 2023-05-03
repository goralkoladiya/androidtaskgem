package com.taskgem.Activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.CountDownTimer;
import android.view.animation.DecelerateInterpolator;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.taskgem.MainActivity;
import com.taskgem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class spin extends AppCompatActivity {
    Toolbar toolbar;
    float[] angleArray = {22.5f, 67.5f, 112.5f, 157.5f, 202.5f, 247.5f, 292.5f, 337.5f};
    int[] valueArray = {20, 2, 5, 8, 11, 14, 16, 18};
    ImageView spinView,play;
    TextView spincount,credit,timer;
    int count=3;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit;
    int rewards=0;
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);
        toolbar=findViewById(R.id.toolbar);
        timer=findViewById(R.id.timer);
        spincount=findViewById(R.id.spincount);
        credit=findViewById(R.id.credit);
        toolbar.setContentInsetsAbsolute(0,toolbar.getContentInsetStartWithNavigation());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(spin.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        rewards=sharedPreferences.getInt("rewards",0);
//        myEdit.putInt("count",3).commit();
        count= sharedPreferences.getInt("count",3);
        System.out.println(sharedPreferences.getInt("count",11));
        System.out.println(""+count);
        spinView = findViewById(R.id.ivSpinner);
        play = findViewById(R.id.play);
        spinView.setRotation(angleArray[new Random().nextInt(angleArray.length)]);
        credit.setVisibility(View.GONE);
        spincount.setText("Spin Left : "+count);
        startTimer();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        System.out.println(sharedPreferences.getString("futuredate","0"));
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count!=0)
                {
                    credit.setVisibility(View.GONE);
                    play.setEnabled(false);
                    int finalIndex = new Random().nextInt(valueArray.length);
                    float finalRotation = (360 * 20) + angleArray[finalIndex];
                    ObjectAnimator animSpin = ObjectAnimator.ofFloat(spinView, View.ROTATION, spinView.getRotation(), finalRotation);
                    animSpin.setDuration(4000L);
                    animSpin.setInterpolator(new DecelerateInterpolator());
                    animSpin.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            spinView.setRotation(angleArray[finalIndex]);
                            count=count-1;
                            myEdit.putInt("count",count);
                            myEdit.commit();
                            spincount.setText("Spin Left : "+count);
                            credit.setVisibility(View.VISIBLE);
                            credit.setText("+"+valueArray[finalIndex]+" Coins Credited");
                            rewards=rewards+valueArray[finalIndex];
                            myEdit.putInt("rewards",rewards);
                            myEdit.commit();
                            Toast.makeText(spin.this, "" + valueArray[finalIndex], Toast.LENGTH_SHORT).show();
                            //remove
//                            play.setEnabled(true);
                            if(count==0)
                            {
                                timer.setVisibility(View.VISIBLE);
                                Date currentTime = new Date();
                                String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(currentTime);
                                System.out.println(currentDateandTime);
                                getFutureDate(currentTime,2);
                                startTimer();
                            }
                            RequestQueue queue = Volley.newRequestQueue(spin.this);
                            String url = "http://taskgem.in/taskgem/admin/rewards-insert-api.php";

// Request a string response from the provided URL.
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            System.out.println(response);
                                            try {
                                                JSONObject jsonObject=new JSONObject(response);
                                                if(jsonObject.getBoolean("status"))
                                                {
                                                    play.setEnabled(true);
                                                }
                                            } catch (JSONException e) {
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
                                    params.put("rewards",""+valueArray[finalIndex] );
                                    params.put("reason", "credited via spin");
                                    return params;
                                }
                            };

// Add the request to the RequestQueue.
                            queue.add(stringRequest);
                        }
                    });
                    animSpin.start();
                }
                else {
                    Toast.makeText(spin.this, "You have not enough coin please wait", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    void startTimer()
    {
        if(getTimeDifference()<=0)
        {
            timer.setVisibility(View.GONE);
            play.setEnabled(true);
            myEdit.putString("futuredate","").commit();
            if(count==0)
            {
                myEdit.putInt("count",3).commit();
                count=3;
            }
            spincount.setText("Spin Left : "+count);
            timer.setVisibility(View.GONE);
        }

        new CountDownTimer(getTimeDifference(), 1000) {
            public void onTick(long millisUntilFinished) {

                long minute = millisUntilFinished / (60 * 1000) % 60;
                long second = millisUntilFinished / 1000 % 60;
                timer.setText(String.format("%02d:%02d", minute, second));
            }

            public void onFinish() {
                play.setEnabled(true);
               if(count==0)
               {
                   myEdit.putInt("count",3).commit();
                   count=3;

               }
                spincount.setText("Spin Left : "+count);
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
        String futuredate=sharedPreferences.getString("futuredate","0");
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
        myEdit.putString("futuredate",currentDateandTime);
        myEdit.commit();
        System.out.println(currentDateandTime);

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(spin.this,MainActivity.class);
        startActivity(intent);
        finish();
//        super.onBackPressed();
    }
}