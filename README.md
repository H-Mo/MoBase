# MoBase

MoBase 是一个面向传统 View 体系 Android 应用的基础库，提供常用的 Activity / Fragment 生命周期模板、RecyclerView Adapter 基类、运行时权限、应用语言、加载对话框、Toast 和 FlowLayout。

当前版本为 **2.0.0**，已发布至 [Maven Central](https://central.sonatype.com/artifact/moe.div/mobase/2.0.0)。常规项目请通过 Maven Central 引入；需要调试源码或参与开发时，可使用本地 Gradle 模块。

## 功能

- `BaseActivity`：统一的页面初始化流程、主线程消息处理、Toast、对话框和加载状态。
- `BaseFragment`：Fragment 初始化流程，以及与 Activity 一致的提示和加载能力。
- `MoBaseRecyclerAdapter`：列表数据管理、点击/长按回调和可选的底部视图支持。
- `PermissionHelper`：基于 Activity Result API 的运行时权限请求，无需覆写 `onRequestPermissionsResult`。
- `LanguageUtils`：中文、英文与跟随系统语言的应用级语言设置。
- `LoadingDialog`、`FlowLayout`：可复用的加载对话框和流式布局组件。

## 环境要求

| 项目 | 要求 |
| --- | --- |
| 最低 Android 版本 | API 29（Android 10） |
| 编译 SDK | API 37 |
| Android Gradle Plugin | 9.3.2 |
| Gradle Wrapper | 9.5 |
| 构建 JDK | 21 |
| Java 源码兼容性 | Java 8 |

库的命名空间和包名前缀均为 `moe.div.mobase`。

## 接入

### 从 Maven Central 引入（推荐）

确保宿主工程的依赖仓库包含 `google()` 与 `mavenCentral()`。

Groovy DSL（`settings.gradle`）：

```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

Kotlin DSL（`settings.gradle.kts`）：

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

然后在应用模块中添加依赖。

Groovy DSL（`build.gradle`）：

```groovy
dependencies {
    implementation 'moe.div:mobase:2.0.0'
}
```

Kotlin DSL（`build.gradle.kts`）：

```kotlin
dependencies {
    implementation("moe.div:mobase:2.0.0")
}
```

### 作为本地 Gradle 模块引入（源码调试）

将本仓库中的 `mobase` 目录复制或以 Git 子模块方式放入宿主工程后，在宿主工程的 `settings.gradle` 中声明模块。若目录位于其他位置，同时指定模块目录：

Groovy DSL（`settings.gradle`）：

```groovy
include ':mobase'
project(':mobase').projectDir = file('../MoBase/mobase')
```

Kotlin DSL（`settings.gradle.kts`）：

```kotlin
include(":mobase")
project(":mobase").projectDir = file("../MoBase/mobase")
```

然后在应用模块中添加依赖。

Groovy DSL（`build.gradle`）：

```groovy
dependencies {
    implementation project(':mobase')
}
```

Kotlin DSL（`build.gradle.kts`）：

```kotlin
dependencies {
    implementation(project(":mobase"))
}
```

MoBase 已声明 AndroidX AppCompat 和 Material Components 的 API 依赖。宿主工程仍应启用 AndroidX：

```properties
android.useAndroidX=true
```

本仓库内的 `app` 模块是用于源码开发与验证的可运行示例；通过 Maven Central 接入时无需引入该模块。

## 快速使用

### Activity

继承 `BaseActivity` 后，实现 `initView()` 和 `handleMyMessage(Message)`；后者即使暂不处理消息也必须保留。

```java
public final class ExampleActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_example);
    }

    @Override
    protected void initData() {
        // 加载初始数据
    }

    @Override
    protected void initEvent() {
        // 注册界面事件
    }

    @Override
    protected void handleMyMessage(Message message) {
        // 处理通过 mHandler 发送的主线程消息
    }
}
```

`BaseActivity` 的创建顺序为：`onPreCreate()`、`initSaveData()`、`initView()`、`initController()`、`initData()`、`initEvent()`。可按需覆写其中的可选方法。

### Fragment

```java
public final class ExampleFragment extends BaseFragment {
    @Override
    protected View initView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    protected void initData() {
        // 加载数据
    }
}
```

`initView()` 必须返回非空的视图；`initData()` 和 `initEvent()` 会在 `onViewCreated()` 后调用。

### RecyclerView Adapter

`MoBaseRecyclerAdapter` 负责基础的点击与长按处理。覆写 `onBindViewHolder()` 时应先调用父类，避免丢失这些行为。

```java
public final class ExampleAdapter
        extends MoBaseRecyclerAdapter<String, ExampleAdapter.Holder> {

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_example, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.textView.setText(getItem(position));
    }

    static final class Holder extends RecyclerView.ViewHolder {
        final TextView textView;

        Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }
}
```

使用 `setData()` 设置数据、`addData()` 追加数据；使用 `setOnItemClickListener()` 和 `setOnItemLongClickListener()` 注册回调。调用 `setHasFooterView(true)` 可启用底部视图类型，使用 `isFooterView(position)` 在 `onCreateViewHolder()` 中区分普通项与底部项。

### 运行时权限

```java
String[] permissions = {Manifest.permission.CAMERA};
PermissionHelper.requestPermissions(this, permissions, allGranted -> {
    if (allGranted) {
        // 已获取全部权限
    }
});
```

可在 `FragmentActivity` 或 `Fragment` 中调用。调用前可使用 `PermissionHelper.hasPermissions(context, permissions)` 检查权限状态。

### 应用语言

```java
LanguageUtils.setLanguage(this, LanguageUtils.LANG_EN);
recreate();
```

支持 `LANG_ZH`、`LANG_EN` 和 `LANG_DEFAULT`。继承 `BaseActivity` 时，库会在创建上下文与页面时应用已保存的语言设置。

### 加载提示与流式布局

```java
LoadingDialog dialog = new LoadingDialog(this)
        .setMessage("正在加载");
