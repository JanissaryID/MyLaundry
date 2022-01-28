package com.example.mylaundry.socket

import android.content.Context
import android.os.StrictMode
import android.system.ErrnoException
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.mylaundry.services.ForegroundServices
import java.io.IOException
import java.net.*
import java.nio.charset.StandardCharsets

class SocketPrograming(myAddress :String = "228.5.6.7", myPort:Int = 6789, number : Int = 0, type: String = "", cntk: Context) {

    private val IP_ADDRESS = myAddress

    private val PORT_ADDRESS = myPort

    private val NUMBER = number

    private val TYPE = type

    private val context = cntk

    private var HEADER = 0xC8
    private var WASHER = 0x01
    private var DRYER = 0x02
    private var COMMAND = 0x01
    private var DATA = 0x01
    private var myData : String = ""

    private lateinit var group : InetAddress
    private lateinit var s : MulticastSocket

    companion object{
        var getData = ""
//        var statGetData : Boolean = false
    }

    fun sendUDP(messageStr: String) {
// this function sends the UDP message as String
// Hack Prevent crash (sending should be done using an async task)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
//Open a port to send the package
            val socket = DatagramSocket()
            socket.broadcast = true
//get the message from function parameters and make it into a byte array
            val sendData = messageStr.toByteArray()
//craft the packet with the parameters
            val sendPacket = DatagramPacket(sendData, sendData.size, InetAddress.getByName(IP_ADDRESS), PORT_ADDRESS)
            socket.send(sendPacket)
            Log.d("udp", "Check IP : " + IP_ADDRESS)
            Log.d("udp", "Check port : " + PORT_ADDRESS)
            Log.d("udp", "Check data str : " + sendData.decodeToString())
            Log.d("udp", "Check data length : " + sendData.size)
            Log.d("udp", "Check data paket : " + sendPacket)
        } catch (e: IOException) {
            Log.d("udp", "Check data : " + e.toString())
        }
    }

    fun createObjectMulticast() : MulticastSocket{
        group = InetAddress.getByName(IP_ADDRESS)
        s = MulticastSocket(PORT_ADDRESS)
        s.joinGroup(group)

        return s
    }

    fun getDataMachine(){

        if (TYPE == "Dryer Machine"){
            var SUM : Byte = (0.toByte()-DRYER.toByte()-NUMBER.toByte()-COMMAND.toByte()-DATA.toByte()).toByte()
//            var strData = "${HEADER}${String.format("%02d", COMMAND)}${String.format("%02d", DRYER)}${String.format("%02d", NUMBER)}${DATA}${String.format("%02d", SUM)}"
            var strData = "${COMMAND}${DRYER}${NUMBER}"
            myData = strData
        }
        else{
            var SUM : Byte = (0.toByte()-WASHER.toByte()-NUMBER.toByte()-COMMAND.toByte()-DATA.toByte()).toByte()
//            var strData = "${HEADER}${String.format("%02d", COMMAND)}${String.format("%02d", WASHER)}${String.format("%02d", NUMBER)}${DATA}${String.format("%02d", SUM)}"
            var strData = "${COMMAND}${WASHER}${NUMBER}"
            myData = strData
        }
    }

    fun multicastSend(){

        getDataMachine()

//        s.joinGroup(group)
        val bytes = myData.toByteArray(StandardCharsets.UTF_8)
//            Log.d("bytes", "Get Data � : " + bytes)
//            val bytes = myData.toByteArray(StandardCharsets.UTF_8)
        val hi = DatagramPacket(
            bytes, bytes.size,
            group, PORT_ADDRESS
        )
        s.send(hi)
//        s.leaveGroup(group)
        Log.d("getdata", "Send Func � : $getData")
    }

    fun multicastSendService(myDataSend: String){

//        s.joinGroup(group)
        val bytes = myDataSend.toByteArray(StandardCharsets.UTF_8)
        val hi = DatagramPacket(
            bytes, bytes.size,
            group, PORT_ADDRESS
        )
        s.send(hi)

        val bytes1 = "000".toByteArray(StandardCharsets.UTF_8)
        val hi1 = DatagramPacket(
            bytes1, bytes1.size,
            group, PORT_ADDRESS
        )
        s.send(hi1)

//        s.leaveGroup(group)
        ForegroundServices.statsend = false
        Log.d("senddata", "FUCKING SEND DATA")
    }

    fun multicastReceived(){
        try{
//            s.joinGroup(group)
            val buf = ByteArray(4)
            val recv = DatagramPacket(buf, buf.size)
            s.receive(recv)
//            s.leaveGroup(group)
            getData = recv.data.decodeToString()
//            if(getData == ""){
//                statGetData = false
//            }
//            else{
//                statGetData = true
//            }
        }
        catch (e : SocketException){
            Log.d("error", "error : " + e)
        }
        catch (e : IOException){
            Log.d("error", "error : " + e)
        }
        catch (e : ErrnoException){
            Log.d("error", "error : " + e)
        }
    }

    fun multicastSocketPrograming() {
//        var HEADER = 0xC8
//        var WASHER = 0x01
//        var DRYER = 0x02
//        var COMMAND = 0x01
//        var DATA = 0x01
//        var myData : String
//        statGetData = false




        try{


//            getData = recv.data.decodeToString()

//            val getdata2 = getData.toString()

//            Toast.makeText(context, "Reveived Data : $getData" , Toast.LENGTH_SHORT).show()
//            Log.d("getdata", "Get Data � : " + recv.data.decodeToString())
//            val separate1 = getdata2.split("-")[0].toInt()
//            val separate2 = (getdata2.split("-")[1]).toInt()
            Log.d("getdata", "Get Data 2 � : $getData")
//            Log.d("getdata", "Get Data 3 � : " + recv.data)
        }
        catch (e : SocketException){
            Log.d("error", "error : " + e)
        }
        catch (e : IOException){
            Log.d("error", "error : " + e)
        }
        catch (e : ErrnoException){
            Log.d("error", "error : " + e)
        }
    }
}