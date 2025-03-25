package com.profplay.knowledgebox.viewModel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.profplay.knowledgebox.data.repository.UsbRepository
import com.profplay.knowledgebox.helper.controldevices.UsbManagerHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

private const val USB_PERMISSION = "com.profplay.knowledgebox.USB_PERMISSION"

class UsbViewModel(context: Context) : ViewModel() {
    private val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
    private val usbRepository = UsbRepository(usbManager)
    private val appContext = context.applicationContext  // Context'i burada saklıyoruz

    private val _usbDevice = MutableStateFlow<UsbDevice?>(null)
    val usbDevice = _usbDevice.asStateFlow()

    private val _usbData = MutableStateFlow<String?>(null)
    val usbData = _usbData.asStateFlow()

    private val _usbDataState = mutableStateOf("")
    val usbDataState: State<String> = _usbDataState


    private val _usbPermissionGranted = MutableStateFlow(false)
    val usbPermissionGranted = _usbPermissionGranted.asStateFlow()

    /*private val usbReceiver = UsbManagerHelper.createUsbReceiver() { granted ->
        _usbPermissionGranted.value = granted
    }*/

    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                    Log.d("USB", "USB cihazı bağlandı.")
                    _usbDevice.value = UsbManagerHelper.findUsbDevice(usbManager)
                }

                UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                    Log.d("USB", "USB cihazı çıkarıldı.")
                    _usbDevice.value = null
                    _usbPermissionGranted.value = false
                }

                USB_PERMISSION -> {
                    //ToDo: device null geliyor
                    val device: UsbDevice? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                    } else {
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                    }
                    Log.d("USB", "Intent Action: ${intent.action}")
                    //ToDo: İzin verilmesine rağmen granred false geliyor?
                    val granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                    if (granted) {
                        Log.d("USB", "İzin verildi: $device")
                        _usbPermissionGranted.value = true  // İzin verildiyse durumu güncelle
                    } else {
                        Log.d("USB", "İzin reddedildi: $device")
                        _usbPermissionGranted.value = false  // İzin reddedildiyse durumu güncelle
                    }
                    //_usbPermissionGranted.value = granted  // İzin durumunu güncelle!
                }
            }
        }
    }


    init {
        // USB bağlantısını ve izin durumunu dinle
        val filter = IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED).apply {
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            addAction(USB_PERMISSION)  // İzin durumunu da dinliyoruz!
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appContext.registerReceiver(usbReceiver, filter, Context.RECEIVER_EXPORTED)
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                appContext.registerReceiver(usbReceiver, filter, Context.RECEIVER_EXPORTED)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel temizlenirken receiver'ı unregister et
        appContext.unregisterReceiver(usbReceiver)
    }



    fun scanUsbDevice() {
        _usbDevice.value = UsbManagerHelper.findUsbDevice(usbManager)
        _usbDevice.value?.let { usbDevice ->
            UsbManagerHelper.requestUsbPermission(appContext, usbManager, usbDevice)
        }
    }

    fun startReadingUsbData() {
        _usbDevice.value?.let { device ->
            viewModelScope.launch {
                usbRepository.readUsbData(device) { data ->
                    _usbData.value = data
                    Log.d("USB_VIEWMODEL", "Updating USB Data: $data")

                    _usbDataState.value = data
                }
            }
        }
    }
}
