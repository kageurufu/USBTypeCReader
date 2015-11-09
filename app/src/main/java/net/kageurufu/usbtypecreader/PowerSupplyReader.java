package net.kageurufu.usbtypecreader;

import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frank on 11/9/15.
 * <p/>
 * <p/>
 * /sys/class/power_supply/usb/uevent
 * POWER_SUPPLY_NAME=usb
 * POWER_SUPPLY_PRESENT=1
 * POWER_SUPPLY_ONLINE=1
 * POWER_SUPPLY_VOLTAGE_MAX=0
 * POWER_SUPPLY_CURRENT_MAX=500000
 * POWER_SUPPLY_TYPE=USB
 * POWER_SUPPLY_SCOPE=Unknown
 * POWER_SUPPLY_VOLTAGE_NOW=-19
 * POWER_SUPPLY_HEALTH=Good
 * <p/>
 * /sys/class/power_supply/usb-parallel/uevent
 * POWER_SUPPLY_NAME=usb-parallel
 * POWER_SUPPLY_CHARGING_ENABLED=1
 * POWER_SUPPLY_STATUS=Discharging
 * POWER_SUPPLY_PRESENT=1
 * POWER_SUPPLY_CURRENT_MAX=2000
 * POWER_SUPPLY_VOLTAGE_MAX=4450
 * POWER_SUPPLY_CONSTANT_CHARGE_CURRENT_MAX=1000000
 */
public class PowerSupplyReader {
    private final static String TAG = "USBC.PowerSupplyReader";
    private final static String DEFAULT_PATH = "/sys/class/power_supply/usb/";
    private String mDevicePowerSupplyPath;

    public static final Map<String, String> devicePowerSupplyPaths = new HashMap<String, String>() {
        {
            /*
             * TODO: Populate this with {PRODUCT: path} mappings to a proper power_supply readout
             * put("angler", "/sys/class/power_supply/usb/");
             */
        }
    };

    public PowerSupplyReader() throws UnknownDevicePathException {
        if (devicePowerSupplyPaths.containsKey(Build.PRODUCT)) {
            mDevicePowerSupplyPath = devicePowerSupplyPaths.get(Build.PRODUCT);
        } else {
            mDevicePowerSupplyPath = DEFAULT_PATH;
        }

        Log.d(TAG, "Got device ID " + Build.PRODUCT);
        Log.d(TAG, "Using path " + mDevicePowerSupplyPath);
    }

    public PowerSupplyStats readDevice() {
        return new PowerSupplyStats(readSysINode());
    }
    public String readSysINode() {
        File sysFile = new File(mDevicePowerSupplyPath, "uevent");
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(sysFile));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

            br.close();
        } catch (IOException e) {
            //TODO: Add error handling
        }

        return text.toString();
    }

    public static String getErrorText() {
        StringBuilder sb = new StringBuilder();

        sb.append("Device details for " + Build.DEVICE).append('\n').append('\n');
        getFileListing(sb, new File("/sys/class/power_supply/"));

        return sb.toString();
    }

    private static void getFileListing(StringBuilder sb, File path) {
        try {
            for (File file : path.listFiles()) {
                sb.append(file.getAbsolutePath()).append('\n');
            }
        } catch (Exception ex) {
            sb.append("Failed to get " + path.getAbsolutePath()).append('\n');
        }
        sb.append('\n');
    }

    class UnknownDevicePathException extends Exception {
    }
}
