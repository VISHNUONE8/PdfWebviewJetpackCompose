package com.andrayudu.pdfwebviewjetpackcompose

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val isPdfLoading = mutableStateOf(true)
}