package com.profplay.knowledgebox.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.profplay.knowledgebox.helper.registerUsbReceiver
import com.profplay.knowledgebox.helper.requestUsbPermission
import com.profplay.knowledgebox.helper.usbPermissionReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("MutableCollectionMutableState")
class UsbDeviceViewModel(application: Application): AndroidViewModel(application){
    val usbReceiver = usbPermissionReceiver
    val usbManager = getApplication<Application>().getSystemService(Context.USB_SERVICE) as UsbManager
    val deviceList =  mutableStateOf< HashMap<String?, UsbDevice?>?>(null)
    var selectedDevice: UsbDevice? = null

    // USB'den gelen harf kodunu tutan değişken
    //var receivedData = mutableStateOf<String?>(null)
    private val _receivedData = MutableStateFlow<String?>(null)
    val receivedData: StateFlow<String?> get() = _receivedData

    fun listenToUsbDevices(context: Context) {
        deviceList.value = usbManager.deviceList
        deviceList.value?.let {
            deviceList.value?.values?.forEach { device ->
                Log.d("USBListener", "Device: ${device?.deviceName}")
                selectedDevice = device
            }
        }?:Log.d("USBListener", "No USB Devices Connected")
    }

    fun readUsbInput(context: Context, device: UsbDevice) {
        Log.d("USBReadInputDevice", "Device: ${device?.deviceName?:"notReadDevice"}")
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val connection = usbManager.openDevice(device) ?: return
        val usbInterface = device.getInterface(0)
        val endpoint = usbInterface.getEndpoint(0)

        connection.claimInterface(usbInterface, true)
        val buffer = ByteArray(endpoint.maxPacketSize)
        val bytesRead = connection.bulkTransfer(endpoint, buffer, buffer.size, 1000)

        if (bytesRead > 0) {
            val input = String(buffer, 0, bytesRead).trim()
            Log.d("USBInput", "Input Received: $input")

            // Gelen veriyi güncelle
            _receivedData.value = input
        }

        connection.releaseInterface(usbInterface)
        connection.close()
    }

    fun getUsbDevice(context: Context){
        val filter = IntentFilter().apply {
            addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        }

        context.registerReceiver(usbReceiver, filter)
    }



    fun onDestroyUsb() {
        // Alıcıyı kaldırma
        getApplication<Application>().unregisterReceiver(usbReceiver)
    }



}
