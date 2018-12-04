package com.doojaa.base.widget.pullwidget.elasticity;

public interface ElasticityListenerStubs {

    public static class ElasticityStateListenerStub implements IElasticityStateListener {
        public void onOverScrollStateChange(IElasticity decor, int oldState, int newState) {
        }
    }

    public static class ElasticityUpdateListenerStub implements IElasticityUpdateListener {
        public void onOverScrollUpdate(IElasticity decor, int state, float offset) {
        }
    }
}
