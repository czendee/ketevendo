package com.youtochi.ktvendo;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

/*                Intent serviceIntent = new Intent(SplashActivity.this, ControlesBotones.class);
                serviceIntent.putExtra("CualRobot", "01");
                serviceIntent.putExtra("CaracteristicasRobotName", "CoquitoConasupo");
                serviceIntent.putExtra("CaracteristicasRobotTipo", "robotTendero");
                serviceIntent.putExtra("CaracteristicasRobotManada", "no");

                startService(serviceIntent);

                Uri webpage = Uri.parse("https://www.pscp.tv/tochizendejas");

                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);


//                Intent mainIntent = new Intent(SplashActivity.this,InitialActivity.class);
//                SplashActivity.this.startActivity(mainIntent);
//                SplashActivity.this.finish();
*/
            }
        }, SPLASH_DISPLAY_LENGTH);


    }

}
