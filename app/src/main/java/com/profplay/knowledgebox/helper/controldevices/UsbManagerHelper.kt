package com.profplay.knowledgebox.helper.controldevices

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber

object UsbManagerHelper {
    const val USB_PERMISSION =  "com.profplay.knowledgebox.USB_PERMISSION"
    private var usbSerialPort: UsbSerialPort? = null

    // 🔍 USB Cihazını Bul
    fun findUsbDevice(usbManager: UsbManager): UsbDevice? {
        return usbManager.deviceList.values.firstOrNull()
    }

    // 🎯 USB Bağlantısını Aç
    fun openUsbConnection(usbManager: UsbManager, device: UsbDevice): UsbSerialPort? {
        val connection = usbManager.openDevice(device) ?: return null
        val usbSerialDriver = UsbSerialProber.getDefaultProber().probeDevice(device) ?: return null
        val port = usbSerialDriver.ports.firstOrNull() ?: return null

        return try {
            port.open(connection)
            usbSerialPort = port
            port
        } catch (e: Exception) {
            Log.e("USB", "Bağlantı açma hatası", e)
            null
        }
    }

    // ⚙️ USB Seri Port Ayarları Yapılandır
    fun configureUsbPort(baudRate: Int = 115200) {
        usbSerialPort?.setParameters(baudRate, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
    }

    // ❌ USB Bağlantısını Kapat
    fun closeUsbConnection() {
        try {
            usbSerialPort?.close()
            usbSerialPort = null
        } catch (e: Exception) {
            Log.e("USB", "Bağlantı kapatma hatası", e)
        }
    }

    // Receiver'ı tanımla
    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action

            if (USB_PERMISSION == action) {
                val device: UsbDevice? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent?.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                } else {
                    intent?.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                }
                val granted = intent?.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false) ?: false
                if (granted) {
                    Log.d("USB", "İzin verildi: $device")
                } else {
                    Log.d("USB", "İzin reddedildi: $device")
                }
            }
        }
    }

    // 🎟️ USB İzni İste
    fun requestUsbPermission(context: Context, usbManager: UsbManager, device: UsbDevice) {
        if (!usbManager.hasPermission(device)) {
            val permissionIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(USB_PERMISSION),
                PendingIntent.FLAG_IMMUTABLE
            )

            val filter = IntentFilter(USB_PERMISSION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(usbReceiver, filter, Context.RECEIVER_EXPORTED)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.registerReceiver(usbReceiver, filter, Context.RECEIVER_EXPORTED)
            }

            // Receiver'ı kaydet
            usbManager.requestPermission(device, permissionIntent)
        } else {
            Log.d("USB", "Cihazın izni zaten verilmiş.")

        }
    }



    // 🎧 USB İzin Yanıtlarını Dinleyen BroadcastReceiver
    fun createUsbReceiver(onPermissionResult: (Boolean) -> Unit): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d("USB", "BroadcastReceiver çağrıldı: ${intent.action}") // ✅ Gelen intent'i yazdır

                if (intent.action == USB_PERMISSION) {
                    val device: UsbDevice? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                    } else {
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    }
                    val granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)

                    if (device == null) {
                        Log.e("USB", "HATA! Broadcast intent içinde device = ${device.toString()}")
                    } else {
                        Log.d("USB", "İzin sonucu: $granted -> Cihaz: $device")
                    }

                    onPermissionResult(granted)
                }
            }
        }
    }

}
