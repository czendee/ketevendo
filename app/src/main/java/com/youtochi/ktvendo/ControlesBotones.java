package com.youtochi.ktvendo;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by martha on 09/10/2017.
 */
public class ControlesBotones extends Service implements View.OnTouchListener,
                                                         View.OnDragListener {



        Button mButtonIzq;
        Button mButtonArriba;
        Button mButtonAbajo;
        Button mButtonDerecho;
        ImageButton mImgButtonIzq;
        WindowManager.LayoutParams paramsIzq;
        ImageButton mImgButtonArriba;
        //cz oct 2018    ImageButton mImgButtonAbajo;
        ImageButton mImgButtonDerecho;
        WindowManager.LayoutParams paramsDerecho;
        //boton para cerrar: oculta los controles
        ImageButton mImgButtonEnds;

        //boton para persocpe: abre app periscope
        ImageButton mImgButtonPeris;

    //boton para persocpe: abre app facelive
    ImageButton mImgButtonFacelive;

    //boton para personaje: se usara para drag and drop
        ImageButton mImgButtonPerso;
        WindowManager.LayoutParams paramsPerso;

        //boton para personaje 02: se usara para drag and drop
//    ImageButton mImgButtonPerso02;
//    WindowManager.LayoutParams paramsPerso02;
        WindowManager.LayoutParams paramsPeris;
        WindowManager.LayoutParams paramsFacelive;
        WindowManager.LayoutParams paramsTop;
        WindowManager.LayoutParams paramsCerrar;

        //ventana para text: se usara para desplegar texto
        EditText mEditPeris;

        EditText mEditPeris02;
        EditText mEditPeris03;
        EditText mEditPeris04;

    WindowManager.LayoutParams paramsEditPeris;

        LinearLayout mRelLayout;//aqui flota el textEdit y su boton


        LinearLayout mRelLayoutWhats01;//aqui flota el whatsappacontact01
        LinearLayout mRelLayoutWhats02;//aqui flota el whatsappacontact01
        LinearLayout mRelLayoutWhats03;//aqui flota el whatsappacontact01

        //cz 15 oct 2018 add three butons for contacts whatsspp
//    ImageButton mImgButtonWhatsPersona01;
        WindowManager.LayoutParams paramsButtonWhatsPersona01;
        //    ImageButton mImgButtonWhatsPersona02;
        WindowManager.LayoutParams paramsButtonWhatsPersona02;
        //    ImageButton mImgButtonWhatsPersona03;
        WindowManager.LayoutParams paramsButtonWhatsPersona03;

        TextView  mTextWhats01;
        TextView  mTextWhats02;
        TextView  mTextWhats03;
        //cz 16 oct 2018 flag to hide or show botones hijos whats
        boolean botoneshijos;


        boolean botoneshijosjijos01;
        boolean botoneshijosjijos02;
        boolean botoneshijosjijos03;

        WindowManager wm;

        String cualRobotControlamos="00";

        String cualRobotCaracteristicas="NA";

        private DataDescriptorTienda passedDatosDelTienda=null;

        //para manejar la inclinacion y comandos
        private SensorManager mSensorM;
        private Sensor mAcelerador;
        public SensorEventListener listen;

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }


        @Override
        public void onCreate() {
            super.onCreate();


            System.out.println("onCreate       Robot - empiezo");

//1st        defineBotonesControlesYListeners();

            System.out.println("onCreate       Robot - termino");


        }

        @Override
        public void onStart(Intent intent, int startId) {
            super.onStart(intent, startId);
            Bundle extras = intent.getExtras();
//START:obten los dtos del robot que controlaremos

            if(extras == null) {
                Log.d("Service","null");
            } else {
                Log.d("Service","not null");
                String from = (String) extras.get("CualRobot");
                cualRobotControlamos=from;
                String caractRobot = (String) extras.get("CaracteristicasRobotName");


                passedDatosDelTienda=new DataDescriptorTienda(
                        from,
                        "01",
                        (String) extras.get("CaracteristicasRobotName"),
                        (String) extras.get("CaracteristicasRobotTipo"),
                        (String) extras.get("CaracteristicasRobotManada"),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );  //ponemos aqui estas caraacteristicas, para pdoelos usar en el metodo dodne se definen los botones
                cualRobotCaracteristicas=caractRobot;

            }


//END:obten los dtos del robot que controlaremos
            System.out.println("             onStart paso antes de definir botones");
//START:define botones, y listeners para controlar al  robot
            defineBotonesControlesYListeners();
//END:define botones, y listeners para controlar al  robot
            System.out.println("             onStart paso despues de definir botones");

//START:define manager for sensor manager
            mSensorM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mAcelerador=mSensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

//        mSensorM = (SensorManager) getApplicationContext()
//                .getSystemService(SENSOR_SERVICE);
            listen = new SensorListen();
            mSensorM.registerListener(listen, mAcelerador, SensorManager.SENSOR_DELAY_NORMAL);


//END:define manager for sensor manager
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            Toast.makeText(getBaseContext(), "onDestroy", Toast.LENGTH_LONG).show();
            if (mImgButtonIzq != null) {
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonIzq);
                mImgButtonIzq = null;
            }

            if (mImgButtonArriba != null) {
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonArriba);
                mImgButtonArriba = null;
            }
/* cz  oct 2018
        if (mImgButtonAbajo != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonAbajo);
            mImgButtonAbajo = null;
        }
*/
            if (mImgButtonDerecho != null) {
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonDerecho);
                mImgButtonDerecho = null;
            }
            if (mImgButtonEnds != null) {
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonEnds);
                mImgButtonEnds = null;
            }
            if (mImgButtonPeris != null) {
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonPeris);
                mImgButtonPeris = null;
            }

            if (mImgButtonFacelive != null) {
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonFacelive);
                mImgButtonFacelive = null;
            }

            if (mImgButtonPerso != null) {//figure personaje
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonPerso);
                mImgButtonPerso = null;
            }

            if (mRelLayout != null) {//figure personaje
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mRelLayout);
                mRelLayout = null;
            }
