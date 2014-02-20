package es.uc3m.moc.etiquetar.utilities.http;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Post.java
 *
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 * 6/25/13
 *
 * Realizará una petición POST al servidor
 */
public class Post {

    /**
     * Realiza la petición POST al servidor
     *
     * @param URL URL a la que realizar la petición POST
     * @param data los datos que se pasarán al servidor
     * @return un array de JSON con las respuestas; null en caso de error
     */
    public static JSONArray doPost(String URL, JSONArray data) throws IOException{

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost request = new HttpPost(URL);

        StringEntity entity = new StringEntity(data.toString(), "UTF-8");

        request.setHeader("Content-type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setEntity(entity);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10000);
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
        HttpResponse response = httpClient.execute(request);
        InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), "iso-8859-1");

        BufferedReader br = new BufferedReader(isr, 8);

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        Log.i("Info", sb.toString());
        isr.close();
        JSONArray jsonObj = null;
        try {
            jsonObj = new JSONArray(sb.toString());
        } catch (JSONException e) {
            Log.e("JSON", e.getMessage());
            e.printStackTrace();
        }
        return jsonObj;
    }
}