package com.profplay.knowledgebox.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.profplay.knowledgebox.viewModel.ProfileDetailViewModel

@Composable
fun ProfileDetailScreen(navController: NavController, profileDetailViewModel: ProfileDetailViewModel = viewModel()) {
    val context = LocalContext.current
    val user by profileDetailViewModel.user.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Profil", style = MaterialTheme.typography.headlineMedium)

        user?.let {
            Text(text = "E-posta: ${it.email ?: "Bilinmiyor"}")
            Text(text = "UID: ${it.uid}")
            Text(text = "Puan: ***")
        } ?: Text("Kullanıcı bilgisi alınamadı.")

        Divider()

        Button(onClick = {
            val privacyUrl = "https://profplay.com/apps/engelsiz-kesif/privacy_policy_engelsiz_kesif.html"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
            context.startActivity(intent)
        }) {
            Text("Gizlilik Politikasını Görüntüle")
        }

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            onClick = { showDialog = true },
            enabled = isChecked
        ) {
            Text("Hesabımı Sil")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Text("Hesabımı kendi rızamla silmek istiyorum.")
        }
        Text(text="Not: Hesabınızı sildiğiniz takdirde size ait bütün veriler tamamen silinecektir.")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Hesabı Sil") },
            text = { Text("Hesabınızı kalıcı olarak silmek istediğinizden emin misiniz?") },
            confirmButton = {
                TextButton(onClick = {
                    profileDetailViewModel.deleteUser(
                        onSuccess = {
                            Toast.makeText(context, "Hesap silindi", Toast.LENGTH_SHORT).show()
                            profileDetailViewModel.signOut()
                            profileDetailViewModel.navigateToLoginActivity(context)
                        },
                        onFailure = {
                            Toast.makeText(context, "Hesap silinemedi: ${it?.message}", Toast.LENGTH_LONG).show()
                        }
                    )
                    showDialog = false
                }) {
                    Text("Evet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
}
