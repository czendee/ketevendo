package com.youtochi.ktvendo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 813743 on 10/11/2017.
 */
//-------------------------------------------------Tarea asyncrona que lee datos de la web --------------------

public class CRUDSocioGetList extends AsyncTask<String, Void, List<DataDescriptorSocio>> {
//    private final ProgressDialog dialog = new ProgressDialog(MiMapaTiendasActivity.this); //asi porque estamos dentro de un fragment

    public String parametroURL="aqui";
    public List<DataDescriptorSocio> listasociosobtenidos;
    public ArrayList<String> arrayTasks = new ArrayList<>();
    @Override
    protected void onPostExecute(List<DataDescriptorSocio> result) {
        super.onPostExecute(result);
//        dialog.dismiss();
//            adpt.setItemList(result);
//            adpt.notifyDataSetChanged();
        System.out.println("AsyncListViewLoader onPostExecute paso 1");

        //cylce through List DataDescriptorRobot result, y ponerlo en lista de ArrayTask
        if(result!=null) {
            for (DataDescriptorSocio f : result) {
                arrayTasks.add(f.getName() + " - " + f.getMobile() + " - " + f.getPin() + "-" + f.getStatus() + "-");// api socios

                listasociosobtenidos.add(f); //aqui almacenamos cada dataDescriptor de socio
            }
            // adapter.notifyDataSetChanged();//no hay un objeto en la screen que estara siendo modificado
            //quiza se repinten los markers
            System.out.println("AsyncListViewLoader onPostExecute paso 2");
//            updateMap();
            System.out.println("AsyncListViewLoader onPostExecute paso 3");
        }
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        dialog.setMessage("Downloading contacts...");
//        dialog.show();
    }
    @Override
    protected List<DataDescriptorSocio> doInBackground(String... params) {

        System.out.println("get socios doInBackground paso 1");

        List<DataDescriptorSocio> result = new ArrayList<DataDescriptorSocio>();
        System.out.println("get socios doInBackground paso 2");
        try {

            BufferedReader inStream = null;
            System.out.println("get socios - 2"+parametroURL);
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

            System.out.println("get socios doInBackground paso 5");
//antes asp tenia { edificio [ {     }, {    } ] }
//antes asp                JSONObject jsonResponse = new JSONObject(new String(JSONResp));
//antes asp                JSONArray arr = jsonResponse.getJSONArray("edificio");


//ahora solo es [ {     }, {    } ]
            JSONArray arr = new JSONArray(new String(JSONResp));
            System.out.println("get socios doInBackground paso 6");

            for (int i=0; i < arr.length(); i++) {
                result.add(convertDataDescriptorSocio(arr.getJSONObject(i)));
            }
            System.out.println("get socios doInBackground paso 7");
            return result;
        } catch(Throwable t) {
            t.printStackTrace();
        }
        System.out.println("get socios doInBackground paso 8");
        return null;
    }
    private DataDescriptorSocio convertDataDescriptorSocio(JSONObject obj) throws JSONException {

        System.out.println("convertDataDescriptorSocio paso 10");

//            String id = obj.getString("id");
        String id=obj.getString("_id");

        String name = obj.getString("name");
        String mobile = obj.getString("mobile");
        String pin = obj.getString("pin");
        String status = obj.getString("status");
       System.out.println("convertDataDescriptorSocio paso 11:"+name);
        System.out.println("convertDataDescriptorSocio paso 12:"+mobile);
        System.out.println("convertDataDescriptorSocio paso 13:"+pin);
        System.out.println("convertDataDescriptorSocio paso 14:"+status);


        return new DataDescriptorSocio("01",id,name, mobile,pin,status);
    }

}//end private class