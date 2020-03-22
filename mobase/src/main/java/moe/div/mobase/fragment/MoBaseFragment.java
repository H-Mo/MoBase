package moe.div.mobase.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import moe.div.mobase.R;
import moe.div.mobase.weiget.LoadingDialog;

/**
 * @author 林墨
 * @time 20/3/8  15:32
 * @desc 基础类界面-封装常用方法
 */
public abstract class MoBaseFragment extends Fragment {

    /**
     * 吐司对象，可以防止吐司不断出现新的
     */
    protected Toast mSucceedToast;

    /**
     * 吐司提示文本控件，可用于自定义吐司内容
     */
    protected TextView mToastHint;

    /**
     * 加载等待对话框对象
     */
    protected LoadingDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return initView(inflater,container,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
    }

    /**
     * 初始化布局
     * @param inflater 				打气筒
     * @param container				父容器
     * @param savedInstanceState	保存的状态实例
     * @return 需要显示的布局View,不能为null
     */
    protected abstract View initView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();


    /**
     * 统一的成功吐司
     * @param text  显示文本
     */
    public void showMoSucceedToast(String text){
        showMoToast(R.mipmap.toast_success, text);
    }

    /**
     * 统一的失败吐司
     * @param text  显示文本
     */
    public void showMoErrorToast(String text){
        showMoToast(R.mipmap.toast_error, text);
    }

    /**
     * 显示吐司
     * @param text  显示文本
     */
    public void showToast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示成功 Toast
     */
    public void showSucceedToast(){
        showSucceedToast(null);
    }

    /**
     * 显示成功 Toast
     * @param hint  显示文本
     */
    public void showSucceedToast(String hint){
        mSucceedToast = Toast.makeText(getActivity().getApplicationContext(), "Normarl toast", Toast.LENGTH_SHORT);
        mSucceedToast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.toast_vote_succeed_layout, null);
        mToastHint = (TextView) view.findViewById(R.id.hint_text);
        mSucceedToast.setView(view);
        if(hint != null){
            mToastHint.setText(hint);
        }
        mSucceedToast.show();
    }

    /**
     * 显示提示吐司
     * @param resId     图片资源ID
     * @param hint      显示文本
     */
    public void showMoToast(@DrawableRes int resId, String hint){
        mSucceedToast = Toast.makeText(getActivity().getApplicationContext(), "Normarl toast", Toast.LENGTH_SHORT);
        mSucceedToast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.toast_info_layout, null);
        ImageView hintImage = (ImageView) view.findViewById(R.id.hint_image);
        TextView hintText = (TextView) view.findViewById(R.id.hint_text);
        mSucceedToast.setView(view);
        hintImage.setImageResource(resId);
        if(hint != null){
            hintText.setText(hint);
        }
        mSucceedToast.show();
    }



    /**
     * 显示等待对话框
     */
    public void showProgressDialog(){
        showProgressDialog(false);
    }

    /**
     * 显示加载等待对话框
     * @param cancelable    是否可以取消
     */
    public void showProgressDialog(boolean cancelable){
        showProgressDialog(false, null);
    }

    /**
     * 显示加载等待对话框
     * @param cancelable        是否可以取消
     * @param cancelListener    取消监听器
     */
    public void showProgressDialog(boolean cancelable, DialogInterface.OnCancelListener cancelListener){
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        // 初始化等待对话框
        mProgressDialog = new LoadingDialog(getActivity());
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.setOnCancelListener(cancelListener);
        mProgressDialog.show();
    }


    /**
     * 隐藏等待对话框
     */
    public void hideProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    /**
     * 弹出二次确认对话框
     * @param title         标题
     * @param message       信息
     * @param confirmCall   确认回调
     * @param cancelCall    取消回调
     */
    public void showTwiceConfirmDialog(String title, String message,
                                       DialogInterface.OnClickListener confirmCall,
                                       DialogInterface.OnClickListener cancelCall){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("确定", confirmCall)
            .setNegativeButton("取消", cancelCall)
            .create();
        alertDialog.show();
    }

    /**
     * 弹出二次确认对话框
     * @param title         标题
     * @param message       信息
     * @param confirmText   确认文本
     * @param cancelText    取消文本
     * @param confirmCall   确认回调
     * @param cancelCall    取消回调
     */
    public void showTwiceConfirmDialog(String title,
                                       String message,
                                       String confirmText,
                                       String cancelText,
                                       DialogInterface.OnClickListener confirmCall,
                                       DialogInterface.OnClickListener cancelCall){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(title != null && !"".equals(title)){
            builder.setTitle(title);
        }
        if(confirmText != null && !"".equals(confirmText)){
            builder.setPositiveButton(confirmText, confirmCall);
        }
        if(cancelText != null && !"".equals(cancelText)){
            builder.setNegativeButton(cancelText, cancelCall);
        }
        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(51, 51, 51));
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(32, 146, 227));
    }


    /**
     * 弹出错误提示对话框
     * @param title			标题
     * @param message		消息
     * @param positive		确认按钮文本
     * @param confirmCall	确认回调事件
     */
    public void showErrorDialog(String title, String message, String positive,
                                DialogInterface.OnClickListener confirmCall){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positive, confirmCall)
            .setCancelable(false)
            .create();
        alertDialog.show();
    }

    /**
     * 弹出错误提示对话框
     * @param message   显示文本
     */
    public void showErrorDialog(String message){
        showErrorDialog("提示", message, "确定", null);
    }

}
