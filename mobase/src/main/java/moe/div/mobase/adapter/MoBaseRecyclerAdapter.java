package moe.div.mobase.adapter;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 林墨
 * @time 17/2/21  9:32
 * @desc RecyclerView 用的适配器基类
 */
public abstract class MoBaseRecyclerAdapter<T,VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>{

    /**
     * 内容视图TAG
     */
    public static final int TYPE_COMMON_VIEW = 0x2001;

    /**
     * 底部视图TAG
     */
    public static final int TYPE_FOOTER_VIEW = 0x2002;

    /**
     * 数据集合
     */
    protected List<T> mList;

    /**
     * 子项点击监听器
     */
    protected AdapterView.OnItemClickListener mClickListener;

    /**
     * 子项长按监听器
     */
    protected AdapterView.OnItemLongClickListener mLongClickListener;

    /**
     * 是否没有更多数据了
     */
    protected boolean isNoMoreData = false;

    /**
     * 是否开启加载更多
     */
    protected boolean mOpenLoadMore = true;

    /**
     * 是否存在底部视图
     */
    protected boolean hasFooterView = false;

    /**
     * 设置是否存在底部视图
     * @param has       是否存在
     */
    public void setHasFooterView(boolean has){
        hasFooterView = has;
    }

    /**
     * 覆写的获取子项视图类型方法
     * @param position      索引
     * @return  视图类型值
     */
    @Override
    public int getItemViewType(int position) {
        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }
        return TYPE_COMMON_VIEW;
    }

    /**
     * 判断是不是尾部
     * @param position      索引
     * @return  是否是底部
     */
    public boolean isFooterView(int position) {
        return hasFooterView && mOpenLoadMore && position >= getItemCount() - 1;
    }

    /**
     * 设置没有更多了,改变尾部的布局
     * @param isNoMore  是否存在更多内容
     */
    public void setFooterNoMoreData(boolean isNoMore){
        isNoMoreData = isNoMore;
    }

    /**
     * 获取是否存在更多
     * @return  是否存在更多内容
     */
    public boolean isFooterNoMoreData(){
        return isNoMoreData;
    }

    /**
     * 设置数据
     * @param data  数据集合
     */
    public void setData(List<T> data){
        if(mList == null){
            mList = data;
        }else {
            // 清除原来的再添加，避免闪屏
            mList.clear();
            mList.addAll(data);
        }
    }

    /**
     * 追加一个集合的数据
     * @param data  数据集合
     */
    public void addData(List<T> data){
        if(mList == null){
            mList = new ArrayList<T>();
        }
        mList.addAll(data);
    }

    /**
     * 追加单个数据
     * @param data  数据
     */
    public void addData(T data){
        if(mList == null){
            mList = new ArrayList<T>();
        }
        mList.add(data);
    }


    /**
     * 获取数据
     * @return  数据
     */
    public List<T> getData(){
        if(mList == null){
            mList = new ArrayList<T>();
        }
        return mList;
    }

    /**
     * 通过索引删除一个子项
     * @param position  索引
     */
    public void deleteItem(int position){
        mList.remove(position);
    }

    /**
     * 通过对象删除一个Item
     * @param t 对象
     */
    public void deleteItem(T t){
        mList.remove(t);
    }


    /**
     * 获取一个子项对象
     * @param position  索引
     * @return  对象
     */
    public T getItem(int position){
        return mList.get(position);
    }

    /**
     * 设置子项点击事件监听器
     * @param clickListener     点击监听器
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener){
        mClickListener = clickListener;
    }

    /**
     * 设置子项长按事件监听器
     * @param longClickListener     长按监听器
     */
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener longClickListener){
        mLongClickListener = longClickListener;
    }

    /**
     * 覆写onBindViewHolder()方法，设置好点击事件、长按事件
     * @param holder        ViewHolder
     * @param position      索引
     */
    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        // 点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener == null) {
                    return;
                }
                if(isFooterView(position)){
                    // 不处理尾部布局的点击事件
                    return;
                }
                mClickListener.onItemClick(null, holder.itemView, position, getItemId(position));
            }
        });
        // 长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(isFooterView(position)){
                    // 不处理尾部布局的点击事件
                    return false;
                }
                return mLongClickListener != null &&
                        mLongClickListener.onItemLongClick(null,
                                holder.itemView,
                                position,
                                getItemId(position));
            }
        });
        onBindData(holder, position);
    }


    /**
     * 绑定数据内容
     * @param holder        ViewHolder
     * @param position      索引
     */
    protected abstract void onBindData(VH holder, int position);


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size() ;
    }

}
