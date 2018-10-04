package com.zoyi.channel.wi.android;

import android.annotation.SuppressLint;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by mika on 2018. 9. 18..
 */

public class Utils {
  @SuppressLint("HardwareIds")
  public static String getWId() {
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      String interfaceName = "wlan0";

      for (NetworkInterface networkInterface : interfaces) {
        if (!networkInterface.getName().equalsIgnoreCase(interfaceName)) {
          continue;
        }
        byte[] mac = networkInterface.getHardwareAddress();
        if (mac == null) {
          return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte aMac : mac) {
          buf.append(String.format("%02X:", aMac));
        }
        if (buf.length() > 0) {
          buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString().trim();
      }
    } catch (Exception ignored) {
    }
    return null;
  }

}
