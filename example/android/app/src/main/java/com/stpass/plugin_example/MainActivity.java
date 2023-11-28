package com.stpass.plugin_example;

import io.flutter.embedding.android.FlutterActivity;
import android.view.KeyEvent;

import android.util.Log;

import androidx.annotation.NonNull;
import android.os.Handler;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;
import android.os.Message;

import java.util.Map;
import java.util.HashMap;

import java.lang.IllegalAccessException;
import java.lang.reflect.Field;

class BuildType {
    String type;
    String messageType;
    // String message;
    // Boolean loopFlag;
    // Boolean isOpenRfidModule;
    // Boolean isInitialization;
    // int rfidPower;
    // Boolean isBarcodeOpen;
    int data;
    String fun;
    // Object error;
    // Boolean loopBar;
}

public class MainActivity extends FlutterActivity {
    public static final String TAG = "cwrfid.activity";
    public static final String STREAM = "com.stpass.cwrfid/keycode";
    public EventChannel.EventSink sink = null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                System.out.println(getObjectToMap(msg.obj));
                sink.success(getObjectToMap(msg.obj));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

        }
    };

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        Log.d(TAG, "============================================================");
        Log.d(TAG, "==========================configureFlutterEngine==================================");
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), STREAM)
                .setStreamHandler(new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object args, EventChannel.EventSink events) {
                        sink = events;
                    }

                    @Override
                    public void onCancel(Object args) {
                        sink = null;
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "============================================================");
        Log.d(TAG, "onKeyDown: " + keyCode);
        BuildType params = new BuildType();
        params.fun = "onKeyDown";
        params.type = "ONKEY";
        params.messageType = "SUCCESS";
        params.data = keyCode;
        Message msg = handler.obtainMessage();
        msg.obj = params;
        handler.sendMessage(msg);
        return super.onKeyDown(keyCode, event);
    }

    public static Map<String, Object> getObjectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if (value == null) {
                value = "";
            }
            if (fieldName != "this$0") {
                map.put(fieldName, value);
            }
        }
        return map;
    }

}
