package com.fuyou.play.widget.pullwidget.elasticity;

import android.view.View;

public interface IElasticity {
    void detach();

    int getCurrentState();

    View getView();

    void setOverScrollStateListener(IElasticityStateListener iElasticityStateListener);

    void setOverScrollUpdateListener(IElasticityUpdateListener iElasticityUpdateListener);
}
