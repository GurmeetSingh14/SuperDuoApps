package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Gurmeet on 01-01-2016.
 */
public class AlexandriaBarcodeScanner extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    public void handleResult(Result result) {
        Intent resultantIntent = new Intent();
        resultantIntent.putExtra("ISBN", result.getText());
        setResult(RESULT_OK, resultantIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}
