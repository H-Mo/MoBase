package moe.div.mobasetest;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import moe.div.mobase.adapter.MoBaseRecyclerAdapter;

/**
 * @author 涂山墨墨
 * @time 20/3/22  14:27
 * @desc
 */
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
