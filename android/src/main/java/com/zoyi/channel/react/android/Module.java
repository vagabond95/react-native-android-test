package com.zoyi.channel.react.android;

import com.facebook.react.bridge.*;

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
}
