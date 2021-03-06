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
public class CRUDTiendaKAddNew extends AsyncTask<TextView, String, String> {

    TextView t;
    String result = "fail";


    DataDescriptorTiendaK elNuevoTiendaK;

    @Override
    protected String doInBackground(TextView... params) {
        System.out.println("doInBackground - 1");
        this.t = params[0];
        System.out.println("doInBackground - 2");
        return AddNuevoTiendaK(this.elNuevoTiendaK);


    }

    final String AddNuevoTiendaK(
            DataDescriptorTiendaK socioTienditaK
    )
    {
        System.out.println("AddNuevoTiendaK - 1");

        Constants constantValues=new Constants();
        //Create JSONObject here
        JSONObject jsonParam = new JSONObject();
        try{
            jsonParam.put("name", socioTienditaK.getName());
            jsonParam.put("tipo", socioTienditaK.getTipo());
            jsonParam.put("periscope", socioTienditaK.getPeriscope());
            jsonParam.put("facelive", socioTienditaK.getFacelive());
            jsonParam.put("lon", socioTienditaK.getLon());
            jsonParam.put("lat", socioTienditaK.getLat());
            jsonParam.put("status", socioTienditaK.getStatus());
//            jsonParam.put("status", robotNuevecito.getInternalId());

        }catch(Exception e){
            System.out.println("AddNuevoTiendaK - 12. error de json");
            e.printStackTrace();
        }



        String urlStr = constantValues.URL_ADD_NEW_TIENDAK;

        BufferedReader inStream = null;

        try{

            System.out.println("AddNuevoTiendaK - sql:"+urlStr);
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
                    System.out.println("AddNuevoTiendaK - 7");
                    String NL = System.getProperty("line.separator");
                    System.out.println("AddNuevoTiendaK - 8");
                    while ((line = inStream.readLine()) != null) {
                        buffer.append(line + NL);
                    }
                    System.out.println("AddNuevoTiendaK - 9");
                    inStream.close();
                    System.out.println("AddNuevoTiendaK - 10");
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
            System.out.println("AddNuevoTiendaK - 12 error");
            e.printStackTrace();
        } finally {
            System.out.println("AddNuevoTiendaK - 13");
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
        System.out.println("AddNuevoTiendaK onPostExecute - 10");
//        t.setText(result);
        System.out.println("AddNuevoTiendaK onPostExecute - 20");
    }

}