/*
        if (mImgButtonPerso02 != null) { //face orientation
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonPerso02);
            mImgButtonPerso02 = null;
        }

*/
            if (mRelLayoutWhats01 != null) {//figure personaje whats app contact 01
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mRelLayoutWhats01);
                mRelLayoutWhats01=null;
            }
            if (mRelLayoutWhats02 != null) {//figure personaje whats app contact 02
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mRelLayoutWhats02);
                mRelLayoutWhats02=null;
            }
            if (mRelLayoutWhats03 != null) {//figure personaje whats app contact 03
                ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mRelLayoutWhats03);
                mRelLayoutWhats03=null;
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d("OverlayButton onTouch", "touched the button");
                stopSelf();
            }
            return true;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {

            if (event.getAction() == DragEvent.ACTION_DRAG_EXITED) {
                Log.d("OverlayButton onDrag", "dragged the button");
                //stopSelf();
            }
            return true;
        }

        public void defineBotonesControlesYListeners(){





            //Edit field

            //ventana para text: se usara para desplegar texto

            mEditPeris = new EditText(this);
            mEditPeris.setBackgroundResource(R.mipmap.buy_cart_small_keliko02);
            mEditPeris.setTextColor(Color.BLUE);
            mEditPeris.setHeight(80);
            mEditPeris.setWidth(80);


            mEditPeris02 = new EditText(this);
            mEditPeris02.setBackgroundResource(R.mipmap.ic_kekiko01_02_small);
            mEditPeris02.setTextColor(Color.BLUE);
            mEditPeris02.setHeight(80);
            mEditPeris02.setWidth(80);

            mEditPeris03 = new EditText(this);
            mEditPeris03.setBackgroundResource(R.mipmap.buy_pay_small_keliko01);
            mEditPeris03.setTextColor(Color.BLUE);
            mEditPeris03.setHeight(80);
            mEditPeris03.setWidth(80);

            mEditPeris04 = new EditText(this);
            mEditPeris04.setBackgroundResource(R.mipmap.buy_store_small_keliko01);
            mEditPeris04.setTextColor(Color.BLUE);
            mEditPeris04.setHeight(80);
            mEditPeris04.setWidth(80);


//cz        mEditPeris.setBackgroundResource(R.mipmap.ic_botonflechadown);


            ImageButton mImgButtonInternal = new ImageButton(this);
            mImgButtonInternal.setBackgroundResource(R.mipmap.ic_bot_google);
            mImgButtonInternal.setMaxHeight(30);
            mImgButtonInternal.setMaxWidth(30);
            //mEditPeris.setOnTouchListener(this);

            mImgButtonInternal.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == (MotionEvent.ACTION_UP)){
                        Toast.makeText(getBaseContext(), "a pagar esa cantidad  01", Toast.LENGTH_LONG).show();


                        //call fnctionality to process the payment of the value entered by user

                        //then hide the view with edit text and button

                        mRelLayout.setVisibility(View.GONE);
                        wm.updateViewLayout(mRelLayout, paramsEditPeris);
                    }
                    else{
                        //Do whatever you want during press
                        ;
                    }
                    return true;
                }
            });

            ImageButton mImgButtonInternal02 = new ImageButton(this);
            mImgButtonInternal02.setBackgroundResource(R.mipmap.ic_bot_whats);
            mImgButtonInternal02.setMaxHeight(30);
            mImgButtonInternal02.setMaxWidth(30);

            ImageButton mImgButtonInternal03 = new ImageButton(this);
            mImgButtonInternal03.setBackgroundResource(R.mipmap.ic_bot_facelive);
            mImgButtonInternal03.setMaxHeight(30);
            mImgButtonInternal03.setMaxWidth(30);

            ImageButton mImgButtonInternal04 = new ImageButton(this);
            mImgButtonInternal04.setBackgroundResource(R.mipmap.ic_bot_google);
            mImgButtonInternal04.setMaxHeight(30);
            mImgButtonInternal04.setMaxWidth(30);

            ImageButton mImgButtonInternal05 = new ImageButton(this);
            mImgButtonInternal05.setBackgroundResource(R.mipmap.ic_bot_peris);
            mImgButtonInternal05.setMaxHeight(30);
            mImgButtonInternal05.setMaxWidth(30);

            paramsEditPeris = new WindowManager.LayoutParams(500,
                    550, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//for edit field this allows type keybord                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);

            paramsEditPeris.gravity = Gravity.BOTTOM | Gravity.CENTER;


            paramsEditPeris.x = 300;
            paramsEditPeris.y = 300;

            mRelLayout = new TableLayout(this);
            TableRow row1=new TableRow(this);
            row1.addView(mEditPeris);
            row1.addView(mImgButtonInternal);
            row1.addView(mImgButtonInternal02);

            TableRow row2=new TableRow(this);
            row2.addView(mEditPeris02);
            row2.addView(mImgButtonInternal04);
            row2.addView(mImgButtonInternal05);

            TableRow row3=new TableRow(this);
            row3.addView(mEditPeris03);
//        row3.addView(mImgButtonInternal0);
//        row3.addView(mImgButtonInternal05);

            TableRow row4=new TableRow(this);
            row4.addView(mEditPeris04);
//        row4.addView(mImgButtonInternal04);
//        row4.addView(mImgButtonInternal05);


            mRelLayout.addView(row1);
            mRelLayout.addView(row2);
            mRelLayout.addView(row3);
            mRelLayout.addView(row4);
            mRelLayout.setVisibility(View.GONE);


            //nuevo boton flotante imagen personaje 1
            //start

            //ends boton flotante personaje 1

            defineBotonParent();
            defineBotonStop();
            defineBotonPeris();
            defineBotonFacelive();
            defineBotonHijo01();
            defineBotonHijo02();
            defineBotonHijo03();
            defineWhatsAppBotonContact01();// set the whatsapp contact button  , set mImgButtonWhatsPersona01, paramsButtonWhatsPersona01
            defineWhatsAppBotonContact02();// set the whatsapp contact button  , set mImgButtonWhatsPersona02, paramsButtonWhatsPersona02
            defineWhatsAppBotonContact03();// set the whatsapp contact button  , set mImgButtonWhatsPersona03, paramsButtonWhatsPersona03

//agregar los botonas al viw que flta en la pantalla

            wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.addView(mImgButtonIzq, paramsIzq);
            wm.addView(mImgButtonArriba, paramsTop);
//cz oct 2018        wm.addView(mImgButtonAbajo, paramsAbajo);
            wm.addView(mImgButtonDerecho, paramsDerecho);
            wm.addView(mImgButtonEnds, paramsCerrar);
            wm.addView(mImgButtonPeris, paramsPeris);
            wm.addView(mImgButtonFacelive, paramsFacelive);
            wm.addView(mImgButtonPerso, paramsPerso);
//cs oct 14 2018        wm.addView(mEditPeris, paramsEditPeris);
            wm.addView(mRelLayout, paramsEditPeris);

//cs oct 15 2018 add three buttons for whatsapp contacts
//agregar los botonas al viw que flta en la pantalla

            wm.addView(mRelLayoutWhats01, paramsButtonWhatsPersona01);
            //wm.addView(mImgButtonWhatsPersona02, paramsButtonWhatsPersona02);

//agregar los botonas al viw que flta en la pantalla

            wm.addView(mRelLayoutWhats02, paramsButtonWhatsPersona02);
            //wm.addView(mImgButtonWhatsPersona01, paramsButtonWhatsPersona01);

//agregar los botonas al viw que flta en la pantalla

            wm.addView(mRelLayoutWhats03, paramsButtonWhatsPersona03);
            //wm.addView(mImgButtonWhatsPersona03, paramsButtonWhatsPersona03);



        }//termina method defineBotonesControlesYListeners



        //logica para manejo de sensores e inlcinacion

        //START:
        public class SensorListen implements SensorEventListener{

            // se asume que el telefono se utilizara de forma horizontal siempre
            //si z se hace negativo es que el usuario esta mirando hacia abajo, y quiere que el robot retroceda
            //si z se hace positvo es que el usuario esta mirando hacia arriba, y quiere que el robot avance
            //si y se hace negativo es que el usuario esta inclinando su cabeza a la derecha, y quiere que el robot avance a la derecha
            //si y se hace positivo es que el usuario esta inclinando su cabeza a la izquierda, y quiere que el robot avance a la izquierda
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x =event.values[0];
                float y =event.values[1];
                float z =event.values[2];
//            System.out.println("Actual valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
/*//cz oct 2018
            if(Math.abs(z) > Math.abs(y)){
                //arriba abajo

                if(z<0 && mImgButtonPerso02!=null){ //aajo
                    //inclina su cabeza hacia abajo
                    System.out.println("Abajo-valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
                    mImgButtonPerso02.setBackgroundResource(R.mipmap.abajo);

                }
                if(z>0 && mImgButtonPerso02!=null){//arriba
                    //inclina su cabeza hacia arriba
                    System.out.println("arriba- valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
                    mImgButtonPerso02.setBackgroundResource(R.mipmap.arriba);
                }

            }else{
                //inclina su cabeza a la derecha
                if(y<0 && mImgButtonPerso02!=null){
                    System.out.println("der-valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
                    mImgButtonPerso02.setBackgroundResource(R.mipmap.derecha);
                }
                if(y>0 && mImgButtonPerso02!=null){
                    //inclina su cabeza a la izquierad
                    System.out.println("izq-valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
                    mImgButtonPerso02.setBackgroundResource(R.mipmap.izquierda);
                }
            }
            if ( z> (-1.5) && z < (1.5) && y >(-1.5) && y< (1.5) && mImgButtonPerso02!=null){
                mImgButtonPerso02.setBackgroundResource(R.mipmap.centro);
            }

            */
            }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO Auto-generated method stub

            }


            protected void onResume(){
                // super.onResume();
                mSensorM.registerListener(this,mAcelerador,SensorManager.SENSOR_DELAY_NORMAL);
            }


            protected void onPause(){
                // super.onPause();
                mSensorM.unregisterListener(this);
            }
        }
        //END:


        /*

    defineBotonStop
    botones que se desplegaran cuando el usuario de la tienda quier salir

    t

    */
        public void defineBotonStop(){
            //bot0on para terminar/cerrar los controles de boton


            mImgButtonEnds = new ImageButton(this);
            mImgButtonEnds.setBackgroundResource(R.mipmap.stop_smallito);


            mImgButtonEnds.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == (MotionEvent.ACTION_UP)){
                        //Do whatever you want after press
                        Toast.makeText(getBaseContext(), "Cerramos/Close -", Toast.LENGTH_LONG).show();
                        if (mImgButtonPerso != null) {//figure personaje
                            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mImgButtonPerso);
                            mImgButtonPerso = null;
                        }
                        stopSelf();


                    }
                    else{
                        //Do whatever you want during press
                    }
                    return true;
                }
            });
            paramsCerrar = new WindowManager.LayoutParams(50,
                    50, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);

            paramsCerrar.gravity = Gravity.LEFT | Gravity.TOP;

            //bot0on para terminar/cerrar los controles de boton

        }

        /*

       defineBotonPeris
       botones que se desplegaran cuando el usuario de la tienda de click en Peris

       */
        public void defineBotonPeris(){

            mImgButtonPeris = new ImageButton(this);
            mImgButtonPeris.setBackgroundResource(R.mipmap.ic_peris);


            mImgButtonPeris.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == (MotionEvent.ACTION_UP)){
                        //Do whatever you want after press
                        Toast.makeText(getBaseContext(), "Periscope -"+cualRobotControlamos+cualRobotCaracteristicas, Toast.LENGTH_LONG).show();

                        // stopSelf();
                        Uri webpage = Uri.parse("https://www.pscp.tv/czendee");
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        startActivity(webIntent);



                    }
                    else{
                        //Do whatever you want during press
                    }
                    return true;
                }
            });
            paramsPeris = new WindowManager.LayoutParams(50,
                    50, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);

