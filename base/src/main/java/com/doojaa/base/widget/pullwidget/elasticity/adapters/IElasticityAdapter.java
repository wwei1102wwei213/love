package com.doojaa.base.widget.pullwidget.elasticity.adapters;

import android.view.View;

public interface IElasticityAdapter {
    View getView();

    boolean isInAbsoluteEnd();

    boolean isInAbsoluteStart();
}
