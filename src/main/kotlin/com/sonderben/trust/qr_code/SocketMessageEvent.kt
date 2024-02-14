package com.sonderben.trust.qr_code

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.NetworkInterface
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException


class SocketMessageEvent(private var messageListener:MessageListener) {
    private lateinit var serverSocket: ServerSocket
    private var condition = false


    fun startingListening(){
        condition = true
        Thread {
            try {
                serverSocket = ServerSocket(9005)
                while (condition) {

                    var socket:Socket = serverSocket.accept()
                    println("Connected")
                    handlereceiveMessage(socket)

                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun isListening():Boolean = condition
    private fun handlereceiveMessage( socket:Socket ){
        Thread(Runnable {
            try {
                val reader = BufferedReader( InputStreamReader(socket.getInputStream()) )
                val data = reader.readLine()
                println(data)
                messageListener.onReceiveMessage(data)
                socket.close()

            }catch (e:IOException){
                e.printStackTrace()
            }
        }).start()
    }

    fun removeListener(){
       if (serverSocket!=null){
           condition = false
           serverSocket!!.close()
           println("socketMessage close")
       }
    }



}

/* private fun getIpAddress(): String? {
       var ip = ""
       try {
           val enumNetworkInterfaces = NetworkInterface
               .getNetworkInterfaces()
           while (enumNetworkInterfaces.hasMoreElements()) {
               val networkInterface = enumNetworkInterfaces
                   .nextElement()
               val enumInetAddress = networkInterface
                   .inetAddresses
               while (enumInetAddress.hasMoreElements()) {
                   val inetAddress = enumInetAddress.nextElement()
                   if (inetAddress.isSiteLocalAddress) {
                       ip += ("SiteLocalAddress: "
                               + inetAddress.hostAddress + "\n")
                   }
               }
           }
       } catch (ex: SocketException) {
           //Logger.getLogger(JavaFX_Server::class.java.getName()).log(Level.SEVERE, null, ex)
       }
       return ip
   }*/