dialog.show();
// 完成后调用 dialog.dismiss();
```

`BaseActivity` 和 `BaseFragment` 也提供 `showProgressDialog()`、`hideProgressDialog()`、`showToast()`、`showMoSucceedToast()` 与 `showMoErrorToast()` 等便捷方法。

在 XML 中使用流式布局：

```xml
<moe.div.mobase.weiget.FlowLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <!-- 支持带 margin 的子视图，并按可用宽度自动换行 -->

</moe.div.mobase.weiget.FlowLayout>
```

## 从 1.x 迁移到 2.0.0

- 将 `MoBaseActivity` 替换为 `BaseActivity`，将 `MoBaseFragment` 替换为 `BaseFragment`。
- `BaseActivity.initView(@Nullable Bundle)` 改为无参 `initView()`；如需恢复状态，请覆写 `initSaveData(Bundle)`。
- `BaseActivity` 需要实现 `handleMyMessage(Message)`。
- `MoBaseRecyclerAdapter` 不再提供 `onBindData()`。请覆写 `onBindViewHolder()`，并在其中先调用 `super.onBindViewHolder(holder, position)`。
- 原有位图提示资源已迁移为 XML drawable；请使用库提供的 `showMoSucceedToast()` 和 `showMoErrorToast()`，而不是依赖已删除的 mipmap 资源。

## 构建与验证

在仓库根目录执行：

```bash
bash gradlew :mobase:assembleDebug :app:assembleDebug
```

构建产物位于各模块的 `build/outputs/` 目录。`app` 是演示应用，`mobase` 是要被宿主工程依赖的库模块。

## 2.0.0 更新摘要

- 升级至 AndroidX、AGP 9.3.2 与 Gradle 9.5。
- 最低支持版本提升至 Android 10（API 29）。
- 引入 `BaseActivity`、`BaseFragment`、`PermissionHelper` 与 `LanguageUtils`。
- 更新加载与提示资源，并提供英文资源值。
- 发布至 Maven Central，依赖坐标为 `moe.div:mobase:2.0.0`。

## 贡献

欢迎通过 [Issues](https://github.com/H-Mo/MoBase/issues) 报告问题或提出功能建议；提交代码前请确保构建命令执行成功，并通过 [Pull Requests](https://github.com/H-Mo/MoBase/pulls) 提交清晰、聚焦的改动。

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 授权。
