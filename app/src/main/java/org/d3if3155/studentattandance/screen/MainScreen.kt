package org.d3if3155.studentattandance.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3155.studentattandance.R
import org.d3if3155.studentattandance.navigations.Screen
import org.d3if3155.studentattandance.ui.theme.StudentAttandanceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
//                        navController.navigate(Screen.About.route)
                    }) {
//                        Icon(
//                            imageVector = Icons.Outlined.Info,
//                            contentDescription = stringResource(id = R.string.tentang_aplikasi),
//                            tint = MaterialTheme.colorScheme.primary
//                        )
                    }
                }
            )
        }
    ) {padding ->
        ScreenContent(Modifier.padding(padding), navController)
    }
}

@Composable
fun ScreenContent(modifier: Modifier, navController: NavHostController) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 84.dp),
    ) {
        item{
            AcademicYear()
            MataKuliah(navController)
        }
    }
}

@Composable
fun AcademicYear() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tahun akademik: Genap 2024",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp) // Weight to take up space
            )
        }
    }

}

@Composable
fun MataKuliah( navController: NavHostController ) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mobile Programming",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp) // Weight to take up space
            )
            IconButton(
                onClick = { navController.navigate(Screen.Presence.route) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Trailing Icon",
                    tint =  MaterialTheme.colorScheme.inverseSurface
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Game Development",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp) // Weight to take up space
            )
            IconButton(
                onClick = {  },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = "Trailing Icon",
                    tint =  MaterialTheme.colorScheme.inverseSurface
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.background),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mathematics",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp) // Weight to take up space
            )
            IconButton(
                onClick = {  },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_lock_24),
                    contentDescription = "Trailing Icon",
                    tint =  MaterialTheme.colorScheme.inverseSurface
                )
            }
        }



    }


}





@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainPreview() {
    StudentAttandanceTheme {
        MainScreen(rememberNavController())
    }
}