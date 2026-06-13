package com.lucasdpm.cognilink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.lucasdpm.cognilink.ui.navigation.CogniLinkNavGraph
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CogniLinkTheme {
                CogniLinkNavGraph()
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    private fun DefaultPreview() {
        CogniLinkTheme {
            CogniLinkNavGraph()
        }
    }
}