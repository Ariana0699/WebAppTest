package com.androidapp.callwebapplicationtest

import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    private var webAppInterface: WebAppInterface? = null
    private var guiController: GuiController? = null
    lateinit var webView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        guiController = try {
            GuiController.getInstance(applicationContext)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        webView = findViewById(R.id.webview)
        webAppInterface = try {
            WebAppInterface(this, webView)
        } catch (e: java.lang.Exception) {
            throw java.lang.RuntimeException(e)
        }

        guiController!!.setWebAppInterface(webAppInterface)

        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                view.addJavascriptInterface(webAppInterface!!, "Android")
            }
        })
        webView.settings.javaScriptEnabled = true
        webView.getSettings().javaScriptCanOpenWindowsAutomatically = true
        webView.getSettings().allowFileAccess = true
        webView.getSettings().allowContentAccess = true
        webView.addJavascriptInterface(webAppInterface!!, "AndroidBridge")

        webView.loadUrl("https://htmltest-theta.vercel.app/background.html")

        WebView.setWebContentsDebuggingEnabled(true)
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d(
                    "WebView", consoleMessage.message() + " -- From line " +
                            consoleMessage.lineNumber() + " of " +
                            consoleMessage.sourceId()
                )
                return true
            }
        })
    }
}