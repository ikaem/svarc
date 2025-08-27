package com.imkaem.android.svarc

import SvarcComposableApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.imkaem.android.svarc.ui.theme.SvarcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            SvarcTheme {
//                /* TODO lets leave this scaffold for now*/
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    SvarcComposableApp(
//                        modifier = Modifier.padding(innerPadding)
//                    )
////                    Greeting(
////                        name = "Android",
////                        modifier = Modifier.padding(innerPadding)
////                    )
//                }
//            }

            SvarcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SvarcComposableApp()
                }
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SvarcTheme {
//        Greeting("Android")
//    }
//}