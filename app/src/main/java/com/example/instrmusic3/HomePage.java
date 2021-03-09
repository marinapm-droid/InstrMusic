package com.example.instrmusic3;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.example.instrmusic3.activities.SettingsActivity;
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.dispatch.OscConfiguration;
import com.example.instrmusic3.dispatch.OscDispatcher;
import com.example.instrmusic3.dispatch.OscHandler;
import com.example.instrmusic3.dispatch.OscReceiveConfig;
import com.example.instrmusic3.dispatch.OscReceiveConfigSound;
import com.example.instrmusic3.fragment.HomeFragment;
import com.example.instrmusic3.fragment.MultiTouchFragment;
import com.example.instrmusic3.fragment.SensorFragment;
import com.example.instrmusic3.fragment.StartUpEffectsFragment;
import com.example.instrmusic3.fragment.StartUpFavFragment;
import com.example.instrmusic3.fragment.StartUpSoundsFragment;
import com.example.instrmusic3.fragment.StartupFragment;
import com.example.instrmusic3.sensors.Settings;

import org.sensors2.common.dispatch.DataDispatcher;
import org.sensors2.common.dispatch.Measurement;
import org.sensors2.common.nfc.NfcActivity;
import org.sensors2.common.sensors.Parameters;
import org.sensors2.common.sensors.SensorActivity;
import org.sensors2.common.sensors.SensorCommunication;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomePage extends FragmentActivity implements SensorActivity, NfcActivity, CompoundButton.OnCheckedChangeListener, View.OnTouchListener {

    private Settings settings;
    private SensorCommunication sensorCommunication;
    private OscDispatcher dispatcher;
    private SensorManager sensorManager;
    private PowerManager.WakeLock wakeLock;
    private boolean active;
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    public int count = 0;
    String effect;


    public Settings getSettings() {
        return this.settings;
    }

    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // fullscreen
        setContentView(R.layout.main);

        OscReceiveConfig oscReceiveConfigEffect = new OscReceiveConfig();
        OscReceiveConfigSound oscReceiveConfigSound = new OscReceiveConfigSound();
        oscReceiveConfigEffect.receive();
        oscReceiveConfigSound.receive();
        this.settings = this.loadSettings();
        this.dispatcher = new OscDispatcher();
        this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.dispatcher.setSensorManager();
        this.sensorCommunication = new SensorCommunication(this);
        this.wakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getLocalClassName());
        resolveIntent(getIntent());
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord(
        )});


        manager = getSupportFragmentManager();
        HomeFragment f1 = new HomeFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, f1, "A");
        transaction.addToBackStack("addA");
        transaction.commit();
    }

    public List<Parameters> GetSensors(SensorManager sensorManager) {
        List<Parameters> parameters = new ArrayList<>();

        // add Nfc sensor
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            parameters.add(new com.example.instrmusic3.sensors.Parameters(nfcAdapter, this.getApplicationContext()));
        }
        // add device sensors
        parameters.addAll(com.example.instrmusic3.sensors.Parameters.GetSensors(sensorManager, this.getApplicationContext()));
        return parameters;
    }

    public NfcAdapter getNfcAdapter() {
        return this.nfcAdapter;
    }

    @TargetApi(10)
    private NdefRecord newTextRecord() {
        byte[] langBytes = Locale.ENGLISH.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = Charset.forName("UTF-8");
        byte[] textBytes = "Message from NFC Reader :-)".getBytes(utfEncoding);

        int utfBit = 0;
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @TargetApi(10)
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            byte[] empty = new byte[0];
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            if (rawMsgs != null) {
                byte[] payload = new byte[0];
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            } else {
                // Unknown tag type
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }
            // Setup the views
            for (NdefMessage msg : msgs) {
                if (active) {
                    this.sensorCommunication.dispatch(msg);
                }
            }
        }
    }

    @TargetApi(10)
    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize());
                sb.append(" bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (byte aByte : bytes) {
            long value = aByte & 0xffL;
            result += value * factor;
            factor *= 256L;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffL;
            result += value * factor;
            factor *= 256L;
        }
        return result;
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    @Override
    public DataDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public SensorManager getSensorManager() {
        return this.sensorManager;
    }

    public Settings loadSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Settings settings = new Settings(preferences);
        OscConfiguration oscConfiguration = OscConfiguration.getInstance();
        oscConfiguration.setHost(settings.getHost());
        oscConfiguration.setPort(settings.getPort());
        return settings;
    }

    // @Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //  // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.start_up, menu);
    // return true;
    //}


    @Override
    @SuppressLint("NewApi")
    protected void onResume() {
        super.onResume();
        this.loadSettings();
        this.sensorCommunication.onResume();
        if (active && !this.wakeLock.isHeld()) {
            this.wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        }

        if (nfcAdapter != null) {
            if (nfcAdapter.isEnabled()) {
                nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
                nfcAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
            }

        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onPause() {
        super.onPause();
        this.sensorCommunication.onPause();
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
            nfcAdapter.disableForegroundNdefPush(this);
        }
    }

    public void addSensorFragment(SensorFragment sensorFragment) {
        this.dispatcher.addSensorConfiguration(sensorFragment.getSensorConfiguration());
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (active) {
            this.sensorCommunication.dispatch(sensorEvent);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // We do not care about that
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            if (!this.wakeLock.isHeld()) {
                this.wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            this.setRequestedOrientation(this.getCurrentOrientation());
        } else {
            this.wakeLock.release();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        active = isChecked;
    }

    public List<Parameters> getSensors() {
        return sensorCommunication.getSensors();
    }

    public void onStartMultiTouch(View view) {
        if (manager.findFragmentByTag("F") == null) {
            MultiTouchFragment f8 = new MultiTouchFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, f8, "F");
            transaction.addToBackStack("addF");
            transaction.commit();
        } else {
            Fragment fragment = manager.findFragmentByTag("F");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(fragment);
            transaction.commit();
        }
        count++;
    }

    public void onStartSounds(View view) {
        if (manager.findFragmentByTag("C") == null) {
            StartUpSoundsFragment f4 = new StartUpSoundsFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, f4, "C");
            transaction.addToBackStack("addC");
            transaction.commit();

        } else {
            Fragment fragment = manager.findFragmentByTag("C");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(fragment);
            transaction.commit();
        }
    }

    public void onStartEffects(View view) {
        if (manager.findFragmentByTag("D") == null) {
            StartUpEffectsFragment f5 = new StartUpEffectsFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, f5, "D");
            transaction.addToBackStack("addD");
            transaction.commit();
        } else {
            Fragment fragment = manager.findFragmentByTag("D");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(fragment);
            transaction.commit();
        }
    }

    public void onStartLogOut(View view) {
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        startActivity(intent);
    }

    public void onStartSettings(View view) {
        Intent intent = new Intent(HomePage.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onStartSensors(View view) {
        if (manager.findFragmentByTag("B") == null) {
            StartupFragment f2 = new StartupFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, f2, "B");
            transaction.addToBackStack("addB");
            transaction.commit();
        } else {
            Fragment fragment = manager.findFragmentByTag("B");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(fragment);
            transaction.commit();
        }
    }

    public void onStartFavorites(View view) {
        if (manager.findFragmentByTag("G") == null) {
            StartUpFavFragment f6 = new StartUpFavFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, f6, "G");
            transaction.addToBackStack("addG");
            transaction.commit();
        } else {
            Fragment fragment = manager.findFragmentByTag("G");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.show(fragment);
            transaction.commit();
        }
    }

    public static void setEffect(String effect) {
        this.effect = effect;
    }

    public void showSelection(View view) {
        FavoritesParameters favoritesParameters = new FavoritesParameters();
        System.out.println("Efeito: " + this.effect);
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction transaction = manager.beginTransaction();
        FragmentTransaction transaction1 = manager.beginTransaction();
        FragmentTransaction transaction2 = manager.beginTransaction();
        FragmentTransaction transaction4 = manager.beginTransaction();
        FragmentTransaction transaction3 = manager.beginTransaction();

        if (manager.findFragmentByTag("B") != null) {
            Fragment fragment = manager.findFragmentByTag("B");
            transaction.hide(fragment);
            transaction.commit();
        }

        if (manager.findFragmentByTag("C") != null) {
            Fragment fragment = manager.findFragmentByTag("C");
            transaction.hide(fragment);
            transaction1.commit();
        }
        if (manager.findFragmentByTag("D") != null) {
            Fragment fragment = manager.findFragmentByTag("D");
            transaction.hide(fragment);
            transaction3.commit();
        }

        if (manager.findFragmentByTag("G") != null) {
            Fragment fragment = manager.findFragmentByTag("G");
            transaction.hide(fragment);
            transaction4.commit();
        }

        if (manager.findFragmentByTag("F") != null) {
            Fragment fragment = manager.findFragmentByTag("F");
            transaction.hide(fragment);
            transaction2.commit();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println("OnTouch");
        if (active) {
            int width = v.getWidth();
            int height = v.getHeight();
            for (Measurement measurement : Measurement.measurements(event, width, height)) {
                dispatcher.dispatch(measurement);
            }
        }

        return false;
    }

    public int getCurrentOrientation() {

        final Display display = this.getWindowManager().getDefaultDisplay();
        final int width, height;
        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = display.getWidth();
            height = display.getHeight();
        }
        switch (display.getRotation()) {
            case Surface.ROTATION_90:
                if (width > height) {
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                }
            case Surface.ROTATION_180:
                if (height > width) {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                } else {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                }

            case Surface.ROTATION_270:
                if (width > height) {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                } else {
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }
            default:
                if (height > width) {
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                } else {
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                }
        }
    }

}
