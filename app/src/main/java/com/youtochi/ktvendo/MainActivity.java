package com.youtochi.ktvendo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {


    String[] web ={"CDMX- Centro,Mexico",
            "Cuernavaca, Mexico",
            "CDMX -Santa Fe, Mexico",
            "Santiago - Mauipu, Chile",
            "Sao Paulo-Lagoa, rasil"
    };

    Integer[] imageId={
            R.mipmap.ic_robot,
            R.mipmap.ic_robot,
            R.mipmap.ic_robot,
            R.mipmap.ic_robot,
            R.mipmap.ic_robot
    };

    String[] robotPrecios ={"35.00",
            "32.00",
            "28.00",
            "22.00",
            "25.00"
    };

    String[] robotProdId ={"A001",
            "A020",
            "A011",
            "B002",
            "B305"
    };

    String cualUsuarioSoy;

    //gps location
    Double lat,lon;



    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 ; // 1 second

    // Declaring a Location Manager
    protected LocationManager locationManager;

    //obtain the robot list from the web, and display in the screen
    ListaTiendas adapter;
    final ArrayList<String> arrayTasks = new ArrayList<>(); //to have an object with the robots in a list, besides the
    //boton cobrar y puntos
    int sumaTotal=0;
    int cuantosArticulos=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //////////////////gps Start
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(MainActivity.this, "First enable LOCATION ACCESS in settings.", Toast.LENGTH_LONG).show();
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        try {
            lat = location.getLatitude ();
            lon = location.getLongitude ();

        }
        catch (NullPointerException e){
            e.printStackTrace();

        }

        //////////////////gps end


        /////////////////set the cobrar button

        sumaTotal=0;
        cuantosArticulos=0;

        Button cobrarBoton = (Button) findViewById(R.id.comprarBtn);

        cobrarBoton.setText("Compra% Robots con tus Puntos "+sumaTotal);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,R.string.boton_agregarRobot, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //ir a screen de mapa
                Intent i = new Intent(MainActivity.this, MiMapaUnNuevoTiendaActivity.class);
                i.putExtra("cual_usuario", cualUsuarioSoy + "");
                i.putExtra("cual_infoproducto", "nuevo");



                System.out.println("se pasaran como parametros "+lat+"   "+lon );
                i.putExtra("cual_lugarlon", lon+"");


                i.putExtra("cual_lugarlat", lat+"");
                i.putExtra("cual_productoID", lon+"nuevo");
                i.putExtra("cual_precios", lat+"nuevo");

                startActivity(i);
            }
        });





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //arriba esta el codigo estandard

        //boton flotante mapa

        FloatingActionButton fabmap = (FloatingActionButton) findViewById(R.id.fabmap);
        fabmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,R.string.registrate_controlar_robots, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //ir a screen de mapa
                Intent i = new Intent(MainActivity.this, MiMapaTiendasActivity.class);

                System.out.println("se pasaran como parametros "+lat+"   "+lon );
                i.putExtra("cual_lugarlon", lon+"");


                i.putExtra("cual_lugarlat", lat+"");

                startActivity(i);
            }
        });

        //abajo de esta linea esta el codigo de beer app
        cualUsuarioSoy ="1806";

        ListView lstTaskListLocal=(ListView)  findViewById(R.id.lista);



        // se declara el adapter, intermediario, se le pasa context, y el layout. se utilizara el layout default de andriod para lista
        //y la lista de array con los objetos
//        ListaProdServ adapter= new ListaProdServ(
        adapter= new ListaTiendas(
                MainActivity.this,
                web,
                imageId,
                new ArrayList<com.youtochi.ktvendo.DataDescriptorTienda>()
        );
        //ahora amarrar el view, lstTaskListLocal con el adapter, la parte visual view, con el adapter, y el array de objetos

        lstTaskListLocal.setAdapter(adapter);

        lstTaskListLocal.setLongClickable(true);
        System.out.println("antes del OnClick en una robot posicion" );
