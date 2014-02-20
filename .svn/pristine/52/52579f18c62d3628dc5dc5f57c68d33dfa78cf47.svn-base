package es.uc3m.moc.etiquetar.user.normaluser.activities.resources;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.uc3m.moc.etiquetar.R;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;
import es.uc3m.moc.etiquetar.utilities.database.FrequentQuerys;
import es.uc3m.moc.etiquetar.utilities.general.FrequentMethods;
import es.uc3m.moc.etiquetar.utilities.http.ExecuteInBackground;

/**
 * SelectProfileActivity.java
 *
 * 12/08/2013
 *
 * Implementará una actividad que permitirá al usuario seleccionar un determinado perfil asociado a un recurso vía base de datos.
 * Los perfiles se utilizarán en la aplicación para modificar el contenido del recurso en función de las características del usuario.
 * Para poder proceder con la lectura del recurso es necesario que el usuario escoja uno de los perfiles disponibles y que dicho perfil
 * tenga en la web un contenido asociado, ya que cada perfil de cada recurso es único y es necesario definir de forma explícita
 * el contenido asociado al mismo.
 *
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 */

public class SelectProfileActivity extends Activity implements View.OnClickListener{

    private LinearLayout llResourceLayout;

    // TODO implementar todo esto pero contra el servidor de etiquetar en vez de contra el mío

    // Identificador del código QR
    private int id, profileID;
    private String profileName;
    private ProgressDialog pdDialog;
    private int collectionId;
    private CheckBox cb;

