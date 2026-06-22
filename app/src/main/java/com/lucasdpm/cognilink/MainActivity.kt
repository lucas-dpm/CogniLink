package com.lucasdpm.cognilink

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lucasdpm.cognilink.ui.navigation.CogniLinkNavGraph
import com.lucasdpm.cognilink.ui.navigation.Screen
import com.lucasdpm.cognilink.ui.theme.CogniLinkTheme

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            CogniLinkTheme {
                CogniLinkNavGraph(navController = navController)
            }
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val data = intent?.data
        if (data != null && data.host == "cognilink-lucasdpm.firebaseapp.com") {
            val oobCode = data.getQueryParameter("oobCode")
            val mode = data.getQueryParameter("mode")
            
            // Aceita se for o link de reset direto ou o redirecionamento com código
            if (oobCode != null && (mode == null || mode == "resetPassword" || mode == "action")) {
                navController.navigate(Screen.ChangePassword.createRoute(oobCode))
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
