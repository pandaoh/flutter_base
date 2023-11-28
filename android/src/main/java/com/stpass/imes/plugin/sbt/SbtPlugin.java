package com.stpass.imes.plugin.sbt;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.stpass.imes.plugin.sbt.service.Barcode1DService;
import com.stpass.imes.plugin.sbt.service.Barcode2DService;
import com.stpass.imes.plugin.sbt.service.UHFReaderService;
import com.stpass.imes.plugin.sbt.utils.MessageContent;
import com.stpass.imes.plugin.sbt.utils.SoundPlayer;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


/**
 * CwrfidPlugin
 */
@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class SbtPlugin implements FlutterPlugin, MethodCallHandler, StreamHandler {
    static String TAG = "sbt_plugin";

    private MethodChannel channel;
    private EventChannel eventChannel;
    private Context context;
    public EventChannel.EventSink sink = null;

    UHFReaderService uhfReader;
    Barcode1DService barcode1D;
    Barcode2DService barcode2D;
    SoundPlayer soundPlayer;


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        BinaryMessenger messenger = flutterPluginBinding.getBinaryMessenger();

        context = flutterPluginBinding.getApplicationContext();

        channel = new MethodChannel(messenger, "pda/method");
        channel.setMethodCallHandler(this);

        eventChannel = new EventChannel(messenger, "pda/event");
        eventChannel.setStreamHandler(this);

        soundPlayer = SoundPlayer.getInstance().init(context);
        uhfReader = UHFReaderService.getInstance();
        uhfReader.setSoundPlayer(soundPlayer);

        barcode1D = Barcode1DService.getInstance();
        barcode1D.setSoundPlayer(soundPlayer);

        barcode2D = Barcode2DService.getInstance();
        barcode2D.setSoundPlayer(soundPlayer);

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        Log.d(TAG, "===============================[SbtPlugin.onMethodCall：" + call.method + "]===================================");

        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android11 " + android.os.Build.VERSION.RELEASE);
                break;
            case "toast":
                String txt = call.argument("text");
                Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
                break;
            case "initRfid":
                uhfReader.init(context);
                break;
            case "rfidFree":
                uhfReader.free();
                break;
            case "startInventoryTag":
                uhfReader.startScan();
                break;
            case "stopInventory":
                uhfReader.stopScan();
                break;
            case "setPower":
                Integer n = call.argument("n");
                uhfReader.setPower(n == null ? 5 : n);
                break;
            case "getPower":
                uhfReader.getPower();
                break;
            case "initBarcode1DWithSoft":
            case "initBarcode2DWithSoft":
                barcode1D.initBarcode1DWithSoft(context);
                break;
            case "scan1DBarcode":
            case "scanBarcode":
                barcode1D.startScan(context);
                break;
            case "scan1DBarcodeStop":
            case "barcodeStop":
                barcode1D.stopScan(context);
                break;
            case "barcode1DClose":
            case "barcodeClose":
                barcode1D.stopScan(context);
                barcode1D.close(context);
                break;
            case "soundPlayer":
                String soundType = call.argument("soundType");
                soundPlayer.play(soundType == null ? "ERROR" : soundType);
                break;
            case "testSkin":
                break;
            case "eventChannelTest":
                break;
            case "setDisabledInitPlay":
                break;
            default:
                result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        eventChannel.setStreamHandler(null);
        if (uhfReader != null) uhfReader.free();
        if (barcode1D != null) barcode1D.close(context);
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        Log.d(TAG, "===============================[SbtPlugin.onListen()]===================================");
        sink = events;
        if (uhfReader == null || barcode1D == null || barcode2D == null)
            Log.d(TAG, String.format("uhfReader=%s,1D=%s,2D=%s", uhfReader, barcode1D, barcode2D));

        uhfReader.buildMessageHandlerBySetSink(sink);
        barcode1D.buildMessageHandlerBySetSink(sink);
        barcode2D.buildMessageHandlerBySetSink(sink);
    }

    @Override
    public void onCancel(Object arguments) {
        sink = null;
    }

}
