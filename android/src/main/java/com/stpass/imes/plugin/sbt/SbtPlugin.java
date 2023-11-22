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
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


/**
 * CwrfidPlugin
 */
@SuppressWarnings({"SpellCheckingInspection","unused"})
public class SbtPlugin implements FlutterPlugin, MethodCallHandler, StreamHandler {
    static String TAG = "SbtPlugin";

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
        context = flutterPluginBinding.getApplicationContext();
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "com.stpass.cwrfid/plugin");
        channel.setMethodCallHandler(this);
        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "com.stpass.cwrfid/event_channel");
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
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android666 " + android.os.Build.VERSION.RELEASE);
                break;
            case "toast":
                String txt = call.argument("text");
                Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
                break;
            case "eventChannelTest":
            case "setDisabledInitPlay":
                MessageContent params = new MessageContent();
                params.type = "OTHER";
                params.messageType = "SUCCESS";
                params.message = "No setDisabledInitPlay";
                uhfReader.pushMessageAndPlaySound(params);
                // uhfReader.setDisabledInitPlay((Boolean) call.argument("disabledInitPlay"));
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
            case "testSkin":
                testSkin();
                break;
            case "stopInventory":
                uhfReader.stopScan();
                break;
            case "setPower":
                Integer n = call.argument("n");
                uhfReader.setPower(n==null?5:n);
                break;
            case "getPower":
                uhfReader.getPower();
                break;
            case "initBarcode2DWithSoft":
                barcode2D.initBarcode2DWithSoft(context);
                break;
            case "scanBarcode":
                barcode2D.startScan(context);
                break;
            case "barcodeClose":
                barcode2D.stopScan(context);
                barcode2D.close(context);
                break;
            case "initBarcode1DWithSoft":
                barcode1D.initBarcode1DWithSoft(context);
                break;
            case "scan1DBarcode":
                barcode1D.startScan(context);
                break;
            case "barcode1DClose":
                // barcode1D.stopScan(context);
                barcode1D.close(context);
                break;
            case "soundPlayer":
                String soundType = call.argument("soundType");
                soundPlayer(soundType == null ? "ERROR" : soundType);
                break;
            case "scan1DBarcodeStop":
                barcode1D.stopScan(context);
                break;
            case "barcodeStop":
                barcode2D.stopScan(context);
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
        sink = events;
        if(uhfReader == null || barcode1D == null || barcode2D == null)
            Log.d(TAG, String.format("uhfReader=%s,1D=%s,2D=%s",uhfReader,barcode1D,barcode2D));

        uhfReader.buildMessageHandlerBySetSink(sink);
        barcode1D.buildMessageHandlerBySetSink(sink);
        barcode2D.buildMessageHandlerBySetSink(sink);
    }

    @Override
    public void onCancel(Object arguments) {
        sink = null;
    }

    public void testSkin() {
        Log.d(TAG, "taskSkin"+this.sink);
    }

    private void soundPlayer(String soundType) {
        switch (soundType) {
            case "SUCCESS":
                soundPlayer.playSuccess();
                break;
            case "ERROR":
                soundPlayer.playError();
                break;
            case "WARNING":
                soundPlayer.playWarning();
                break;
            case "REGGED":
                soundPlayer.playRegged();
                break;
            case "VIBRATOR":
                soundPlayer.playVibrator();
                break;
            case "VIBRATOR_ERROR":
                soundPlayer.playFailedVibrator();
                break;
            default:
        }
    }
}
