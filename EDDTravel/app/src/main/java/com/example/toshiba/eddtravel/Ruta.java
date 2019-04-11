package com.example.toshiba.eddtravel;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Ruta extends AppCompatActivity {
    final ArrayList<Category> arreglo = new ArrayList<>();
    final ArrayList<String> rutas = new ArrayList<>();
    String seleccion1, seleccion2, message;
    boolean estado1 = false, estado2 = false;
    public static String respuesta="", resprutas="";
    AdapterItem adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        System.out.println(message);

        new ObtenerDestinos().execute();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AsignarDestinos();

        FloatingActionButton cancelar = findViewById(R.id.btn_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //---------------------------------------SPINNER----------------------------------------
        final ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rutas);
        //adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner origen = findViewById(R.id.spinner_origen);
        Spinner destino = findViewById(R.id.spinner_detino);

        origen.setAdapter(adaptador);
        destino.setAdapter(adaptador);
        adaptador.notifyDataSetChanged();
        //---------------------------------------DESTINOS----------------------------------------

        ListView lista = findViewById(R.id.lista_rutas);
        adapter = new AdapterItem(this, arreglo);
        lista.setAdapter(adapter);

        //---------------------------------LISTENER SPINNER------------------------------------
        origen.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if(estado1==true){
                    Object item = parent.getItemAtPosition(pos);
                    seleccion1 = item.toString();
                    arreglo.clear();
                    new ObtenerRutas().execute(seleccion1, seleccion2);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    RutasPorDestinos();
                    adapter.notifyDataSetChanged();
                }
                else{
                    seleccion1 = parent.getItemAtPosition(pos).toString();
                    estado1 = true;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.notifyDataSetChanged();
            }
        });

        destino.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if(estado2==true){
                    Object item = parent.getItemAtPosition(pos);
                    seleccion2 = item.toString();
                    System.out.println(seleccion2);
                    arreglo.clear();
                    new ObtenerRutas().execute(seleccion1, seleccion2);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    RutasPorDestinos();
                    adapter.notifyDataSetChanged();
                }
                else{
                    seleccion2 = parent.getItemAtPosition(pos).toString();
                    estado2 = true;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.notifyDataSetChanged();
            }
        });
        //------------------------------LISTENER LISTVIEW-------------------------------
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                //Aqui se envia al servidor la ruta seleccionada
                new AsignarRutas().execute(message, arreglo.get(pos).getTiempo(), arreglo.get(pos).getCosto(), arreglo.get(pos).getDescription());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Snackbar.make(view, "Se ha registrado el vuelo", Snackbar.LENGTH_LONG)
                        .setAction("Sistema", null).show();
                adapter.notifyDataSetChanged();
            }
        });
    }

    //---------------------------------------METODOS----------------------------------------------
    private void AsignarDestinos() {
        try {
            JSONArray respJSON = new JSONArray(respuesta);
            int i=0;
            for(i=0; i< respJSON.length();i++){
                JSONObject objeto = respJSON.getJSONObject(i);
                rutas.add(objeto.getString("nombre"));
            }

        }catch (JSONException e){
            System.out.println(e.getMessage());
        }
    }

    private void RutasPorDestinos() {
        try {
            JSONArray respJSON = new JSONArray(resprutas);
            int i=0;
            for(i=0; i< respJSON.length();i++){
                JSONObject objeto = respJSON.getJSONObject(i);
                arreglo.add(new Category(String.valueOf(i),objeto.getString("nombre"),
                        objeto.getString("ruta"),objeto.getString("precio"),objeto.getString("tiempo")));
            }

        }catch (JSONException e){
            System.out.println(e.getMessage());
        }
    }


    //-----------------------------------------OBTENER DESTINOS---------------------------------------
    private class ObtenerDestinos extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try
            {
                HttpGet httpRequest = new HttpGet( "http://192.168.43.253:8080/edd/rest/app/getDestinos");
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpRequest);

                String respStr = EntityUtils.toString(response.getEntity());

                respuesta = respStr;
                //System.out.println(respuesta);
                return respStr;

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }
            return null;
        }

        protected void onPostExecute(int code) {
            // TODO: check this.exception
            // retrieve your 'code' here
        }
    }

    //-----------------------------------------OBTENER RUTAS---------------------------------------
    private class ObtenerRutas extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String origen = strings[0];
            String destino = strings[1];
            String aux[] = origen.split(" ");
            int i=0;
            origen = "";
            for (i=0;i<aux.length-1;i++){
                origen += aux[i]+"%20";
            }
            origen += aux[aux.length-1];
            aux = destino.split(" ");
            destino = "";
            for (i=0;i<aux.length-1;i++){
                destino += aux[i]+"%20";
            }
            destino += aux[aux.length-1];

            try
            {
                HttpGet httpRequest = new HttpGet( "http://192.168.43.253:8080/edd/rest/app/ListaRutas/"+origen+"_"+destino);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpRequest);

                String respStr = EntityUtils.toString(response.getEntity());
                resprutas = respStr;
                System.out.println(resprutas);
                return respStr;

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }
            return null;
        }

        protected void onPostExecute(int code) {
            // TODO: check this.exception
            // retrieve your 'code' here
            RutasPorDestinos();
            adapter.notifyDataSetChanged();
        }
    }

    //-----------------------------------------ASIGNAR RUTAS---------------------------------------

    private class AsignarRutas extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String id = strings[0];
            String aux[] = id.split(" ");
            int i;
            id = "";
            for (i=0;i<aux.length-1;i++){
                id += aux[i]+"%20";
            }
            String f[] = aux[aux.length-1].split("\n");
            id += f[0];

            String viaje = strings[3];
            aux = viaje.split(",");
            int j;
            viaje = "";
            for(i=0; i<aux.length; i++){
                String linea[] = aux[i].split(" ");
                for(j=0;j <linea.length-1; j++){
                    viaje+= linea[j] + "%20";
                }
                viaje+= linea[linea.length-1];
                if(i!=aux.length-1)
                    viaje += ",";
            }
            try
            {
                URL url = new URL("http://192.168.43.253:8080/edd/rest/app/AgregarReservacion"); //in the real code, there is an ip and a port
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","text/plain");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("clave", 0);
                jsonParam.put("reservacion", 0);
                jsonParam.put("nombre", id);
                jsonParam.put("tiempo", strings[1]);
                jsonParam.put("costo", strings[2]);
                jsonParam.put("viaje", strings[3]);
                jsonParam.put("activo", 0);
                jsonParam.put("cabezaVuelo", null);
                jsonParam.put("ultimoVuelo", null);


                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                DataInputStream is = new DataInputStream(conn.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder();
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
                System.out.println("este "+total);
                Log.i("RESPUESTA" , conn.getResponseMessage());
                conn.disconnect();

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
            }
            return null;
        }

        protected void onPostExecute(int code) {
            // TODO: check this.exception
            // retrieve your 'code' here
        }
    }
}
