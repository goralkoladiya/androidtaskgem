package com.taskgem.fragments;

import static android.content.Context.MODE_PRIVATE;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
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

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adgatemedia.sdk.classes.AdGateMedia;
import com.adgatemedia.sdk.network.OnOfferWallLoadFailed;
import com.adgatemedia.sdk.network.OnOfferWallLoadSuccess;
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
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.makeopinion.cpxresearchlib.CPXResearchListener;
import com.makeopinion.cpxresearchlib.models.CPXCardConfiguration;
import com.makeopinion.cpxresearchlib.models.SurveyItem;
import com.makeopinion.cpxresearchlib.models.TransactionItem;
import com.monlixv2.MonlixGender;
import com.monlixv2.MonlixOffers;
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
import com.tapr.sdk.PlacementListener;
import com.tapr.sdk.TRPlacement;
import com.tapr.sdk.TapResearch;
import com.taskgem.Activities.ReferActivity;
import com.taskgem.Activities.scratchCard;
import com.taskgem.Activities.spin;
import com.taskgem.CPXApplication;
import com.taskgem.MainActivity;
import com.taskgem.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.bitlabs.sdk.BitLabs;
import kotlin.Unit;
import theoremreach.com.theoremreach.TheoremReach;
import theoremreach.com.theoremreach.TheoremReachRewardListener;


