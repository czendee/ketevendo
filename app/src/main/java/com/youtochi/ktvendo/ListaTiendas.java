package com.youtochi.ktvendo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import com.google.android.gms.maps.model.BitmapDescriptor;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.List;

/**
 * Created by 813743 on 26/03/2017.
 */
public class ListaTiendas extends ArrayAdapter{

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    private List<DataDescriptorTienda> itemList;

    public ListaTiendas(Activity context, String[] web, Integer[] imageId, List<DataDescriptorTienda> itemList){
        super(context,R.layout.lista_single,web);
        this.context= context;
        this.web = web;
        this.imageId=imageId;
        this.itemList = itemList;
    }

    public int getCount() {
        if (itemList != null)
            return itemList.size();
        return 0;
    }
    public DataDescriptorTienda getItem(int position) {
        if (itemList != null)
            return itemList.get(position);
        return null;
    }
    public long getItemId(int position) {
        if (itemList != null)

            return itemList.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflador= context.getLayoutInflater();
        View rowView=inflador.inflate(R.layout.lista_single,null,true);

        TextView txtTitle =(TextView) rowView.findViewById(R.id.txt);
        ImageView imagenV= (ImageView) rowView.findViewById(R.id.img);
        //txtTitle.setText(web[position]);

        System.out.println("ListaRobot getView paso 3:"+position);
        DataDescriptorTienda c = itemList.get(position);
        txtTitle.setText(c.getName());

        System.out.println("Lista DataDescriptorTienda ListaRobot paso 1");


//        imagenV.setImageResource(imageId[position]);;

//        imagenV.setImageResource(imageId[0]);;//imagen de robot posicion 1

        //change the icon for the robot, based on the type of robot
        if(c.getTipo()!=null && c.getTipo().equals("conosmoviles")){
            imagenV.setImageResource(R.mipmap.typeconosmoviles_small);


        }else if(c.getTipo()!=null && c.getTipo().equals("muchasbolas")){
            imagenV.setImageResource(R.mipmap.type_muchasbolas_small);

        }else if(c.getTipo()!=null && c.getTipo().equals("bolaAcuavia")){
            imagenV.setImageResource(R.mipmap.type_bola_acuavia_small);

        }else if(c.getTipo()!=null && c.getTipo().equals("bolasganaderas")){
            imagenV.setImageResource(R.mipmap.type_muchasbolas_vaquero_small);
        }else{
//            imagenV.setImageResource(imageId[0]);;
            imagenV.setImageResource(R.mipmap.ic_mapicon);;
        }



        Button btnTitle =(Button) rowView.findViewById(R.id.btnRegresarCobrar);
        btnTitle.setHint(position+"");//this value is used later when user clicks on the button, hint position value

        return rowView;

    }

    public List<DataDescriptorTienda> getItemList() {
        return itemList;
    }

    public void setItemList(List<DataDescriptorTienda> itemList) {
        this.itemList = itemList;
    }
}