//cz oct 2018 incio        paramsPeris.gravity = Gravity.LEFT | Gravity.BOTTOM;
//cz oct 2018 incio
            paramsPeris.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            paramsPeris.x = 120;
            paramsPeris.y = 200;

            //cz 2018 fin

        }





    /*

   defineBotonFacelive
   botones que se desplegaran cuando el usuario de la tienda de click en Facelive

   */
    public void defineBotonFacelive(){

        mImgButtonFacelive = new ImageButton(this);
        mImgButtonFacelive.setBackgroundResource(R.mipmap.ic_facelive);


        mImgButtonFacelive.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == (MotionEvent.ACTION_UP)){
                    //Do whatever you want after press
                    Toast.makeText(getBaseContext(), "Face -"+cualRobotControlamos+cualRobotCaracteristicas, Toast.LENGTH_LONG).show();

                    // stopSelf();
                    openFacebookApp();
                    /*
                    Uri webpage = Uri.parse("https://www.facebook.com/carlos.zendejas.75");
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webIntent);
*/
                }
                else{
                    //Do whatever you want during press
                }
                return true;
            }
        });
        paramsFacelive = new WindowManager.LayoutParams(50,
                50, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);

//cz oct 2018 incio
        paramsFacelive.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        paramsFacelive.x = 180;
        paramsFacelive.y = 200;

        //cz 2018 fin

    }

    /*

        defineBotonParent
        botones que se desplegaran cuando el usuario de la tienda a

        tenra  tres botones hijos

        */
        public void defineBotonParent(){
            mImgButtonPerso = new ImageButton(this);
//        mImgButtonPerso.setBackgroundResource(R.mipmap.personaje30vectorized);
            mImgButtonPerso.setBackgroundResource(R.mipmap.ic_kekiko01_01_small);
            mImgButtonPerso.setMaxHeight(50);
            mImgButtonPerso.setMaxWidth(50);
            botoneshijos= false;

            mImgButtonPerso.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;


                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    System.out.println("boton qtienditas");
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            System.out.println("boton qtienditas abre opciones 01");
                            //Update the layout with new X & Y coordinate
////cz oct 2018 incio                        paramsPerso.
//cz oct 2018 incio                        wm.updateViewLayout(v, paramsPerso);

                            if(botoneshijos){
                                //estan mostrados sus botones hijos
                                //procedemos a ocultarl esos botones hijos

                                mImgButtonArriba.setVisibility(View.GONE);
                                wm.updateViewLayout(mImgButtonArriba, paramsTop);
                                mImgButtonDerecho.setVisibility(View.GONE);
                                wm.updateViewLayout(mImgButtonDerecho, paramsDerecho);
                                mImgButtonIzq.setVisibility(View.GONE);
                                wm.updateViewLayout(mImgButtonIzq, paramsIzq);
                                botoneshijos=false;
                            }else{
                                //no estan mostrados sus botones hijos
                                //procedemos a mostrarlos
                                mImgButtonArriba.setVisibility(View.VISIBLE);
                                wm.updateViewLayout(mImgButtonArriba, paramsTop);
                                mImgButtonDerecho.setVisibility(View.VISIBLE);
                                wm.updateViewLayout(mImgButtonDerecho, paramsDerecho);
                                mImgButtonIzq.setVisibility(View.VISIBLE);
                                wm.updateViewLayout(mImgButtonIzq, paramsIzq);
                                botoneshijos=true;
                            }


                        case MotionEvent.ACTION_DOWN:
                            System.out.println("boton qtienditas 01");

                            System.out.println("boton qtienditas 01x:"+paramsPerso.x);
                            System.out.println("boton qtienditas 01y:"+paramsPerso.y);

                            //remember the initial position.
                            initialX = paramsPerso.x;
                            initialY = paramsPerso.y;


                            //get the touch location
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            System.out.println("boton qtienditas 03");
                            //Calculate the X and Y coordinates of the view.
                            paramsPerso.x = initialX - (int) (event.getRawX() - initialTouchX);
                            paramsPerso.y = initialY - (int) (event.getRawY() - initialTouchY);



                            //Update the layout with new X & Y coordinate
                            wm.updateViewLayout(v, paramsPerso);


                            paramsPeris.x = initialX +100- (int) (event.getRawX() - initialTouchX);
                            paramsPeris.y = initialY +0 - (int) (event.getRawY() - initialTouchY);

                            //Update the layout with new X & Y coordinate
                            wm.updateViewLayout(mImgButtonPeris, paramsPeris);

                            wm.updateViewLayout(v, paramsPerso);

                            paramsFacelive.x = initialX +160- (int) (event.getRawX() - initialTouchX);
                            paramsFacelive.y = initialY +0 - (int) (event.getRawY() - initialTouchY);

                            //Update the layout with new X & Y coordinate

                            wm.updateViewLayout(mImgButtonFacelive, paramsFacelive);


                            paramsDerecho.x = initialX -50- (int) (event.getRawX() - initialTouchX);
                            paramsDerecho.y = initialY +100- (int) (event.getRawY() - initialTouchY);


                            //Update the layout with new X & Y coordinate
                            wm.updateViewLayout(mImgButtonDerecho, paramsDerecho);

                            paramsIzq.x = initialX - 50 -(int) (event.getRawX() - initialTouchX);
                            paramsIzq.y = initialY -100- (int) (event.getRawY() - initialTouchY);
                            //Update the layout with new X & Y coordinate
                            wm.updateViewLayout(mImgButtonIzq, paramsIzq);


                            paramsTop.x = initialX - 80 -(int) (event.getRawX() - initialTouchX);
                            paramsTop.y = initialY -0- (int) (event.getRawY() - initialTouchY);
                            //Update the layout with new X & Y coordinate
                            wm.updateViewLayout(mImgButtonArriba, paramsTop);


                            paramsEditPeris.x = initialX - 280 -(int) (event.getRawX() - initialTouchX);
                            paramsEditPeris.y = initialY -0- (int) (event.getRawY() - initialTouchY);
                            //Update the layout with new X & Y coordinate
                            wm.updateViewLayout(mRelLayout, paramsEditPeris);

                            /// contac01 , 02 and 03
                            paramsButtonWhatsPersona01.x = initialX - 180 -(int) (event.getRawX() - initialTouchX);
                            paramsButtonWhatsPersona01.y = initialY +100- (int) (event.getRawY() - initialTouchY);
                            //Update the layout with new X & Y coordinate
                            wm.updateViewLayout(mRelLayoutWhats01, paramsButtonWhatsPersona01);

                            paramsButtonWhatsPersona02.x = initialX - 180 -(int) (event.getRawX() - initialTouchX);
                            paramsButtonWhatsPersona02.y = initialY -0- (int) (event.getRawY() - initialTouchY);
                            //Update the layout with new X & Y coordinate
                            wm.updateViewLayout(mRelLayoutWhats02, paramsButtonWhatsPersona02);

                            paramsButtonWhatsPersona03.x = initialX - 180 -(int) (event.getRawX() - initialTouchX);
                            paramsButtonWhatsPersona03.y = initialY -100- (int) (event.getRawY() - initialTouchY);
                            //Update the layout with new X & Y coordinate
                            wm.updateViewLayout(mRelLayoutWhats03, paramsButtonWhatsPersona03);

                            return true;
                    }
                    System.out.println("boton qtienditas 04");
                    return false;
                }
            });

