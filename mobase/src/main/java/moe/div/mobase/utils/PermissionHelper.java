package moe.div.mobase.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * 运行时权限申请工具类，替代已废弃的 RxPermissions
 * 采用无界面 Headless Fragment + Activity Result API (registerForActivityResult) 实现
 * 支持链式/回调式调用，无需在 Activity/Fragment 中重写 onRequestPermissionsResult
 *
 * @author 自动生成
 */
public class PermissionHelper {

    /**
     * 权限请求结果回调接口
     */
    public interface PermissionCallback {
        /**
         * 权限请求结果
         *
         * @param allGranted 是否已经授予了全部请求的权限
         */
        void onResult(boolean allGranted);
    }

    /**
     * 检查是否已经拥有所有指定的权限
     *
     * @param context     Context
     * @param permissions 权限列表
     * @return true 表示全部已授权，false 表示存在未授权权限
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context == null || permissions == null) {
            return false;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 在 FragmentActivity 中申请权限
     *
     * @param activity    FragmentActivity
     * @param permissions 需要申请的权限数组
     * @param callback    回调函数
     */
    public static void requestPermissions(FragmentActivity activity, String[] permissions, PermissionCallback callback) {
        if (hasPermissions(activity, permissions)) {
            if (callback != null) {
                callback.onResult(true);
            }
            return;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        PermissionFragment fragment = PermissionFragment.newInstance(permissions, callback);
        fragmentManager.beginTransaction()
                .add(fragment, "PermissionHelperFragment")
                .commitAllowingStateLoss();
    }

    /**
     * 在 Fragment 中申请权限
     *
     * @param fragment    Fragment
     * @param permissions 需要申请的权限数组
     * @param callback    回调函数
     */
    public static void requestPermissions(Fragment fragment, String[] permissions, PermissionCallback callback) {
        if (fragment == null) {
            return;
        }
        FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            requestPermissions(activity, permissions, callback);
        }
    }

    /**
     * 内部用于申请权限的 Headless Fragment
     */
    public static class PermissionFragment extends Fragment {
        private String[] permissions;
        private PermissionCallback callback;

        public static PermissionFragment newInstance(String[] permissions, PermissionCallback callback) {
            PermissionFragment fragment = new PermissionFragment();
            fragment.permissions = permissions;
            fragment.callback = callback;
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean isAllGranted = true;
                    for (java.util.Map.Entry<String, Boolean> entry : result.entrySet()) {
                        if (!entry.getValue()) {
                            // 特殊处理 Android 14+ 部分媒体权限逻辑
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                String key = entry.getKey();
                                // 如果申请的是视频/图片/音频权限但被拒了，检查是否授予了“部分选择”权限
                                if (Manifest.permission.READ_MEDIA_VIDEO.equals(key) ||
                                    Manifest.permission.READ_MEDIA_IMAGES.equals(key) ||
                                    Manifest.permission.READ_MEDIA_AUDIO.equals(key)) {
                                    if (Boolean.TRUE.equals(result.get(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED))) {
                                        // 授予了部分权限，在逻辑上视为成功，允许应用继续运行
                                        continue;
                                    }
                                }
                            }
                            isAllGranted = false;
                            break;
                        }
                    }
                    if (callback != null) {
                        callback.onResult(isAllGranted);
                    }
                    // 申请结束，从 FragmentManager 中安全地移除当前 Fragment
                    getParentFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
                }
            );

            if (savedInstanceState == null) {
                if (permissions != null) {
                    requestPermissionLauncher.launch(permissions);
                }
            } else {
                // 如果是发生配置改变（如旋转屏幕）导致销毁重建，由于 callback 回调已丢失，直接安全地移除自身，避免残留
                getParentFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
            }
        }
    }
}
