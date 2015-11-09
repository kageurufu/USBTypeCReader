package net.kageurufu.usbtypecreader;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class PowerSupplyStats {
    private final static String TAG = "USBC.PowerSupplyStats";
    private Map<String, String> mUeventValues;

    private static final String POWER_SUPPLY_NAME = "POWER_SUPPLY_NAME";
    private static final String POWER_SUPPLY_PRESENT = "POWER_SUPPLY_PRESENT";
    private static final String POWER_SUPPLY_ONLINE = "POWER_SUPPLY_ONLINE";
    private static final String POWER_SUPPLY_VOLTAGE_MAX = "POWER_SUPPLY_VOLTAGE_MAX";
    private static final String POWER_SUPPLY_CURRENT_MAX = "POWER_SUPPLY_CURRENT_MAX";
    private static final String POWER_SUPPLY_TYPE = "POWER_SUPPLY_TYPE";
    private static final String POWER_SUPPLY_SCOPE = "POWER_SUPPLY_SCOPE";
    private static final String POWER_SUPPLY_VOLTAGE_NOW = "POWER_SUPPLY_VOLTAGE_NOW";
    private static final String POWER_SUPPLY_HEALTH = "POWER_SUPPLY_HEALTH";

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

    public String getType() {
        if (mUeventValues.containsKey(POWER_SUPPLY_TYPE)) {
            return mUeventValues.get(POWER_SUPPLY_TYPE);
        }
        return null;
    }

    public String getScope() {
        if (mUeventValues.containsKey(POWER_SUPPLY_SCOPE)) {
            return mUeventValues.get(POWER_SUPPLY_SCOPE);
        }
        return null;
    }

    public String getHealth() {
        if (mUeventValues.containsKey(POWER_SUPPLY_HEALTH)) {
            return mUeventValues.get(POWER_SUPPLY_HEALTH);
        }
        return null;
    }

    public Boolean isPresent() {
        if (mUeventValues.containsKey(POWER_SUPPLY_PRESENT)) {
            return mUeventValues.get(POWER_SUPPLY_PRESENT).equals("1");
        }
        return false;
    }

    public Boolean isOnline() {
        if (mUeventValues.containsKey(POWER_SUPPLY_ONLINE)) {
            return mUeventValues.get(POWER_SUPPLY_ONLINE).equals("1");
        }
        return false;
    }

    public Integer getVoltageMax() {
        if (mUeventValues.containsKey(POWER_SUPPLY_VOLTAGE_MAX)) {
            return Integer.parseInt(mUeventValues.get(POWER_SUPPLY_VOLTAGE_MAX));
        }
        return 0;
    }

    public Integer getVoltageNow() {
        if (mUeventValues.containsKey(POWER_SUPPLY_VOLTAGE_NOW)) {
            return Integer.parseInt(mUeventValues.get(POWER_SUPPLY_VOLTAGE_NOW));
        }
        return 0;
    }

    public Integer getCurrentMax() {
        if (mUeventValues.containsKey(POWER_SUPPLY_CURRENT_MAX)) {
            return Integer.parseInt(mUeventValues.get(POWER_SUPPLY_CURRENT_MAX)) / 1000;
        }
        return 0;
    }

}
