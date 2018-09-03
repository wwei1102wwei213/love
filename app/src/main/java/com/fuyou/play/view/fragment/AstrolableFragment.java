package com.fuyou.play.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fuyou.play.R;
import com.fuyou.play.util.HORUtils.BornAstrolabeView;
import com.fuyou.play.util.HORUtils.SubHelper;
import com.fuyou.play.view.BaseFragment;

/**
 * Created by Administrator on 2018-08-11.
 */

public class AstrolableFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_astrolabe, container, false);
        initStatusBar(findViewById(R.id.status_bar));
        initTitleViews();
        initViews();
        return rootView;
    }

    private void initTitleViews() {
        findViewById(R.id.iv_back_base).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.tv_title_base)).setText(getString(R.string.title_info_astrolabe));
    }

    private BornAstrolabeView bav;
    private void initViews() {
        bav = (BornAstrolabeView)findViewById(R.id.bav);
        bav.reDraw(SubHelper.getInstance().getSubMainData(context),0);
    }

}
