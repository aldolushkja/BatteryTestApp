package com.example.testactivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.
        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            int scale = -1;
            int level = -1;
            int voltage = -1;
            int temp = -1;

            @Override
            public void onReceive(Context context, Intent intent) {
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                Log.e("BatteryManager", "level is " + level + "/" + scale + ", temp is " + temp + ", voltage is " + voltage);

                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = context.registerReceiver(null, ifilter);

                // How are we charging?
                int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
                if (level > 85) {
                    Log.e("BatteryManager", "Livello della batteria buono");
//                    Toast.makeText(this, "Ciao a tutti!", Toast.LENGTH_SHORT).show();


//                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
//                    builder.setTitle("Attenzione!");
//                    builder.setMessage("Operazione non valida!");
//                    builder.show();
//                    alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//                    intent = new Intent(context, MainActivity.class);
//                    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//
//                    alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                            SystemClock.elapsedRealtime() +
//                                    60 * 1000, alarmIntent);
                } else if (level < 45) {
                    Log.e("BatteryManager", "Livello della batteria non ottimale!");
                } else {
                    Log.e("BatteryManager", "Non saprei dirti!");
//                    alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//                    intent = new Intent(context, MainActivity.class);
//                    alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//
//                    alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                            SystemClock.elapsedRealtime() +
//                                    60 * 1000, alarmIntent);
                }

                if (usbCharge) {
                    Log.e("BatteryManager", "USB Charge");
                } else if (acCharge) {
                    Log.e("BatteryManager", "AC Charge");
                } else {
                    Log.e("BatteryManager", "Non so come stai caricando");
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        registerReceiver(batteryReceiver, filter);
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

        wl.release();
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, Alarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 10, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
