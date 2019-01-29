package com.youtochi.ktvendo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MiMapaTiendasActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback {

    private GoogleMap mMap;


    final ArrayList<String> arrayTasks = new ArrayList<>();//store the list of robots from the web API

    final ArrayList<Marker> mTienda = new ArrayList<>();//store the list of map markers robots,with info from the web API

    final ArrayList<DataDescriptorTienda> dTienda = new ArrayList<>();//store the list of map markers robots,with info from the web API

    private String Mode;

    //to hide/show buttons
    FloatingActionButton  fabArriba;
    FloatingActionButton  fabAbajo;
    FloatingActionButton  fabIzq;
    FloatingActionButton  fabDer;
    FloatingActionButton fabPlay;

    private Marker mTiendaBeingUsed;

    private Marker markerBeingUsed;

    public int playing;

    public DataDescriptorTienda tiendaDataBeingUsed;


    //boton para personaje 02: se usara para sensor de movimiento de celulr por posicion
       ImageButton mImgButtonPerso02;
    FloatingActionButton fabSensorPos;

    //para manejar la inclinacion y comandos
    private SensorManager mSensorM;
    private Sensor mAcelerador;
    public SensorEventListener listen;

    ///center del mapa
    LatLng dondeEstoyLatLon;
    public String mapaLat;
    public String mapaLon;


    public boolean useSensorPosForMovement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_mapa_tiendas);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            System.out.println("Detalle robot  ProdId  step 2:");

            mapaLon = extras.getString("cual_lugarlon");//los paso la anterior screen main
            mapaLat = extras.getString("cual_lugarlat");//los paso la anterior screen main

        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        useSensorPosForMovement=false;

        FloatingActionButton fabMode = (FloatingActionButton) findViewById(R.id.mapafab01);
        fabMode.setImageResource(R.mipmap.hats);
        fabMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view,R.string.aqui_esta_en_mapa_robots, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                FloatingActionButton fabModeCambiarlo = (FloatingActionButton) findViewById(R.id.mapafab01);
                if(Mode.equals("mapa")){
                    Mode="floatingControls";
                    //initially the buttons will be hide
                    fabArriba.hide();
                    fabAbajo.hide();
                    fabIzq.hide();
                    fabDer.hide();
                    ///before the next Mode is being used, set the icon  back to the original icon for the previous reboto/marker
                    //change the icon for the robot, based on the type of robot

                    if(tiendaDataBeingUsed!=null ){//antes hubo seleccionado un robot/marker
                        dejaElIconoComoEstaba( );//regreesa el icono al original
                    }

                    if(tiendaDataBeingUsed!=null && tiendaDataBeingUsed.getTransmite()!=null && tiendaDataBeingUsed.getTransmite().equals("periscope")){
                        fabModeCambiarlo.setImageResource(R.mipmap.ic_peris);
                    }else  if(tiendaDataBeingUsed!=null &&  tiendaDataBeingUsed.getTransmite()!=null && tiendaDataBeingUsed.getTransmite().equals("whatsapp")){
                        fabModeCambiarlo.setImageResource(R.mipmap.ic_whatsapp);
                    }else{
                        fabModeCambiarlo.setImageResource(R.mipmap.personaje16vectorized);
                    }
                        mTiendaBeingUsed=null;//if a marker was previously  selected, it is now released
                    tiendaDataBeingUsed=null; //if a robot was previously  selected, it is now released


                }else{
                    Mode="mapa";
                    fabModeCambiarlo.setImageResource(R.mipmap.hats);
                    //aun cuando se abrio el floatingControls,  se quedo en la app como marcado, ahora desmarcarlo
                    if(tiendaDataBeingUsed!=null ){//antes hubo seleccionado un robot/marker
                        dejaElIconoComoEstaba( );//regreesa el icono al original
                    }
                    mTiendaBeingUsed=null;//if a marker was previously  selected, it is now released
                    tiendaDataBeingUsed=null; //if a robot was previously  selected, it is now released


                }

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mapafab02);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,R.string.regresar_a_lista_robots, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //ir a screen de mapa
                Intent i = new Intent(MiMapaTiendasActivity.this, MainActivity.class);

                startActivity(i);
            }
        });


        fabSensorPos = (FloatingActionButton) findViewById(R.id.mapasensorpos);



        fabSensorPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(useSensorPosForMovement){
                    useSensorPosForMovement=false;
                    //show the buttons, the sensor pos will not be used
                    fabArriba.show();
                    fabAbajo.show();
                    fabIzq.show();
                    fabDer.show();
                }else{
                    useSensorPosForMovement=true;
                    //hide the buttons, the sensor pos will  be used
                    fabArriba.hide();
                    fabAbajo.hide();
                    fabIzq.hide();
                    fabDer.hide();

                }
            }
        });