public class mainfragment extends Fragment implements
        PollfishSurveyCompletedListener,
        PollfishOpenedListener,
        PollfishClosedListener,
        PollfishSurveyReceivedListener,
        PollfishSurveyNotAvailableListener,
        PollfishUserNotEligibleListener,
        PollfishUserRejectedSurveyListener,
        CPXResearchListener
{
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
    public void onPollfishSurveyCompleted(@NotNull SurveyInfo surveyInfo) {
//        coinsBtn.setVisibility(View.GONE);

        // in a real world scenario you should wait here for verification from s2s callback prior rewarding your users
//        loggingTxt.setText(getString(R.string.survey_completed, surveyInfo.getRewardValue() == null ? 200 : surveyInfo.getRewardValue()));
    }

    @Override
    public void onPollfishSurveyReceived(SurveyInfo surveyInfo) {
//        coinsBtn.setVisibility(View.VISIBLE);
//        coinsBtn.setText(getString(R.string.win_coins, surveyInfo != null && surveyInfo.getRewardValue() != null ? surveyInfo.getRewardValue() : 200));
        Log.d(TAG, getString(R.string.survey_received));
    }

    @Override
    public void onPollfishClosed() {
        Log.d(TAG, getString(R.string.on_pollfish_closed));
    }

    @Override
    public void onPollfishOpened() {
        Log.d(TAG, getString(R.string.on_pollfish_opened));
    }

    @Override
    public void onPollfishSurveyNotAvailable() {
        Log.d(TAG, getString(R.string.survey_not_available));
//        loggingTxt.setText(R.string.survey_not_available);
    }

    @Override
    public void onUserNotEligible() {
        Log.d(TAG, getString(R.string.user_not_eligible));
//        coinsBtn.setVisibility(View.GONE);
//        loggingTxt.setText(R.string.user_not_eligible);
    }

    @Override
    public void onUserRejectedSurvey() {
//        coinsBtn.setVisibility(View.GONE);
//        loggingTxt.setText(R.string.user_rejected_survey);
    }
    //https://api.adgatemedia.com/v1/advertiser/report?adv=82987&api_key=be59b188800005663a65efb2cb08d3ec&start_date=06-05-2023&end_date=06-12-2028
    private void initPollfish() {
        Params params = new Params.Builder("3cc67a3f-e1fb-4ab0-887f-a143f8871be6")
                .releaseMode(true)
                .rewardMode(true)
//                .userLayout(scratch)
                .build();
        Pollfish.initWith(getActivity(), params);
    }
    CPXApplication app;
    CPXCardConfiguration cardConfig;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        app = (CPXApplication) getActivity().getApplication();
        app.getCpxResearch().registerListener(this);
//        app.getCpxResearch().setSurveyVisibleIfAvailable(true, getActivity());
//        app.getCpxResearch().exportLog(getActivity());
        cardConfig = new CPXCardConfiguration.Builder()
                .build(); //don't set anything, just use default values
        // Inflate the layout for this fragment
        initPollfish();
        new MonlixOffers.Builder()
                .setAppId("APP_ID")
                .setUserId("USER_ID")
                .setAge(20)
                .setGender(MonlixGender.MALE) // MALE, FEMALE, OTHER
                .build(getContext());

        return inflater.inflate(R.layout.fragment_mainfragment, container, false);
    }
    final HashMap<String, String> subids = new HashMap<String, String>();
    TRPlacement mPlacement;


    @Override
    public void onPause() {
        super.onPause();
        TheoremReach.getInstance().onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        TheoremReach.getInstance().onResume(getActivity());
        TapResearch.getInstance().initPlacement("1268093ef9ffe17ef8ec502c93077e68", new PlacementListener() {
            @Override
            public void onPlacementReady(TRPlacement placement) {
                System.out.println("pla="+placement.getPlacementCode()+"="+placement);
                if (placement.getPlacementCode() == TRPlacement.PLACEMENT_CODE_SDK_NOT_READY) {
                    mPlacement = placement;
                    if (mPlacement.isSurveyWallAvailable()) {
                        System.out.println("available");
//                        mPlacement.showSurveyWall(null);
                    }
                    else {
                        System.out.println("not available");
                    }
                } else {
                    System.out.println("sdk not available");
                    //SDK is not ready
                }
            }

        });
    }
    int rewards=0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("MySharedPref",MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        rewards=sharedPreferences.getInt("rewards",0);
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
        user.setText("Hello "+sharedPreferences.getString("first_name","name") + " "+sharedPreferences.getString("last_name","name")+" \uD83D\uDC4B");
        mopinion = new Mopinion(requireActivity(), getViewLifecycleOwner());
        mopinion.event("action", formState -> Unit.INSTANCE);

//        app.getCpxResearch().insertCPXResearchCardsIntoContainer(getActivity(), l1, cardConfig);
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        TheoremReach.initWithApiKeyAndUserIdAndActivityContext("2935044d317d7d7436b3807b4911", "ANDROID_TEST_ID", getActivity());
        TheoremReach.getInstance().setTheoremReachRewardListener(new TheoremReachRewardListener() {
            @Override
            public void onReward(int i) {
                rewards=sharedPreferences.getInt("rewards",0);
                rewards=rewards+i;
                myEdit.putInt("rewards",rewards);
                myEdit.commit();
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "http://taskgem.in/taskgem/admin/rewards-insert-api.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(response);
//                                Intent intent=new Intent(getContext(),MainActivity.class);
//                                startActivity(intent);
//                                getActivity().finish();
                                TheoremReach.getInstance().onRewardCenterClosed();
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
                        params.put("rewards",""+i);
                        params.put("reason", "credited via theoremreach");
                        return params;
                    }
                };

// Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });
        BitLabs.INSTANCE.setOnRewardListener(v -> {
            System.out.println(v);
            rewards=sharedPreferences.getInt("rewards",0);
            rewards=rewards+(int)v;
            myEdit.putInt("rewards",rewards);
            myEdit.commit();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://taskgem.in/taskgem/admin/rewards-insert-api.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
//                                Intent intent=new Intent(getContext(),MainActivity.class);
//                                startActivity(intent);
//                                getActivity().finish();
                            TheoremReach.getInstance().onRewardCenterClosed();
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
                    params.put("rewards",""+v);
                    params.put("reason", "credited via theoremreach");
                    return params;
                }
            };

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView mAdView = view.findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        InterstitialAd.load(getContext(),"ca-app-pub-8628133762932459/3482839340", adRequest,
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
        String mId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        System.out.println("id="+mId);
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
                        Toast.makeText(getActivity(), "No internet connection please connnect Internet", Toast.LENGTH_SHORT).show();
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
            MonlixOffers.INSTANCE.showWall(getContext(),"");
//            if (netInfo != null) {
//                if (netInfo.isConnectedOrConnecting()) {
//                    if (mInterstitialAd != null) {
//                        mInterstitialAd.show(getActivity());
//                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
//                            @Override
//                            public void onAdClicked() {
//                                // Called when a click is recorded for an ad.
//                                Log.d(TAG, "Ad was clicked.");
//                            }
//
//                            @Override
//                            public void onAdDismissedFullScreenContent() {
//                                // Called when ad is dismissed.
//                                // Set the ad reference to null so you don't show the ad a second time.
//                                Log.d(TAG, "Ad dismissed fullscreen content.");
//                                mInterstitialAd = null;
//                                Intent intent=new Intent(getActivity(), com.taskgem.Activities.videozone.class);
//                                startActivity(intent);
//                                getActivity().finish();
//                            }
//
//                            @Override
//                            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                // Called when ad fails to show.
//                                Log.e(TAG, "Ad failed to show fullscreen content.");
//                                mInterstitialAd = null;
//                            }
//
//                            @Override
//                            public void onAdImpression() {
//                                // Called when an impression is recorded for an ad.
//                                Log.d(TAG, "Ad recorded an impression.");
//                            }
//
//                            @Override
//                            public void onAdShowedFullScreenContent() {
//                                // Called when ad is shown.
//                                Log.d(TAG, "Ad showed fullscreen content.");
//                            }
//                        });
//                    } else {
//                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
//                    }
//                }else {
//                    Intent intent=new Intent(getActivity(), com.taskgem.Activities.videozone.class);
//                    startActivity(intent);
//                    getActivity().finish();
//                }
//            }
//            else {
//                Intent intent=new Intent(getActivity(), com.taskgem.Activities.videozone.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
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

            mPlacement.showSurveyWall(null);
            TheoremReach.getInstance().showRewardCenter();
//            if (netInfo != null) {
//                if (netInfo.isConnectedOrConnecting()) {
//                    if (mInterstitialAd != null) {
//                        mInterstitialAd.show(getActivity());
//                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
//                            @Override
//                            public void onAdClicked() {
//                                // Called when a click is recorded for an ad.
//                                Log.d(TAG, "Ad was clicked.");
//                            }
//
//                            @Override
//                            public void onAdDismissedFullScreenContent() {
//                                // Called when ad is dismissed.
//                                // Set the ad reference to null so you don't show the ad a second time.
//                                Log.d(TAG, "Ad dismissed fullscreen content.");
//                                mInterstitialAd = null;
//                                Intent intent=new Intent(getActivity(), com.taskgem.Activities.scratchCard.class);
//                                startActivity(intent);
//                                getActivity().finish();
//                            }
//
//                            @Override
//                            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                // Called when ad fails to show.
//                                Log.e(TAG, "Ad failed to show fullscreen content.");
//                                mInterstitialAd = null;
//                            }
//
//                            @Override
//                            public void onAdImpression() {
//                                // Called when an impression is recorded for an ad.
//                                Log.d(TAG, "Ad recorded an impression.");
//                            }
//
//                            @Override
//                            public void onAdShowedFullScreenContent() {
//                                // Called when ad is shown.
//                                Log.d(TAG, "Ad showed fullscreen content.");
//                            }
//                        });
//                    } else {
//                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
//                    }
//                }else {
//                    Intent intent=new Intent(getActivity(), com.taskgem.Activities.scratchCard.class);
//                    startActivity(intent);
//                    getActivity().finish();
//                }
//            }else {
//                Intent intent=new Intent(getActivity(), com.taskgem.Activities.scratchCard.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
        });
        telegram.setOnClickListener(view1 -> {
            Intent intent;
            try {
                try { // check for telegram app
                    getContext().getPackageManager().getPackageInfo("org.telegram.messenger", 0);
                } catch (PackageManager.NameNotFoundException e) {
                    // check for telegram X app
                    getContext().getPackageManager().getPackageInfo("org.thunderdog.challegram", 0);
                }
                // set app Uri
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=taskgem"));
            } catch (PackageManager.NameNotFoundException e) {
                // set browser URI
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/taskgem"));
            }
            startActivity(intent);
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
                Pollfish.show();
//
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CPXApplication) getActivity().getApplication()).getCpxResearch().openSurveyList(getActivity());


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

    @Override
    public void onSurveysDidClose() {
        Log.d("CPXDEMO", "Surveys did close.");
    }

    @Override
    public void onSurveysDidOpen() {
        Log.d("CPXDEMO", "Surveys did open.");
    }

    @Override
    public void onSurveysUpdated() {
//        CPXApplication app = (CPXApplication) getActivity().getApplication();
//        List<SurveyItem> surveys = app.getCpxResearch().getSurveys();
//        Log.d("CPXDEMO", "Surveys updated: " + surveys);
    }

    @Override
    public void onSurveyDidClose() { Log.d("CPXDEMO", "Single survey closed."); }

    @Override
    public void onSurveyDidOpen() { Log.d("CPXDEMO", "Single survey opened."); }

    @Override
    public void onTransactionsUpdated(List<TransactionItem> unpaidTransactions) {
        Log.d("CPXDEMO", String.format("Transactions updated with %d items", unpaidTransactions.size()));
        for (int i = 0; i < unpaidTransactions.size(); i++) {
            Log.d("CPXDEMO", unpaidTransactions.get(i).getEarningPublisher());
        }
    }


}