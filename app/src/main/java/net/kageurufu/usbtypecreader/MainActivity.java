package net.kageurufu.usbtypecreader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer mTimer;
    final Handler mHandler = new Handler();
    final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            PowerSupplyStats powerSupplyStats = mPowerSupplyReader.readDevice();

            StringBuilder sb = new StringBuilder();

            if (powerSupplyStats.isPresent()) {
                sb.append(String.format("Currently %s<br/>",
                        powerSupplyStats.getStatus()));
                sb.append(String.format("It's maximum current is %dmAh<br/>",
                        powerSupplyStats.getCurrentMax()));
                sb.append(String.format("It's constant charge current max is %d<br/>",
                        powerSupplyStats.getConstantChargeCurrentMax(), powerSupplyStats.getVoltageMax()));
                sb.append(String.format("It's maximum voltage is %d<br/><br/>",
                        powerSupplyStats.getVoltageMax()));
                sb.append('\n');
                if (powerSupplyStats.getCurrentMax() > 2500 /*mAh*/) {
                    sb.append(String.format("<font color=\"red\">" +
                                    "If you are using a Type-A to Type-C cable, most " +
                                    "likely it is not standards compliant as it is allowing up to %dmAh, " +
                                    "which is above the maximum 2000mAh Type-A is allowed to provide" +
                                    "</font><br/>",
                            powerSupplyStats.getCurrentMax()));
                } else {
                    sb.append(String.format("<font color=\"#00B800\" size=\"14dp\">" +
                                    "Your cable is allowing maximum %dmAh, which is under the " +
                                    "maximum current allowed for a Type-A cable" +
                                    "</font><br/>",
                            powerSupplyStats.getCurrentMax()
                    ));
                }
            } else {
                sb.append("Not connected to power supply").append('\n');
            }

            sb.append(mPowerSupplyReader.readSysINode().replace("\n", "<br/>")).append('\n');
            mOutput.setText(Html.fromHtml(sb.toString()));
        }
    };

    private PowerSupplyReader mPowerSupplyReader;
    private TextView mOutput;
    private Button mSendDebugEmailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOutput = (TextView) findViewById(R.id.powerOutput);
        mSendDebugEmailBtn = (Button) findViewById(R.id.sendDebugEmailBtn);
        mSendDebugEmailBtn.setVisibility(View.INVISIBLE);
        mSendDebugEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendErrorEmail();
            }
        });

        try {
            mPowerSupplyReader = new PowerSupplyReader();
        } catch (PowerSupplyReader.UnknownDevicePathException e) {
            mOutput.setText(e.getStackTrace().toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTimer = new Timer();

        if (mPowerSupplyReader != null) {
            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(mRunnable);
                }
            }, 0, 500);
        } else {
            mOutput.setText(getText(R.string.errorText));
            mSendDebugEmailBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        mTimer.cancel();
    }

    private void sendErrorEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "usbtypecreader@kageurufu.net");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Unknown Device: " + Build.PRODUCT);
        intent.putExtra(Intent.EXTRA_TEXT, PowerSupplyReader.getErrorText());

        startActivity(intent);
    }

}
