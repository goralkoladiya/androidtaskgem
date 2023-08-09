package com.taskgem;

import com.tapr.sdk.TRPlacement;

public interface PlacementEventListener {
    void placementReady(TRPlacement placement);

    void placementUnavailable(String placementId);
}