package net.kageurufu.usbtypecreader;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class PowerSupplyStats {
    private final static String TAG = "USBC.PowerSupplyStats";
    private Map<String, String> mUeventValues;
    private final static String POWER_SUPPLY_NAME = "POWER_SUPPLY_NAME"; //usb-parallel
    private final static String POWER_SUPPLY_CHARGING_ENABLED = "POWER_SUPPLY_CHARGING_ENABLED"; //1
    private final static String POWER_SUPPLY_STATUS = "POWER_SUPPLY_STATUS"; //Not charging
    private final static String POWER_SUPPLY_PRESENT = "POWER_SUPPLY_PRESENT"; //1
    private final static String POWER_SUPPLY_CURRENT_MAX = "POWER_SUPPLY_CURRENT_MAX"; //2000
    private final static String POWER_SUPPLY_VOLTAGE_MAX = "POWER_SUPPLY_VOLTAGE_MAX"; //4450
    private final static String POWER_SUPPLY_CONSTANT_CHARGE_CURRENT_MAX = "POWER_SUPPLY_CONSTANT_CHARGE_CURRENT_MAX"; //1000000

    public PowerSupplyStats(String uevent) {

        mUeventValues = new HashMap<>();

        for (String line : uevent.split("\n")) {
            if (!line.isEmpty() && line.contains("=")) {
                String[] line_data = line.split("=");
                mUeventValues.put(line_data[0], line_data[1]);
            }
        }

        Log.d(TAG, mUeventValues.toString());
    }

    public String getName() {
        if (mUeventValues.containsKey(POWER_SUPPLY_NAME)) {
            return mUeventValues.get(POWER_SUPPLY_NAME);
        }
        return null;
    }

    public String getStatus() {
        if (mUeventValues.containsKey(POWER_SUPPLY_STATUS)) {
            return mUeventValues.get(POWER_SUPPLY_STATUS);
        }
        return null;
    }

    public Boolean isEnabled() {
        if (mUeventValues.containsKey(POWER_SUPPLY_CHARGING_ENABLED)) {
            return mUeventValues.get(POWER_SUPPLY_CHARGING_ENABLED).equals("1");
        }
        return false;
    }

    public Boolean isPresent() {
        if (mUeventValues.containsKey(POWER_SUPPLY_PRESENT)) {
            return mUeventValues.get(POWER_SUPPLY_PRESENT).equals("1");
        }
        return false;
    }


    public Integer getVoltageMax() {
        if (mUeventValues.containsKey(POWER_SUPPLY_VOLTAGE_MAX)) {
            return Integer.parseInt(mUeventValues.get(POWER_SUPPLY_VOLTAGE_MAX));
        }
        return 0;
    }

    public Integer getCurrentMax() {
        if (mUeventValues.containsKey(POWER_SUPPLY_CURRENT_MAX)) {
            return Integer.parseInt(mUeventValues.get(POWER_SUPPLY_CURRENT_MAX));
        }
        return 0;
    }

    public Integer getConstantChargeCurrentMax() {
        if (mUeventValues.containsKey(POWER_SUPPLY_CONSTANT_CHARGE_CURRENT_MAX)) {
            return Integer.parseInt(mUeventValues.get(POWER_SUPPLY_CONSTANT_CHARGE_CURRENT_MAX)) / 1000;
        }
        return 0;
    }

}