//        lstTaskListLocal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
        lstTaskListLocal.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0,
            public void onItemClick(AdapterView<?> arg0,
                                    View arg1,
                                    int position,
                                    long arg3){

                System.out.println("OnClick en una robot posicion es:" + position);

                String valorNombre =  "hola1";
                int valorImagen =  imageId[0];
                String valorPrecio =  "555.5";
                String valorProdId =  "444.4";


                System.out.println("OnClick en una robot nombre:" +valorNombre );
                System.out.println("OnClick en una robot valorImagen:" + valorImagen);
                System.out.println("OnClick en una robot  beerPrecios:" + valorPrecio);
                System.out.println("OnClick en una robot  ProdId:" + valorProdId);

                DataDescriptorTienda robotAUtilizar= adapter.getItem(position);

                if(valorNombre!=null && valorPrecio!=null) {


                    Intent i = null;


//                    i = new Intent(MainActivity.this, DespliegaDetalleTiendaActivity.class);
                    i = new Intent(MainActivity.this, MiMapaDetalleUnTiendaActivity.class);

                    i.putExtra("cual_usuario", cualUsuarioSoy + "");
                    i.putExtra("cual_infoproducto", valorNombre + "");
                    i.putExtra("cual_productoID", valorProdId + "");
                    i.putExtra("cual_precio", valorPrecio + "");
                    i.putExtra("cual_imagen", valorImagen + "");

                    i.putExtra("cual_lugarlat", lat + "");
                    i.putExtra("cual_lugarlon", lon + "");
                    if(robotAUtilizar!=null) {
                        i.putExtra("cual_usuario", cualUsuarioSoy + "");
                        i.putExtra("cual_robot_id", robotAUtilizar.getId() + "");
                        i.putExtra("cual_robot_name", robotAUtilizar.getName() + "");
                        i.putExtra("cual_robot_tipo", robotAUtilizar.getTipo() + "");
                        i.putExtra("cual_robot_manada", robotAUtilizar.getManada() + "");
                        i.putExtra("cual_robot_lon", robotAUtilizar.getPosLon() + "");
                        i.putExtra("cual_robot_lat", robotAUtilizar.getPosLat() + "");
                        i.putExtra("cual_robot_rentacosto", robotAUtilizar.getRentaCosto() + "");
                        i.putExtra("cual_robot_rentatiempo", robotAUtilizar.getRentaTiempo() + "");
                        int valorImagenRobot =  imageId[0];
                        i.putExtra("cual_robot_imagen", valorImagenRobot + "");
                        i.putExtra("cual_lugarlat", robotAUtilizar.getPosLat() + "");
                        i.putExtra("cual_lugarlon", robotAUtilizar.getPosLon() + "");

                    }




                    startActivity(i);
                }else{
                    System.out.println("No se puede desplegar el detalle" );
                }
//                return true;   it was used for long click
            }

        });//end onclick

        //get the list of prodserv
        AsyncListViewLoader task=new AsyncListViewLoader();
//        task.parametroURL="http://mexico.brinkster.net/tochi_get_lista_habitaciones_rentadas_mobil.asp?operacion=lista&soy="+cualUsuarioSoy+"&mes="+mes;
        Constants f=new Constants();
        task.parametroURL=f.URL_LIST_ROBOTS;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)

            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();


        System.out.println("ListBuildingsActivity paso 5");
