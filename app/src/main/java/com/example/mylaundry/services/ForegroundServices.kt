package com.example.mylaundry.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.mylaundry.MainActivity
import com.example.mylaundry.R
import com.example.mylaundry.room.dryermachine.Dryer
import com.example.mylaundry.room.dryermachine.DryerDao
import com.example.mylaundry.room.dryermachine.DryerDatabase
import com.example.mylaundry.room.washermachine.Washer
import com.example.mylaundry.room.washermachine.WasherDao
import com.example.mylaundry.room.washermachine.WasherDatabase
import com.example.mylaundry.socket.SocketPrograming
import kotlinx.coroutines.*
import java.lang.Thread.sleep


class ForegroundServices() : Service() {

    private lateinit var uiScope : CoroutineScope

    private lateinit var daoWasher : WasherDao
    private lateinit var daoDryer : DryerDao

    private var COMMANDSEND = 0x07
    private var DATA = 0x01
    private var COMMAND = ""
    private var NUMBER = ""
    private var TYPE = ""
    private var NUMBERINT = 0
    private var TYPEINT = 0
    private var myData : String = ""

    val soket = SocketPrograming(cntk = this)

    private val CHANNEL_ID = "ForegroundService Laundry"
    companion object {
        var statConfrimActivation : Boolean = false
        var statsend : Boolean = true

        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, ForegroundServices::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundServices::class.java)
            context.stopService(stopIntent)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //do heavy work on a background thread
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Service Laundry")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_washing)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        wifiManagerBlock()
        doBackDoor2()
//        doBackDoorTriger()
        //stopSelf();
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private fun doBackDoor2(){
        val mainLooper = Looper.getMainLooper()

//        val s = soket.createObjectMulticast()
        Thread(Runnable {
//            val socketrecv = SocketPrograming(cntk = this)
            try{
                soket.createObjectMulticast()
                while (true){
                    soket.multicastReceived()
                    if (SocketPrograming.getData != ""){
                        COMMAND = SocketPrograming.getData[0].toString()
                        NUMBER = SocketPrograming.getData[2].toString()
                        TYPE = SocketPrograming.getData[1].toString()
//                          Log.d("getdata", "Get Stat Service � : ${SocketPrograming.statGetData}")
                        Log.d("getdata", "Get Data Service � : ${SocketPrograming.getData}")

                        if(COMMAND != "0"){
                            Handler(mainLooper).post {
                                Toast.makeText(applicationContext, "Get Data ${SocketPrograming.getData}" , Toast.LENGTH_SHORT).show()
                            }
                            if(COMMAND == "2"){
                                Handler(mainLooper).post {
                                    if(TYPE == "1"){
                                        Toast.makeText(applicationContext, "Washer machine number ${NUMBER} is active" , Toast.LENGTH_SHORT).show()
                                    }
                                    else if(TYPE == "2"){
                                        Toast.makeText(applicationContext, "Dryer machine number ${NUMBER} is active" , Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            else if(COMMAND == "6"){
                                if(TYPE == "1"){
                                    doBackDoorSend()
                                    Toast.makeText(applicationContext, "Washer machine number ${NUMBER} is available now" , Toast.LENGTH_SHORT).show()
                                }
                                else if(TYPE == "2"){
                                    doBackDoorSend()
                                    Toast.makeText(applicationContext, "Dryer machine number ${NUMBER} is available now" , Toast.LENGTH_SHORT).show()
                                }
                            }
                            else if(COMMAND == "9"){
                                Handler(mainLooper).post {
                                    Toast.makeText(applicationContext, "Triger Data" , Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
//                    sleep(30L)
                }
            }
            catch (e : Exception){
                Log.d("error", "ERROR $e")
            }
        }).start()
    }

    private fun doBackDoorTriger(){
        val mainLooper = Looper.getMainLooper()
//        val socketsend = SocketPrograming(cntk = this)
        val myDataTriger = "911"

        Thread(Runnable {
            try{
//                socketsend.createObjectMulticast()
                while (true){
                    statsend = true
                    Log.d("triger", "Triger 30 Second")
                    sleep(30000L)

                    if (statsend){
                        soket.multicastSendService(myDataTriger)
                    }

                    Log.d("triger", "Send Triger")
                }
            }
            catch (e : Exception){
                Log.d("error", "ERROR $e")
            }
        }).start()
    }

    fun updateMachine(typeMachine : Int, numberMachine : Int, statMachine : Boolean){
        try {
            Log.d("dao", "Update")
            Log.d("dao", "Update id ${(numberMachine-1)}")
            Log.d("dao", "Update Number ${numberMachine}")
            if (typeMachine == 1){
                val updateWasher = Washer((numberMachine-1), numberMachine, statMachine)
                Thread{
                    daoWasher = WasherDatabase.getDatabase(application).washerDao()
                    daoWasher.updateValueWasher(updateWasher)
                    Log.d("dao", "OK")
                }.start()
            }
            else{
                val updateDryer = Dryer((numberMachine-1), numberMachine, statMachine)
                Thread{
                    daoDryer = DryerDatabase.getDatabase(application).dryerDao()
                    daoDryer.updateValueDryer(updateDryer)
                    Log.d("dao", "OK")
                }.start()
            }
        }
        catch (e : Exception){
            Log.d("error", "Error $e")
        }
    }

    private fun wifiManagerBlock(){
        val manager = getSystemService(WIFI_SERVICE) as WifiManager
        val lock = manager.createMulticastLock("lock name")
        lock.setReferenceCounted(true)
        lock.acquire()
    }

    private fun doBackDoorSend(){
        val socketsend = SocketPrograming(cntk = this)
//        var mysum : Byte = (0-COMMANDSEND-TYPEINT-NUMBERINT-DATA).toByte()
        myData = "$COMMANDSEND$TYPE$NUMBER"

        uiScope.launch {
            withContext(Dispatchers.IO) {
                socketsend.createObjectMulticast()
                socketsend.multicastSendService(myData)
            }
        }
    }
}