///////////////////////////////////////////mapa controles



        Mode="mapa";
        playing=0;


        fabArriba = (FloatingActionButton) findViewById(R.id.mapaAllfabArriba);//button Arriba
        fabArriba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Mode.equals("mapa")){
                    System.out.println("arriba 0 before:" +mTiendaBeingUsed.getPosition().latitude);
                    double nuevaLat =mTiendaBeingUsed.getPosition().latitude +0.00001;
                    double nuevaLon=mTiendaBeingUsed.getPosition().longitude;

                    System.out.println("robot arriba 1 lat:" +nuevaLat);
                    System.out.println("robot arriba 1 lon:" +nuevaLon);
//                    mTienda01.setPosition(new LatLng(point.latitude, point.longitude));
                    mTiendaBeingUsed.setPosition(new LatLng( nuevaLat, nuevaLon));
                }else{
                    //it should not be display in any other mode
                }



            }
        });

        fabAbajo = (FloatingActionButton) findViewById(R.id.mapaAllfabAbajo);//button Abajo
        fabAbajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Mode.equals("mapa")){
                    System.out.println("robot abajo 0 before:" +mTiendaBeingUsed.getPosition().latitude);
                    double nuevaLat =mTiendaBeingUsed.getPosition().latitude -0.00001;
                    double nuevaLon=mTiendaBeingUsed.getPosition().longitude;

                    System.out.println("robot abajo 1 lat:" +nuevaLat);
                    System.out.println("robot abajo 1 lon:" +nuevaLon);
//                    mTienda01.setPosition(new LatLng(point.latitude, point.longitude));
                    mTiendaBeingUsed.setPosition(new LatLng( nuevaLat, nuevaLon));

                }else{
                    //it should not be display in any other mode
                }



            }
        });

        fabIzq = (FloatingActionButton) findViewById(R.id.mapaAllfabIzq);//button Izq
        fabIzq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Mode.equals("mapa")){
                    System.out.println("robot izq 0 before:" +mTiendaBeingUsed.getPosition().latitude);
                    double nuevaLat =mTiendaBeingUsed.getPosition().latitude ;
                    double nuevaLon=mTiendaBeingUsed.getPosition().longitude -0.00001;


                    System.out.println("robot izq 1 lat:" +nuevaLat);
                    System.out.println("robot izq 1 lon:" +nuevaLon);
//                    mTienda01.setPosition(new LatLng(point.latitude, point.longitude));
                    mTiendaBeingUsed.setPosition(new LatLng( nuevaLat, nuevaLon));



                }else{
                    //it should not be display in any other mode
                }


            }
        });

        fabDer = (FloatingActionButton) findViewById(R.id.mapaAllfabDer);//button Der
        fabDer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Mode.equals("mapa")){
                    System.out.println("robot der 0 before:" +mTiendaBeingUsed.getPosition().latitude);
                    double nuevaLat =mTiendaBeingUsed.getPosition().latitude ;
                    double nuevaLon=mTiendaBeingUsed.getPosition().longitude +0.00001;

                    System.out.println("robot der 1 lat:" +nuevaLat);
                    System.out.println("robot der 1 lon:" +nuevaLon);
//                    mTienda01.setPosition(new LatLng(point.latitude, point.longitude));
                    mTiendaBeingUsed.setPosition(new LatLng( nuevaLat, nuevaLon));




                }else{
                    //it should not be display in any other mode
                }

            }
        });

        //initially the buttons will be hide
        fabArriba.hide();
        fabAbajo.hide();
        fabIzq.hide();
        fabDer.hide();

//initially set the buttons to be images rows
        fabArriba.setImageResource(R.mipmap.arriba);
        fabAbajo.setImageResource(R.mipmap.abajo);
        fabIzq.setImageResource(R.mipmap.izquierda);
        fabDer.setImageResource(R.mipmap.derecha);


/*
se movio esta funcionalidad para el menu tools/settings
// Check if Android M or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(myIntent);
        }
*/

//llmar el que traiga la lista de robots y su info del API
        System.out.println("ListRobots paso 1");
        AsyncListViewLoader task=new AsyncListViewLoader();
        System.out.println("ListRobots paso 2");
        Constants constantes= new Constants();
//        task.parametroURL="http://mexico.brinkster.net/tochi_get_lista_edificios_mobil.asp?operacion=lista";
        task.parametroURL=constantes.URL_LIST_ROBOTS;

        System.out.println("ListRobots paso 3");
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)

            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();


        System.out.println("ListRobots paso 4");

//START:define manager for sensor manager
        mSensorM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAcelerador=mSensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

//        mSensorM = (SensorManager) getApplicationContext()
//                .getSystemService(SENSOR_SERVICE);
        listen = new SensorListen();
        mSensorM.registerListener(listen, mAcelerador, SensorManager.SENSOR_DELAY_NORMAL);


//END:define manager for sensor manager
    }


    private Marker mTienda01;
    private Marker mTienda02;
    private Marker mTienda03;

    private  Marker mTienda04;

    final ArrayList<Marker> arrayMarkers = new ArrayList<>();//store the list of robots from the web API
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        LatLng mexicoCity = new LatLng(19.429, -99.146);


        LatLng robotPosicion01 = new LatLng(19.429, -99.148);

        LatLng robotPosicion02 = new LatLng(19.428, -99.148);

