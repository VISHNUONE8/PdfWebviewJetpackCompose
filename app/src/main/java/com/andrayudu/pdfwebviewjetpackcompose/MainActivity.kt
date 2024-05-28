package com.andrayudu.pdfwebviewjetpackcompose


import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andrayudu.pdfwebviewjetpackcompose.ui.theme.PdfWebviewJetpackComposeTheme
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PdfWebviewJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   PdfWebview()
                }
            }
        }
    }
}


@Composable
fun CircularProgressBar(viewModel: MainViewModel = viewModel(), modifier: Modifier ){
    val isPdfLoading = viewModel.isPdfLoading.value
    if (isPdfLoading){
        CircularProgressIndicator(
            modifier = modifier
                .width(100.dp)
                .height(100.dp),
            color = ProgressIndicatorDefaults.circularColor,
            strokeWidth = ProgressIndicatorDefaults.CircularStrokeWidth
        )
    }

}
@Composable
fun PdfWebview(viewModel: MainViewModel = viewModel()) {

//    var url by remember { mutableStateOf("https://stevdza-san.com") }
    var url by remember { mutableStateOf("https://drive.google.com/file/d/168HtsWKFXA5-uqrG21QvQNfEJPvyhORJ/view?usp=sharing") }
    val state = rememberWebViewState(url = url)
    val navigator = rememberWebViewNavigator()
    var textFieldValue by remember(state.content.getCurrentUrl()) {
        mutableStateOf(state.content.getCurrentUrl() ?: "")
    }
    // A custom WebViewClient and WebChromeClient can be provided via subclassing
    val webClient = remember {
        object : AccompanistWebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("Accompanist WebView", "Page finished loading for $url")
                viewModel.isPdfLoading.value = false
            }

            override fun onPageStarted(
                view: WebView?,
                url: String?,
                favicon: Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                Log.d("Accompanist WebView", "Page started loading for $url")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){

        Column {
            Row(modifier = Modifier.padding(all = 12.dp)) {
                BasicTextField(
                    modifier = Modifier.weight(9f),
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    maxLines = 1
                )
                if (!state.errorsForCurrentRequest.isEmpty()) {
                    Icon(
                        modifier = Modifier
                            .weight(1f),
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = Color.Red
                    )
                }
            }



            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = loadingState.progress,
                    modifier = Modifier.fillMaxWidth()
                )

            }

            WebView(state = state,
                modifier = Modifier.weight(1f),
                navigator = navigator,
                onCreated = {webView ->
                    webView.settings.javaScriptEnabled = true
                },
                client = webClient
            )
        }

        CircularProgressBar(modifier = Modifier
            .width(100.dp)
            .height(100.dp))

    }

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PdfWebviewJetpackComposeTheme {
        Greeting("Android")
    }
}