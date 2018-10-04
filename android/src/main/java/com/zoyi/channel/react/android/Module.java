package com.zoyi.channel.react.android;

import com.facebook.react.bridge.*;
import com.zoyi.channel.plugin.android.*;
import com.facebook.react.bridge.ReadableMap;

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
}
