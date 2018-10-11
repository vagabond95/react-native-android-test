package com.zoyi.channel.react.android;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.zoyi.channel.plugin.android.model.entity.Guest;
import com.zoyi.channel.plugin.android.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mika on 2018. 9. 18..
 */

public class Utils {

  public static Double getDouble(ReadableMap readableMap, String key) {
    if (readableMap.hasKey(key)) {
      return readableMap.getDouble(key);
    }
    return 0.0;
  }

  public static Float getFloat(ReadableMap readableMap, String key) {
    if (readableMap.hasKey(key)) {
      return getDouble(readableMap, key).floatValue();
    }
    return 0f;
  }

  public static Boolean getBoolean(ReadableMap readableMap, String key) {
    if (readableMap.hasKey(key)) {
      return readableMap.getBoolean(key);
    }
    return null;
  }

  public static String getString(ReadableMap readableMap, String key) {
    if (readableMap.hasKey(key)) {
      return readableMap.getString(key);
    }
    return null;
  }

  public static ReadableMap getReadableMap(ReadableMap readableMap, String key) {
    if (readableMap.hasKey(key)) {
      return readableMap.getMap(key);
    }
    return null;
  }

}