//        WindowManager.LayoutParams paramsPerso = new WindowManager.LayoutParams(50,
            paramsPerso = new WindowManager.LayoutParams(100,
                    100, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);

            paramsPerso.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            paramsPerso.x = 200;
            paramsPerso.y = 200;

        }



        /*

    defineBotonHijo01
    botones que se desplegaran cuando el usuario de la tienda de click en el botpn padre



     */
        public void defineBotonHijo01(){
            //boton de derecho

            mImgButtonDerecho = new ImageButton(this);
            mImgButtonDerecho.setBackgroundResource(R.mipmap.ic_kekiko01_02_medium);
            // mImgButtonDerecho.setOnTouchListener(this);
            mImgButtonDerecho.setMaxHeight(50);
            mImgButtonDerecho.setMaxWidth(50);
            mImgButtonDerecho.setVisibility(View.GONE);

            mImgButtonDerecho.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == (MotionEvent.ACTION_UP)){
                        //Do whatever you want after press
//                    Toast.makeText(getBaseContext(), "Izq -"+cualRobotControlamos+cualRobotCaracteristicas, Toast.LENGTH_LONG).show();
                        Toast.makeText(getBaseContext(), "Izq ", Toast.LENGTH_LONG).show();

                        // stopSelf();
/* comentar si no hay coneccion web o error pagina url heroku
/* comentar si no hay coneccion web o error pagina url heroku
/* comentar si no hay coneccion web o error pagina url heroku

                    System.out.println("goToAdd Comando LEFT Robot - 2");
                    RequestTaskEnviarComandoTiendaWeb th=new RequestTaskEnviarComandoTiendaWeb();
                    System.out.println("goToAdd Comando LEFT Robot - 3"+passedDatosDelTienda.getName());
//                    th.nombreRutina="saltitos";
                    th.nombreRutina=passedDatosDelTienda.getName();
                    th.secuencia="24";

//                    th.secuencia=listasecuencia;
                    System.out.println("goToAdd Comando LEFT Robot - 4");
                    th.comando="LEFT";
//                    th.comando=listacomandos;
                    System.out.println("goToAdd Comando LEFT Robot - 5");
                    th.tiempo="2344";
//                    th.tiempo=listatiempos;
                    th.status="creado";
//                    TextView txtText = (TextView) findViewById(R.id.textResult);
                    TextView txtText=null;
                    th.execute(txtText); // here the result in text will be displayed
                    System.out.println("goToAdd LEFT ComandoRobot - 4");
*/
                        mImgButtonDerecho.setBackgroundResource(R.mipmap.ic_kekiko01_02_medium);
                        //Update the layout with new X & Y coordinate
                        wm.updateViewLayout(mImgButtonDerecho, paramsDerecho);

                    }
                    else{
                        //Do whatever you want during press
                        mImgButtonDerecho.setBackgroundResource(R.mipmap.ic_kekiko01_02_medium);
                        //Update the layout with new X & Y coordinate
                        wm.updateViewLayout(mImgButtonDerecho, paramsDerecho);
                    }
                    return true;
                }
            });
