package com.profplay.knowledgebox.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import com.profplay.knowledgebox.model.CityDetail

@Composable
fun CityDetailsScreen(city: City?, cityDetails: List<CityDetail?>){
    Column(
        modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()) // Enables scrolling
        .padding(16.dp)) {
        city?.let {
            Text(
                text = "Şehir Detay",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(2.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            it.avatar?.let { avatar ->
                AsyncImage(
                    model = avatar,
                    contentDescription = city.name,
                    modifier = Modifier.padding(16.dp).size(300.dp, 200.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = it.name,
                style = MaterialTheme.typography.bodyLarge,
            )
        }?: run {
            // Veriler yüklenene kadar bir yükleniyor göstergesi
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }


        /*val imageBitmap = city?.avatar?.let as ByteArray { byteArray ->
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)?.asImageBitmap()
        }

        */
        //ToDo: Avatar String? tanımlı buradaki işlem için avatarin yuklendigi adres lazim

        if (cityDetails.isEmpty()) {
            // Veriler yükleniyor göstergesi
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        else{
            cityDetails.forEach {
                it?.let {
                    /*ToDo: image ismi olacak. R.drawable... yerine ekle*/
                    it.image?.let { image ->
                        AsyncImage(
                            model = image,
                            contentDescription = it.type,
                            modifier = Modifier.padding(16.dp).size(300.dp, 200.dp)
                        )
                    }
                    Text(
                        text = it.type, /*ToDo: cityDetail.type gelecek viewModel kısmında yap*/
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(2.dp),
                        fontWeight = FontWeight.Normal,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = it.feature, /*ToDo: cityDetail.feature gelecek viewModel kısmında yap*/
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.padding(2.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}