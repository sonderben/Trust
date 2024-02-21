package com.sonderben.trust

import java.net.InetAddress
import java.net.NetworkInterface
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object Util {


    fun timeStampToCalendar(timestamp:Timestamp?):Calendar{
        if (timestamp!=null){
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp.time
            return calendar
        }

            val cal = Calendar.getInstance()
            cal.set(1111,11,11)
        return cal


    }

    fun getLocalIpAddress(): String {
        try {
            val interfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface: NetworkInterface = interfaces.nextElement()
                val addresses: Enumeration<InetAddress> = networkInterface.inetAddresses

                while (addresses.hasMoreElements()) {
                    val address: InetAddress = addresses.nextElement()

                    // Verifica que no sea una dirección IPv6 y que no sea la dirección loopback
                    if (!address.isLoopbackAddress && address.hostAddress.indexOf(':') == -1) {

                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "No se pudo obtener la dirección IP"
    }


}