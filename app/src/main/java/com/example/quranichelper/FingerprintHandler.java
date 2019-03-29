package com.example.quranichelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.media.Image;
import android.os.Build;
import android.os.CancellationSignal;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    //FingerPrintLock lock;
    private static String setMesssage;

    public FingerprintHandler(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        setMesssage = "Authenticating your finger print";


    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.update("There was an Auth Error. " + errString, false);
        setMesssage = "There was an Auth Error. " + errString;

    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Auth Failed. ", false);
        setMesssage =  "Auth Failed. ";

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);
        setMesssage = "Error: ";

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        this.update("You can now access the app.", true);
        setMesssage = "You can now access the app.";

    }

    private void update(String s, boolean b) {

        TextView paraLabel = (TextView) ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = (ImageView) ((Activity)context).findViewById(R.id.fingerprintImage);


        paraLabel.setText(s);

        if(b == false){

            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        } else {

            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            imageView.setImageResource(R.mipmap.action_done_web);

        }

    }
    public  void setSpeacInput(String str)
    {
        setMesssage = str;

    }
    public static String getMessage()
    {
        return  setMesssage;
    }
}