/*
        Button ticketBtn = (Button) findViewById(R.id.ticketBtn);

        cuantosArticulos=cuantosArticulos+1;
//        ticketBtn.setText(""+cuantosArticulos);
*/
///////////////////////////////////////////////////////////////////////////77
/////////////////////abre los controles de la tienda

        Intent serviceIntent = new Intent(MainActivity.this, ControlesBotones.class);
        serviceIntent.putExtra("CualRobot", "01");
        serviceIntent.putExtra("CaracteristicasRobotName", "CoquitoConasupo");
        serviceIntent.putExtra("CaracteristicasRobotTipo", "robotTendero");
        serviceIntent.putExtra("CaracteristicasRobotManada", "no");

        startService(serviceIntent);

        Uri webpage = Uri.parse("https://www.pscp.tv/tochizendejas");

        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(MainActivity.this, MiMapaTiendasActivity.class);
            System.out.println("se pasaran como parametros "+lat+"   "+lon );
            i.putExtra("cual_lugarlon", lon+"");

            i.putExtra("cual_lugarlat", lat+"");
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            // Check if Android M or higher
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Show alert dialog to the user saying a separate permission is needed
                // Launch the settings activity if the user prefers
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }


        } else if (id == R.id.nav_share) {

/*            Intent i = new Intent(Intent.ACTION_MAIN);
            PackageManager managerclock = getPackageManager();
            i = managerclock.getLaunchIntentForPackage("pscp://broadcast/1RDtochizendejas");
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
*/
            Uri webpage = Uri.parse("https://www.pscp.tv/tochizendejas");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(webIntent);
        } else if (id == R.id.nav_send) {
            // Check if Android M or higher
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Show alert dialog to the user saying a separate permission is needed
                // Launch the settings activity if the user prefers
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
                startActivity(myIntent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /** Called when the user clicks the gotoDetalle  row */
    public void irDetalleRobot(View view) {

        System.out.println("OnClick boton en una irDetalleRobot:"  );
        Button btnTitle =(Button) findViewById(R.id.btnRegresarCobrar);
        String positionStr =(String) btnTitle.getHint();

        int valorPosition=-1;
        try{
            valorPosition = Integer.parseInt(positionStr);
        }catch(Exception e){

        }
        if (valorPosition>-1){
            System.out.println("OnClick boton en una cerveza posicion es:" + valorPosition);

            String valorNombre =  web[valorPosition];
            int valorImagen =  imageId[valorPosition];
            String valorPrecio =  robotPrecios[valorPosition];
            String valorProdId =  robotProdId[valorPosition];


            System.out.println("OnClick boton en una  robot nombre:" +valorNombre );
            System.out.println("OnClick boton en una  robot valorImagen:" + valorImagen);
            System.out.println("OnClick boton en una  robot  beerPrecios:" + valorPrecio);
            System.out.println("OnClick boton en una  robot  ProdId:" + valorProdId);

            if(valorNombre!=null && valorPrecio!=null) {


                Intent i = null;


//                i = new Intent(MainActivity.this, DespliegaDetalleTiendaActivity.class);

                //ir a screen de mapa
                i = new Intent(MainActivity.this, MiMapaDetalleUnTiendaActivity.class);

                i.putExtra("cual_usuario", cualUsuarioSoy + "");
                i.putExtra("cual_infoproducto", valorNombre + "");
                i.putExtra("cual_productoID", valorProdId + "");
                i.putExtra("cual_precio", valorPrecio + "");
                i.putExtra("cual_imagen", valorImagen + "");


                startActivity(i);
            }else{
                System.out.println("OnClick boton No se puede desplegar el detalle" );
            }
        }else{
            System.out.println("OnClick boton No hay itmes" );
        }


        //Intent i = new Intent(EdificioListaActivity.this, EdificionewActivity.class);

        //startActivity(i);
    }
/////////////////////////for the gps locationListener

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

        try {
            lat = location.getLatitude ();
            lon = location.getLongitude ();

        }
        catch (NullPointerException e){
            e.printStackTrace();

        }

//        int latitude = (int) (location.getLatitude());
//        int longitude = (int) (location.getLongitude());

//        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
        Log.i("Geo_Location", "Latitude: " + lat + ", Longitude: " + lon);
        System.out.println("Geo_Location"+ "Latitude: " + lat + ", Longitude: " + lon);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }





    //-------------------------------------------------Tarea asyncrona que lee datos de la web --------------------

    private class AsyncListViewLoader extends AsyncTask<String, Void, List<DataDescriptorTienda>> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this); //asi porque estamos dentro de un fragment

        public String parametroURL="aqui";

        @Override
        protected void onPostExecute(List<DataDescriptorTienda> result) {
            super.onPostExecute(result);
            dialog.dismiss();
//            adpt.setItemList(result);
//            adpt.notifyDataSetChanged();
            System.out.println("Lista rpodserv onPostExecute paso 1");

            //cylce through List Edificio result, y ponerlo en lista de ArrayTask
            arrayTasks.clear();
            if(result!=null) {
                System.out.println("Lista rpodserv onPostExecute paso 2");
                for (DataDescriptorTienda f : result) {
                    arrayTasks.add(f.getId().toUpperCase() + " # " + f.getName().toUpperCase() + " - " + f.getTipo().toUpperCase() + " - " + f.getPosLon().toUpperCase() + "-"+ f.getPosLat().toUpperCase() + "-");// origianl               f.getEstado();
                }
                System.out.println("Lista rpodserv onPostExecute paso 3");

                adapter.setItemList(result);
                System.out.println("ListBuildingsActivity onPostExecute paso 1");
                adapter.notifyDataSetChanged();
                System.out.println("Lista rpodserv onPostExecute paso 4");
            }
            System.out.println("Lista rpodserv onPostExecute paso 5");
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
                System.out.println("ValidaDueno - 2"+parametroURL);
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

//                JSONObject jsonResponse = new JSONObject(new String(JSONResp));
//                JSONArray arr = jsonResponse.getJSONArray("edificio");

                JSONArray arr = new JSONArray(new String(JSONResp));
                System.out.println("PrimerFragment doInBackground paso 6");

                for (int i=0; i < arr.length(); i++) {
                    result.add(convertIntoDataDescriptorRobot(arr.getJSONObject(i)));
                }
                System.out.println("PrimerFragment doInBackground paso 7");
                return result;
            } catch(Throwable t) {
                t.printStackTrace();
            }
            System.out.println("PrimerFragment doInBackground paso 8");
            return null;
        }
        private DataDescriptorTienda convertIntoDataDescriptorRobot(JSONObject obj) throws JSONException {

            System.out.println("ListBuildingsActivity convertEdificio paso 10");

            String id=obj.getString("_id");
//            String internalid=obj.getString("internalid");
            String name=obj.getString("name");
            String tipo=obj.getString("tipo");
            String manada=obj.getString("manada");
            String posLon=obj.getString("lon");
            String posLat=obj.getString("lat");
            String rentaTiempo=obj.getString("rentatiempo");
            String rentaCosto=obj.getString("rentacosto");
            String transmite=obj.getString("transmite");
            String transmiteCanal=obj.getString("transmitecanal");



            System.out.println("ListBuildingsActivity convertEdificio paso 11:"+name);
            System.out.println("ListBuildingsActivity convertEdificio paso 12:"+tipo);

            return new DataDescriptorTienda("01",id,name,tipo,  manada,  posLon,  posLat,  rentaTiempo,  rentaCosto,  transmite,  transmiteCanal);

        }

    }//end private class


    public void gotocuarto(View view){
        System.out.println("entramos ");

        System.out.println("entramos "+"hoy es martes");

        String nombre ="Carlos";


    }

    /** Called when the user clicks the quiero pagar/checkout  row */
    public void irCheckOut(View view) {
        Intent i = null;

/*
        i = new Intent(MainActivity.this, MainTicketActivity.class);

        i.putExtra("cual_usuario", cualUsuarioSoy + "");
        i.putExtra("cual_infoproducto", 5 + "");
        i.putExtra("cual_productoID", 10 + "");
        i.putExtra("cual_articulos", cuantosArticulos+ "");
        i.putExtra("cual_total", sumaTotal + "");


        startActivity(i);
*/
    }

    /** Called when the user clicks the quiero ir luna */
    public void irLunaMap(View view) {
        Intent i = null;

/*
        i = new Intent(MainActivity.this, MiMapaLunaRobotsActivity.class);

        i.putExtra("cual_usuario", cualUsuarioSoy + "");
        i.putExtra("cual_infoproducto", 5 + "");
        i.putExtra("cual_productoID", 10 + "");
        i.putExtra("cual_articulos", cuantosArticulos+ "");
        i.putExtra("cual_total", sumaTotal + "");


        startActivity(i);
*/
    }
    /** Called when the user clicks the quiero ir Marte */
    public void irMarteMap(View view) {
        Intent i = null;

/*
        i = new Intent(MainActivity.this, MiMapaMarteRobotsActivity.class);

        i.putExtra("cual_usuario", cualUsuarioSoy + "");
        i.putExtra("cual_infoproducto", 5 + "");
        i.putExtra("cual_productoID", 10 + "");
        i.putExtra("cual_articulos", cuantosArticulos+ "");
        i.putExtra("cual_total", sumaTotal + "");


        startActivity(i);
*/
    }
    /** Called when the user clicks the quiero ir tierra */
    public void irTierraMap(View view) {
        Intent i = null;
        //ir a screen de mapa
        i = new Intent(MainActivity.this, MiMapaTiendasActivity.class);

        i.putExtra("cual_lugarlon", lon+"");

        i.putExtra("cual_lugarlat", lat+"");

        i.putExtra("cual_usuario", cualUsuarioSoy + "");
        i.putExtra("cual_infoproducto", 5 + "");
        i.putExtra("cual_productoID", 10 + "");
        i.putExtra("cual_articulos", cuantosArticulos+ "");
        i.putExtra("cual_total", sumaTotal + "");


        startActivity(i);
    }
}
