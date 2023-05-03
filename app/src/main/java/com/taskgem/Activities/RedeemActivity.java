package com.taskgem.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.taskgem.MainActivity;
import com.taskgem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RedeemActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit;
    LinearLayout paytm1,paytm2,paytm3,gp1,gp2,gp3;
    TextView coin;
    int coins;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0,toolbar.getContentInsetStartWithNavigation());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RedeemActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        paytm1=findViewById(R.id.paytm1);
        paytm2=findViewById(R.id.paytm2);
        paytm3=findViewById(R.id.paytm3);
        gp1=findViewById(R.id.gp1);
        gp2=findViewById(R.id.gp2);
        gp3=findViewById(R.id.gp3);
        coin=findViewById(R.id.coin);
        coins=sharedPreferences.getInt("rewards",0);
        coin.setText(""+coins);

        paytm1.setOnClickListener(this);
        paytm2.setOnClickListener(this);
        paytm3.setOnClickListener(this);
        gp1.setOnClickListener(this);
        gp2.setOnClickListener(this);
        gp3.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.paytm1)
        {
            if(coins>=1600)
            {
                send_request("paytm",1600,15);
            }
            else {
                Toast.makeText(this, "you have to earn more coins to Redeem", Toast.LENGTH_SHORT).show();
            }

        }
        if(view.getId()==R.id.paytm2)
        {
            if(coins>=4800)
            {
                send_request("paytm",4800,50);
            }
            else {
                Toast.makeText(this, "you have to earn more coins to Redeem", Toast.LENGTH_SHORT).show();
            }

        }
        if(view.getId()==R.id.paytm3)
        {
            if(coins>=9500)
            {
                send_request("paytm",9500,100);
            }
            else {
                Toast.makeText(this, "you have to earn more coins to Redeem", Toast.LENGTH_SHORT).show();
            }

        }
        if(view.getId()==R.id.gp1)
        {
            if(coins>=1100)
            {
                send_request("gpay",1100,15);
            }
            else {
                Toast.makeText(this, "you have to earn more coins to Redeem", Toast.LENGTH_SHORT).show();
            }

        }
        if(view.getId()==R.id.gp2)
        {
            if(coins>=2500)
            {
                send_request("gpay",2500,25);
            }
            else {
                Toast.makeText(this, "you have to earn more coins to Redeem", Toast.LENGTH_SHORT).show();
            }

        }
        if(view.getId()==R.id.gp3)
        {
            if(coins>=4800)
            {
                send_request("gpay",4800,50);
            }
            else {
                Toast.makeText(this, "you have to earn more coins to Redeem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void send_request(String mode,int coin,int amount)
    {

        new MaterialAlertDialogBuilder(RedeemActivity.this)
                .setTitle("Redeem Coins")
                .setMessage("Are you sure want to Redeem Coins?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        RequestQueue queue = Volley.newRequestQueue(RedeemActivity.this);
                        String url = "http://taskgem.in/taskgem/admin/transaction-insert-api.php";

// Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println(response);
                                        JSONObject jsonObject= null;
                                        try {
                                            jsonObject = new JSONObject(response);
                                            if(jsonObject.getBoolean("status"))
                                            {
                                                coins=coins-coin;
                                                myEdit.putInt("rewards",coins);
                                                myEdit.commit();
                                                Intent intent=new Intent(RedeemActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
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
                                params.put("payment_mode",mode);
                                params.put("coin", ""+coin);
                                params.put("amount", ""+amount);
                                return params;
                            }
                        };

// Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
//        super.onBackPressed();
    }
}