//        BitmapDescriptor bmd=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory. .HUE_AZURE);

        BitmapDescriptor bmd= BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapicon);

        //19.4294359!4d-99.1469533
        mTienda01=  mMap.addMarker(
                 new MarkerOptions().position(mexicoCity).title("Ahora:DISPONIBLE - CDMX Centro MEXICO - Tipo: (Human Walker)").snippet(" Renta por:(5mins) Renta $:(Gratis) Manada:(No) Transmite Periscope:(Si)")
        );
        mTienda01.setIcon(bmd);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexicoCity));

        //19.4294359!4d-99.1469533
        mTienda02= mMap.addMarker(
                new MarkerOptions().position(robotPosicion01).title("Ahora:DISPONIBLE - CDMX Centro MEXICO - Tipo: (MuchasBolas)").snippet(" Renta por:(5mins) Renta $:(Gratis) Manada:(No) Transmite Periscope:(No)")
        );
        mTienda02.setIcon(bmd);


        //19.4294359!4d-99.1469533
        mTienda03= mMap.addMarker(
                new MarkerOptions().position(robotPosicion02).title("Ahora:DISPONIBLE - CDMX Centro MEXICO - Tipo: (MuchasBolas)").snippet(" Renta por:(10mins) Renta $:(Gratis) Manada:(Si) Transmite Periscope:(Si)")
        );
        mTienda03.setIcon(bmd);


        //gps position passed as paramter, will set the center of the map: start
        System.out.println("zooooom onMapReady 1 antes de checar los" );
        if(mapaLon!=null && mapaLat!=null && !mapaLon.equals("null") && !mapaLat.equals("null")){
            System.out.println("zooooom onMapReady 2" );
            dondeEstoyLatLon=new LatLng( Double.valueOf( mapaLat),Double.valueOf( mapaLon));
            System.out.println("zooooom onMapReady 2.1" );
            System.out.println("zooooom onMapReady 2.1" );
        }else{
            System.out.println("zooooom onMapReady 3" );
            dondeEstoyLatLon=mexicoCity;
        }

        System.out.println("zooooom onMapReady 4" );
        //gps position passed as paramter, will set the center of the map: end


        System.out.println("zooooom original" +mMap.getMaxZoomLevel());
