package com.profplay.knowledgebox.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.profplay.knowledgebox.model.City
import com.profplay.knowledgebox.model.CityWithType

@Composable
fun CityDetailsScreen( city :City, cityWithDetails :List<CityWithType>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Enables scrolling
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Şehir bilgilerini göster
        cityWithDetails.forEach {

        }
        city?.let {
            Text(
                text = "${it.plateNumber} ${it.name}",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(2.dp)
                    .fillMaxWidth(),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            it.avatar?.let { avatar ->
                AsyncImage(
                    model = avatar,
                    contentDescription = it.name,
                    modifier = Modifier.padding(8.dp,16.dp).fillMaxWidth().height(200.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        } ?: run {
            // Veriler yüklenene kadar bir yükleniyor göstergesi
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }


        // Şehir türlerini gruplandır ve ekrana yazdır
        if (cityWithDetails == null) {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        } else {
            val groupedTypes = cityWithDetails.groupBy {it.typeOfCityDetail.typeName}
            groupedTypes.forEach { (typeName, cityWithTypes) ->
                // Tür adı
                Text(
                    text = typeName.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(top = 8.dp),
                    fontWeight = FontWeight.Thin,
                    color = Color.Blue,
                    textAlign = TextAlign.Start
                )
                // Türe ait özellikler
                cityWithTypes.forEach {cityType ->
                    Text(
                        text = cityType.cityDetail.feature,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(vertical = 0.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                }
                Divider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 48.dp)
                )
            }
        }
    }
}
