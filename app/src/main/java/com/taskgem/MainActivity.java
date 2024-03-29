package com.taskgem;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mopinion.mopinion_android_sdk.ui.mopinion.Mopinion;
import com.pollfish.Pollfish;
import com.pollfish.builder.Params;
import com.pollfish.callback.PollfishClosedListener;
import com.pollfish.callback.PollfishOpenedListener;
import com.pollfish.callback.PollfishSurveyCompletedListener;
import com.pollfish.callback.PollfishSurveyNotAvailableListener;
import com.pollfish.callback.PollfishSurveyReceivedListener;
import com.pollfish.callback.PollfishUserNotEligibleListener;
import com.pollfish.callback.PollfishUserRejectedSurveyListener;
import com.pollfish.callback.SurveyInfo;
import com.rajat.pdfviewer.PdfViewerActivity;
import com.taskgem.Activities.RedeemActivity;
import com.taskgem.Activities.ReferActivity;
import com.taskgem.Activities.loginActivity;
import com.taskgem.Activities.spin;
import com.taskgem.Activities.transaction;
import com.taskgem.fragments.mainfragment;

import org.jetbrains.annotations.NotNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import ai.bitlabs.sdk.BitLabs;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView user, email;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView logoimg, profile;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;
    FirebaseAuth mFirebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    InterstitialAd mInterstitialAd;
    TextView coin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        drawerLayout = findViewById(R.id.drawer);
        coin=findViewById(R.id.coin);
        logoimg = findViewById(R.id.logoimg);
        navigationView = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.syncState();
        toolbar.setNavigationIcon(R.drawable.threedot);
        navigationView.setItemIconTintList(null);
        mFirebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1026119959686-gkhb6etfqdh8e0nu306iml9smuu2iiv3.apps.googleusercontent.com")
                .requestEmail()
                .build();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-8628133762932459/3482839340", adRequest,
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
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        getSupportFragmentManager().beginTransaction().replace(R.id.linear, new mainfragment()).commit();
        View view = navigationView.getHeaderView(0);
        user = view.findViewById(R.id.user);
        email = view.findViewById(R.id.email);
        profile = view.findViewById(R.id.profile);

        BitLabs.INSTANCE.init(this, "6d283b86-f543-4e3c-90b9-68e24e7b2744", sharedPreferences.getString("email", "user"));
        Glide
                .with(this)
                .load(sharedPreferences.getString("profile", "user"))
                .centerCrop()
                .placeholder(R.mipmap.profile)
                .into(profile);
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        user.setText(sharedPreferences.getString("email", "user"));
        email.setText(sharedPreferences.getString("first_name", "name") + " " + sharedPreferences.getString("last_name", "name"));
        coin.setText(""+sharedPreferences.getInt("rewards",0));
        Mopinion.Companion.initialise(this, "@FORM_KEY", true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.refer) {
                    if (netInfo != null) {
                        if (netInfo.isConnectedOrConnecting()) {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(MainActivity.this);
                                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        // Called when a click is recorded for an ad.
                                        Log.d(TAG, "Ad was clicked.");
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when ad is dismissed.
                                        // Set the ad reference to null so you don't show the ad a second time.
                                        Log.d(TAG, "Ad dismissed fullscreen content.");
                                        mInterstitialAd = null;
                                        Intent intent = new Intent(MainActivity.this, ReferActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when ad fails to show.
                                        Log.e(TAG, "Ad failed to show fullscreen content.");
                                        mInterstitialAd = null;
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(TAG, "Ad recorded an impression.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when ad is shown.
                                        Log.d(TAG, "Ad showed fullscreen content.");
                                    }
                                });
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, ReferActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(MainActivity.this, ReferActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                if (item.getItemId() == R.id.reward) {
                    if (netInfo != null) {
                        if (netInfo.isConnectedOrConnecting()) {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(MainActivity.this);
                                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        // Called when a click is recorded for an ad.
                                        Log.d(TAG, "Ad was clicked.");
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when ad is dismissed.
                                        // Set the ad reference to null so you don't show the ad a second time.
                                        Log.d(TAG, "Ad dismissed fullscreen content.");
                                        mInterstitialAd = null;
                                        Intent intent = new Intent(MainActivity.this, RedeemActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when ad fails to show.
                                        Log.e(TAG, "Ad failed to show fullscreen content.");
                                        mInterstitialAd = null;
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(TAG, "Ad recorded an impression.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when ad is shown.
                                        Log.d(TAG, "Ad showed fullscreen content.");
                                    }
                                });
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, RedeemActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(MainActivity.this, RedeemActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                if (item.getItemId() == R.id.transaction) {
                    if (netInfo != null) {
                        if (netInfo.isConnectedOrConnecting()) {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(MainActivity.this);
                                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        // Called when a click is recorded for an ad.
                                        Log.d(TAG, "Ad was clicked.");
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when ad is dismissed.
                                        // Set the ad reference to null so you don't show the ad a second time.
                                        Log.d(TAG, "Ad dismissed fullscreen content.");
                                        mInterstitialAd = null;
                                        Intent intent = new Intent(MainActivity.this, transaction.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when ad fails to show.
                                        Log.e(TAG, "Ad failed to show fullscreen content.");
                                        mInterstitialAd = null;
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(TAG, "Ad recorded an impression.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when ad is shown.
                                        Log.d(TAG, "Ad showed fullscreen content.");
                                    }
                                });
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, transaction.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(MainActivity.this, transaction.class);
                        startActivity(intent);
                        finish();
                    }

                }
                if (item.getItemId() == R.id.play) {
                    if (netInfo != null) {
                        if (netInfo.isConnectedOrConnecting()) {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(MainActivity.this);
                                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdClicked() {
                                        // Called when a click is recorded for an ad.
                                        Log.d(TAG, "Ad was clicked.");
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when ad is dismissed.
                                        // Set the ad reference to null so you don't show the ad a second time.
                                        Log.d(TAG, "Ad dismissed fullscreen content.");
                                        mInterstitialAd = null;
                                        Intent intent = new Intent(MainActivity.this, spin.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when ad fails to show.
                                        Log.e(TAG, "Ad failed to show fullscreen content.");
                                        mInterstitialAd = null;
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        // Called when an impression is recorded for an ad.
                                        Log.d(TAG, "Ad recorded an impression.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when ad is shown.
                                        Log.d(TAG, "Ad showed fullscreen content.");
                                    }
                                });
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                            }
                        } else {
                            Intent intent = new Intent(MainActivity.this, spin.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(MainActivity.this, spin.class);
                        startActivity(intent);
                        finish();
                    }

                }
                if (item.getItemId() == R.id.rate) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.taskgem")));
                }if (item.getItemId() == R.id.rateus) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.taskgem")));
                }
                if (item.getItemId() == R.id.support) {

                    Intent email = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:taskgemapp@gmail.com"));
//                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"taskgemapp@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "TaskGem Support");
                    email.putExtra(Intent.EXTRA_TEXT, "Hello");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));

                }
                if (item.getItemId() == R.id.logout) {
                    mFirebaseAuth.signOut();
                    mGoogleSignInClient.signOut();
                    myEdit.remove("rewards");
//                    myEdit.remove("count");
                    myEdit.commit();
                    Intent intent = new Intent(MainActivity.this, loginActivity.class);
                    startActivity(intent);
                    finish();

                }
                if(item.getItemId()==R.id.privacy)
                {
                    startActivity(
                            PdfViewerActivity.Companion.launchPdfFromPath(
                                    MainActivity.this,
                                    "privacy.pdf",
                                    "Privacy Policy",
                                    "assets",
                                    false,
                                    true
                            )
                    );
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }


}