package com.example.mylaundry.socket

import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.FragmentActivity
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class Socket( myAddress :String, myPort:Int) {

    private val IP_ADDRESS = myAddress

    private val PORT_ADDRESS = myPort

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
}