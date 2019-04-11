package com.example.toshiba.eddtravel;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText usu, contra;
    public static final String EXTRA_MESSAGE = "com.example.toshiba.eddtravel";
    static String id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        usu = findViewById(R.id.txt_nom);
        contra = findViewById(R.id.txt_pass);
        Button aceptar = findViewById(R.id.btn_login);
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Login().execute(usu.getText().toString(), contra.getText().toString());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(id!=""){
                    System.out.println("EL ID" + id);
                    Intent intent = new Intent(view.getContext(), vuelo.class);
                    intent.putExtra(EXTRA_MESSAGE,id);
                    id = "";
                    startActivity(intent);
                }
                else{
                    Snackbar snackbar = Snackbar.make(view, "Datos incorrectos", Snackbar.LENGTH_LONG)
                            .setAction("Sistema", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.parseColor("#FFE4656E"));
                    snackbar.show();
                }
            }
        });
    }

    //---------------------------------------LOGIN-----------------------------------------
    private class Login extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            //String id = txtId.getText().toString();
            try
            {
                URL url = new URL("http://192.168.43.253:8080/edd/rest/app/ValidarUsuario"); //in the real code, there is an ip and a port
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","text/plain");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("nombre", strings[0]);
                jsonParam.put("password", strings[1]);
                jsonParam.put("id", "0");
                jsonParam.put("izquierda", null);
                jsonParam.put("derecha", null);


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
                id = total.toString();
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
            System.out.println("el id: "+id);
        }
    }
}
