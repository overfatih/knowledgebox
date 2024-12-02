package com.profplay.knowledgebox.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.profplay.knowledgebox.ui.theme.KnowledgeBoxTheme

@Composable
fun MainScreen(name: String, navController: NavController) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inverseSurface)
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(1f)
                .padding(bottom = 50.dp)
            ) {
            MainButton("Knowledge Pool", Icons.Filled.Done){navController.navigate("knowledge_details_screen")}
            MainButton("Game",Icons.Filled.Done){ navController.navigate("game_screen") }
        }
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(1f)
                .padding(bottom = 50.dp)
        ) {
            MainButton("Setting",Icons.Filled.Done){ navController.navigate("setting_screen") }
            MainButton(" ",Icons.Filled.Done){  }
        }
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(1f)
                .padding(bottom = 50.dp)
        ) {
            MainButton(" ", Icons.Filled.List){ }
            MainButton(" ", Icons.Filled.Call){ }
        }
    }
}

@Composable
fun MainButton(buttomDescription:String, iconVector:ImageVector, onClick: () -> Unit){

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(150.dp, 150.dp)
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
    ) {
        Icon(
            iconVector,
            contentDescription = buttomDescription,
            tint = MaterialTheme.colorScheme.inverseOnSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    KnowledgeBoxTheme {
        val navControllerPreview = rememberNavController()
        MainScreen("Knowledge Box", navControllerPreview)
    }
}