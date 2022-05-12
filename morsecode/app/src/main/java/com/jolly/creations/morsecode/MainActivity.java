package com.jolly.creations.morsecode;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private CameraManager mCameraManager;

    private String mCameraId;

    private LinearLayout sound;

    private LinearLayout light;

    private LinearLayout text;

    EditText input;

    TextView encoded_text;

    ToneGenerator toneGen1;

    Dialog dialog;
    RotateAnimation rotate;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        encoded_text=findViewById(R.id.enoded_text);
        input=findViewById(R.id.input);
//loader
        dialog = new Dialog(MainActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        dialog.setContentView(R.layout.dialog); // change to dialog.setContentView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        }
        
        rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setRepeatCount(Animation.INFINITE);
        
        final ImageView IVcon = (ImageView) dialog.findViewById(R.id.progress);
        ImageView close = (ImageView) dialog.findViewById(R.id.close);
    
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            
                dialog.dismiss();
                
            
            }
        });
        
        //IVcon.setBackgroundResource(R.drawable.animation);
    
        IVcon.startAnimation(rotate);

       // final AnimationDrawable animcon = (AnimationDrawable) IVcon.getBackground(); // instead of getDrawable() use this
        dialog.setCancelable(false);
//for sound 
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
//for light checking
        if (!isFlashAvailable) {
            showNoFlashError();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {

                mCameraId = mCameraManager.getCameraIdList()[0];

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        }
//soundbutton
        sound = findViewById(R.id.sound);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                IVcon.startAnimation(rotate);
    //handler -> send process messages to queue
                Handler h =new Handler() ;
                h.postDelayed(new Runnable() {
                    public void run() {
                        //put your code here
                        if(dialog.isShowing())
                        {
                            String s=input.getText().toString();
                            encoded_text.setText("");
                            StringBuilder t= new StringBuilder();
    //converting morse code
                            for (int i = 0;i<s.length(); i++)
                            {
                                t.append(morseEncode(s.charAt(i)));
                            }
                            encoded_text.setText(t.toString());
        
        //sounding morse code
                            for (int i = 0;i<s.length(); i++)
                            {
                                morseseps(morseEncode(s.charAt(i)));
                            }
                        }
                        dialog.dismiss();
                    }
        
                }, 1000);
            }
            
        });
// morse code text
        text = findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                IVcon.startAnimation(rotate);
    
                Handler h =new Handler() ;
                h.postDelayed(new Runnable() {
                    public void run() {
                        String s=input.getText().toString();
                        encoded_text.setText("");
                        StringBuilder t= new StringBuilder();
    // converting morse code
                        for (int i = 0;i<s.length(); i++)
                        {
                            t.append(morseEncode(s.charAt(i)));
                        }
                        encoded_text.setText(t.toString());
                        dialog.dismiss();
                    }
        
                }, 1000);
                
                

            }
        });
// light morse code
        light = findViewById(R.id.light);
        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                IVcon.startAnimation(rotate);
    
                Handler h =new Handler() ;
                h.postDelayed(new Runnable() {
                    public void run() {
                        String s=input.getText().toString();
                        encoded_text.setText("");
                        StringBuilder t= new StringBuilder();
        // converting morse code
                        for (int i = 0;i<s.length(); i++)
                        {
                            t.append(morseEncode(s.charAt(i)));
                        }
                        encoded_text.setText(t.toString());
    // converting to light
                        for (int i = 0;i<s.length(); i++)
                        {
                            //encoded_text.setText(encoded_text.getText().toString()+morseEncode(s.charAt(i)));
                            morsesepl(morseEncode(s.charAt(i)));
        
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            try {
                                mCameraManager.setTorchMode(mCameraId, false);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                    }
        
                }, 1000);
    
                Handler h1 =new Handler() ;
                h1.postDelayed(new Runnable() {
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            try {
                                mCameraManager.setTorchMode(mCameraId, false);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                    }
        
                }, 100);
                
                

            }
        });

    }

    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }
// morse table
    String morseEncode(char x) {

        // refer to the Morse table
        // image attached in the article
        switch (x) {
            case 'a':
                return ".- ";
            case 'b':
                return "-... ";
            case 'c':
                return "-.-. ";
            case 'd':
                return "-.. ";
            case 'e':
                return ". ";
            case 'f':
                return "..-. ";
            case 'g':
                return "--. ";
            case 'h':
                return ".... ";
            case 'i':
                return ".. ";
            case 'j':
                return ".--- ";
            case 'k':
                return "-.- ";
            case 'l':
                return ".-.. ";
            case 'm':
                return "-- ";
            case 'n':
                return "-. ";
            case 'o':
                return "--- ";
            case 'p':
                return ".--. ";
            case 'q':
                return "--.- ";
            case 'r':
                return ".-. ";
            case 's':
                return "... ";
            case 't':
                return "- ";
            case 'u':
                return "..- ";
            case 'v':
                return "...- ";
            case 'w':
                return ".-- ";
            case 'x':
                return "-..- ";
            case 'y':
                return "-.-- ";
            case 'z':
                return "--.. ";
            case '1':
                return ".---- ";
            case '2':
                return "..--- ";
            case '3':
                return "...-- ";
            case '4':
                return "....- ";
            case '5':
                return "..... ";
            case '6':
                return "-.... ";
            case '7':
                return "--... ";
            case '8':
                return "---.. ";
            case '9':
                return "----. ";
            case '0':
                return "----- ";

                default:return "";
        }

    }


    void morsesepl(String s)
    {
        for (int i = 0;i<s.length(); i++)
        {
            switch(s.charAt(i))
            {
                case '.': dotl(); break; // represet dot as single short flash
                case '-': dashl(); break;// represet dash as single long flash
                case '/': slashl(); break;// stops the light
                case '|': slashl(); break;
            }

        }
    }

    void morseseps(String s)
    {
        for (int i = 0;i<s.length(); i++)
        {
            switch(s.charAt(i))
            {
                case '.': dots(); break;// represet dot as single short tone sound
                case '-': dashs(); break;// represet dash as single long tone sound
                case '/': slashs(); break;// stops the tone
                case '|': slashs(); break;
                default:dialog.dismiss();break;
            }

        }
    }

    void dotl()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
               // encoded_text.setText(encoded_text.getText().toString()+".");
                mCameraManager.setTorchMode(mCameraId, false); //torch off
                Thread.sleep(100);//delay
                mCameraManager.setTorchMode(mCameraId, true); // torch on

            } catch (CameraAccessException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void dashl()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
               // encoded_text.setText(encoded_text.getText().toString()+"-");
                mCameraManager.setTorchMode(mCameraId, false); // torch off
                Thread.sleep(400);//delay
                mCameraManager.setTorchMode(mCameraId, true); // torch on

            } catch (CameraAccessException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void slashl()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode(mCameraId, false);
                toneGen1.stopTone(); // tone stops
            } catch (CameraAccessException  e) {
                e.printStackTrace();
            }
        }
    }

    void dots()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {

                toneGen1.stopTone();
                Thread.sleep(100);//delay
                toneGen1.startTone(ToneGenerator.TONE_CDMA_HIGH_L,100); //starts tone

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void dashs()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                toneGen1.stopTone();
                Thread.sleep(400);//delay
                toneGen1.startTone(ToneGenerator.TONE_CDMA_HIGH_L,400); //starts tone

            } catch ( InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void slashs()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode(mCameraId, false);
                toneGen1.stopTone(); // tone stops
            } catch (CameraAccessException  e) {
                e.printStackTrace();
            }
        }
    }


    public void switchFlashLight(boolean status) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (status) {
                    mCameraManager.setTorchMode(mCameraId, true); // torch on
                }
                else {
                    mCameraManager.setTorchMode(mCameraId, false); // torch off
                }



            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
