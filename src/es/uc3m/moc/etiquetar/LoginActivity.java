package es.uc3m.moc.etiquetar;

/**
 * LoginActivity.java
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 *
 * 25/06/2013
 * Implementa la actividad principal de acceso a la aplicación
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.uc3m.moc.etiquetar.user.normaluser.activities.DashboardActivity;
import es.uc3m.moc.etiquetar.user.normaluser.activities.resources.comments.ResourceCommentsActivity;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;
import es.uc3m.moc.etiquetar.utilities.constants.DBConstants;
import es.uc3m.moc.etiquetar.utilities.database.DBManager;
import es.uc3m.moc.etiquetar.utilities.general.FrequentMethods;
import es.uc3m.moc.etiquetar.utilities.http.ExecuteInBackground;

public class LoginActivity extends Activity {

    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button bEntrar;
    private ProgressDialog pdLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences =  getSharedPreferences(Constants.APP_NAME,MODE_PRIVATE);
        // TODO BORRAR
        sharedPreferences.edit().clear().commit();
//        sharedPreferences.edit().putBoolean(Constants.SHARED_PREFERENCES_LOGGED, false).commit();
//        sharedPreferences.edit().putString(Constants.SHARED_PREFERENCES_NAME, "kape").commit();
        if(sharedPreferences.getBoolean(Constants.SHARED_PREFERENCES_LOGGED, false)) {
            launchDashboardActivity();
        }
        setContentView(R.layout.login_activity_layout);
        getReferences();
        createListeners();
        // TODO borrar esto; se hace para tener que evitar el login todo el rato
        // Cuando el usuario se loguea, es necesario crear la base de datos (no deben de existir datos previamente)
        dropDB();
//        createDB();
        simulateDB();
//        ExecuteInBackground prueba = new ExecuteInBackground(this);
//        prueba.execute(24);
//        Intent launch = new Intent(this, DashboardActivity.class);
//        startActivity(launch);
//        Intent launch = new Intent(this, ResourceCommentsActivity.class);
//        launch.putExtra(Constants.RESOURCE_ID_EXTRA, 1);
//        startActivity(launch);
//        finish();
//        Intent commentsActivity = new Intent(this, ResourceCommentsActivity.class);
//        commentsActivity.putExtra(Constants.RESOURCE_ID_EXTRA, 1);
//        startActivity(commentsActivity);
    }

    private void simulateDB() {
        SQLiteDatabase sqLiteDatabase = DBManager.getOpenedDatabase(this);
        String resourceName, resourceInfo, resourceURL, resourceContentType, collectionName, collectionCreatedAt, profileName;
        int resourceID, collectionID, userID, numberViews, numberComments, profileID, qrID;
        long date;
        //**************************************************************************************************************\\
        userID = 70;
        collectionName = "Dinosaurios";
        collectionID = 115;
        collectionCreatedAt = "2013-09-14 16:59:20";
        ContentValues collectionContentValues = new ContentValues();
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_NAME, collectionName);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_ID, collectionID);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_CREATED_AT, collectionCreatedAt);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_USER_ID, userID);
        sqLiteDatabase.insert(DBConstants.COLLECTION_TABLE, null, collectionContentValues);
        //**************************************************************************************************************\\
        resourceName = "Tiranosaurio Rex";
        resourceInfo = "";
        resourceURL = "http://bibliotecadeinvestigaciones.files.wordpress.com/2012/03/tiranosaurio-rex.jpg";
        resourceContentType = "image";
        resourceID = 318;
        numberViews = 1;
        numberComments = 0;
        date = System.currentTimeMillis();
        profileID = 136;
        profileName = "Estudiantes";
        qrID = 228;

        ContentValues resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

        resourceName = "Diplodocus";
        resourceInfo = "";
        resourceURL = "http://dinosaurios.org/wp-content/uploads/2013/07/diplodocus9.jpg";
        resourceContentType = "image";
        resourceID = 319;
        qrID = 229;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

        resourceName = "Velociraptor";
        resourceInfo = "";
        resourceURL = "http://images4.wikia.nocookie.net/__cb20111212212920/jurassicpark/es/images/c/cc/Velociraptor_in_color_by_Ahrkeath.jpg";
        resourceContentType = "image";
        resourceID = 320;
        qrID = 230;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

        resourceName = "Espinosaurio";
        resourceInfo = "";
        resourceURL = "http://www.artistasdelatierra.com/obras/foto72236.jpg";
        resourceContentType = "image";
        resourceID = 321;
        qrID = 231;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

        resourceName = "Triceratops";
        resourceInfo = "";
        resourceURL = "http://upload.wikimedia.org/wikipedia/commons/1/1e/Triceratops_BW.jpg";
        resourceContentType = "image";
        resourceID = 322;
        qrID = 232;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

        resourceName = "Tuojiangosaurio";
        resourceInfo = "";
        resourceURL = "http://images.fineartamerica.com/images-medium-large-5/tuojiangosaurus-multispinus-dinosaur-roman-garcia-mora.jpg";
        resourceContentType = "image";
        resourceID = 323;
        qrID = 233;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

        resourceName = "Documental BBC";
        resourceInfo = "";
        resourceURL = "http://www.youtube.com/watch?v=6V70RXR99Kw";
        resourceContentType = "video";
        resourceID = 324;
        qrID = 234;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);

        //**************************************************************************************************************\\
        userID = 70;
        collectionName = "Felinos";
        collectionID = 116;
        collectionCreatedAt = "2013-09-14 16:59:20";
        collectionContentValues = new ContentValues();
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_NAME, collectionName);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_ID, collectionID);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_CREATED_AT, collectionCreatedAt);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_USER_ID, userID);
        sqLiteDatabase.insert(DBConstants.COLLECTION_TABLE, null, collectionContentValues);
        //**************************************************************************************************************\\

        resourceName = "Lince Ibérico";
        resourceInfo = "";
        resourceURL = "http://esmateria.com/wp-content/uploads/2012/08/linces12.jpeg";
        resourceContentType = "image";
        resourceID = 325;
        numberViews = 1;
        numberComments = 0;
        date = System.currentTimeMillis();
        profileID = 137;
        profileName = "Estudiantes";
        qrID = 235;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

        resourceName = "Tigre dientes de sable";
        resourceInfo = "";
        resourceURL = "http://www.bfotos.com/albums/animales-raros/dientes-sable-dibujo.jpg";
        resourceContentType = "image";
        resourceID = 326;
        qrID = 236;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

        resourceName = "Documental leones";
        resourceInfo = "";
        resourceURL = "http://www.youtube.com/watch?v=err7FmCYrO4";
        resourceContentType = "video";
        resourceID = 327;
        qrID = 237;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);

        //**************************************************************************************************************\\
        userID = 70;
        collectionName = "Aves";
        collectionID = 117;
        collectionCreatedAt = "2013-09-14 16:59:20";
        collectionContentValues = new ContentValues();
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_NAME, collectionName);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_ID, collectionID);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_CREATED_AT, collectionCreatedAt);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_USER_ID, userID);
        sqLiteDatabase.insert(DBConstants.COLLECTION_TABLE, null, collectionContentValues);
        //**************************************************************************************************************\\

        resourceName = "Buitre";
        resourceInfo = "";
        resourceURL = "http://www.tarotida.com/wp-content/imagenes/buitre.jpg";
        resourceContentType = "image";
        resourceID = 328;
        numberViews = 1;
        numberComments = 0;
        date = System.currentTimeMillis();
        profileID = 138;
        profileName = "Estudiantes";
        qrID = 238;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

        resourceName = "Periquito";
        resourceInfo = "";
        resourceURL = "http://static.hogarutil.com/archivos/201104/periquito-xl-668x400x80xX.jpg";
        resourceContentType = "image";
        resourceID = 329;
        qrID = 239;

        resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, resourceContentType);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURL);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, date);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, numberViews);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, numberComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        //**************************************************************************************************************\\

    }

    private void dropDB() {
        if(getApplicationContext() != null)
            getApplicationContext().deleteDatabase(DBConstants.DB_NAME);
    }

    private void createDB() {
        // Crea la base de datos al abrirla y que no exista (no debe existir)
        DBManager.getOpenedDatabase(getApplicationContext());
        // Almacena la contraseña de la base de datos en las preferencias privadas de la aplicación
    }

    /**
     * Crea las escuchas de los botones y del text view para registrarse
     */
    private void createListeners() {
        // Escucha para el botón de loguearse contra el servidor
        bEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdLogin = new ProgressDialog(LoginActivity.this);
                pdLogin.setMessage(getString(R.string.pdLogin));
                pdLogin.setCancelable(false);
                pdLogin.show();
                // Se lanza una tarea en segundo plano para ejecutar el login, enviando los credenciales al servidor utilizando la URL {@link Constants.ID_EXECUTE_LOGIN}
                ExecuteInBackground login = new ExecuteInBackground(LoginActivity.this);
                if (etLoginEmail.getText() != null && etLoginPassword.getText() != null)
                    login.execute(Constants.ID_EXECUTE_LOGIN, etLoginEmail.getText().toString(), etLoginPassword.getText().toString());
                // Se lanza un progress dialog para avisar al usuario de lo que se está realizando
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se lanza la actividad de registro, sin finalizar la de login por si el usuario da para atras.
                Intent startRegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(startRegisterActivity);
            }
        });
    }

    /**
     * Obtiene las referencias a los objetos de la vista
     */
    private void getReferences() {
        etLoginEmail = (EditText) findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        bEntrar = (Button) findViewById(R.id.bEntrar);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
    }

    /**
     *
     */
    private void launchDashboardActivity() {
        Intent startMainActivity = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivityForResult(startMainActivity, Constants.REQUEST_CODE_DASHBOARD);
    }

    /**
     * Método llamado cuando se ha comprobado los credenciales y son adecuados
     */
    public void logOk(Object data) {
        pdLogin.dismiss();
        if(data instanceof Integer) {
            if((Integer) data == -2) {
                FrequentMethods.serverNotResponding(this, false);
            }
        } else if(data instanceof JSONArray) {
            try {
                JSONArray resultArray = (JSONArray) data;
                JSONObject jsonData = resultArray.getJSONObject(0);
                // Se obtiene el resultado del acceso
                if(jsonData.getString(Constants.JSON_SUCCESS).equals("OK")) {
                    SharedPreferences.Editor sharedPreferencesEditor =  getSharedPreferences(Constants.APP_NAME,MODE_PRIVATE).edit();
                    sharedPreferencesEditor.putString(Constants.SHARED_PREFERENCES_NAME, jsonData.getString(Constants.JSON_USER_NAME));
                    sharedPreferencesEditor.putBoolean(Constants.SHARED_PREFERENCES_LOGGED, true);
                    sharedPreferencesEditor.commit();
                    launchDashboardActivity();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.errorLoginText)
                            .setCancelable(true)
                            .setTitle(getString(R.string.incorrect_login))
                            .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            }catch (JSONException e) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.REQUEST_CODE_DASHBOARD) {
            if(resultCode == 0) {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //
}
