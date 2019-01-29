package com.youtochi.ktvendo;

import android.os.AsyncTask;

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

/**
 * Created by 813743 on 10/11/2017.
 */
//-------------------------------------------------Tarea asyncrona que lee datos de la web --------------------

public class CRUDTiendaKGetOne extends AsyncTask<String, Void, List<DataDescriptorTiendaK>> {
//    private final ProgressDialog dialog = new ProgressDialog(MiMapaTiendasActivity.this); //asi porque estamos dentro de un fragment

    public String parametroURL="aqui";
    public DataDescriptorTiendaK tiendaKobtenido;
    public String socioStr = " ";
    @Override
    protected void onPostExecute(List<DataDescriptorTiendaK> result) {
        super.onPostExecute(result);
//        dialog.dismiss();
//            adpt.setItemList(result);
//            adpt.notifyDataSetChanged();
        System.out.println("CRUDTiendaKGetOne onPostExecute paso 1");

        //cylce through List DataDescriptorRobot result, y ponerlo en lista de ArrayTask
        if(result!=null) {
            for (DataDescriptorTiendaK f : result) {
                socioStr=f.getName() + " - " + f.getTipo() + " - " + f.getPeriscope() + "-" + f.getFacelive() + "-"  + f.getStatus() + "-";// api socios
                tiendaKobtenido.setId(f.getId());
                tiendaKobtenido.setInternalId(f.getInternalId());
                tiendaKobtenido.setTipo(f.getTipo());
                tiendaKobtenido.setPeriscope(f.getPeriscope());
                tiendaKobtenido.setLon(f.getLon());
                tiendaKobtenido.setLat(f.getLat());
                tiendaKobtenido.setStatus(f.getStatus());
                System.out.println("CRUDTiendaKGetOne onPostExecute paso 2");
                break;
            }
            // adapter.notifyDataSetChanged();//no hay un objeto en la screen que estara siendo modificado
            //quiza se repinten los markers
            System.out.println("CRUDTiendaKGetOne onPostExecute paso 2");
//            updateMap();
            System.out.println("CRUDTiendaKGetOne onPostExecute paso 3");
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        dialog.setMessage("Downloading contacts...");
//        dialog.show();
    }
    @Override
    protected List<DataDescriptorTiendaK> doInBackground(String... params) {

        System.out.println("get tiendaK doInBackground paso 1");

        List<DataDescriptorTiendaK> result = new ArrayList<DataDescriptorTiendaK>();
        System.out.println("get tiendaK doInBackground paso 2");
        try {

            BufferedReader inStream = null;
            System.out.println("get tiendak - 2"+parametroURL);
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

            System.out.println("get tiendak doInBackground paso 5");
//antes asp tenia { edificio [ {     }, {    } ] }
//antes asp                JSONObject jsonResponse = new JSONObject(new String(JSONResp));
//antes asp                JSONArray arr = jsonResponse.getJSONArray("edificio");


//ahora solo es [ {     }, {    } ]
            JSONArray arr = new JSONArray(new String(JSONResp));
            System.out.println("get tiendaK doInBackground paso 6");

            for (int i=0; i < arr.length(); i++) {
                result.add(convertDataDescriptorTiendaK(arr.getJSONObject(i)));
            }
            System.out.println("get tiendak doInBackground paso 7");
            return result;
        } catch(Throwable t) {
            t.printStackTrace();
        }
        System.out.println("get tiendak doInBackground paso 8");
        return null;
    }
    private DataDescriptorTiendaK convertDataDescriptorTiendaK(JSONObject obj) throws JSONException {

        System.out.println("convertDataDescriptorTiendak paso 10");

//            String id = obj.getString("id");
        String id=obj.getString("_id");

        String name = obj.getString("name");
        String tipo = obj.getString("tipo");
        String periscope = obj.getString("periscope");
        String facelive = obj.getString("facelive");
        String lon = obj.getString("lon");
        String lat = obj.getString("lat");
        String status = obj.getString("status");
       System.out.println("convertDataDescriptorTiendak paso 11:"+name);
        System.out.println("convertDataDescriptorTiendak paso 12:"+tipo);
        System.out.println("convertDataDescriptorTiendak paso 13:"+periscope);
        System.out.println("convertDataDescriptorTiendak paso 14:"+facelive);
        System.out.println("convertDataDescriptorTiendak paso 15:"+lon);
        System.out.println("convertDataDescriptorTiendak paso 16:"+lat);
        System.out.println("convertDataDescriptorTiendak paso 17:"+status);


        return new DataDescriptorTiendaK("01",id,name, tipo,periscope,facelive,lon, lat,status);
    }

}//end private class