package moe.div.mobasetest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moe.div.mobase.fragment.BaseFragment;

/**
 * @author 涂山墨墨
 * @time 20/3/22  14:26
 * @desc
 */
public class TestFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 设置布局已经找控件，这里必须返回一个视图，不能为null
        return null;
    }

    @Override
    protected void initData() {
        // 设置数据
    }

    @Override
    protected void initEvent() {
        // 设置事件
    }

}
