package com.taskgem.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.taskgem.MainActivity;
import com.taskgem.Questions;
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

public class MathQuiz extends AppCompatActivity {
    Toolbar toolbar;
    TextView ques, counter, timer;
    EditText ans;
    Button submit;
    List<String> quelist;
    List<String> anslist;
    int count = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    int random = 0;
    RewardedAd rewardedAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz);

        ques = findViewById(R.id.ques);
        timer = findViewById(R.id.timer);
        counter = findViewById(R.id.count);
        ans = findViewById(R.id.ans);
        submit = findViewById(R.id.submit);
        quelist = new ArrayList<String>(Arrays.asList(Questions.arr));
        anslist = new ArrayList<String>(Arrays.asList(Questions.result));
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        count = sharedPreferences.getInt("quecount", 8);
        counter.setText("" + count);
        toolbar = findViewById(R.id.toolbar);
        random = new Random().nextInt(Questions.arr.length - 0);
        ques.setText(quelist.get(random));
        toolbar.setContentInsetsAbsolute(0, toolbar.getContentInsetStartWithNavigation());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MathQuiz.this, MainActivity.class);
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
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d("TAG", "Ad was loaded.");
                    }
                });
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        submit.setEnabled(true);
        startTimer();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (count != 0) {
                        if (ans.getText().toString().equals(anslist.get(random))) {
                            new MaterialAlertDialogBuilder(MathQuiz.this)
                                    .setMessage("Yahh!! Right Answer")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            submit.setEnabled(false);
                                            count = count - 1;
                                            myEdit.putInt("quecount", count);
                                            myEdit.commit();
                                            counter.setText("" + count);
                                            if (count == 0) {
                                                timer.setVisibility(View.VISIBLE);
                                                Date currentTime = new Date();
                                                String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(currentTime);
                                                System.out.println(currentDateandTime);
                                                getFutureDate(currentTime, 2);
                                                startTimer();
                                            }
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                            if(count%2==0)
                                            {
                                                if (rewardedAd != null) {
                                                    Activity activityContext = MathQuiz.this;
                                                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                                        @Override
                                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                                            // Handle the reward.
                                                            Log.d("TAG", "The user earned the reward.");
                                                            int rewardAmount = rewardItem.getAmount();
                                                            String rewardType = rewardItem.getType();
                                                        }
                                                    });
                                                    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                                        @Override
                                                        public void onAdClicked() {
                                                            // Called when a click is recorded for an ad.
                                                            Log.d("TAG", "Ad was clicked.");
                                                        }

                                                        @Override
                                                        public void onAdDismissedFullScreenContent() {
                                                            // Called when ad is dismissed.
                                                            // Set the ad reference to null so you don't show the ad a second time.
                                                            Log.d("TAG", "Ad dismissed fullscreen content.");
                                                            rewardedAd = null;
                                                            Intent intent=new Intent(MathQuiz.this,MathQuiz.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                                            // Called when ad fails to show.
                                                            Log.e("TAG", "Ad failed to show fullscreen content.");
                                                            rewardedAd = null;
                                                        }

                                                        @Override
                                                        public void onAdImpression() {
                                                            // Called when an impression is recorded for an ad.
                                                            Log.d("TAG", "Ad recorded an impression.");
                                                        }

                                                        @Override
                                                        public void onAdShowedFullScreenContent() {
                                                            // Called when ad is shown.
                                                            Log.d("TAG", "Ad showed fullscreen content.");
                                                        }
                                                    });
                                                } else {
                                                    Log.d("TAG", "The rewarded ad wasn't ready yet.");
                                                }
                                            }
                                            else {
                                                Intent intent = new Intent(MathQuiz.this, MathQuiz.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    })
                                    .show();
                        } else {
                            new MaterialAlertDialogBuilder(MathQuiz.this)
                                    .setMessage("Wrong Answer Please Try Again")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .show();
                        }

                    } else {
                        Toast.makeText(MathQuiz.this, "Limit Over Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                }


        });
    }

    void startTimer() {
        if (getTimeDifference() <= 0) {
            timer.setVisibility(View.GONE);
            submit.setEnabled(true);
            myEdit.putString("quefuturedate", "").commit();
            if (count == 0) {
                myEdit.putInt("quecount", 8).commit();
                count = 8;
            }
            counter.setText("" + count);
            timer.setVisibility(View.GONE);
        }

        new CountDownTimer(getTimeDifference(), 1000) {
            public void onTick(long millisUntilFinished) {

                long minute = millisUntilFinished / (60 * 1000) % 60;
                long second = millisUntilFinished / 1000 % 60;
                timer.setText(String.format("%02d:%02d", minute, second));
                timer.setVisibility(View.VISIBLE);
                submit.setEnabled(false);
            }

            public void onFinish() {
                submit.setEnabled(true);
                if (count == 0) {
                    myEdit.putInt("quecount", 8).commit();
                    count = 8;

                }
                counter.setText("" + count);
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

    int getTimeDifference() {
        Date currentTime = new Date();
        String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(currentTime);
        String futuredate = sharedPreferences.getString("quefuturedate", "0");
        int dateDifference = (int) getDateDiff(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"), futuredate, currentDateandTime);
        System.out.println("dateDifference: " + dateDifference);
        return dateDifference;
    }

    public void getFutureDate(Date currentDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
//        cal.add(Calendar.MINUTE, days);
        cal.add(Calendar.HOUR, 3);
        Date futureDate = cal.getTime();
        String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(futureDate);
        myEdit.putString("quefuturedate", currentDateandTime);
        myEdit.commit();
        System.out.println(currentDateandTime);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MathQuiz.this, MainActivity.class);
        startActivity(intent);
        finish();
//        super.onBackPressed();
    }
}