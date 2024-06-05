package com.androidapp.callwebapplicationtest;

import android.content.Context;


public class GuiHandler {
    private Context context;
    private static GuiHandler instance;
    private GuiController guiController;

    public GuiHandler(Context context) throws Exception {
        this.context = context;
        guiController = GuiController.getInstance(context);

    }

    public static synchronized GuiHandler getInstance(Context context) throws Exception {
        if (instance == null) {
            instance = new GuiHandler(context);
        }
        return instance;
    }

    public void call(String to) {
        guiController.receivingCall(to);
    }

    public void endCall() {
        guiController.callEnded();
    }

    public void acceptCall() {
        guiController.callStarted("Hanna");
    }
}