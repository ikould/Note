package com.ikould.frame.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikould.frame.utils.KeyBoardUtils;

import java.util.List;


/**
 * 基础Fragment
 * <p>
 * Created by ikould on 2017/5/31.
 */
public abstract class BaseFragment extends Fragment {

    protected View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onBaseCreateView(inflater, container, savedInstanceState);
        return mContentView;
    }

    protected abstract void onBaseCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 获取ContentView
     *
     * @return
     */
    protected void setBaseContentView(@LayoutRes int layoutId) {
        mContentView = LayoutInflater.from(getActivity()).inflate(layoutId, null);
    }


    /**
     * 获取ContentView
     *
     * @return
     */
    protected void setBaseContentView(View view) {
        mContentView = view;
    }

    /**
     * 替换该Fragment内部的layout显示为fragment
     */
    protected void replaceChildFragment(int layoutId, Fragment fragment, boolean isDoAnim, int... anim) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if (isDoAnim) {
            fragmentTransaction.setCustomAnimations(anim[0], anim[1]);
        }
        fragmentTransaction.replace(layoutId, fragment);
        KeyBoardUtils.closeKeyboard(getActivity());
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 更换Fragment
     */
    protected void replaceFragment(int id, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (tag == null) {
            fragmentTransaction.replace(id, fragment);
        } else {
            fragmentTransaction.replace(id, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
        }
        KeyBoardUtils.closeKeyboard(getActivity());
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 返回最初的fragment页
     */
    public void goToFirstFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
    }

    /**
     * 弹出最上层fragment页
     */
    public void backFragment() {
        Log.i("fuck", "backFragment: " + getActivity().getSupportFragmentManager().getBackStackEntryCount());
        getActivity().getSupportFragmentManager().popBackStackImmediate();

    }

    public void backFragment(String tag) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.hide(this)
                .show(getActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag(tag))
                .commit();
    }

    public void backFragment(int number) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < number; i++) {
            supportFragmentManager.popBackStackImmediate();
        }
    }

    /**
     * 判断当前fragment是否存活
     *
     * @param fragment 传入fragment (基类中已经将this保存为self 例：isAlive(self);)
     * @return
     */
    public boolean isAlive(Fragment fragment) {
        try {
            FragmentManager sFm = getActivity().getSupportFragmentManager();
            List<Fragment> fragments = sFm.getFragments();
            return fragments.contains(fragment);
        } catch (NullPointerException e) {
            return false;
        }
    }

}
