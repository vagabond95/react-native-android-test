package com.zoyi.channel.react.android;

import android.util.Log;

import com.facebook.react.bridge.*;
import com.zoyi.channel.plugin.android.*;
import com.facebook.react.bridge.ReadableMap;

import java.util.HashMap;
import java.util.Map;

public class Module extends ReactContextBaseJavaModule {

  private boolean debug = false;

  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "ChannelIO";
  }

  @ReactMethod
  public void getDeviceId(Callback callback) {
    callback.invoke(Utils.getWId());
  }

  @ReactMethod
  public void boot(ReadableMap settings) {
    String pluginKey = getString(settings, "pluginKey");
    String userId = getString(settings, "userId");
    String locale = getString(settings, "locale");
    Boolean debugMode = getBoolean(settings, "debugMode");
    Boolean enabledTrackDefaultEvent = getBoolean(settings, "enabledTrackDefaultEvent");
    Boolean hideDefaultInAppPush = getBoolean(settings, "hideDefaultInAppPush");

    ChannelPluginSettings channelPluginSettings = new ChannelPluginSettings(pluginKey)
        .setUserId(userId)
        .setLocale(CHLocale.fromString(locale))
        .setDebugMode(debugMode)
        .setEnabledTrackDefaultEvent(enabledTrackDefaultEvent)
        .setHideDefaultInAppPush(hideDefaultInAppPush);

    ChannelIO.boot(channelPluginSettings);
  }

  private Boolean getBoolean(ReadableMap settings, String key) {
    if (settings.hasKey(key)) {
      return settings.getBoolean(key);
    }
    return null;
  }

  private String getString(ReadableMap settings, String key) {
    if (settings.hasKey(key)) {
      return settings.getString(key);
    }
    return null;
  }

  @ReactMethod
  public void show() {
    ChannelIO.show();
  }

  @ReactMethod
  public void hide() {
    ChannelIO.show();
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
  public void isChannelPushNotification(ReadableMap userInfo) {
    
  }

  @ReactMethod
  public void track(String name, ReadableMap eventProperty) {
    String pluginKey = null;

    if (PrefSupervisor.getPluginSetting != null) {
      pluginKey = PrefSupervisor.getPluginSetting.getPluginKey();
    }

    Map<String, Object> eventMap = new HashMap<>();
    ReadableMapKeySetIterator iterator = eventProperty.keySetIterator();

    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      ReadableType type = eventProperty.getType(key);

      switch (type) {
        case Boolean:
          eventMap.put(key, eventProperty.getBoolean(key));
          break;
        case Array:
          eventMap.put(key, eventProperty.getArray(key));
          break;

        case Number:
          Log.e("test","test value : " + eventProperty.getString(key));
          break;

        case String:
          eventMap.put(key, eventProperty.getString(key));
          break;

        case Map:
          eventMap.put(key, eventProperty.getMap(key));
          break;

        default:
          break;
      }
    }

    ChannelIO.track(getCurrentActivity(), pluginKey, name, eventMap);
  }
}