//        final WindowManager.LayoutParams paramsDerecho = new WindowManager.LayoutParams(100,
            paramsDerecho = new WindowManager.LayoutParams(100,
                    100, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);

//cz oct 2018 incio        paramsDerecho.gravity = Gravity.RIGHT | Gravity.CENTER;
//cz oct 2018 incio
            paramsDerecho.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            paramsDerecho.x = 120;
            paramsDerecho.y = 230;

        }

        /*

    defineBotonHijo02
    botones que se desplegaran cuando el usuario de la tienda de click en el botpn padre

    tendra tres hijos

     */
        public void defineBotonHijo02(){
//boton de arriba
            //cz oct 2018
            mImgButtonArriba = new ImageButton(this);


            mImgButtonArriba.setBackgroundResource(R.mipmap.ic_bot_whats);
            botoneshijosjijos01= false;
            mImgButtonArriba.setMaxHeight(50);
            mImgButtonArriba.setMaxWidth(50);
            mImgButtonArriba.setVisibility(View.GONE);

            mImgButtonArriba.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == (MotionEvent.ACTION_UP)){

                        //Do whatever you want after press
//                    Toast.makeText(getBaseContext(), "Arriba-"+cualRobotControlamos+cualRobotCaracteristicas, Toast.LENGTH_LONG).show();
                        Toast.makeText(getBaseContext(), "Arriba", Toast.LENGTH_LONG).show();
//                    stopSelf(); oculta los cuatro botones

/*               solo mientras no hay repuesta
/* comentar si no hay coneccion web o error pagina url heroku
/* comentar si no hay coneccion web o error pagina url heroku



                    System.out.println("goToAdd Comando Arriba/Adelante Robot - 2");
                    RequestTaskEnviarComandoTiendaWeb th=new RequestTaskEnviarComandoTiendaWeb();
                    System.out.println("goToAdd Comando Arriba/Adelante Robot - 3"+passedDatosDelTienda.getName());
//                    th.nombreRutina="saltitos";
                    th.nombreRutina=passedDatosDelTienda.getName();
                    th.secuencia="24";

//                    th.secuencia=listasecuencia;
                    System.out.println("goToAdd Comando Arriba/Adelante Robot - 4");
                    th.comando="UP";
//                    th.comando=listacomandos;
                    System.out.println("goToAdd Comando Arriba/Adelante Robot - 5");
                    th.tiempo="2344";
//                    th.tiempo=listatiempos;
                    th.status="creado";
//                    TextView txtText = (TextView) findViewById(R.id.textResult);
                    TextView txtText=null;
                    th.execute(txtText); // here the result in text will be displayed
                    System.out.println("goToAdd Arriba/Adelante ComandoRobot - 4");
                    */
                        if(botoneshijosjijos01){
                            //estan mostrados sus botones hijos
                            //procedemos a ocultarl esos botones hijos

                            mRelLayoutWhats01.setVisibility(View.GONE);
                            wm.updateViewLayout(mRelLayoutWhats01, paramsButtonWhatsPersona01);
                            mRelLayoutWhats02.setVisibility(View.GONE);
                            wm.updateViewLayout(mRelLayoutWhats02, paramsButtonWhatsPersona02);
                            mRelLayoutWhats03.setVisibility(View.GONE);
                            wm.updateViewLayout(mRelLayoutWhats03, paramsButtonWhatsPersona03);
                            botoneshijosjijos01=false;
                        }else{
                            //no estan mostrados sus botones hijos
                            //procedemos a mostrarlos
                            mRelLayoutWhats01.setVisibility(View.VISIBLE);
                            wm.updateViewLayout(mRelLayoutWhats01, paramsButtonWhatsPersona01);
                            mRelLayoutWhats02.setVisibility(View.VISIBLE);
                            wm.updateViewLayout(mRelLayoutWhats02, paramsButtonWhatsPersona02);
                            mRelLayoutWhats03.setVisibility(View.VISIBLE);
                            wm.updateViewLayout(mRelLayoutWhats03, paramsButtonWhatsPersona03);
                            botoneshijosjijos01=true;
                        }
                    }
                    else{
                        //Do whatever you want during press
                    }
                    return true;
                }
            });

            paramsTop = new WindowManager.LayoutParams(100,
                    100, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, PixelFormat.TRANSLUCENT);

            paramsTop.gravity = Gravity.TOP | Gravity.CENTER;

            paramsTop.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            paramsTop.x = 420;
            paramsTop.y = 230;

        }

        /*

    defineBotonHijo03
    botones que se desplegaran cuando el usuario de la tienda de click en el botpn padre



    */
        public void defineBotonHijo03(){
            mImgButtonIzq = new ImageButton(this);
            mImgButtonIzq.setBackgroundResource(R.mipmap.buy_store_small_keliko01);
//        mImgButtonIzq.setOnTouchListener(this);

            mImgButtonIzq.setVisibility(View.GONE);

            botoneshijosjijos02= false;
            mImgButtonIzq.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == (MotionEvent.ACTION_UP)){
                        //Do whatever you want after press
//                    Toast.makeText(getBaseContext(), "Der-"+cualRobotControlamos+cualRobotCaracteristicas, Toast.LENGTH_LONG).show();
                        Toast.makeText(getBaseContext(), "Dere", Toast.LENGTH_LONG).show();
                        // stopSelf();

/* comentar si no hay coneccion web o error pagina url heroku
/* comentar si no hay coneccion web o error pagina url heroku
/* comentar si no hay coneccion web o error pagina url heroku


                    System.out.println("goToAdd Comando RIGHT Robot - 2");
//old asp api /accessDB                   RequestTaskAddNuevaRutinaWeb th=new RequestTaskAddNuevaRutinaWeb();
// new nodejs api/mongodb
                    RequestTaskEnviarComandoTiendaWeb th=new RequestTaskEnviarComandoTiendaWeb();
                    System.out.println("goToAdd Comando RIGHT Robot - 3"+passedDatosDelTienda.getName());

//                    th.nombreRutina="saltitos";
                    th.nombreRutina=passedDatosDelTienda.getName();
                    th.secuencia="24";

//                    th.secuencia=listasecuencia;
                    System.out.println("goToAdd Comando RIGHT Robot - 4");
                    th.comando="RIGHT";
//                    th.comando=listacomandos;
                    System.out.println("goToAdd Comando RIGHT Robot - 5");
                    th.tiempo="2344";
                    th.status="creado";
//                    th.tiempo=listatiempos;

//                    TextView txtText = (TextView) findViewById(R.id.textResult);
                    TextView txtText=null;
                    th.execute(txtText); // here the result in text will be displayed
                    System.out.println("goToAdd RIGHT ComandoRobot - 4");

*/
                        mImgButtonIzq.setBackgroundResource(R.mipmap.buy_store_small_keliko01);
                        //Update the layout with new X & Y coordinate


                        if(botoneshijosjijos02){
                            //estan mostrados su vista flotante- hija
                            //procedemos a ocultarl vista flotante  - hija

                            mRelLayout.setVisibility(View.GONE);
                            wm.updateViewLayout(mRelLayout, paramsEditPeris);
                            botoneshijosjijos02=false;
                        }else{
                            //no estan mostrados sus botones hijos
                            //procedemos a mostrarlos
                            mRelLayout.setVisibility(View.VISIBLE);
                            wm.updateViewLayout(mRelLayout, paramsEditPeris);
                            botoneshijosjijos02=true;
                        }

                    }
                    else{
                        //Do whatever you want during press
                        mImgButtonIzq.setBackgroundResource(R.mipmap.buy_store_small_keliko01);
                        //Update the layout with new X & Y coordinate
                        wm.updateViewLayout(mImgButtonIzq, paramsIzq);
                    }
                    return true;
                }
            });


            paramsIzq= new WindowManager.LayoutParams(100,

                    100,

                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,

                    PixelFormat.TRANSLUCENT);

