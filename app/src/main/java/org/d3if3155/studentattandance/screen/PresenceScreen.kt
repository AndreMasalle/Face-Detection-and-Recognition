package org.d3if3155.studentattandance.screen

import android.content.ContentResolver
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import org.d3if3155.studentattandance.R
import org.d3if3155.studentattandance.model.Datanya
import org.d3if3155.studentattandance.model.User
import org.d3if3155.studentattandance.network.ApiStatus
import org.d3if3155.studentattandance.network.ModuleApi
import org.d3if3155.studentattandance.network.UserDataStore
import org.d3if3155.studentattandance.ui.theme.StudentAttandanceTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresenceScreen(navController: NavHostController) {

    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(initial = User())

    val viewModel : PresenceViewModel = viewModel()
    var showDataDialog by remember { mutableStateOf(false) }
//
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver,it)
        if (bitmap!=null) showDataDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                },
                title = {
                    Text(text = "Mobile Programming")
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = false,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(options)

                }
            )
            {
                Icon(imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.tambah),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->
        PresenceContent(Modifier.padding(padding),user.email,viewModel)

        if (showDataDialog){
            DataDialog(bitmap = bitmap, onDismissRequest = { showDataDialog=false }){ nama, namaLatin ->
                viewModel.saveData(user.email, nama, namaLatin, bitmap!!)
                showDataDialog = false
            }
        }


    }
}

@Composable
fun PresenceContent(
    modifier: Modifier,
    userId:String,
    viewModel: PresenceViewModel) {

    val data by viewModel.data
    val status by viewModel.status.collectAsState()
    //sementara




    LaunchedEffect(userId){
        viewModel.retrieveData(userId)
    }

    when(status){
        ApiStatus.LOADING ->{
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
            Text(text = "Loading")
        }

        ApiStatus.SUCCESS ->{
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PersenKehadiran()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data){
                ListGambar(userId,datanya = it, viewModel)
            }
        }
    }
        }

        ApiStatus.FAILED ->{
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.error))
                Button(
                    onClick = { viewModel.retrieveData(userId)},
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }

    }


}

@Composable
fun PersenKehadiran() {


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
                text = "Presence History",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

}

@Composable
fun ListGambar(userId: String,datanya: Datanya, viewModel: PresenceViewModel) {
    var onShowDeleting by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, color = Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.ayam),
//            contentDescription = "sementara",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(4.dp)
//        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    ModuleApi.getHewanUrl(datanya.imageId)
                )
                .crossfade(true)
                .build(),
            contentDescription = "stringResource(id = R.string.gambar,hewan.nama)",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.baseline_broken_image_24),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
                // alpha = transparansi
                .padding(4.dp)
        ) {
            Text(text = datanya.nama, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = datanya.namaLatin, fontWeight = FontWeight.Bold, color = Color.White)
        }
        if (datanya.mine == 1){
            IconButton(onClick = {
                onShowDeleting = true
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.hapus),
                    tint = Color.White
                )
            }
        }
        if (onShowDeleting){
            hapusDialog(onDismissRequest = { onShowDeleting = false }, onConfirmation = {
                onShowDeleting = false
                viewModel.deletingData(userId, id = datanya.id)
            }, id = datanya.id, datanya = datanya)
        }
    }


}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful){
        Log.e("IMAGE","Error: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
        MediaStore.Images.Media.getBitmap(resolver,uri)
    }else{
        val source = ImageDecoder.createSource(resolver,uri)
        ImageDecoder.decodeBitmap(source)
    }

}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PresencePreview() {
    StudentAttandanceTheme {
        PresenceScreen(rememberNavController())
    }
}