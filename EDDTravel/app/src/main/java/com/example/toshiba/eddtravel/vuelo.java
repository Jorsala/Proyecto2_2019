package com.example.toshiba.eddtravel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class vuelo extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeContainer;
    ArrayList<Category> arreglo;
    AdapterItem adapter;
    String respuesta, message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vuelo);
        Intent intent = getIntent();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        System.out.println(message);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.srlContainer);
        swipeContainer.setOnRefreshListener(this);

        new ObtenerRutas().execute(message);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Ruta.class);
                intent.putExtra(MainActivity.EXTRA_MESSAGE,message);
                startActivity(intent);

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        FloatingActionButton salir = findViewById(R.id.btn_salir);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        arreglo = new ArrayList<>();
        AsignarVuelos();
//        arreglo.add(new Category("123","Guatemala->EEUU","Guatemala->EEUU","150", "200"));
        ListView lista = findViewById(R.id.reservas);

        adapter = new AdapterItem(this, arreglo);
        lista.setAdapter(adapter);

        /*lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                arreglo.add(new Category("177","Guatemala->EEUU","Guatemala->EEUU","150"));
                adapter.notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public void onRefresh() {
        new ObtenerRutas().execute(message);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        arreglo.clear();
        AsignarVuelos();
        swipeContainer.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    //--------------------------------METODOS--------------------------------
    private void AsignarVuelos(){
        try {
            JSONArray respJSON = new JSONArray(respuesta);
            int i=0;
            for(i=0; i< respJSON.length();i++){
                JSONObject objeto = respJSON.getJSONObject(i);
                arreglo.add(new Category(String.valueOf(objeto.getInt("codigo")),objeto.getString("origen")+"->"+objeto.getString("destino"),
                        objeto.getString("ruta"),objeto.getString("precio"),objeto.getString("tiempo")));
            }

        }catch (JSONException e){
            System.out.println(e.getMessage());
        }
    }

    //---------------------------------------OBTENER RUTAS-----------------------------------------
    private class ObtenerRutas extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            //String id = txtId.getText().toString();
            String id = strings[0];
            String aux[] = id.split(" ");
            int i;
            id = "";
            for (i=0;i<aux.length-1;i++){
                id += aux[i]+"%20";
            }
            String f[] = aux[aux.length-1].split("\n");
            id += f[0];
            try
            {
                HttpGet httpRequest = new HttpGet( "http://192.168.43.253:8080/edd/rest/app/getVuelos/"+id);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httpRequest);

                String respStr = EntityUtils.toString(response.getEntity());

                System.out.println(respStr);
                respuesta = respStr;
                return "";

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
