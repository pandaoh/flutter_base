package com.stpass.imes.plugin.sbt.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.stpass.imes.plugin.sbt.utils.CommonUtils;
import com.stpass.imes.plugin.sbt.utils.MessageContent;
import com.stpass.imes.plugin.sbt.utils.SoundPlayer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.flutter.plugin.common.EventChannel;

abstract public class BaseOperatorService {
   final String TAG = "BaseOperator";
   // flutter消息处理
   public MessageHandler messageHandler;
   // 设备声音播放器
   public SoundPlayer soundPlayer;
   public static final Executor EXEC = new ThreadPoolExecutor(4, 4, 20, TimeUnit.MINUTES,
           new ArrayBlockingQueue<Runnable>(20));

   public void buildMessageHandlerBySetSink(EventChannel.EventSink sink){
      this.messageHandler = new MessageHandler(sink);
   }

   public void setSoundPlayer(SoundPlayer soundPlayer){
      this.soundPlayer = soundPlayer;
   }

   private static class MessageHandler extends Handler {
      EventChannel.EventSink sink;
      public MessageHandler(EventChannel.EventSink sink){
         super(Looper.getMainLooper());
         this.sink = sink;
      }
      @Override
      public void handleMessage(Message msg) {
         try {
            sink.success(CommonUtils.getMapByObject(msg.obj));
         } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
         }
      }
   }

   public abstract MessageContent addContent(MessageContent params);

   public void pushMessageAndPlaySound(MessageContent params){
      if(messageHandler == null) {
         Log.d(TAG, "messageHandlerMissing");
         return;
      }

      Message msg = messageHandler.obtainMessage();
      msg.obj = addContent(params);
      messageHandler.sendMessage(msg);

      if(soundPlayer == null) {
         Log.d(TAG, "soundPlayerMissing");
         return;
      }

      switch (params.messageType) {
         case "SUCCESS":
            soundPlayer.playSuccess();
            break;
         case "ERROR":
            soundPlayer.playError();
            break;
         default:
            soundPlayer.playWarning();
      }
   }
}
