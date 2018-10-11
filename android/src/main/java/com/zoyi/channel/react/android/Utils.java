package com.zoyi.channel.react.android;

import android.annotation.SuppressLint;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.zoyi.channel.plugin.android.model.entity.Guest;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mika on 2018. 9. 18..
 */

public class Utils {

  public static WritableArray toWritableArray(Object[] array) {
    WritableArray writableArray = Arguments.createArray();

    for (int i = 0; i < array.length; i++) {
      Object value = array[i];

      if (value == null) {
        writableArray.pushNull();
      }
      if (value instanceof Boolean) {
        writableArray.pushBoolean((Boolean) value);
      }
      if (value instanceof Double) {
        writableArray.pushDouble((Double) value);
      }
      if (value instanceof Integer) {
        writableArray.pushInt((Integer) value);
      }
      if (value instanceof String) {
        writableArray.pushString((String) value);
      }
      if (value instanceof Map) {
        writableArray.pushMap(Utils.toWritableMap((Map<String, Object>) value));
      }
      if (value.getClass().isArray()) {
        writableArray.pushArray(Utils.toWritableArray((Object[]) value));
      }
    }

    return writableArray;
  }

  public static WritableMap toWritableMap(Map<String, Object> map) {
    WritableMap writableMap = Arguments.createMap();
    Iterator iterator = map.entrySet().iterator();

    while (iterator.hasNext()) {
      Map.Entry pair = (Map.Entry)iterator.next();
      Object value = pair.getValue();

      if (value == null) {
        writableMap.putNull((String) pair.getKey());
      } else if (value instanceof Boolean) {
        writableMap.putBoolean((String) pair.getKey(), (Boolean) value);
      } else if (value instanceof Double) {
        writableMap.putDouble((String) pair.getKey(), (Double) value);
      } else if (value instanceof Integer) {
        writableMap.putInt((String) pair.getKey(), (Integer) value);
      } else if (value instanceof String) {
        writableMap.putString((String) pair.getKey(), (String) value);
      } else if (value instanceof Map) {
        writableMap.putMap((String) pair.getKey(), Utils.toWritableMap((Map<String, Object>) value));
      } else if (value.getClass() != null && value.getClass().isArray()) {
        writableMap.putArray((String) pair.getKey(), Utils.toWritableArray((Object[]) value));
      }

      iterator.remove();
    }

    return writableMap;
  }

  public static WritableMap parseGuest(Guest guest) {
    WritableMap guestMap = Arguments.createMap();

    Map<String, Object> profile = guest.getProfile();
    if (profile != null) {
      guestMap.putMap("profile", Utils.toWritableMap(profile));
    }

    guestMap.putInt("alert", guest.getAlert());
    guestMap.putString("mobileNumber", guest.getMobileNumber());
    guestMap.putBoolean("named", guest.isNamed());

    return guestMap;
  }

}