//cz oct 2018 incio        paramsIzq.gravity = Gravity.LEFT | Gravity.CENTER;
//cz oct 2018 incio
            paramsIzq.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            paramsIzq.x = 150;
            paramsIzq.y = 300;


        }



        /*

        Whatsapp contact 01
        botones que se desplegaran cuando el usuario de la tienda le de click al Boton Gat1-arriba

        seran tres botones con las personas mas frecuentes de whatsapp

         */
        public void defineWhatsAppBotonContact01(){


            //Edit field

            //ventana para text: se usara para desplegar texto

            mTextWhats01= new TextView(this);
            mTextWhats01.setBackgroundResource(R.mipmap.ic_whatsapp_small);
            mTextWhats01.setText("Boriti");
            mTextWhats01.setTextColor(Color.BLUE);

            ImageButton  mImgButtonWhatsPersona01 = new ImageButton(this);
            mImgButtonWhatsPersona01.setBackgroundResource(R.mipmap.ic_whatsapp_small);
//        mImgButtonIzq.setOnTouchListener(this);

            mImgButtonWhatsPersona01.setVisibility(View.GONE);

            mImgButtonWhatsPersona01.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == (MotionEvent.ACTION_UP)){
                        Toast.makeText(getBaseContext(), "Whats Persona 01", Toast.LENGTH_LONG).show();
                        // stopSelf();


                        System.out.println("Whats Persona 01 - 2");
                        RequestTaskEnviarComandoTiendaWeb th=new RequestTaskEnviarComandoTiendaWeb();
                        System.out.println("Whats Persona 01 - 3"+passedDatosDelTienda.getName());
                        th.nombreRutina=passedDatosDelTienda.getName();
                        th.secuencia="24";
                        System.out.println("Whats Persona 01- 4");
                        th.comando="RIGHT";
                        System.out.println("Whats Persona 01 - 5");
                        th.tiempo="2344";
                        th.status="creado";
                        TextView txtText=null;
                        th.execute(txtText); // here the result in text will be displayed
                        System.out.println("Whats Persona 01 - 4");


                    }
                    else{
                        //Do whatever you want during press
                    }
                    return true;
                }
            });

            paramsButtonWhatsPersona01= new WindowManager.LayoutParams(100,

                    100,

                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,

                    PixelFormat.TRANSLUCENT);

