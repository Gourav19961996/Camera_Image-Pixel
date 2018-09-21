package com.example.gourav.delhi;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ImageView ivImage;
    Button btOpenCamera;
    public static final int CAMERA_REQUEST = 0;
    Toolbar toolbar;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                String per[] = {Manifest.permission.CAMERA
                };
                requestPermissions(per, 1000);
            }
        }

        ivImage = findViewById(R.id.ivImage);
        btOpenCamera = findViewById(R.id.btOpenCamera);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv = findViewById(R.id.tv);


        btOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, CAMERA_REQUEST);

                final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1000, 1000);
                ivImage.setLayoutParams(params);


                ivImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Bitmap bitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
                        bitmap = Bitmap.createScaledBitmap(bitmap, params.width, params.height, false);
                        int[] viewCorrds = new int[2];
                        ivImage.getLocationOnScreen(viewCorrds);

                        Log.d(TAG, "onTouch bitmapHeight: " + bitmap.getHeight());
                        Log.d(TAG, "onTouch bitmpWidth: " + bitmap.getWidth());
                        Log.d(TAG, "onTouch leftX: " + viewCorrds[0]);
                        Log.d(TAG, "onTouch topY: " + viewCorrds[1]);

                        int x = (int) event.getRawX() - viewCorrds[0];
                        int y = (int) event.getRawY() - viewCorrds[1];
                        Log.d(TAG, "onTouch X: " + x);
                        Log.d(TAG, "onTouch Y: " + y);
                        int pixel = bitmap.getPixel(x, y);
                        Log.d(TAG, "onTouch pixel: " + pixel);
                        String red = String.valueOf(Color.red(pixel));
                        String green = String.valueOf(Color.green(pixel));
                        String blue = String.valueOf(Color.blue(pixel));
                        Log.d(TAG, "onTouch red: " + red);
                        Log.d(TAG, "onTouch green: " + green);
                        Log.d(TAG, "onTouch blue: " + blue);


                        final Dialog dialog = new Dialog(MainActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                        View vh = inflater.inflate(R.layout.dialog, null);
                        TextView etColorView = vh.findViewById(R.id.tvRGB);
                        dialog.setContentView(vh);
                        etColorView.setText("Red(" + red + ")\t" + "Green(" + green + ")\t" + "Blue(" + blue + ")\t");
                        dialog.show();

                        Button btOk = vh.findViewById(R.id.btOk);
                        btOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });


                        return false;


                    }

                });


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ivImage.setImageBitmap(bitmap);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean checkPermission(){
        boolean is=false;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                is = true;
            } else {
                is = false;
            }
        }
        return is;
    }
}

