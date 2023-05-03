package com.taskgem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.taskgem.MainActivity;
import com.taskgem.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ReferActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView invite,invitees;
    ImageView share,whatsapp,telegram,fb;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        toolbar=findViewById(R.id.toolbar);
        share=findViewById(R.id.share);
        whatsapp=findViewById(R.id.whatsapp);
        telegram=findViewById(R.id.telegram);
        fb=findViewById(R.id.fb);

        toolbar.setContentInsetsAbsolute(0,toolbar.getContentInsetStartWithNavigation());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReferActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        invite=findViewById(R.id.invite);
        invitees=findViewById(R.id.invitees);



        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://taskgem.in/taskgem/admin/get-referrer-api.php?code="+sharedPreferences.getString("token","");

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getBoolean("status"))
                            {
                                invitees.setText("TOTAL INVITES - "+jsonObject.getInt("total"));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);


        invite.setText("INVITE CODE: "+sharedPreferences.getString("token","ABCD"));
        invitees.setText("TOTAL INVITES - 0");

        share.setOnClickListener(view -> {
            String msg="Hey, we’re happy to invite you to join our referral program and share it with your friends to get more rewards" +
                    "https://play.google.com/store/apps/details?id=com.taskgem";
            Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
            txtIntent.setType("text/plain");
            txtIntent.putExtra(android.content.Intent.EXTRA_TEXT,msg);
            startActivity(Intent.createChooser(txtIntent ,"Share"));
        });
        fb.setOnClickListener(view -> {
            // Use package name which we want to check
            boolean isAppInstalled = appInstalledOrNot("com.facebook.katana");

            if(isAppInstalled) {
                String msg="Hey, we’re happy to invite you to join our referral program and share it with your friends to get more rewards" +
                        "https://play.google.com/store/apps/details?id=com.taskgem";
                Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                txtIntent.setType("text/plain");
                txtIntent.setPackage("com.facebook.katana");
                txtIntent .putExtra(android.content.Intent.EXTRA_TEXT,msg);
                startActivity(txtIntent);
            } else {
                Toast.makeText(this, "Application is not currently installed.", Toast.LENGTH_SHORT).show();
            }

        });
        whatsapp.setOnClickListener(view -> {
            boolean isAppInstalled = appInstalledOrNot("com.whatsapp");
            if(isAppInstalled)
                {
                    String msg="Hey, we’re happy to invite you to join our referral program and share it with your friends to get more rewards" +
                            "https://play.google.com/store/apps/details?id=com.taskgem";
                    Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                    txtIntent .setType("text/plain");
                    txtIntent.setPackage("com.whatsapp");
                    txtIntent .putExtra(android.content.Intent.EXTRA_TEXT,msg);
                    startActivity(Intent.createChooser(txtIntent ,"Share"));
                }
            else if(appInstalledOrNot("com.whatsapp.w4b"))
                {
                    String msg="Hey, we’re happy to invite you to join our referral program and share it with your friends to get more rewards" +
                            "https://play.google.com/store/apps/details?id=com.taskgem";
                    Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                    txtIntent .setType("text/plain");
                    txtIntent.setPackage("com.whatsapp.w4b");
                    txtIntent .putExtra(android.content.Intent.EXTRA_TEXT,msg);
                    startActivity(Intent.createChooser(txtIntent ,"Share"));
                    Toast.makeText(this, "Application is not currently installed.", Toast.LENGTH_SHORT).show();
                }
            else {
                Toast.makeText(this, "Application is not currently installed.", Toast.LENGTH_SHORT).show();
            }
        });
        telegram.setOnClickListener(view -> {
            boolean isAppInstalled = appInstalledOrNot("org.telegram.messenger");
            if(isAppInstalled)
            {
                String msg="Hey, we’re happy to invite you to join our referral program and share it with your friends to get more rewards" +
                        "https://play.google.com/store/apps/details?id=com.taskgem";
                Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                txtIntent .setType("text/plain");
                txtIntent.setPackage("org.telegram.messenger");
                txtIntent .putExtra(android.content.Intent.EXTRA_TEXT,msg);
                startActivity(Intent.createChooser(txtIntent ,"Share"));
            }else {
                Toast.makeText(this, "Application is not currently installed.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
//        super.onBackPressed();
    }
}