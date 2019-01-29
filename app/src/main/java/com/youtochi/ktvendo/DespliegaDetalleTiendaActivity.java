package com.youtochi.ktvendo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class DespliegaDetalleTiendaActivity extends AppCompatActivity {

    public String cualUsuarioSoy;
    public String cualInfoProductoSoy;
    public String cualProductoIDSoy;
    public String cualPrecioSoy;
    public String cualImagenSoy;

    public String cualRobotId;
    public String cualRobotName;
    public String cualRobotTipo;
    public String cualRobotManada;
    public String cualRobotLon;
    public String cualRobotLat;
    public String cualRobotRentacosto;
    public String cualRobotRentatiempo;
    public String cualRobotImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despliega_detalle_tienda);

        Bundle extras = getIntent().getExtras();

        cualUsuarioSoy="ninguno";
        System.out.println("Detalle robot  ProdId:" );

        if (extras != null) {
            System.out.println("Detalle robot  ProdId  step 2:" );
            cualUsuarioSoy = extras.getString("cual_usuario");


            cualInfoProductoSoy =extras.getString("cual_infoproducto");
            cualProductoIDSoy= extras.getString("cual_productoID");
            cualPrecioSoy=  extras.getString("cual_precio");
            cualImagenSoy=  extras.getString("cual_imagen");


            cualRobotId=extras.getString("cual_robot_id");
            cualRobotName=extras.getString("cual_robot_name");
            cualRobotTipo=extras.getString("cual_robot_tipo");
            cualRobotManada=extras.getString("cual_robot_manada");
            cualRobotRentacosto=extras.getString("cual_robot_rentacosto");
            cualRobotRentatiempo=extras.getString("cual_robot_rentatiempo");
            cualRobotImage=extras.getString("cual_robot_image");
            cualRobotLat=extras.getString("cual_robot_lat");
            cualRobotLon=extras.getString("cual_robot_lon");

            System.out.println("Detalle robot  ProdId  step 3 a:"+cualInfoProductoSoy+" b:"+ cualProductoIDSoy+ " c:"+cualPrecioSoy +" d:"+cualImagenSoy);
            int valorImagenInt=0;
            try{
                valorImagenInt = Integer.parseInt(cualImagenSoy);
            }catch(Exception e){

            }

            System.out.println("Detalle robot  ProdId  step 3:"+ valorImagenInt);
            EditText textitoName =(EditText) findViewById(R.id.screenRobotName);
            textitoName.setText(cualRobotName);

            EditText textitoID =(EditText) findViewById(R.id.screenRobotId);
            textitoID.setText(cualRobotId);

            EditText textitoTipo =(EditText) findViewById(R.id.screenRobotTipo);
            textitoTipo.setText(cualRobotTipo);

            EditText textitoManada =(EditText) findViewById(R.id.screenRobotManada);
            textitoManada.setText(cualRobotManada);

            EditText textitoLat =(EditText) findViewById(R.id.screenRobotLat);
            textitoLat.setText(cualRobotLat);

            EditText textitoLon =(EditText) findViewById(R.id.screenRobotLon);
            textitoLon.setText(cualRobotLon);

            EditText textitoTiempo =(EditText) findViewById(R.id.screenRobotTiempo);
            textitoTiempo.setText(cualRobotRentatiempo);

            EditText textitoCosto =(EditText) findViewById(R.id.screenRobotCosto);
            textitoCosto.setText(cualRobotRentacosto);

            if(valorImagenInt>0) {
                ImageView imagenV = (ImageView) findViewById(R.id.productoImagen);

                imagenV.setImageResource(valorImagenInt);

            }

            System.out.println("Detalle robot  ProdId  step 6:");
        }//if extras

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddProdShopping);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Registrate para ir a Carrito Compras", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab02 = (FloatingActionButton) findViewById(R.id.btnRegresarRentar);
        fab02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                System.out.println("back to previous activity - 4");
            }
        });




        FloatingActionButton fabmap = (FloatingActionButton) findViewById(R.id.detallerobotfab01);
        fabmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,R.string.regresar_a_lista_robots, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //ir a screen de mapa
                Intent i = new Intent(DespliegaDetalleTiendaActivity.this, MiMapaDetalleUnTiendaActivity.class);


                startActivity(i);
            }
        });
    }
}