//  old 1      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mexicoCity,mMap.getMaxZoomLevel()-14));
//old 2        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mexicoCity,mMap.getMaxZoomLevel()-4));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dondeEstoyLatLon,mMap.getMaxZoomLevel()-10));
        System.out.println("zooooom menos" );
        mMap.setBuildingsEnabled(true);





        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(19.429, -98.146))
                .title("San Francisco")
                .snippet("Population: 776733"))
                .setIcon(bmd);

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        int icual=0;

        ///before the next marker/robot being used, set the icon  back to the original icon for the previous reboto/marker
        //change the icon for the robot, based on the type of robot

        if(tiendaDataBeingUsed!=null ){//antes hubo seleccionado un robot/marker
            dejaElIconoComoEstaba( );//regreesa el icono al original


        }

        mTiendaBeingUsed=null;//the marker you  selected, it is now released
        tiendaDataBeingUsed=null; //the robot data you  selected, it is now released

        for( Marker actualMarker: mTienda)
        {
            System.out.println("Regresar - 0 actualMarker"+actualMarker.getSnippet());
            System.out.println("Regresar - 0.1 actualMarker"+actualMarker.getTitle());

            System.out.println("Regresar - 0.2 actualMarker "+dTienda.get(icual).getTipo()+" "+dTienda.get(icual).getName());
            System.out.println("Regresar - 0.3 actualMarker "+dTienda.get(icual).getPosLat()+" "+dTienda.get(icual).getPosLon());
            if (marker.equals(actualMarker))
            {
                if(Mode.equals("mapa")){
                    fabArriba.show();
                    fabAbajo.show();
                    fabIzq.show();
                    fabDer.show();
                    playing=1;//you are playing now
                    mTiendaBeingUsed=actualMarker;//the marker you had just selected
                    tiendaDataBeingUsed=dTienda.get(icual); //the robot data you had just selected

                    //change the icon for the robot, based on the type of robot
                    ponElIconoComoSeleccionado( );



                }else{
                    //your current mode is floatingControls
                    //change the icon for the robot, based on the type of robot
                    //aun cuando se vaya a floatingControls, que se quede en la app como marcado
                        mTiendaBeingUsed=actualMarker;//the marker you had just selected
                        tiendaDataBeingUsed=dTienda.get(icual); //the robot data you had just selected
                        ponElIconoComoSeleccionado( );




                    //Intent intent=new Intent(MarkerDemoActivity.this,AnotherActivity.class);
                        //startActivity();

                        System.out.println("Regresar - 3 actualMarker"+actualMarker.getSnippet());

                        //CZ 09 Nov 2017:start
                        //Open the floating controls in system
                        System.out.println("onMarkerClick paso conectar 1 actualMarker");
//            startService(new Intent(MiMapaTiendasActivity.this, ControlesBotones.class));


//                Uri webpage = Uri.parse("https://www.pscp.tv/tochizendejas");

                    //Open the perispcope in the channel
                    if(tiendaDataBeingUsed!=null && tiendaDataBeingUsed.getTransmite()!=null
                            && tiendaDataBeingUsed.getTransmite().equals("periscope")){
                        //open the floating controls inside a service


                        Intent serviceIntent = new Intent(MiMapaTiendasActivity.this, ControlesBotones.class);
                        serviceIntent.putExtra("CualRobot", "01");
                        serviceIntent.putExtra("CaracteristicasRobotName", dTienda.get(icual).getName());
                        serviceIntent.putExtra("CaracteristicasRobotTipo", dTienda.get(icual).getTipo());
                        serviceIntent.putExtra("CaracteristicasRobotManada", dTienda.get(icual).getManada());

                        startService(serviceIntent);

                        Uri webpage = Uri.parse("https://www.pscp.tv/"+dTienda.get(icual).getTransmiteCanal());
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                        startActivity(webIntent);

                        //old asp api /accessDB                   RequestTaskAddNuevaRutinaWeb th=new RequestTaskAddNuevaRutinaWeb();
                        // new nodejs api/mongodb
                       //Inserta comando que indica se hara videocall en whatsapp, asi controlara el robot remotoe
                        RequestTaskEnviarComandoTiendaWeb th=new RequestTaskEnviarComandoTiendaWeb();
                        System.out.println("goToAdd Comando PERISCOPE Robot - 3"+tiendaDataBeingUsed.getName());
                        th.nombreRutina=tiendaDataBeingUsed.getName();


                        System.out.println("goToAdd Comando PERISCOPE Robot - 4");
                        th.comando="PERISCOPE";
                        th.secuencia="userRemoteName_carlin_tel_521777189778";// TODO add  in th one for Remote user name
                                                                        // TODO and one for telefone Remote User
                        if(tiendaDataBeingUsed!=null && tiendaDataBeingUsed.getTransmiteCanal()!=null){
                            th.secuencia="https://www.pscp.tv/"+dTienda.get(icual).getTransmiteCanal();
                        }else{
                            th.secuencia="https://www.pscp.tv/tochizendejas";
                        }
                        System.out.println("goToAdd Comando PERISCOPE Robot - 5");
                        th.tiempo=tiendaDataBeingUsed.getTipo();//pasa en tiempo, el tipo de robot
                        th.status="creado";
                        TextView txtText=null;
                        th.execute(txtText); // here the result in text will be displayed
                        System.out.println("goToAdd PERISCOPE ComandoRobot - 4");

                    }else if(tiendaDataBeingUsed!=null && tiendaDataBeingUsed.getTransmite()!=null
                            && tiendaDataBeingUsed.getTransmite().equals("whatsapp")){
                        //store in  the web, the robot command #robot needs to make a whatsapp videocall to user"
                        //with this command, the Application in the Remote Robot will:
                        //1.Owner User  need to add a contact the number of the Remote User in the cellphone
                        //2. Robot to make a videocall
                        System.out.println("goToAdd Comando VIDEOCALL Robot - 2");
            //old asp api /accessDB                   RequestTaskAddNuevaRutinaWeb th=new RequestTaskAddNuevaRutinaWeb();
            // new nodejs api/mongodb
                        //Inserta comando que indica se hara videocall en whatsapp, asi controlara el robot remotoe
                        RequestTaskEnviarComandoTiendaWeb th=new RequestTaskEnviarComandoTiendaWeb();
                        System.out.println("goToAdd Comando VIDEOCALL Robot - 3"+tiendaDataBeingUsed.getName());
                        th.nombreRutina=tiendaDataBeingUsed.getName();
                        th.secuencia="userRemoteName_carlin_tel_521777189778";// TODO add  in th one for Remote user name
                        // TODO and one for telefone Remote User
                       System.out.println("goToAdd Comando VIDEOCALL Robot - 4");
                        th.comando="VIDEOCALL";
                        System.out.println("goToAdd Comando VIDEOCALL Robot - 5");
                        th.tiempo=tiendaDataBeingUsed.getTipo();//pasa en tiempo, el tipo de robot
                        th.status="creado";
                        TextView txtText=null;
                        th.execute(txtText); // here the result in text will be displayed
                        System.out.println("goToAdd VIDEOCALL ComandoRobot - 4");


                        //Then: open the floating controls inside a service

                        Intent serviceIntent = new Intent(MiMapaTiendasActivity.this, ControlesBotones.class);
                        serviceIntent.putExtra("CualRobot", "01");
                        serviceIntent.putExtra("CaracteristicasRobotName", dTienda.get(icual).getName());
                        serviceIntent.putExtra("CaracteristicasRobotTipo", dTienda.get(icual).getTipo());
                        serviceIntent.putExtra("CaracteristicasRobotManada", dTienda.get(icual).getManada());

                        startService(serviceIntent);

                    }else{
                        Toast.makeText(MiMapaTiendasActivity.this, "Virtual/Robot/Virtual. No Visual Contact/o", Toast.LENGTH_LONG).show();
                        //Inserta comando que sera virtual, sin contacto visaul, usando botones control de robot screen
                        RequestTaskEnviarComandoTiendaWeb th=new RequestTaskEnviarComandoTiendaWeb();
                        System.out.println("goToAdd Comando VIRTUAL Robot - 3"+tiendaDataBeingUsed.getName());
                        th.nombreRutina=tiendaDataBeingUsed.getName();

                        th.secuencia="521777189778";// TODO add  in th one for Remote user name
                        // TODO and one for telefone Remote User

                        System.out.println("goToAdd Comando VIRTUAL Robot - 4");
                        th.comando="VIRTUAL";
                        System.out.println("goToAdd Comando VIRTUAL Robot - 5");
                        th.tiempo=tiendaDataBeingUsed.getTipo();//pasa en tiempo, el tipo de robot
                        th.status="creado";


                        TextView txtText=null;
                        th.execute(txtText); // here the result in text will be displayed
                        System.out.println("goToAdd VIRTUAL ComandoRobot - 4");


                    }

                }
            }


            icual++; //increase the countador
        }//end for


        //este es el marker fijo de prueba/para comparar los marker de la web

        if (marker.equals(mTienda01))
        {

            //Intent intent=new Intent(MarkerDemoActivity.this,AnotherActivity.class);
            //startActivity();

            System.out.println("Regresar - 3");

            //CZ 09 Nov 2017:start
            //Open the floating controls in system
            System.out.println("onMarkerClick paso conectar 1");
//            startService(new Intent(MiMapaTiendasActivity.this, ControlesBotones.class));

            Intent serviceIntent = new Intent(MiMapaTiendasActivity.this, ControlesBotones.class);
            serviceIntent.putExtra("CualRobot", "01");
            serviceIntent.putExtra("CaracteristicasRobot", mTienda01.getSnippet());
            startService(serviceIntent);

           Uri webpage = Uri.parse("https://www.pscp.tv/tochizendejas");


            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(webIntent);
        }






        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


    /*
    Custom method, to be used when the list of markers are read from the web


     */
    public void updateMap() {
//        mMap //ya contiene los amrkers y mapa que se definieron

        mMap.clear(); //limpia markers actuales

        //ahora agreguemos los markers que qeuremos
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        LatLng mexicoCity = new LatLng(19.429, -99.146);


        LatLng robotPosicion01 = new LatLng(19.429, -99.148);

        LatLng robotPosicion02 = new LatLng(19.428, -99.148);



//        BitmapDescriptor bmd=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory. .HUE_AZURE);

        //original
        BitmapDescriptor bmd= BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapicon);

        //19.4294359!4d-99.1469533
        mTienda01=  mMap.addMarker(
                new MarkerOptions().position(mexicoCity).title("Ahora:DISPONIBLE - CDMX Centro MEXICO - Tipo: (Human Walker)").snippet(" Renta por:(5mins) Renta $:(Gratis) Manada:(No) Transmite Periscope:(Si)")
        );
        mTienda01.setIcon(bmd);





        System.out.println("robot 4 de web 1" );
        int primero=0;
        Marker localMarker=null;
        LatLng currentRobotPosicion = null;

        int icual=0;

        for( String actualElemento: arrayTasks)

        {
            System.out.println("robot 4 de web 2 "+actualElemento );
                double ivalorLat;
                double ivalorLon;
                try{
                    String valorLat= dTienda.get(icual).getPosLat();
                    String valorLon= dTienda.get(icual).getPosLon();
                     ivalorLat = new Double(valorLat);
                     ivalorLon = new Double(valorLon);

                    System.out.println("robot 4 de web 2.0  "+ ivalorLat +"  "+ ivalorLon );
                    currentRobotPosicion = new LatLng(ivalorLat, ivalorLon);
                    mTienda04= mMap.addMarker(
//    1st                    new MarkerOptions().position(currentRobotPosicion).title(actualElemento).snippet("SII Renta por:(10mins) Renta $:(Gratis) Manada:(Si) Transmite Periscope:(Si)")
//    2nd                        new MarkerOptions().position(currentRobotPosicion).title(actualElemento).snippet("SII Renta por:("+dTienda.get(icual).getRentaTiempo()+") Renta $:("+dTienda.get(icual).getRentaCosto()+") Manada:("+dTienda.get(icual).getManada()+") Transmite:("+dTienda.get(icual).getTransmite()+")")
                            new MarkerOptions().position(currentRobotPosicion).title(dTienda.get(icual).getName()).snippet("[:)   Renta por:("+dTienda.get(icual).getRentaTiempo()+") Renta $:("+dTienda.get(icual).getRentaCosto()+") Manada:("+dTienda.get(icual).getManada()+") Transmite:("+dTienda.get(icual).getTransmite()+")")
                    );
                    System.out.println("robot 5 de web 2.1" );
                    if(dTienda.get(icual).getTipo()!=null){
                        System.out.println("robot 6 de web 2.0  "+ dTienda.get(icual).getTipo()  );
                    }else{
                        System.out.println("robot 7 de web 2.0  ");
                    }

                    //change the icon for the robot, based on the type of robot


                    if(dTienda.get(icual).getTipo()!=null && dTienda.get(icual).getTipo().equals("conosmoviles")){
                        BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.typeconosmoviles_small);
                        mTienda04.setIcon(bmd01);
                    }else if(dTienda.get(icual).getTipo()!=null && dTienda.get(icual).getTipo().equals("muchasbolas")){
                        BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.type_muchasbolas_small);

                        mTienda04.setIcon(bmd01);

                    }else if(dTienda.get(icual).getTipo()!=null && dTienda.get(icual).getTipo().equals("bolaAcuavia")){

                        BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.type_bola_acuavia_small);
                        mTienda04.setIcon(bmd01);

                    }else if(dTienda.get(icual).getTipo()!=null && dTienda.get(icual).getTipo().equals("bolasganaderas")){

                        BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.type_muchasbolas_vaquero_small);
                        mTienda04.setIcon(bmd01);
                    }else{
//            imagenV.setImageResource(imageId[0]);;
                        BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapicon);
                        mTienda04.setIcon(bmd01);
                    }

                    //mTienda04.setIcon(bmd);
                    localMarker=mTienda04;
                }catch (Exception e){
                    ivalorLat = new Double(0.0);
                    ivalorLon = new Double(0.0);

                }

            //arrayMarkers

            mTienda.add(localMarker); //agregar aqui los markers en esta lista
            localMarker=null;
            icual++;
            System.out.println("robot 4 de web 3" );
        }//end for
        System.out.println("robot 4 de web 4" );

        System.out.println("zooooom original" +mMap.getMaxZoomLevel());
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mexicoCity,mMap.getMaxZoomLevel()-14));

        //gps position passed as paramter, will set the center of the map: start
        System.out.println("zooooom onMapReady 1 nulito" );
        if(mapaLon!=null && mapaLat!=null && !mapaLon.equals("null") && !mapaLat.equals("null")){
            System.out.println("zooooom onMapReady 2" );
            dondeEstoyLatLon=new LatLng( Double.valueOf( mapaLat),Double.valueOf( mapaLon));
        }else{
            System.out.println("zooooom onMapReady 3" );
            dondeEstoyLatLon=mexicoCity;
        }
        System.out.println("zooooom onMapReady 4" );
        //gps position passed as paramter, will set the center of the map: end


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dondeEstoyLatLon,mMap.getMaxZoomLevel()-10));

        //old mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mexicoCity,mMap.getMaxZoomLevel()-4));


        System.out.println("zooooom menos" );
        mMap.setBuildingsEnabled(true);





        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(19.429, -98.146))
                .title("San Francisco")
                .snippet("Population: 776733"))
                .setIcon(bmd);

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
    }

