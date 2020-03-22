# MoBase

提供了集成的类供以继承。

### Activity

```java
public class TestActivity extends MoBaseActivity {

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
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
}

```


### Fragment

```java
public class TestFragment extends MoBaseFragment {

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
    protected void onBindData(TestHolder holder, int position) {
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


