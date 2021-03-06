package com.example.flutterinnative;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flutterinnative.methodinject.MethodChannelInject;
import com.example.flutterinnative.methodinject.MethodChannelUtil;
import com.example.flutterinnative.methodinject.MethonType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodChannel;

/**
 * @description: 通过 FlutterView/FlutterFragment 引入 Flutter 编写的页面
 * @author: zhukai
 * @date: 2019/5/26 14:27
 */
public class FlutterPageActivity extends AppCompatActivity {

    private FlutterEngine flutterEngine;
    private FlutterView flutterView;

    private static final String CHANNEL_NATIVE = "com.example.flutter/native";
    private static final String CHANNEL_FLUTTER = "com.example.flutter/flutter";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("dk", "onCreate");
        setContentView(R.layout.activity_flutter_page);

        // 创建FlutterEngine对象
        flutterEngine = createFlutterEngine();
        //MethodChannelUtil.register(this, flutterEngine);
        // 通过FlutterView引入Flutter编写的页面
//        flutterView = new FlutterView(this);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        FrameLayout flContainer = findViewById(R.id.fl_container);
//        flContainer.addView(flutterView, lp);
//        flutterView.attachToFlutterEngine(flutterEngine);

        // 通过FlutterFragment引入Flutter编写的页面
        FlutterFragment flutterFragment = FlutterFragment.withCachedEngine("my_engine_id")
                .build();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, flutterFragment)
                .commit();

        MethodChannel nativeChannel = new MethodChannel(flutterEngine.getDartExecutor(), CHANNEL_NATIVE);
        nativeChannel.setMethodCallHandler((methodCall, result) -> {
//            switch (methodCall.method) {
//                case "goBack":
//                    finish();
//                    break;
//                case "goBackWithResult":
//                    // 返回上一页，携带数据
//                    Intent backIntent = new Intent();
//                    backIntent.putExtra("message", (String) methodCall.argument("message"));
//                    setResult(RESULT_OK, backIntent);
//                    finish();
//                    break;
//                case "jumpToNative":
//                    // 跳转原生页面
//                    Intent jumpToNativeIntent = new Intent(this, NativePageActivity.class);
//                    jumpToNativeIntent.putExtra("name", (String) methodCall.argument("name"));
//                    startActivityForResult(jumpToNativeIntent, 0);
//                    break;
//                default:
//                    result.notImplemented();
//                    break;
//            }
            MethodChannelUtil.proxy(this, methodCall, result);
        });
    }

    // 返回上一页
    @MethodChannelInject(methodName = "goBack", paramsKey = {"type"})
    public void goBack(Map<String, Object> map) {
        Log.d("dk", "返回上一页");
        finish();
    }

    // 返回上一页，携带数据
    @MethodChannelInject(methodName = "goBackWithResult", paramsKey = {"message"})
    public void goBackWithResult(Map<String, Object> map) {
        Log.d("dk", "返回上一页，携带数据");
        Intent backIntent = new Intent();
        String msg = (String) map.get("message");
        backIntent.putExtra("message", msg);
        setResult(RESULT_OK, backIntent);
        finish();
    }

    // 跳转原生页面
    @MethodChannelInject(methodName = "jumpToNative", paramsKey = "name")
    public void jumpToNative(Map<String, Object> map) {
        Log.d("dk", "跳转原生页面");
        Intent jumpToNativeIntent = new Intent(this, NativePageActivity.class);
        String name = (String) map.get("name");
        jumpToNativeIntent.putExtra("name", name);
        startActivityForResult(jumpToNativeIntent, 0);
    }

    /**
     * 创建可缓存的FlutterEngine
     *
     * @return
     */
    private FlutterEngine createFlutterEngine() {
        // 实例化FlutterEngine对象
        FlutterEngine flutterEngine = new FlutterEngine(this);
        // 设置初始路由
        flutterEngine.getNavigationChannel().setInitialRoute("route1?{\"name\":\"" + getIntent().getStringExtra("name") + "\"}");
        // 开始执行dart代码来pre-warm FlutterEngine
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        // 缓存FlutterEngine
        FlutterEngineCache.getInstance().put("my_engine_id", flutterEngine);
        return flutterEngine;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data != null) {
                String message = data.getStringExtra("message");
                Map<String, Object> result = new HashMap<>();
                result.put("message", message);
                // 创建MethodChannel
                MethodChannel flutterChannel = new MethodChannel(flutterEngine.getDartExecutor(), CHANNEL_FLUTTER);
                flutterChannel.invokeMethod("onActivityResult", result);
            }
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("message", "empty");
            // 创建MethodChannel
            MethodChannel flutterChannel = new MethodChannel(flutterEngine.getDartExecutor(), CHANNEL_FLUTTER);
            flutterChannel.invokeMethod("onCallback", result);
        }
    }

    @Override
    public void onBackPressed() {
        MethodChannel flutterChannel = new MethodChannel(flutterEngine.getDartExecutor(), CHANNEL_FLUTTER);
        flutterChannel.invokeMethod("goBack", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flutterEngine.getLifecycleChannel().appIsResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        flutterEngine.getLifecycleChannel().appIsInactive();
    }

    @Override
    protected void onStop() {
        super.onStop();
        flutterEngine.getLifecycleChannel().appIsPaused();
    }

    @Override
    protected void onDestroy() {
        if (flutterView != null) {
            flutterView.detachFromFlutterEngine();
        }
        super.onDestroy();
    }
}
