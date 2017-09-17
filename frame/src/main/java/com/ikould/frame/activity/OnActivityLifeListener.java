package com.ikould.frame.activity;

/**
 * describe
 * Created by liudong on 2017/9/7.
 */

public interface OnActivityLifeListener {

    int ON_CREATE  = 0x01;
    int ON_START   = 0x02;
    int ON_RESTART = 0x03;
    int ON_RESUME  = 0x04;
    int ON_PAUSE   = 0x05;
    int ON_STOP    = 0x06;
    int ON_DESTROY = 0x07;

    void onLifeCall(int lifeType);
}
