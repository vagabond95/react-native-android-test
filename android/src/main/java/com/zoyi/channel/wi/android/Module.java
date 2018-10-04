package com.zoyi.channel.wi.android;

import com.facebook.react.bridge.*;

public class Module extends ReactContextBaseJavaModule {

  private boolean debug = false;

  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "ChannelIOSynergy";
  }

  @ReactMethod
  public void getDeviceId(Callback callback) {
    callback.invoke(Utils.getWId());
  }
}
