package com.talhakara.instagram.View


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.talhakara.instagram.ViewModel.DmViewModel


@Composable
fun DmSayfasi(navController: NavController, viewModel: DmViewModel) {
    val dmGenelList = viewModel.getDmGenelList()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Column {
                LazyColumn {
                    items(dmGenelList) { dmGenel ->
                        DmGenelItem(dmGenel)
                    }
                }

            }
            Column {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("DmAtmaSayfa")
                    },
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)

                    // .align(Alignment.End), // Sağ alt köşeye hizalamak için Alignment.BottomEnd kullanılır.
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Mail Icon"
                    )
                }
            }
        }

    }
}

@Composable
fun DmGenelItem(dmGenel: DmGenel) {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Text(text = " ${dmGenel.aliciKA}", style = MaterialTheme.typography.bodyLarge)
        Text(text = " ${dmGenel.sonMesaj}", style = MaterialTheme.typography.titleMedium)
    }
}



data class DmGenel(val aliciKA: String,var sonMesaj: String){
    constructor(): this("","")
}

data class DmOzel(val aliciKA: String,var tumMesajlar: ArrayList<String>){
    constructor(): this("", tumMesajlar=arrayListOf(""))
}