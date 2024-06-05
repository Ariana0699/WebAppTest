package com.androidapp.callwebapplicationtest;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

public class WebAppInterface {
    private static WebAppInterface instance;

    private Context context;
    private GuiHandler guiHandler;
    private WebView webView;

    public WebAppInterface(Context c, WebView webView) throws Exception {
        context = c;
        guiHandler = GuiHandler.getInstance(context);
        this.webView = webView;
    }
    public static synchronized WebAppInterface getInstance(Context context, WebView webView) throws Exception {
        if (instance == null) {
            instance = new WebAppInterface(context,webView);
        }
        return instance;
    }

    // processAction is the only method js speaks to
    @JavascriptInterface
    public void processAction(String message) {
        Log.d("WebAppInterface", "Received message: " + message);
        try {
            if (message == null || message.equals("undefined") || message.trim().isEmpty()) {
                throw new JSONException("Message is null, undefined, or empty");
            } else {
                Log.d("WebAppInterface", message);
            }

            JSONObject json = new JSONObject(message);
            String action = json.getString("action");
            JSONObject body = json.optJSONObject("body");
            handleAction(action, body);
        } catch (JSONException e) {
            Log.e("WebAppInterface", "Failed to parse JSON", e);
        }
    }

    private void handleAction(String action, JSONObject body) throws JSONException {
        String from = body != null ? body.optString("from") : null;
        String to = body != null ? body.optString("to") : null;
        String callbackId = body != null ? body.optString("callbackId") : null; // Extract callbackId

        switch (action) {
            case "call":
                if (to != null) {
                    guiHandler.call(to);
                }
                break;
            case "accept-call":
                if (from != null) {
                    guiHandler.acceptCall();
                }
                break;
            case "reject-call":
                guiHandler.endCall();
                break;

            case "end-call":
                guiHandler.endCall();
                break;

            case "fetch":
                if (callbackId != null) {
                    //Log.d("webapp", "responding with callback" + callbackId);
                    //sendResponseToJavaScript(callbackId, dataSaveHelper.getInviteLog().toString());
                }
                break;
        }
    }

    public void ring(String to) {
        ((MainActivity) context).runOnUiThread(() -> webView.evaluateJavascript("controller.ring('" + to + "');", null));
    }
    public void callStarted(String from) {
        ((MainActivity) context).runOnUiThread(() -> webView.evaluateJavascript("controller.callStarted('" + from + "');", null));
    }
    public void callEnded() {
        ((MainActivity) context).runOnUiThread(() -> webView.evaluateJavascript("controller.callEnded();", null));
    }
    public void receivingCall(String from) {
        ((MainActivity) context).runOnUiThread(() -> webView.evaluateJavascript("controller.receivingCall('" + from + "');", null));
    }
}