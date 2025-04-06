package com.profplay.knowledgebox.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HearingDisabled
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToPlayScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Yardım Menüsü")
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                ExpandableSection(title = "Nasıl Oynanır?") {
                    Text(text = "Oyun 3 farklı şekilde oynanabilir.")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.HearingDisabled,
                            contentDescription = "Hearing Disabled"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "1. Kısım: İlk olarak sorunun doğru cevabı olduğunu düşündüğünüz şıkkın üzerini tıklayarak cevap verebilirsiniz. Bu kısım işitme engelliler için tasarlanmıştır.")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.Gray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Dialpad,
                            contentDescription = "Dialpad"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "2. Kısım: Görme Engelliler için Tuş Takımı (GETT) yardımı ile oynayabilirsiniz. Ayarlar bölümünden GETT aparatını android cihazınıza tanıtabilirsiniz. Oyun esnasında Braille Alfabesini kullanarak a-b-c-d şıklarını GETT yardımıyla işaretleyebilirsiniz.")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.Gray, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "Visibility Off"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "3. Kısım: Görme Engelliler için Sesli yanıt sistemidir. Sorunun ve şıkların sesli olarak okunması bittikten sonra sesli olarak cevap verebilirsiniz.")
                    }
                }
                ExpandableSection(title = "Bilgi Bankası") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.MenuBook,
                            contentDescription = "Menu Book"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "İllerin özelliklerini listeli olarak bulabilceğiniz bölümdür.")
                    }
                }
                ExpandableSection(title = "Ayarlar") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Ayarlar Menüsüdür. USB bağlantı ayarlarını bu bölümden yapabilirsiniz. Profil bölümüne buradan ulaşılır. İstersenizi tamamen hesabınızı silebilirsiniz.")
                    }
                }
                ExpandableSection(title = "Profil") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Kullanıcı hesabı bölümüdür. Çıkış yapabilir, kullanıcı bilgilerinizi görebilirsiniz.")
                    }
                }
                ExpandableSection(title = "Profil Detay") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ManageAccounts,
                            contentDescription = "Profile Detail"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Kayıtlı e-posta adresinizi ve gizlilik politikasına buradan ulaşabilirsiniz.")
                    }
                }
                ExpandableSection(title = "Çıkış") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Exit"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Kullanıcı hesabınızdan çıkabilirsiniz.")
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableSection(
    title: String,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Küçült" else "Genişlet"
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                content() // 👈 Burada artık istediğin UI'yi yazabilirsin
            }
        }
    }
}
