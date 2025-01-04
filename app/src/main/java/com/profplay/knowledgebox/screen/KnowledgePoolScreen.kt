package com.profplay.knowledgebox.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.profplay.knowledgebox.model.City

@Composable
fun KnowledgePoolScreen(cityList: List<City>, navController: NavController ){
    LazyColumn(
            modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        items(cityList) {
            CityRow(city = it, navController=navController)
            }
    }

}


@Composable
fun CityRow(city: City, navController: NavController) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .clickable {
            navController.navigate(
                "city_details_screen/${city.cityId}"
            )
        }
    ) {
        Text(text=city.name,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(2.dp),
            fontWeight = FontWeight.Bold
        )
    }
}