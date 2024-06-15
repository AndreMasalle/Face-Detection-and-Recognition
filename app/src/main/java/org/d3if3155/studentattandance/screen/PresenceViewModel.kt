package org.d3if3155.studentattandance.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3155.studentattandance.model.Datanya
import org.d3if3155.studentattandance.network.ApiStatus
import org.d3if3155.studentattandance.network.ModuleApi
import java.io.ByteArrayOutputStream

class PresenceViewModel : ViewModel() {


    var data = mutableStateOf(emptyList<Datanya>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set


    fun retrieveData(userId: String){
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = ModuleApi.service.getHewan(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception){
                Log.d("PresenceViewModel", "Failure: ${e.message} ")
                status.value = ApiStatus.FAILED
            }
        }
    }


    fun saveData(
        userId: String,
        nama: String,
        namaLatin: String,
        bitmap: Bitmap
    ){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ModuleApi.service.postHewan(
                    userId,
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    namaLatin.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            }catch (e:Exception){
                Log.d("MainVIewModel","Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deletingData(userId: String,id: Long){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = ModuleApi.service.deleteHewan(userId = userId,id = id.toString())
                if (result.status == "success")
                    retrieveData(userId = userId)
                else
                    throw Exception(result.message)
            }catch (e: Exception){
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }


    private fun Bitmap.toMultipartBody(): MultipartBody.Part{
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG,80,stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(),0,byteArray.size)

        return MultipartBody.Part.createFormData("image","image.jpg",requestBody)
    }

    fun clearMessage() {errorMessage.value = null}
}