package com.example.rafael.iaapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    private ImageView btnFlash;
    private Camera camera;
    Camera.Parameters params;
    private boolean isFlashOn;
    private boolean hasFlash;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        btnFlash = (ImageView) findViewById(R.id.btn_flash);
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        isFlashOn = false;

        /*mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraId = mCameraManager.getCameraIdList();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        };
        */

        if (!hasFlash) {
            Toast.makeText(this, "Este dispositivo no tiene flash", Toast.LENGTH_SHORT).show();
        }

        //scannerView =new ZXingScannerView(this);
        scannerView= (ZXingScannerView) findViewById(R.id.zxscan);
        //setContentView(scannerView);
        scannerView.setResultHandler(this);
        scannerView.startCamera();



        btnFlash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                turnOnFlash();

                scannerView.resumeCameraPreview(MainActivity.this);
                Toast.makeText(MainActivity.this, "Flash", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn){
            isFlashOn = true;
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
        }
        else {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            isFlashOn = false;
        }


    }

    /*
    @Override
    protected void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }
    */
    @Override
    public void handleResult(Result result) {
        /*
        Log.v("Resultado", result.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Result");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        */

        Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();

        scannerView.resumeCameraPreview(this);
    }
}