    /*
        Pasos para implementar estas acciones:
        1. Lectura de código QR y petición de perfiles al servidor (perfiles asociados a ese QR)
        2. Una vez seleccionado el perfil, petición del recurso asociado a ese perfil al servidor.
        3. Mostrar el recurso. Si el usuario pincha en comentarios, petición de comentarios asociados a ese recurso.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtiene el ID del QR
        // TODO modificar esto cuando se vea el formato del código QR original
        id = getIntent().getIntExtra(Constants.QR_ID_EXTRA, 0);
        if(getIntent().getBooleanExtra(Constants.LAUNCH_RESOURCE_EXTRA, false)) {
            launchSeeResourceIntent(id, false, getIntent().getIntExtra(Constants.PROFILE_ID_EXTRA, 0), null);
        } else {
            // Se muestra el progress dialog para dar feedback al usario
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE);
            int profileID = sharedPreferences.getInt(Constants.SHARED_PREFERENCES_COLLECTION_PROFILE + sharedPreferences.getInt(Constants.SHARED_PREFERENCES_COLLECTION_ID_FOR_QR + id, 0), 0);
            if(profileID != 0) {
                ExecuteInBackground getResource = new ExecuteInBackground(this);
                getResource.execute(Constants.ID_GET_RESOURCE_PROFILE, id,  profileID);
                pdDialog = new ProgressDialog(this);
                pdDialog.setMessage(getResources().getString(R.string.pdResource));
                pdDialog.setCancelable(false);
                pdDialog.show();
            } else {
                setContentView(R.layout.select_profile_layout);
                getReferences();
                pdDialog = new ProgressDialog(this);
                pdDialog.setMessage(getResources().getString(R.string.pdProfile));
                pdDialog.setCancelable(false);
                pdDialog.show();
                // Acción en segundo plano para obtener los perfiles asociados a un determinado recurso
                ExecuteInBackground getProfiles = new ExecuteInBackground(this);
                getProfiles.execute(Constants.ID_GET_PROFILES, id);
            }
        }
    }

    /**
     * Obtiene las referencias del layout
     */
    private void getReferences() {
        llResourceLayout = (LinearLayout) findViewById(R.id.llResourceLayout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Método llamado al recibir los perfiles del servidor
     * @param data datos devueltos por el servidor; puede ser un entero (en caso de error) o un JSONArray (en caso de haber obtenido los perfiles correctamente)
     */
    public void profilesObtained(Object data) {

        pdDialog.dismiss();
        if(data instanceof Integer) {
            // Error
            if((Integer) data == -2) {
                FrequentMethods.serverNotResponding(this, true);
            }
        } else if(data instanceof JSONArray) {
            try {
                JSONArray profiles = (JSONArray) data;
                this.collectionId = profiles.getJSONObject(0).getInt("collectionID");

                int profileID = getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE).getInt(Constants.SHARED_PREFERENCES_COLLECTION_PROFILE + collectionId, 0);
                if(profileID != 0) {
                    ExecuteInBackground getResource = new ExecuteInBackground(this);
                    getResource.execute(Constants.ID_GET_RESOURCE_PROFILE, id,  profileID);
                    pdDialog = new ProgressDialog(this);
                    pdDialog.setMessage(getResources().getString(R.string.pdResource));
                    pdDialog.setCancelable(false);
                    pdDialog.show();
                }
                // Se obtiene todos los perfiles del array
                for(int i = 0; i < profiles.length(); i++) {

                    // Se crea un textview con cada uno de los perfiles que existen
                    Button btnProfile = (Button) getLayoutInflater().inflate(R.layout.button_template, null);
                    // Se obtiene el nombre del perfil
                    if(btnProfile != null) {
                        btnProfile.setText(profiles.getJSONObject(i).getString("profileName"));
                        btnProfile.setId(profiles.getJSONObject(i).getInt("profileID"));
                        // Se crean los parámetros de los botones
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(40, 20, 40, 0);
                        btnProfile.setLayoutParams(params);
                        llResourceLayout.addView(btnProfile);
                        btnProfile.setOnClickListener(this);
                    }
                }
                cb = new CheckBox(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(40, 20, 40, 0);
                cb.setLayoutParams(params);
                cb.setText(getString(R.string.cb_remember_profile));
                cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                llResourceLayout.addView(cb);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        Button pressButton = (Button) view;
        // Ver si el botón de recordar perfil está activado
        if(cb.isChecked()) {
            getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE).edit().putInt(Constants.SHARED_PREFERENCES_COLLECTION_ID_FOR_QR + id, collectionId).commit();
            getSharedPreferences(Constants.APP_NAME, MODE_PRIVATE).edit().putInt(Constants.SHARED_PREFERENCES_COLLECTION_PROFILE + collectionId, pressButton.getId()).commit();
        }
        profileID = view.getId();
        // Obtiene el botón pulsado por parte del usuario. Habrá que ver el texto de dicho botón y realizar una petición al servidor en función del contenido del botón.
        launchSeeResourceIntent(id, true, pressButton.getId(), pressButton);

    }

    private void launchSeeResourceIntent(int resourceID, boolean isButton, int profileID, Button pressButton) {
        ExecuteInBackground getResource = new ExecuteInBackground(this);
        getResource.execute(Constants.ID_GET_RESOURCE_PROFILE, resourceID, profileID);
        if(isButton)
            if(pressButton.getText() != null) profileName = pressButton.getText().toString();
        pdDialog = new ProgressDialog(this);
        pdDialog.setMessage(getResources().getString(R.string.pdResource));
        pdDialog.setCancelable(true);
        pdDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FrequentMethods.onOptiomItemSelected(SelectProfileActivity.this, item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    // Método llamado cuando se ha obtenido el recurso del servidor
    public void resourceObtained(Object data) {
        pdDialog.dismiss();
        // El primer paso es ver que tipo de contenido contiene el recurso para mostrar un layout u otro
        if(data instanceof Integer) {
            if((Integer) data == -2)
                FrequentMethods.serverNotResponding(this, false);
        } else if(data instanceof JSONArray) {
            try {
                JSONObject result = ((JSONArray) data).getJSONObject(0);
                String success = result.getString("success");
                if(success.equals("OK")) {
                    // Almacena los datos en la base de datos, obteniendo los datos de la colección y del recurso
                    String name, info, URL, contentType, collectionName, collectionCreatedAt;
                    int resourceID, viewCounter, collectionID, collectionIdUser, comments;
                    collectionID = result.getInt("collectionID");
                    collectionIdUser = result.getInt("collection_userID");
                    collectionCreatedAt = result.getString("collection_created_at");
                    collectionName = result.getString("collection_name");
                    contentType = result.getString("content_type");
                    name = result.getString("name");
                    info = result.getString("info").equals("null") ? "" : result.getString("info");
                    URL = result.getString("uri");
                    resourceID = result.getInt("resourceID");
                    viewCounter = result.getString("view_counter").equals("null") ? 0 : result.getInt("view_counter");
                    comments = result.getString("resource_comments").equals("null") ? 0 : result.getInt("resource_comments");
                    FrequentQuerys.insertTagAndGetCollection(this,
                            id,
                            resourceID,
                            name,
                            info,
                            URL,
                            viewCounter,
                            contentType,
                            profileName,
                            profileID,
                            collectionID,
                            collectionName,
                            collectionCreatedAt,
                            collectionIdUser,
                            comments);
                    if(contentType.equals("image")) {
                        Intent imageResource = new Intent(getApplicationContext(), ImageResourceActivity.class);
                        imageResource.putExtra(Constants.RESOURCE_NAME, name);
                        imageResource.putExtra(Constants.RESOURCE_INFO, info);
                        imageResource.putExtra(Constants.RESOURCE_URL, URL);
                        imageResource.putExtra(Constants.RESOURCE_ID, resourceID);
                        imageResource.putExtra(Constants.RESOURCE_VIEW_COUNTER, viewCounter);
                        imageResource.putExtra(Constants.RESOURCE_NUMBER_COMMENTS, comments);
                        startActivity(imageResource);
                    } else if(contentType.equals("text")) {
                        // Se obtienen las referencias a los elementos del layout de un contenido tipo texto
                        Intent textResource = new Intent(getApplicationContext(), TextResourceActivity.class);
                        textResource.putExtra(Constants.RESOURCE_NAME, name);
                        textResource.putExtra(Constants.RESOURCE_INFO, URL);
                        textResource.putExtra(Constants.RESOURCE_URL, URL);
                        textResource.putExtra(Constants.RESOURCE_ID, resourceID);
                        textResource.putExtra(Constants.RESOURCE_VIEW_COUNTER, viewCounter);
                        textResource.putExtra(Constants.RESOURCE_NUMBER_COMMENTS, comments);
                        startActivity(textResource);
                    } else if(contentType.equals("video")) {
                        Intent videoResource = new Intent(getApplicationContext(), VideoResourceActivity.class);
                        videoResource.putExtra(Constants.RESOURCE_NAME, name);
                        videoResource.putExtra(Constants.RESOURCE_INFO, info);
                        videoResource.putExtra(Constants.RESOURCE_URL, URL);
                        videoResource.putExtra(Constants.RESOURCE_ID, resourceID);
                        videoResource.putExtra(Constants.RESOURCE_VIEW_COUNTER, viewCounter);
                        videoResource.putExtra(Constants.RESOURCE_NUMBER_COMMENTS, comments);
                        startActivity(videoResource);
                    } else if(contentType.equals("url")) {
                        Intent urlResource = new Intent(getApplicationContext(), URLResourceActivity.class);
                        urlResource.putExtra(Constants.RESOURCE_NAME, name);
                        urlResource.putExtra(Constants.RESOURCE_INFO, info);
                        urlResource.putExtra(Constants.RESOURCE_URL, URL);
                        urlResource.putExtra(Constants.RESOURCE_ID, resourceID);
                        urlResource.putExtra(Constants.RESOURCE_VIEW_COUNTER, viewCounter);
                        urlResource.putExtra(Constants.RESOURCE_NUMBER_COMMENTS, comments);
                        startActivity(urlResource);
                    }
                    this.finish();
//                    getVideoReferences();
//                    setVideo();
                } else if(success.equals("KO")) {
                    // error al realizar la petición del recurso (no existe)
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.alertDialogNoResource)).
                            setTitle(getResources().getString(R.string.alertDialogNoResourceTitle)).
                            setCancelable(true).
                            setPositiveButton("OK", null).
                            show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}