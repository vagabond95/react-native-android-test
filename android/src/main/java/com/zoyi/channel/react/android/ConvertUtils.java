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
 * Created by jerry on 2018. 10. 11..
 */

public class ConvertUtils {
  public static WritableArray toWritableArray(Object[] array) {
    WritableArray writableArray = Arguments.createArray();

    if (array == null) {
      return writableArray;
    }

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
        writableArray.pushMap(toWritableMap((Map<String, Object>) value));
      }
      if (value.getClass().isArray()) {
        writableArray.pushArray(toWritableArray((Object[]) value));
      }
    }

    return writableArray;
  }

  public static WritableMap toWritableMap(Map<String, Object> map) {
    WritableMap writableMap = Arguments.createMap();

    if (map == null) {
      return writableMap;
    }

    Iterator iterator = map.entrySet().iterator();

    while (iterator.hasNext()) {
      Map.Entry pair = (Map.Entry) iterator.next();
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
        writableMap.putMap((String) pair.getKey(), toWritableMap((Map<String, Object>) value));
      } else if (value.getClass() != null && value.getClass().isArray()) {
        writableMap.putArray((String) pair.getKey(), toWritableArray((Object[]) value));
      }

      iterator.remove();
    }

    return writableMap;
  }

  public static Map<String, Object> toHashMap(ReadableMap readableMap) {
    Map<String, Object> hashMap = new HashMap<>();

    if (readableMap == null) {
      return hashMap;
    }

    ReadableMapKeySetIterator iterator = readableMap.keySetIterator();

    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      ReadableType type = readableMap.getType(key);

      Log.d("toHashMap", "key : " + key + " type : " + type);
      switch (type) {
        case Boolean:
          hashMap.put(key, Utils.getBoolean(readableMap, key));
          break;
        case Array:
          hashMap.put(key, Utils.getReadableArray(readableMap, key));
          break;

        case Number:
          hashMap.put(key, Utils.getDouble(readableMap, key));
          break;

        case String:
          hashMap.put(key, Utils.getString(readableMap, key));
          break;

        case Map:
          hashMap.put(key, Utils.getReadableMap(readableMap, key));
          break;

        default:
          break;
      }
    }

    return hashMap;
  }

  public static WritableMap guestToMap(Guest guest) {
    WritableMap guestMap = Arguments.createMap();

    if (guest == null) {
      return guestMap;
    }

    Map<String, Object> profile = guest.getProfile();
    if (profile != null) {
      guestMap.putMap("profile", toWritableMap(profile));
    }

    guestMap.putInt("alert", guest.getAlert());
    guestMap.putString("mobileNumber", guest.getMobileNumber());
    guestMap.putBoolean("named", guest.isNamed());

    return guestMap;
  }

  public static LauncherConfig toLauncherConfig(ReadableMap launcherConfigMap) {
    if (launcherConfigMap != null) {
      String positionString = Utils.getString(launcherConfigMap, "position");
      Position launcherPosition;

      if (positionString != null) {
        if ("right".equals(positionString)) {
          launcherPosition = Position.RIGHT;
        } else {
          launcherPosition = Position.LEFT;
        }
      } else {
        launcherPosition = Position.RIGHT;
      }

      return new LauncherConfig(
          launcherPosition,
          Utils.getFloat(launcherConfigMap, "xMargin"),
          Utils.getFloat(launcherConfigMap, "yMargin"));
    }

    return null;
  }

  public static Profile toProfile(ReadableMap profileMap) {
    if (profileMap != null) {
      Profile profile = Profile.create()
          .setName(Utils.getString(profileMap, "name"))
          .setEmail(Utils.getString(profileMap, "email"))
          .setMobileNumber(Utils.getString(profileMap, "mobileNumber"))
          .setAvatarUrl(Utils.getString(profileMap, "avatarUrl"));

      Iterator propertyIterator = ConvertUtils
          .toHashMap(Utils.getReadableMap(profileMap, "property"))
          .entrySet()
          .iterator();

      while (propertyIterator.hasNext()) {
        Map.Entry pair = (Map.Entry) propertyIterator.next();
        Object value = pair.getValue();

        profile.setProperty((String) pair.getKey(), value);

        propertyIterator.remove();
      }

      Log.d("toProfile name", profile.getName());
      Log.d("toProfile email", profile.getEmail());
      Log.d("toProfile mobilenumber", profile.getMobileNumber());
      Log.d("toProfile avatar", profile.getAvatarUrl());
      Log.d("toProfile property", profile.getProperty().toString());
      return profile;
    }
    return null;
  }
}
