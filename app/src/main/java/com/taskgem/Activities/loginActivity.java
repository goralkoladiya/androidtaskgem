package com.taskgem.Activities;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.taskgem.MainActivity;
import com.taskgem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class loginActivity extends AppCompatActivity {
    InstallReferrerClient referrerClient;
    SignInButton login;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=1;
    CheckBox checkBox;
//    private Object mGoogleSignInClient;
    FirebaseAuth mFirebaseAuth;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit;
    ProgressDialog progressDialog;
    String referrer = "";
    @Override
    public void onStart() {

        super.onStart();
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=findViewById(R.id.sign_in_button);
        checkBox=findViewById(R.id.check);
        mFirebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
         myEdit = sharedPreferences.edit();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1026119959686-gkhb6etfqdh8e0nu306iml9smuu2iiv3.apps.googleusercontent.com")
                .requestEmail()
                .build();
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            // on below line we are getting referrer details
                            // by calling get install referrer.
                            response = referrerClient.getInstallReferrer();

                            // on below line we are getting referrer url.
                            String referrerUrl = response.getInstallReferrer();

                            // on below line we are getting referrer click time.
                            long referrerClickTime = response.getReferrerClickTimestampSeconds();

                            // on below line we are getting app install time
                            long appInstallTime = response.getInstallBeginTimestampSeconds();

                            // on below line we are getting our time when
                            // user has used our apps instant experience.
                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                            // on below line we are getting our
                            // apps install referrer.
                            referrer = response.getInstallReferrer();

                            // on below line we are setting all detail to our text view.
                            System.out.println("Referrer is : \n" + referrerUrl + "\n" + "Referrer Click Time is : " + referrerClickTime + "\nApp Install Time : " + appInstallTime);
                        } catch (RemoteException e) {
                            // handling error case.
                            e.printStackTrace();
                        }
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked())
                {
                    if (netInfo != null) {
                        if (netInfo.isConnectedOrConnecting()) {
                            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                            startActivityForResult(signInIntent, RC_SIGN_IN);
                        }else {
                            Toast.makeText(loginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(loginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(loginActivity.this, "Please check Policy", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        for (int i = 0; i < login.getChildCount(); i++) {
            View v = login.getChildAt(i);

            if (v instanceof TextView)
            {
                TextView tv = (TextView) v;
//                tv.setTextSize(14);
//                tv.setTypeface(null, Typeface.NORMAL);
//                tv.setText("My Text");
                tv.setTextColor(Color.parseColor("#00000000"));
                tv.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.playbutton));
                tv.setSingleLine(true);
//                tv.setPadding(15, 15, 15, 15);

                return;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatus().getStatusCode());
//            updateUI(null);
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        progressDialog=ProgressDialog.show(loginActivity.this,"","");
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    System.out.println(authResult.getUser().getDisplayName());
                    System.out.println(authResult.getUser().getEmail());
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = "http://taskgem.in/taskgem/admin/user-insert-api.php";

// Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println(response);
                                    //{"message":"User data inserted.","status":true}

                                    try {
                                        JSONObject jsonObject=new JSONObject(response);
                                        String message=jsonObject.getString("message");
                                        boolean status=jsonObject.getBoolean("status");

                                        if(status)
                                        {
                                            if(!sharedPreferences.getString("email","").equals(jsonObject.getString("Email")))
                                            {
                                                myEdit.remove("count");
                                                myEdit.remove("scratchcount");
                                                myEdit.remove("quecount");
                                                myEdit.commit();
                                            }
                                            myEdit.putString("uid", jsonObject.getString("uid"));
                                            myEdit.putString("profile", authResult.getUser().getPhotoUrl().toString());
                                            myEdit.putString("email", jsonObject.getString("Email"));
                                            myEdit.putString("first_name", jsonObject.getString("First Name"));
                                            myEdit.putString("last_name", jsonObject.getString("Last Name"));
                                            myEdit.putInt("rewards", jsonObject.getInt("Rewards"));
                                            myEdit.putString("task", jsonObject.getString("Task"));
                                            myEdit.putString("token", jsonObject.getString("Token"));
                                            myEdit.commit();
                                            progressDialog.dismiss();
                                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

//
                                }
                            },
                            new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.getMessage());
                            Toast.makeText(loginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("email", authResult.getUser().getEmail());
                            params.put("first_name", authResult.getUser().getDisplayName().split(" ")[0]);
                            params.put("last_name", authResult.getUser().getDisplayName().split(" ")[1]);
                            params.put("rewards", "10");
                            params.put("task", "cashback");
                            params.put("referrer", referrer);
                            return params;
                        }
                    };

// Add the request to the RequestQueue.
                    queue.add(stringRequest);

                })

                .addOnFailureListener(this, e -> Toast.makeText(loginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
//        super.onBackPressed();

    }
}