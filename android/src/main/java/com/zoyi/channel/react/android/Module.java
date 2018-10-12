package com.zoyi.channel.react.android;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.*;
import com.zoyi.channel.plugin.android.*;
import com.zoyi.channel.plugin.android.global.*;
import com.zoyi.channel.plugin.android.model.entity.*;

import com.facebook.react.bridge.ReadableMap;
import com.zoyi.channel.plugin.android.model.etc.PushEvent;

import java.util.HashMap;
import java.util.Map;

public class Module extends ReactContextBaseJavaModule implements ChannelPluginListener {

  private boolean debug = false;
  private boolean handleChatLink = false;

  private ReactContext reactContext;

  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "ChannelIO";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    return super.getConstants();
  }

  @ReactMethod
  public void boot(ReadableMap settings, final Promise promise) {
    String pluginKey = Utils.getString(settings, "pluginKey");
    String userId = Utils.getString(settings, "userId");
    String locale = Utils.getString(settings, "locale");

    Boolean debugMode = Utils.getBoolean(settings, "debugMode");
    Boolean enabledTrackDefaultEvent = Utils.getBoolean(settings, "enabledTrackDefaultEvent");
    Boolean hideDefaultInAppPush = Utils.getBoolean(settings, "hideDefaultInAppPush");

    ReadableMap launcherConfig = Utils.getReadableMap(settings, "launcherConfig");
    ReadableMap profile = Utils.getReadableMap(settings, "profile");

    ChannelPluginSettings channelPluginSettings = new ChannelPluginSettings(pluginKey)
        .setUserId(userId)
        .setLocale(CHLocale.fromString(locale))
        .setDebugMode(debugMode)
        .setEnabledTrackDefaultEvent(enabledTrackDefaultEvent)
        .setHideDefaultInAppPush(hideDefaultInAppPush)
        .setLauncherConfig(ConvertUtils.toLauncherConfig(launcherConfig));

    ChannelIO.boot(channelPluginSettings, ConvertUtils.toProfile(profile), new OnBootListener() {
      @Override
      public void onCompletion(ChannelPluginCompletionStatus status, @Nullable Guest guest) {
        WritableMap result = Arguments.createMap();

        switch (status) {
          case SUCCESS:
            ChannelIO.setChannelPluginListener(Module.this);

            if (guest != null) {
              result.putMap("guest", ConvertUtils.guestToMap(guest));
            } else {
              result.putNull("guest");
            }

            result.putString("status", status.toString());
            promise.resolve(result);
            break;

          default:
            result.putString("status", status.name());
            promise.resolve(result);
            break;
        }
      }
    });
  }

  @ReactMethod
  public void show() {
    ChannelIO.show();
  }

  @ReactMethod
  public void hide() {
    ChannelIO.hide();
  }

  @ReactMethod
  public void shutdown() {
    ChannelIO.shutdown();
  }

  @ReactMethod
  public void open(boolean animated) {
    ChannelIO.open(getCurrentActivity(), animated);
  }

  @ReactMethod
  public void close(boolean animated) {
    ChannelIO.close(animated);
  }

  @ReactMethod
  public void openChat(String chatId, boolean animated) {
    ChannelIO.openChat(getCurrentActivity(), chatId, animated);
  }

  @ReactMethod
  public void initPushToken(String tokenData) {
    PrefSupervisor.setDeviceToken(getCurrentActivity(), tokenData);
  }

  @ReactMethod
  public void handlePushNotification(ReadableMap userInfo) {
    ChannelIO.handlePushNotification(getCurrentActivity());
  }

  @ReactMethod
  public void isChannelPushNotification(ReadableMap userInfo, Promise promise) {
    Map<String, String> message = new HashMap<>();
    ReadableMapKeySetIterator iterator = userInfo.keySetIterator();

    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      message.put(key, userInfo.getString(key));
    }

    if (ChannelIO.isChannelPushNotification(message)) {
      promise.resolve(true);
    } else {
      promise.resolve(false);
    }
  }

  @ReactMethod
  public void track(String name, ReadableMap eventProperty) {
    String pluginKey = null;

    if (PrefSupervisor.getPluginSetting(getCurrentActivity()) != null) {
      pluginKey = PrefSupervisor.getPluginSetting(getCurrentActivity()).getPluginKey();
    }

    ChannelIO.track(getCurrentActivity(), pluginKey, name, ConvertUtils.toHashMap(eventProperty));
  }

  @ReactMethod
  public void setHandleChatLink(boolean handleChatLink) {
    this.handleChatLink = handleChatLink;
  }

  @Override
  public void willShowMessenger() {
    Utils.sendEvent(reactContext, "WillShowMessenger", null);
  }

  @Override
  public void willHideMessenger() {
    Utils.sendEvent(reactContext, "WillHideMessenger", null);
  }

  @Override
  public void onChangeBadge(int count) {
    WritableMap writableMap = Arguments.createMap();
    writableMap.putInt("count", count);

    Utils.sendEvent(reactContext, "OnChangeBadge", writableMap);
  }

  @Override
  public void onReceivePush(PushEvent pushEvent) {
    if (pushEvent != null) {
      WritableMap writableMap = Arguments.createMap();
      writableMap.putString("chatId", pushEvent.getChatId());
      writableMap.putString("senderAvatarUrl", pushEvent.getSenderAvatarUrl());
      writableMap.putString("senderName", pushEvent.getSenderName());
      writableMap.putString("message", pushEvent.getMessage());

      Utils.sendEvent(reactContext, "OnReceivePush", writableMap);
    }
  }

  @Override
  public boolean onClickChatLink(String url) {
    WritableMap writableMap = Arguments.createMap();
    writableMap.putString("url", url);

    Utils.sendEvent(reactContext, "OnClickChatLink", writableMap);
    return handleChatLink;
  }
}