//cz oct 2018 incio        paramsIzq.gravity = Gravity.LEFT | Gravity.CENTER;
//cz oct 2018 incio
            paramsButtonWhatsPersona01.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            paramsButtonWhatsPersona01.x = 150;
            paramsButtonWhatsPersona01.y = 300;

            mRelLayoutWhats01= new LinearLayout(this);

            mRelLayoutWhats01.addView(mTextWhats01);
            mRelLayoutWhats01.addView(mImgButtonWhatsPersona01);

            mRelLayoutWhats01.setVisibility(View.GONE);

        }//termina method defineWhatsAppBotonContact01

        /*

        Whatsapp contact 02
        botones que se desplegaran cuando el usuario de la tienda le de click al Boton Gat1-arriba

        seran tres botones con las personas mas frecuentes de whatsapp

         */
        public void defineWhatsAppBotonContact02(){

            //Edit field

            //ventana para text: se usara para desplegar texto

            mTextWhats02= new TextView(this);
            mTextWhats02.setBackgroundResource(R.mipmap.ic_whatsapp_small);
            mTextWhats02.setText("Carnavalito");
            mTextWhats02.setTextColor(Color.BLUE);

            ImageButton  mImgButtonWhatsPersona02 = new ImageButton(this);
            mImgButtonWhatsPersona02.setBackgroundResource(R.mipmap.ic_whatsapp_small);
//        mImgButtonIzq.setOnTouchListener(this);
            mImgButtonWhatsPersona02.setVisibility(View.GONE);
            mImgButtonWhatsPersona02.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == (MotionEvent.ACTION_UP)){
                        Toast.makeText(getBaseContext(), "Whats Persona 01", Toast.LENGTH_LONG).show();
                        // stopSelf();


                        System.out.println("Whats Persona 02 - 2");
                        RequestTaskEnviarComandoTiendaWeb th=new RequestTaskEnviarComandoTiendaWeb();
                        System.out.println("Whats Persona 02 - 3"+passedDatosDelTienda.getName());
                        th.nombreRutina=passedDatosDelTienda.getName();
                        th.secuencia="24";
                        System.out.println("Whats Persona 02 - 4");
                        th.comando="RIGHT";
                        System.out.println("Whats Persona 02 - 5");
                        th.tiempo="2344";
                        th.status="creado";
                        TextView txtText=null;
                        th.execute(txtText); // here the result in text will be displayed
                        System.out.println("Whats Persona 02 - 4");


                    }
                    else{
                        //Do whatever you want during press
                    }
                    return true;
                }
            });

            paramsButtonWhatsPersona02= new WindowManager.LayoutParams(100,

                    100,

                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,

                    PixelFormat.TRANSLUCENT);

