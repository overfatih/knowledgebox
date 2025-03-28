package com.profplay.knowledgebox.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.profplay.knowledgebox.util.UsbViewModelFactory
import com.profplay.knowledgebox.viewModel.UsbViewModel

@Composable
fun UsbScreen(usbViewModel: UsbViewModel) {
    //val context = LocalContext.current  // Burada Context'i alıyoruz
    //val appContext = context.applicationContext
    //val usbViewModel: UsbViewModel = viewModel(factory = UsbViewModelFactory(appContext))

    val usbDevice by usbViewModel.usbDevice.collectAsState()
    val usbPermissionGranted by usbViewModel.usbPermissionGranted.collectAsState()
    val usbData by usbViewModel.usbData.collectAsState()


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("USB Bağlantı Durumu: ${if (usbDevice != null) "Bağlandı" else "Bağlı Değil"}")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { usbViewModel.scanUsbDevice() }) {
            Text("ESP32'yi Tara")
        }
        Log.d("USBDevice", "usbDevice: $usbDevice")
        Log.d("USBPermission", "usbPermissionGranted: $usbPermissionGranted")

        //if (usbDevice != null && usbPermissionGranted) {
        if (usbDevice != null) { //ToDo: izin meselesini halledince üstteki ifi kullan
            Button(onClick = { usbViewModel.startReadingUsbData() }) {
                Text("Veriyi Oku")
            }
        } else {
            Text("USB Cihazı bağlanmadı ya da izin verilmedi")
        }
        usbData?.let {
            Text("Gelen Veri: $it")
            Log.d("USB US", it.toString())
        }
    }
}