package com.taskgem.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.taskgem.Adapters.MyListAdapter;
import com.taskgem.MainActivity;
import com.taskgem.Modal.MyListData;
import com.taskgem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class transaction extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit;
    ArrayList<MyListData> listdata;
    int rewards=0;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        toolbar=findViewById(R.id.toolbar);
        listdata=new ArrayList<>();
        toolbar.setContentInsetsAbsolute(0,toolbar.getContentInsetStartWithNavigation());
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(transaction.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        recyclerView=findViewById(R.id.recycle);
        myEdit = sharedPreferences.edit();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://taskgem.in/taskgem/admin/rewards-detail-api.php?uid="+sharedPreferences.getString("uid","1");
        progressDialog=ProgressDialog.show(this,"","Fetching Records");
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                MyListData m=new MyListData();
                                m.setId(jsonObject.getString("id"));
                                m.setUid(jsonObject.getString("uid"));
                                m.setReason(jsonObject.getString("reason"));
                                m.setRewards(jsonObject.getString("rewards"));
                                m.setStatus(jsonObject.getInt("status"));
                                if(jsonObject.getString("reason").equals("rewards debited"))
                                {
                                    rewards=rewards-Integer.parseInt(jsonObject.getString("rewards"));
                                }
                                else {
                                    rewards=rewards+Integer.parseInt(jsonObject.getString("rewards"));
                                }
                                m.setTimestamp(jsonObject.getString("timestamp"));
                                listdata.add(m);
                            }

                            myEdit.putInt("rewards",rewards);
                            myEdit.commit();
                            MyListAdapter adapter = new MyListAdapter(listdata);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(transaction.this));
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
//        super.onBackPressed();
    }
}