//cz oct 2018 incio        paramsIzq.gravity = Gravity.LEFT | Gravity.CENTER;
//cz oct 2018 incio
            paramsButtonWhatsPersona02.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            paramsButtonWhatsPersona02.x = 100;
            paramsButtonWhatsPersona02.y = 300;

            mRelLayoutWhats02= new LinearLayout(this);

            mRelLayoutWhats02.addView(mTextWhats02);
            mRelLayoutWhats02.addView(mImgButtonWhatsPersona02);

            mRelLayoutWhats02.setVisibility(View.GONE);

        }//termina method defineWhatsAppBotonContact02

        /*

        Whatsapp contact 03
        botones que se desplegaran cuando el usuario de la tienda le de click al Boton Gat1-arriba

        seran tres botones con las personas mas frecuentes de whatsapp

         */
        public void defineWhatsAppBotonContact03(){

            //Edit field

            //ventana para text: se usara para desplegar texto

            mTextWhats03= new TextView(this);
            mTextWhats03.setBackgroundResource(R.mipmap.ic_whatsapp_small);
            mTextWhats03.setText("Manily");
            mTextWhats03.setTextColor(Color.BLUE);


            ImageButton  mImgButtonWhatsPersona03 = new ImageButton(this);
            mImgButtonWhatsPersona03.setBackgroundResource(R.mipmap.ic_whatsapp_small);
            mImgButtonWhatsPersona03.setVisibility(View.GONE);
            mImgButtonWhatsPersona03.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction() == (MotionEvent.ACTION_UP)){
                        Toast.makeText(getBaseContext(), "Whats Persona 03", Toast.LENGTH_LONG).show();
                        // stopSelf();


                        System.out.println("Whats Persona 03 - 2");
                        RequestTaskEnviarComandoTiendaWeb th=new RequestTaskEnviarComandoTiendaWeb();
                        System.out.println("Whats Persona 03 - 3"+passedDatosDelTienda.getName());
                        th.nombreRutina=passedDatosDelTienda.getName();
                        th.secuencia="24";
                        System.out.println("Whats Persona 03 - 4");
                        th.comando="RIGHT";
                        System.out.println("Whats Persona 03 - 5");
                        th.tiempo="2344";
                        th.status="creado";
                        TextView txtText=null;
                        th.execute(txtText); // here the result in text will be displayed
                        System.out.println("Whats Persona 03 - 4");


                    }
                    else{
                        //Do whatever you want during press
                    }
                    return true;
                }
            });

            paramsButtonWhatsPersona03= new WindowManager.LayoutParams(100,

                    100,

                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,

                    PixelFormat.TRANSLUCENT);

//cz oct 2018 incio        paramsIzq.gravity = Gravity.LEFT | Gravity.CENTER;
//cz oct 2018 incio
            paramsButtonWhatsPersona03.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            paramsButtonWhatsPersona03.x = 50;
            paramsButtonWhatsPersona03.y = 300;

            mRelLayoutWhats03= new LinearLayout(this);

            mRelLayoutWhats03.addView(mTextWhats03);
            mRelLayoutWhats03.addView(mImgButtonWhatsPersona03);

            mRelLayoutWhats03.setVisibility(View.GONE);


        }//termina method defineWhatsAppBotonContact02

    private void openFacebookApp() {
//        String facebookUrl = "www.facebook.com/carlos.zendejas.75";
        String facebookUrl = "https://www.facebook.com/jesus.herrera.1029";
//        String facebookID = "carlos.zendejas.75";
        String facebookID = "jesus.herrera.1029";

        try {
            int versionCode = this.getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

            if(!facebookID.isEmpty()) {
                // open the Facebook app using facebookID (fb://profile/facebookID or fb://page/facebookID)
                Uri uri = Uri.parse("fb://page/" + facebookID);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else if (versionCode >= 3002850 && !facebookUrl.isEmpty()) {
                // open Facebook app using facebook url
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                // Facebook is not installed. Open the browser
                Uri uri = Uri.parse(facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
/*
                Uri webpage = Uri.parse("https://www.facebook.com/carlos.zendejas.75");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
*/
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebook is not installed. Open the browser
            Uri uri = Uri.parse(facebookUrl);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    }



