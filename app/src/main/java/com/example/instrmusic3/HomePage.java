package com.example.instrmusic3;

import androidx.annotation.NonNull;
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

import com.example.instrmusic3.activities.AboutActivity;
import com.example.instrmusic3.activities.MainActivity;
import com.example.instrmusic3.activities.ProfileActivity;
import com.example.instrmusic3.activities.SettingsActivity;
import com.example.instrmusic3.auth.UserHelperClass;
import com.example.instrmusic3.dispatch.OscConfiguration;
import com.example.instrmusic3.dispatch.OscDispatcher;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.sensors2.common.dispatch.DataDispatcher;
import org.sensors2.common.dispatch.Measurement;
import org.sensors2.common.nfc.NfcActivity;
import org.sensors2.common.sensors.Parameters;
import org.sensors2.common.sensors.SensorActivity;
import org.sensors2.common.sensors.SensorCommunication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
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
    static String effect, sound, sensor, username;
    String fragmento;
    int onOff = 0;
    int count;
    long num;
    FirebaseDatabase mDatabase;
    FragmentManager manager;

    public Settings getSettings() {
        return this.settings;
    }


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


        //HOME
        manager = getSupportFragmentManager();
        HomeFragment f1 = new HomeFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, f1, "A");
        transaction.addToBackStack("addA");
        transaction.commit();

        //SENSOR
        StartupFragment f2 = new StartupFragment();
        FragmentTransaction transaction1 = manager.beginTransaction();
        transaction1.add(R.id.container, f2, "B");
        transaction1.addToBackStack("addB");
        transaction1.commit();
        transaction1.hide(f2);
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
        fragmento = "F";
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
            assert fragment != null;
            transaction.show(fragment);
            transaction.commit();
        }
        fragmento = "C";
    }

    public void onStartEffects(View view) {
        if (manager.findFragmentByTag("D") == null) {
            StartUpEffectsFragment f8 = new StartUpEffectsFragment();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.container, f8, "D");
            transaction.addToBackStack("addD");
            transaction.commit();
        } else {
            Fragment fragment = manager.findFragmentByTag("D");
            FragmentTransaction transaction = manager.beginTransaction();
            assert fragment != null;
            transaction.show(fragment);
            transaction.commit();
        }
        fragmento = "D";
    }

    public void onStartLogOut(View view) {
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        startActivity(intent);
    }


    public void onStartInfo(View view) {
        Intent intent = new Intent(HomePage.this, AboutActivity.class);
        startActivity(intent);
    }

    public void onStartSettings(View view) {
        Intent intent = new Intent(HomePage.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onStartProfile(View view) {
        Intent intent = new Intent(HomePage.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onStartSensors(View view) {

        Fragment fragment = manager.findFragmentByTag("B");
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.show(fragment);
        transaction.commit();

        fragmento = "B";
    }

    public void onStartFavorites(View view) {

        StartUpFavFragment f6 = new StartUpFavFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, f6, "G");
        transaction.addToBackStack("addG");
        transaction.commit();

        fragmento = "G";
    }

    public static void setEffect(String effectSelected) {
        effect = effectSelected;
    }

    public static void setSensor(String sensorSelected) {
        sensor = sensorSelected;
    }

    public static void setSound(String soundSelected) {
        sound = soundSelected;
    }

    public static void setUsername(String usernameActive) {
        username = usernameActive;
    }

    public static String getSound() {
        return sound;
    }

    public static String getEffect() {
        return effect;
    }

    public static String getSensor() {
        return sensor;
    }

    public void saveFavorites(View view) {
        if (onOff == 0) {
            onOff = 1;
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
            Query checkUser = ref.orderByChild("nome").equalTo(username);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    num = dataSnapshot.child(username).child("favorites").getChildrenCount();
                    num++;
                    String numString = String.valueOf(num);
                    UserHelperClass helperClass = new UserHelperClass();
                    helperClass.UserHelperClass1(sensor,  effect, sound);
                    mDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = mDatabase.getReference("users");
                    myRef.child(username).child("favorites").child(numString).setValue(helperClass);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        } else {
            onOff = 0;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction transaction = manager.beginTransaction();
        FragmentTransaction transaction1 = manager.beginTransaction();
        FragmentTransaction transaction2 = manager.beginTransaction();
        FragmentTransaction transaction4 = manager.beginTransaction();
        FragmentTransaction transaction3 = manager.beginTransaction();
        switch (fragmento) {
            case "A": {
                break;
            }
            case "B":
                Fragment fragment = manager.findFragmentByTag("B");
                transaction.hide(fragment);
                transaction.commit();
                break;
            case "C":
                Fragment fragment1 = manager.findFragmentByTag("C");
                transaction1.hide(fragment1);
                transaction1.commit();
                break;
            case "D":
                Fragment fragment2 = manager.findFragmentByTag("D");
                transaction2.hide(fragment2);
                transaction2.commit();
                break;
            case "F":
                Fragment fragment3 = manager.findFragmentByTag("F");
                transaction3.hide(fragment3);
                fragmento = "B";
                transaction3.commit();
                break;
            case "G":
                Fragment fragment4 = manager.findFragmentByTag("G");
                transaction4.remove(fragment4);
                transaction4.commit();
                break;
            default:
                System.out.println("erro");
        }

    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
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
