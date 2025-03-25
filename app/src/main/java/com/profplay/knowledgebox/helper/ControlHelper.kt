package com.profplay.knowledgebox.helper

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context

import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver

class VoiceControlHelper(context: Context, onAnswerProvided: (String) -> Unit) {
    /*val activity = context as Activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()

            if (!spokenText.isNullOrEmpty()) {
                onAnswerProvided(spokenText) // Gelen cevabı işleme fonksiyonuna gönderiyoruz.
            }
        }
    }
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR")
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Cevabınızı söyleyin")
    }
    */

}
private const val ACTION_USB_PERMISSION = "android.permission.USB_PERMISSION"

fun requestUsbPermission(context: Context, usbManager: UsbManager, usbDevice: UsbDevice) {
    val permissionIntent = PendingIntent.getBroadcast(
        context, 0, Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE
    )
    usbManager.requestPermission(usbDevice, permissionIntent)
}

//class UsbReceiver : BroadcastReceiver() {
val usbPermissionReceiver = object : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                device?.let {
                    Log.d("UsbReceiver", "USB Device Attached: $device")
                }
            }
            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                device?.let {
                    Log.d("UsbReceiver", "USB Device Detached: $device")
                }
            }
            ACTION_USB_PERMISSION -> {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                val granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                if (granted) {
                    Log.d("USB_DEBUG", "USB erişim izni verildi: ${device?.deviceName}")
                } else {
                    Log.e("USB_DEBUG", "USB erişim izni reddedildi!")
                }
            }
        }
        if (intent?.action == ACTION_USB_PERMISSION) {
            val granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
            val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)

            if (granted) {
                Log.d("USB_PERMISSION", "USB erişim izni verildi!")
                connectToUsbDevice(context, device)
            } else {
                Log.e("USB_PERMISSION", "USB erişim izni reddedildi!")
            }
        }


    }

}



private fun connectToUsbDevice(context:Context, device: UsbDevice?) {
    if (device == null) {
        Log.e("USB_CONNECT", "Bağlanacak cihaz bulunamadı!")
        return
    }

    val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
    val connection = usbManager.openDevice(device)

    if (connection == null) {
        Log.e("USB_CONNECT", "USB cihazına bağlanılamadı! (İzin problemi olabilir!)")
    } else {
        Log.d("USB_CONNECT", "USB cihazına başarıyla bağlanıldı: ${device.deviceName}")
        // Cihaz ile iletişime geçmek için bağlantıyı burada kullanabilirsin
        registerReceiver(context,usbPermissionReceiver, IntentFilter(ACTION_USB_PERMISSION), RECEIVER_EXPORTED)
    }
}

fun registerUsbReceiver(context: Context) {
    /*val filter = IntentFilter(ACTION_USB_PERMISSION)
    ContextCompat.registerReceiver(
        context, usbPermissionReceiver, filter, RECEIVER_EXPORTED
    )

     */

    val filter2 = IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED)
    filter2.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
    filter2.addAction(ACTION_USB_PERMISSION)
    ContextCompat.registerReceiver(
        context, usbPermissionReceiver, filter2, RECEIVER_EXPORTED
    )
}

/*class usbReceiver = BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // USB bağlanma/çıkartma işlemleri burada ele alınır
        val action = intent?.action
        if (UsbManager.ACTION_USB_DEVICE_ATTACHED == action) {
            // USB cihaz bağlandı
            val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
            device?.let {
                // Cihaz üzerinde işlem yapılabilir
            }
        } else if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
            // USB cihaz çıkarıldı
        }
    }
}

 */
