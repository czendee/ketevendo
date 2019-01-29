package com.youtochi.ktvendo;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 813743 on 10/11/2017.
 */
public class RequestTaskEnviarNuevoTiendaWeb extends AsyncTask<TextView, String, String> {

    TextView t;
    String result = "fail";


    DataDescriptorTienda elNuevoTienda;

    @Override
    protected String doInBackground(TextView... params) {
        System.out.println("doInBackground - 1");
        this.t = params[0];
        System.out.println("doInBackground - 2");
        return AddNuevaComandoRobot(this.elNuevoTienda);


    }

    final String AddNuevaComandoRobot(
            DataDescriptorTienda robotTiendita
    )
    {
        System.out.println("AddNuevaComandoRobotWeb - 1");

        Constants constantValues=new Constants();
        //Create JSONObject here
        JSONObject jsonParam = new JSONObject();
        try{



            jsonParam.put("name", robotTiendita.getName());
            jsonParam.put("tipo", robotTiendita.getTipo());
            jsonParam.put("manada", robotTiendita.getManada());
            jsonParam.put("lat", robotTiendita.getPosLat());

            jsonParam.put("lon", robotTiendita.getPosLon());
            jsonParam.put("rentatiempo", robotTiendita.getRentaTiempo());
            jsonParam.put("rentacosto", robotTiendita.getRentaCosto());
            jsonParam.put("transmite", robotTiendita.getTransmite());
            jsonParam.put("transmitecanal", robotTiendita.getTransmiteCanal());
//            jsonParam.put("status", robotNuevecito.getInternalId());

        }catch(Exception e){
            System.out.println("AddNuevaComandoRobotWeb - 12. error de json");
            e.printStackTrace();
        }



        String urlStr = constantValues.URL_ADD_NEW_TIENDA;

        BufferedReader inStream = null;



        try{

            System.out.println("AddNuevoRobotWeb - sql:"+urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(jsonParam.toString());
            wr.flush();

            try {

                //display what returns the POST request

                StringBuilder sb = new StringBuilder();
                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    //readStream(in);

                    inStream = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuffer buffer = new StringBuffer("");
                    String line = "";
                    System.out.println("AddNuevoRobotWeb - 7");
                    String NL = System.getProperty("line.separator");
                    System.out.println("AddNuevoRobotWeb - 8");
                    while ((line = inStream.readLine()) != null) {
                        buffer.append(line + NL);
                    }
                    System.out.println("AddNuevoRobotWeb - 9");
                    inStream.close();
                    System.out.println("AddNuevoRobotWeb - 10");
                    result = buffer.toString();
                    System.out.println("GetSomething - 11");
                } else {
                    System.out.println(urlConnection.getResponseMessage());
                }



            } finally {
                urlConnection.disconnect();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("AddNuevoRobotWeb - 12 error");
            e.printStackTrace();
        } finally {
            System.out.println("AddNuevoRobotWeb - 13");
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        System.out.println("GetSomething - 14");
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("AddNuevoRobottWeb onPostExecute - 10");
//        t.setText(result);
        System.out.println("AddNuevoRobotWeb onPostExecute - 20");
    }

}
