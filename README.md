# MoBase

Android 基础库，提供 Activity / Fragment 生命周期模板、RecyclerView Adapter 基类、运行时权限、应用语言、加载对话框、Toast 与 FlowLayout。

2.0.0 的最低支持版本为 Android 10（API 29），包名为 `moe.div.mobase`。

### Activity

```java
public class TestActivity extends BaseActivity {

    @Override
    protected void initView() {
        // 设置布局已经找控件
    }

    @Override
    protected void initData() {
        // 设置数据
    }

    @Override
    protected void initEvent() {
        // 设置事件
    }

    @Override
    protected void handleMyMessage(Message msg) {
        // 处理主线程消息
    }
}

```


### Fragment

```java
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
```

### RecyclerAdapter

```java
public class TestAdapter extends MoBaseRecyclerAdapter<String, TestAdapter.TestHolder> {

    @NonNull
    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建ViewHolder
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TestHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        // 绑定数据
    }


    /**
     * ViewHolder
     */
    public static class TestHolder extends RecyclerView.ViewHolder {

        public TestHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

}
```

