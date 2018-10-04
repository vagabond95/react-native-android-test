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
  public void boot(ReadableMap pluginSettingsJs) {
    String pluginKey = pluginSettingsJs.getString("pluginKey");
    String userId = pluginSettingsJs.getString("userId");
    String locale = pluginSettingsJs.getString("locale");
    Boolean debugMode = pluginSettingsJs.getString("debugMode");
    Boolean enabledTrackDefaultEvent = pluginSettingsJs.getString("enabledTrackDefaultEvent");
    Boolean hideDefaultInAppPush = pluginSettingsJs.getString("hideDefaultInAppPush");

    ChannelPluginSettings channelPluginSettings = ChannelPluginSettings.create(pluginKey)
        .setUserId(userId)
        .setLocale(Locale.fromString(locale))
        .setDebugMode(debugMode)
        .setEnabledTrackDefaultEvent(enabledTrackDefaultEvent)
        .setHideDefaultInAppPush(hideDefaultInAppPush);

    ChannelIO.boot(channelPluginSettings);
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
