package com.taskgem.fragments;

import static android.content.Context.MODE_PRIVATE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.mopinion.mopinion_android_sdk.ui.mopinion.Mopinion;
import com.taskgem.Activities.ReferActivity;
import com.taskgem.MainActivity;
import com.taskgem.R;

import ai.bitlabs.sdk.BitLabs;
import kotlin.Unit;


public class mainfragment extends Fragment {
    InterstitialAd mInterstitialAd;
    ImageView telegram,rate;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor myEdit;
    TextView user;
    View spin;
    RelativeLayout videozone,playzone,scratch,math;
    LinearLayout l1,l2,l3;
    Mopinion mopinion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mainfragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        user=view.findViewById(R.id.user);
        videozone=view.findViewById(R.id.videozone);
        math=view.findViewById(R.id.math);
        playzone=view.findViewById(R.id.playzone);
        scratch=view.findViewById(R.id.scratch);
        telegram=view.findViewById(R.id.telegram);
        rate=view.findViewById(R.id.rate);
        spin=view.findViewById(R.id.spin);
        l1=view.findViewById(R.id.l1);
        l2=view.findViewById(R.id.l2);
        l3=view.findViewById(R.id.l3);
        user.setText("Hello "+sharedPreferences.getString("first_name","name") + " "+sharedPreferences.getString("last_name","name"));
        mopinion = new Mopinion(requireActivity(), getViewLifecycleOwner());
        mopinion.event("action", formState -> Unit.INSTANCE);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView mAdView = view.findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        InterstitialAd.load(getContext(),"ca-app-pub-3940256099942544/1033173712", adRequest,
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
        ConnectivityManager mgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = mgr.getActiveNetworkInfo();
        spin.setOnClickListener(view1 -> {
            if (netInfo != null) {
                if (netInfo.isConnectedOrConnecting()) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(getActivity());
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
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
                                Intent intent=new Intent(getActivity(), com.taskgem.Activities.spin.class);
                                startActivity(intent);
                                getActivity().finish();
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
                }else {
                    Intent intent=new Intent(getActivity(), com.taskgem.Activities.spin.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }else {
                Intent intent=new Intent(getActivity(), com.taskgem.Activities.spin.class);
                startActivity(intent);
                getActivity().finish();
            }

        });
        videozone.setOnClickListener(view1 -> {
            if (netInfo != null) {
                if (netInfo.isConnectedOrConnecting()) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(getActivity());
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
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
                                Intent intent=new Intent(getActivity(), com.taskgem.Activities.videozone.class);
                                startActivity(intent);
                                getActivity().finish();
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
                }else {
                    Intent intent=new Intent(getActivity(), com.taskgem.Activities.videozone.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
            else {
                Intent intent=new Intent(getActivity(), com.taskgem.Activities.videozone.class);
                startActivity(intent);
                getActivity().finish();
            }

        });
        playzone.setOnClickListener(view1 -> {
            if (netInfo != null) {
                if (netInfo.isConnectedOrConnecting()) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(getActivity());
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
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
                                Intent intent=new Intent(getActivity(), com.taskgem.Activities.playzone.class);
                                startActivity(intent);
                                getActivity().finish();
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
                }else {
                    Intent intent=new Intent(getActivity(), com.taskgem.Activities.playzone.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }else {
                Intent intent=new Intent(getActivity(), com.taskgem.Activities.playzone.class);
                startActivity(intent);
                getActivity().finish();
            }

        });
        scratch.setOnClickListener(view1 -> {
            if (netInfo != null) {
                if (netInfo.isConnectedOrConnecting()) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(getActivity());
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
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
                                Intent intent=new Intent(getActivity(), com.taskgem.Activities.scratchCard.class);
                                startActivity(intent);
                                getActivity().finish();
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
                }else {
                    Intent intent=new Intent(getActivity(), com.taskgem.Activities.scratchCard.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }else {
                Intent intent=new Intent(getActivity(), com.taskgem.Activities.scratchCard.class);
                startActivity(intent);
                getActivity().finish();
            }

        });
        telegram.setOnClickListener(view1 -> {
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
                Toast.makeText(getContext(), "Application is not currently installed.", Toast.LENGTH_SHORT).show();
            }
        });
        rate.setOnClickListener(view1 -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.taskgem")));
        });
        math.setOnClickListener(view1 -> {
            if (netInfo != null) {
                if (netInfo.isConnectedOrConnecting()) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(getActivity());
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
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
                                Intent intent=new Intent(getActivity(), com.taskgem.Activities.MathQuiz.class);
                                startActivity(intent);
                                getActivity().finish();
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
                }else {
                    Intent intent=new Intent(getActivity(), com.taskgem.Activities.MathQuiz.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }else {
                Intent intent=new Intent(getActivity(), com.taskgem.Activities.MathQuiz.class);
                startActivity(intent);
                getActivity().finish();
            }

        });

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitLabs.INSTANCE.launchOfferWall(getContext());
            }
        });
    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}