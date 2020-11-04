package io.flutter.plugins;

import io.flutter.plugin.common.PluginRegistry;
import com.nover.flutternativeadmob.FlutterNativeAdmobPlugin;

/**
 * Generated file. Do not edit.
 */
public final class GeneratedPluginRegistrant {
  public static void registerWith(PluginRegistry registry) {
    if (alreadyRegisteredWith(registry)) {
      return;
    }
    FlutterNativeAdmobPlugin.registerWith(registry.registrarFor("com.nover.flutternativeadmob.FlutterNativeAdmobPlugin"));
  }

  private static boolean alreadyRegisteredWith(PluginRegistry registry) {
    final String key = GeneratedPluginRegistrant.class.getCanonicalName();
    if (registry.hasPlugin(key)) {
      return true;
    }
    registry.registrarFor(key);
    return false;
  }
}
