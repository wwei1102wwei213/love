package com.yyspbfq.filmplay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class DeviceUuidFactory {


    private static final String PREFS_FILE = "sl_device_id.xml";
    private static final String PREFS_DEVICE_ID = "sl_device_id";
    private static volatile UUID uuid;

    public DeviceUuidFactory(Context context) {
        if (uuid == null) {
            synchronized (DeviceUuidFactory.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context
                            .getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        uuid = UUID.fromString(id);
                    } else {
                        try {
                            final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                            try {
                                final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                final String deviceId = tm.getDeviceId();
                                final String serialId = tm.getSimSerialNumber();
                                if (deviceId!=null&&serialId!=null){
                                    uuid = new UUID(androidId.hashCode(), ((long)deviceId.hashCode() << 32) | serialId.hashCode());
                                } else {
                                    if (!TextUtils.isEmpty(androidId)&&!"9774d56d682e549c".equals(androidId)) {
                                        uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                                    } else {
                                        uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8"))
                                                : UUID.nameUUIDFromBytes(getUniquePsuedoIDShort().getBytes("utf8"));
                                    }
                                }
                            } catch (Exception e){
                                if (!TextUtils.isEmpty(androidId)&&!"9774d56d682e549c".equals(androidId)) {
                                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                                } else {
                                    uuid = UUID.nameUUIDFromBytes(getUniquePsuedoIDShort().getBytes("utf8"));
                                }
                            }
                        } catch (Exception e) {
                            uuid = UUID.nameUUIDFromBytes(getUniquePsuedoIDShort().getBytes());
                        }
                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).apply();
                    }
                }
            }
        }
    }

    public UUID getDeviceUuid() {
        return uuid;
    }

    public String getUuid() {
        return uuid==null?"0":uuid.toString();
    }

    /**
     * Return pseudo unique ID
     * @return ID
     */
    public String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10)
                + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10)
                + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10)
                + (Build.PRODUCT.length() % 10);
        String serial = null;
        try{
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception e) {
            serial = "horoscoper"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    private String getUniquePsuedoIDShort(){
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 + Build.TYPE.length()%10 +
                Build.USER.length()%10 ; //13 digits
        return m_szDevIDShort;
    }


}
