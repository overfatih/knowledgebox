package com.profplay.knowledgebox.data.repository
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import com.profplay.knowledgebox.helper.controldevices.UsbManagerHelper
import kotlin.concurrent.thread

class UsbRepository(private val usbManager: UsbManager) {

    fun readUsbData(device: UsbDevice, onDataReceived: (String) -> Unit) {
        val usbSerialPort = UsbManagerHelper.openUsbConnection(usbManager, device)
        if (usbSerialPort == null) {
            Log.e("USB", "Bağlantı açılamadı!")
            return
        }

        UsbManagerHelper.configureUsbPort()  // Baud rate ve UART ayarlarını yap

        thread {
            val buffer = ByteArray(64)
            while (true) {
                val received = usbSerialPort.read(buffer, 1000)
                if (received > 0) {
                    val message = String(buffer, 0, received, Charsets.UTF_8)
                    Log.d("USB", "Gelen Veri: $message")
                    onDataReceived(message)
                }
            }
        }
    }

    fun closeUsbConnection() {
        UsbManagerHelper.closeUsbConnection()
    }
}
