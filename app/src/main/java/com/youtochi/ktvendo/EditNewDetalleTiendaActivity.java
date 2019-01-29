package com.youtochi.ktvendo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditNewDetalleTiendaActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_edit_new_detalle_tienda);

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
                Snackbar.make(view, "Registrando el Nuevo Robot", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                RequestTaskEnviarNuevoTiendaWeb th=new RequestTaskEnviarNuevoTiendaWeb();

                DataDescriptorTienda pasarLosDatos=  llenaDatosPasar();
                System.out.println("goToAdd Comando RIGHT Robot - 3"+pasarLosDatos.getName());

                th.elNuevoTienda=pasarLosDatos;
                TextView txtText=null;
//                textResultadoOperacion
                txtText =(TextView) findViewById(R.id.textResultadoOperacion);

                th.execute(txtText); // here the result in text will be displayed
                System.out.println("goToAdd RIGHT ComandoRobot - 4");

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
/*
                Intent i = new Intent(EditNewDetalleTiendaActivity.this, MiMapaDetalleUnRobotActivity.class);


                startActivity(i);
*/
            }
        });
    }

    public DataDescriptorTienda llenaDatosPasar(){
        DataDescriptorTienda temp= new DataDescriptorTienda();
        System.out.println("Detalle robot  ProdId  step 3:");

        EditText textitoName =(EditText) findViewById(R.id.screenRobotName);
        temp.setName(textitoName.getText().toString());

        EditText textitoID =(EditText) findViewById(R.id.screenRobotId);
        String a;
        a="";
        if(textitoID!=null){
            if(textitoID.getText() != null){
                a= textitoID.getText().toString();
            }
        }

        temp.setInternalId(a);

        EditText textitoTipo =(EditText) findViewById(R.id.screenRobotTipo);
        a="";
        if(textitoTipo!=null){
            if(textitoTipo.getText() != null){
                a= textitoTipo.getText().toString();
            }
        }
        temp.setTipo(a);

        EditText textitoManada =(EditText) findViewById(R.id.screenRobotManada);
        a="";
        if(textitoManada!=null){
            if(textitoManada.getText() != null){
                a= textitoManada.getText().toString();
            }
        }
        temp.setManada(a);

        EditText textitoLat =(EditText) findViewById(R.id.screenRobotLat);
        a="";
        if(textitoLat!=null){
            if(textitoLat.getText() != null){
                a= textitoLat.getText().toString();
            }
        }
        temp.setPosLat(a);

        EditText textitoLon =(EditText) findViewById(R.id.screenRobotLon);
        a="";
        if(textitoLon!=null){
            if(textitoLon.getText() != null){
                a= textitoLon.getText().toString();
            }
        }

        temp.setPosLon(a);

        EditText textitoTiempo =(EditText) findViewById(R.id.screenRobotTiempo);
        //the text in the screen is the hint @string/new_default_tiempo
        a="";
        if(textitoTiempo!=null){
            if(textitoTiempo.getHint() != null){
                a= textitoTiempo.getHint().toString();
            }
            if(textitoTiempo.getText() != null){
                a= textitoTiempo.getText().toString();
            }
        }
        temp.setRentaTiempo(a);

        EditText textitoCosto =(EditText) findViewById(R.id.screenRobotCosto);
        //the text in the screen is the hint @string/new_default_costo
        a="";
        if(textitoCosto!=null){
            if(textitoCosto.getHint() != null){
                a= textitoCosto.getHint().toString();
            }
            if(textitoCosto.getText() != null){
                a= textitoCosto.getText().toString();
            }
        }
        temp.setRentaCosto(a);

        EditText textitoTrans =(EditText) findViewById(R.id.screenRobotTransmite);
        a="";
        if(textitoTrans!=null){
            if(textitoTrans.getHint() != null){
                a= textitoTrans.getHint().toString();
            }
            if(textitoTrans.getText() != null){
                a= textitoTrans.getText().toString();
            }

        }
        temp.setTransmite(a);

        EditText textitoTransCan =(EditText) findViewById(R.id.screenRobotTransmiteCanal);
        a="";
        if(textitoTransCan!=null){
            if(textitoTransCan.getHint() != null){
                a= textitoTransCan.getHint().toString();
            }
            if(textitoTransCan.getText() != null){
                a= textitoTransCan.getText().toString();
            }
        }
        temp.setTransmiteCanal(a);


        return temp;
    }
}
