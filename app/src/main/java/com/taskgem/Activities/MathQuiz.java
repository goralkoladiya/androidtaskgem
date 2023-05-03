package com.taskgem.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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

        count = sharedPreferences.getInt("quecount", 3);
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

                                        Intent intent = new Intent(MathQuiz.this, MathQuiz.class);
                                        startActivity(intent);
                                        finish();
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
                    Toast.makeText(MathQuiz.this, "You have not enough coin please wait", Toast.LENGTH_SHORT).show();
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
                myEdit.putInt("quecount", 3).commit();
                count = 3;
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
                    myEdit.putInt("quecount", 3).commit();
                    count = 3;

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
        cal.add(Calendar.MINUTE, 1);
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