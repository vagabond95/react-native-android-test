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
        writableMap.putMap((String) pair.getKey(), Utils.toWritableMap((Map<String, Object>) value));
      } else if (value.getClass() != null && value.getClass().isArray()) {
        writableMap.putArray((String) pair.getKey(), Utils.toWritableArray((Object[]) value));
      }

      iterator.remove();
    }

    return writableMap;
  }

  public static Map<String, Object> toMap(ReadableMap readableMap) {
    Map<String, Object> hashMap = new HashMap<>();

    if (readableMap == null) {
      return hashMap;
    }

    ReadableMapKeySetIterator iterator = readableMap.keySetIterator();

    while (iterator.hasNextKey()) {
      String key = iterator.nextKey();
      ReadableType type = readableMap.getType(key);

      switch (type) {
        case Boolean:
          hashMap.put(key, readableMap.getBoolean(key));
          break;
        case Array:
          hashMap.put(key, readableMap.getArray(key));
          break;

        case Number:
          Log.e("test", "test value : " + readableMap.getString(key));
          break;

        case String:
          hashMap.put(key, readableMap.getString(key));
          break;

        case Map:
          hashMap.put(key, readableMap.getMap(key));
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
      guestMap.putMap("profile", Utils.toWritableMap(profile));
    }

    guestMap.putInt("alert", guest.getAlert());
    guestMap.putString("mobileNumber", guest.getMobileNumber());
    guestMap.putBoolean("named", guest.isNamed());

    return guestMap;
  }

  public static Profile mapToProfile(ReadableMap profileMap) {
    if (profileMap != null) {
      Profile profile = Profile.create()
          .setName(profileMap.getString("name"))
          .setEmail(profileMap.getString("email"))
          .setMobileNumber(profileMap.getString("mobileNumber"))
          .setAvatarUrl(profileMap.getString("avatarUrl"));

      Iterator propertyIterator = Utils
          .toMap(profileMap.getMap("property"))
          .entrySet()
          .iterator();

      while (propertyIterator.hasNext()) {
        Map.Entry pair = (Map.Entry) propertyIterator.next();
        Object value = pair.getValue();

        profile.setProperty((String) pair.getKey(), value);

        propertyIterator.remove();
      }


      return profile;
    }
    return null;
  }

  public static Boolean getBoolean(ReadableMap settings, String key) {
    if (settings.hasKey(key)) {
      return settings.getBoolean(key);
    }
    return null;
  }

  public static String getString(ReadableMap settings, String key) {
    if (settings.hasKey(key)) {
      return settings.getString(key);
    }
    return null;
  }

  public static ReadableMap getMap(ReadableMap settings, String key) {
    if (settings.hasKey(key)) {
      return settings.getMap(key);
    }
    return null;
  }

}
