package com.taskgem;

import android.app.Application;

import androidx.annotation.NonNull;

import com.makeopinion.cpxresearchlib.CPXResearch;
import com.makeopinion.cpxresearchlib.models.CPXConfiguration;
import com.makeopinion.cpxresearchlib.models.CPXConfigurationBuilder;
import com.makeopinion.cpxresearchlib.models.CPXStyleConfiguration;
import com.makeopinion.cpxresearchlib.models.SurveyPosition;
import com.tapr.sdk.RewardListener;
import com.tapr.sdk.TRReward;
import com.tapr.sdk.TapResearch;

public class CPXApplication extends Application {
    private CPXResearch cpxResearch;

    @Override
    public void onCreate() {
        super.onCreate();
        TapResearch.configure("431e537eb86b8c8400d2ef346af58d5e", this);
        TapResearch.getInstance().setUniqueUserIdentifier("a230db4d62ac345a");
        initCPX();

    }

    @NonNull
    public CPXResearch getCpxResearch() {
        return cpxResearch;
    }

    private void initCPX() {
        CPXStyleConfiguration style = new CPXStyleConfiguration(SurveyPosition.SideRightNormal,
                "Earn up to 3 Coins in<br> 4 minutes with surveys",
                20,
                "#ffffff",
                "#ffaf20",
                true);

        CPXConfiguration config = new CPXConfigurationBuilder("18649",
                "taskgem",
                "1ZBhw8SD5qVp7mGbkRWE1lurrUcR4rel",
                style)
                .build();

        cpxResearch = CPXResearch.Companion.init(config);
    }
}