//-------------------------------------------------Tarea asyncrona que lee datos de la web --------------------

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<DataDescriptorTienda>> {
        private final ProgressDialog dialog = new ProgressDialog(MiMapaTiendasActivity.this); //asi porque estamos dentro de un fragment

        public String parametroURL="aqui";

        @Override
        protected void onPostExecute(List<DataDescriptorTienda> result) {
            super.onPostExecute(result);
            dialog.dismiss();
//            adpt.setItemList(result);
//            adpt.notifyDataSetChanged();
            System.out.println("AsyncListViewLoader onPostExecute paso 1");

            //cylce through List DataDescriptorRobot result, y ponerlo en lista de ArrayTask
            if(result!=null) {
                for (DataDescriptorTienda f : result) {
//                    arrayTasks.add(f.getName().toUpperCase() + " - " + f.getTipo().toUpperCase() + " - " + f.getManada().toUpperCase() + "-"+ f.getPosLon().toUpperCase() + "-"+ f.getPosLat().toUpperCase() + "-");// origianl
                    arrayTasks.add(f.getName() + " - " + f.getTipo() + " - " + f.getManada() + "-" + f.getPosLon() + "-" + f.getPosLat() + "-");// api robots

                    dTienda.add(f); //aqui almacenamos cada dataDescriptor de robot, asi esta lista tendra los datos del robot web
                }
               // adapter.notifyDataSetChanged();//no hay un objeto en la screen que estara siendo modificado
                //quiza se repinten los markers
                System.out.println("AsyncListViewLoader onPostExecute paso 2");
                updateMap();
                System.out.println("AsyncListViewLoader onPostExecute paso 3");
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Downloading contacts...");
            dialog.show();
        }
        @Override
        protected List<DataDescriptorTienda> doInBackground(String... params) {

            System.out.println("PrimerFragment doInBackground paso 1");

            List<DataDescriptorTienda> result = new ArrayList<DataDescriptorTienda>();
            System.out.println("PrimerFragment doInBackground paso 2");
            try {

                BufferedReader inStream = null;
                System.out.println("ValidaRobot - 2"+parametroURL);
                String JSONResp =null;

                URL url = new URL(parametroURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                try {

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    //readStream(in);

                    inStream = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                    StringBuffer buffer = new StringBuffer("");

                    System.out.println("Obten Lista - 5");
                    String line = "";
                    System.out.println("Obten Lista - 7");
                    String NL = System.getProperty("line.separator");
                    System.out.println("Obten Lista - 8");
                    while ((line = inStream.readLine()) != null) {
                        buffer.append(line + NL);
                    }
                    System.out.println("Obten Lista - 9");
                    inStream.close();
                    System.out.println("Obten Lista - 10");
                    JSONResp = buffer.toString();
                    System.out.println("GetSomething - 11");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.out.println("Obten Lista - 12 error");
                    e.printStackTrace();
                } finally {
                    System.out.println("Obten Lista - 13");
                    if (inStream != null) {
                        try {
                            inStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("PrimerFragmentdoInBackground paso 5");
//antes asp tenia { edificio [ {     }, {    } ] }
//antes asp                JSONObject jsonResponse = new JSONObject(new String(JSONResp));
//antes asp                JSONArray arr = jsonResponse.getJSONArray("edificio");


//ahora solo es [ {     }, {    } ]
                JSONArray arr = new JSONArray(new String(JSONResp));
                System.out.println("PrimerFragment doInBackground paso 6");

                for (int i=0; i < arr.length(); i++) {
                    result.add(convertDataDescriptorRobot(arr.getJSONObject(i)));
                }
                System.out.println("PrimerFragment doInBackground paso 7");
                return result;
            } catch(Throwable t) {
                t.printStackTrace();
            }
            System.out.println("PrimerFragment doInBackground paso 8");
            return null;
        }
        private DataDescriptorTienda convertDataDescriptorRobot(JSONObject obj) throws JSONException {

            System.out.println("ListBuildingsActivity convertDataDescriptorRobot paso 10");

//            String id = obj.getString("id");
            String id=obj.getString("_id");

            String name = obj.getString("name");
            String tipo = obj.getString("tipo");
            String manada = obj.getString("manada");
            String posLon = obj.getString("lon");
            String posLat = obj.getString("lat");
            String rentaTiempo = obj.getString("rentatiempo");
            String rentaCosto = obj.getString("rentacosto");

            String transmite = obj.getString("transmite");
            String transmiteCanal=obj.getString("transmitecanal");
            System.out.println("ListBuildingsActivity convertDataDescriptorRobot paso 11:"+name);
            System.out.println("ListBuildingsActivity convertDataDescriptorRobot paso 12:"+posLon);
            System.out.println("ListBuildingsActivity convertDataDescriptorRobot paso 13:"+posLat);
            System.out.println("ListBuildingsActivity convertDataDescriptorRobot paso 14:"+transmite);
            System.out.println("ListBuildingsActivity convertDataDescriptorRobot paso 15:"+transmiteCanal);

            return new DataDescriptorTienda("01",id,name, tipo,manada,posLon,posLat,rentaTiempo,rentaCosto,transmite, transmiteCanal);
        }

    }//end private class



    public void dejaElIconoComoEstaba( ){
        if( tiendaDataBeingUsed.getTipo()!=null && tiendaDataBeingUsed.getTipo().equals("conosmoviles")){
            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.typeconosmoviles_small);
            mTiendaBeingUsed.setIcon(bmd01);
        }else if( tiendaDataBeingUsed.getTipo()!=null && tiendaDataBeingUsed.getTipo().equals("muchasbolas")){
            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.type_muchasbolas_small);

            mTiendaBeingUsed.setIcon(bmd01);

        }else if(tiendaDataBeingUsed.getTipo()!=null && tiendaDataBeingUsed.getTipo().equals("bolaAcuavia")){

            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.type_bola_acuavia_small);
            mTiendaBeingUsed.setIcon(bmd01);

        }else if(tiendaDataBeingUsed.getTipo()!=null && tiendaDataBeingUsed.getTipo().equals("bolasganaderas")){

            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.type_muchasbolas_vaquero_small);
            mTiendaBeingUsed.setIcon(bmd01);
        }else{
//            imagenV.setImageResource(imageId[0]);;
            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapicon);
            mTiendaBeingUsed.setIcon(bmd01);
        }

    }

    public void ponElIconoComoSeleccionado( ){
        if(tiendaDataBeingUsed.getTipo()!=null && tiendaDataBeingUsed.getTipo().equals("conosmoviles")){
            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.typeconosmoviles_small_being_used);
            mTiendaBeingUsed.setIcon(bmd01);
        }else if(tiendaDataBeingUsed.getTipo()!=null && tiendaDataBeingUsed.getTipo().equals("muchasbolas")){
            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.type_muchasbolas_small_being_used);

            mTiendaBeingUsed.setIcon(bmd01);

        }else if(tiendaDataBeingUsed.getTipo()!=null && tiendaDataBeingUsed.getTipo().equals("bolaAcuavia")){

            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.type_bola_acuavia_small_being_used);
            mTiendaBeingUsed.setIcon(bmd01);

        }else if(tiendaDataBeingUsed.getTipo()!=null && tiendaDataBeingUsed.getTipo().equals("bolasganaderas")){

            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.type_muchasbolas_vaquero_small_being_used);
            mTiendaBeingUsed.setIcon(bmd01);
        }else{
//            imagenV.setImageResource(imageId[0]);;
            BitmapDescriptor bmd01= BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapicon_being_used);
            mTiendaBeingUsed.setIcon(bmd01);
        }


    }



    //logica para manejo de sensores e inlcinacion

    //START:
    public class SensorListen implements SensorEventListener {

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
            System.out.println("Actual valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
            if(Math.abs(z) > Math.abs(y)){
                //arriba abajo
                if(z<0 && fabSensorPos!=null){ //aajo
                    //inclina su cabeza hacia abajo
                    System.out.println("Abajo-valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
                    fabSensorPos.setImageResource(R.mipmap.abajo);
                    //abajo

                    if(Mode.equals("mapa") && mTiendaBeingUsed!=null && useSensorPosForMovement){
                        System.out.println("robot abajo 0 before:" +mTiendaBeingUsed.getPosition().latitude);
                        double nuevaLat =mTiendaBeingUsed.getPosition().latitude -0.00001;
                        double nuevaLon=mTiendaBeingUsed.getPosition().longitude;

                        System.out.println("robot abajo 1 lat:" +nuevaLat);
                        System.out.println("robot abajo 1 lon:" +nuevaLon);
//                    mTienda01.setPosition(new LatLng(point.latitude, point.longitude));
                        mTiendaBeingUsed.setPosition(new LatLng( nuevaLat, nuevaLon));

                    }else{
                        //it should not be display in any other mode
                    }
                }
                if(z>0 && fabSensorPos!=null){//arriba
                    //inclina su cabeza hacia arriba
                    System.out.println("arriba- valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
                    fabSensorPos.setImageResource(R.mipmap.arriba);
                    //arriba


                    if(Mode.equals("mapa") && mTiendaBeingUsed!=null && useSensorPosForMovement){
                        System.out.println("arriba 0 before:" +mTiendaBeingUsed.getPosition().latitude);
                        double nuevaLat =mTiendaBeingUsed.getPosition().latitude +0.00001;
                        double nuevaLon=mTiendaBeingUsed.getPosition().longitude;

                        System.out.println("robot arriba 1 lat:" +nuevaLat);
                        System.out.println("robot arriba 1 lon:" +nuevaLon);
//                    mTienda01.setPosition(new LatLng(point.latitude, point.longitude));
                        mTiendaBeingUsed.setPosition(new LatLng( nuevaLat, nuevaLon));
                    }else{
                        //it should not be display in any other mode
                    }
                }
            }else{
                //inclina su cabeza a la derecha
                if(y<0 && fabSensorPos!=null){
                    System.out.println("der-valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
                    fabSensorPos.setImageResource(R.mipmap.derecha);

                    //der

                    if(Mode.equals("mapa") && mTiendaBeingUsed!=null && useSensorPosForMovement){
                        System.out.println("robot izq 0 before:" +mTiendaBeingUsed.getPosition().latitude);
                        double nuevaLat =mTiendaBeingUsed.getPosition().latitude ;
                        double nuevaLon=mTiendaBeingUsed.getPosition().longitude -0.00001;


                        System.out.println("robot izq 1 lat:" +nuevaLat);
                        System.out.println("robot izq 1 lon:" +nuevaLon);
//                    mTienda01.setPosition(new LatLng(point.latitude, point.longitude));
                        mTiendaBeingUsed.setPosition(new LatLng( nuevaLat, nuevaLon));

                    }else{
                        //it should not be display in any other mode
                    }
                }
                if(y>0 && fabSensorPos!=null){
                    //inclina su cabeza a la izquierad
                    System.out.println("izq-valor de x:"+x+  "  valor y:" +  y +" valor z:"+z);
                    fabSensorPos.setImageResource(R.mipmap.izquierda);
                    //izq

                    if(Mode.equals("mapa") && mTiendaBeingUsed!=null && useSensorPosForMovement){

                        System.out.println("robot der 0 before:" +mTiendaBeingUsed.getPosition().latitude);
                        double nuevaLat =mTiendaBeingUsed.getPosition().latitude ;
                        double nuevaLon=mTiendaBeingUsed.getPosition().longitude +0.00001;

                        System.out.println("robot der 1 lat:" +nuevaLat);
                        System.out.println("robot der 1 lon:" +nuevaLon);
//                    mTienda01.setPosition(new LatLng(point.latitude, point.longitude));
                        mTiendaBeingUsed.setPosition(new LatLng( nuevaLat, nuevaLon));

                    }else{
                        //it should not be display in any other mode
                    }
                }
            }
            if ( z> (-1.5) && z < (1.5) && y >(-1.5) && y< (1.5) && fabSensorPos!=null){
                fabSensorPos.setImageResource(R.mipmap.centro);
            }
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }


        protected void onResume(){
            // super.onResume();
            mSensorM.registerListener(this,mAcelerador, SensorManager.SENSOR_DELAY_NORMAL);
        }


        protected void onPause(){
            // super.onPause();
            mSensorM.unregisterListener(this);
        }
    }
    //END:

}//end class MiMapaTiendasActivity

