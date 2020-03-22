package moe.div.mobasetest;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import moe.div.mobase.adapter.MoBaseRecyclerAdapter;

/**
 * @author 林墨
 * @time 20/3/22  14:27
 * @desc
 */
public class TestAdapter extends MoBaseRecyclerAdapter<String, TestAdapter.TestHolder> {


    @Override
    protected void onBindData(TestHolder holder, int position) {

    }

    @NonNull
    @Override
    public TestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public static class TestHolder extends RecyclerView.ViewHolder {

        public TestHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
