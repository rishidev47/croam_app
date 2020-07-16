package com.example.croam

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.*
import android.util.Log
import android.widget.Toast

class EndlessService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    private var myBroadcastReciever: MyBroadCastReciever? = null
    override fun onBind(intent: Intent): IBinder? {
//        log("Some component want to bind with the service")
        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("service", "onStartCommand executed with startId: $startId")
        if (intent != null) {
            val action = intent.action
//            log("using an intent with action $action")
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> Log.e("service", "This should never happen. No action in the received intent")
            }
        } else {
            Log.e("service", "with a null intent. It has been probably restarted by the system."
            )
        }
        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("service", "The service has been created".toUpperCase())
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        myBroadcastReciever = MyBroadCastReciever(this)
        registerReceiver(myBroadcastReciever, filter)
        val notification = createNotification()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("service", "The service has been destroyed".toUpperCase())
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
        unregisterReceiver(myBroadcastReciever)
    }

    private fun startService() {
        if (isServiceStarted) return
        Log.e("service", "Starting the foreground service task")
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
                (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                        acquire()
                    }
                }
    }

    private fun stopService() {
//        log("Stopping the foreground service")
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            Log.e("service", "Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            val channel = NotificationChannel(
                    notificationChannelId,
                    "Endless Service notifications channel",
                    NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                this,
                notificationChannelId
        ) else Notification.Builder(this)

        return builder
                .setContentTitle("Endless Service")
                .setContentText("This is your favorite endless service working")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Ticker text")
                .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
                .build()
    }

    fun startCroamService() {
        val serviceIntent = Intent(this, CRoamService::class.java)
        serviceIntent.putExtra("inputExtra", "hardwareButton")
        //        ContextCompat.startForegroundService(this, serviceIntent);
        Handler().postDelayed({ stopCroamService() }, 120000)
        startService(serviceIntent)
//        Toast.makeText(baseContext, "Service Started", Toast.LENGTH_SHORT).show()
//        Log.v(TAG, "Service Started");

    }

    fun stopCroamService() {
        val serviceIntent = Intent(this, CRoamService::class.java)
        stopService(serviceIntent)
        Toast.makeText(baseContext, "Service Stopped", Toast.LENGTH_SHORT).show()
    }
}


internal class MyBroadCastReciever(var activity: EndlessService) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("HELP_HARDWARE_BTN",
                "BroadcastReceiver :" + CRoamService.screenOff1 + CRoamService.screenOff2
                        + CRoamService.screenOn1 + CRoamService.screenOn2)

        if (intent.action == Intent.ACTION_SCREEN_OFF) {
            //DO HERE
            if (!CRoamService.screenOff1) {
                object : CountDownTimer(1500, 500) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        CRoamService.screenOff1 = false
                        CRoamService.screenOff2 = false
                        CRoamService.screenOn1 = false
                        CRoamService.screenOn2 = false
                    }
                }.start()
                CRoamService.screenOff1 = true
            } else if (CRoamService.screenOff1 && !CRoamService.screenOff2) {
                CRoamService.screenOff2 = true
                Log.e("HELP_HARDWARE_BTN", "screenOff: Help detected")
//                croam.onDetectingHelp()
            }
        } else if (intent.action == Intent.ACTION_SCREEN_ON) {
            //DO HERE
            if (!CRoamService.screenOn1) {
                Log.e("df", "fd")
                object : CountDownTimer(1500, 500) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        CRoamService.screenOff1 = false
                        CRoamService.screenOff2 = false
                        CRoamService.screenOn1 = false
                        CRoamService.screenOn2 = false
                    }
                }.start()
                CRoamService.screenOn1 = true
            } else if (CRoamService.screenOn1 && !CRoamService.screenOn2) {
                CRoamService.screenOn2 = true
                Log.e("HELP_HARDWARE_BTN", "screenOn: Help detected")
//                croam.onDetectingHelp()
            }
        }
    }

}