package com.profplay.knowledgebox.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.profplay.knowledgebox.model.City

@Composable
fun CityDetailsScreen(city: City?){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = city?.name ?: "",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        //ToDo: Avatar int? tanımlı buradaki işlem için ByteArray? lazım
        /*val imageBitmap = city?.avatar?.let as ByteArray { byteArray ->
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)?.asImageBitmap()
        }

        Image(
            bitmap = imageBitmap ?: ImageBitmap.imageResource(id = R.drawable.ic_launcher_background),
            contentDescription = city?.name ?: "",
            modifier = Modifier
                .padding(16.dp)
                .size(300.dp, 200.dp)
        )*/

        Text(
            text = city?.name ?: "",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Text(

            text = "cityDetail.name", /*ToDo: cityDetail.name gelecek viewModel kısmında yap*/
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}