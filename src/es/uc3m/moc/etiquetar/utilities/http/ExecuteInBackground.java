package es.uc3m.moc.etiquetar.utilities.http;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import es.uc3m.moc.etiquetar.LoginActivity;
import es.uc3m.moc.etiquetar.RegisterActivity;
import es.uc3m.moc.etiquetar.user.normaluser.activities.HomeActivity;
import es.uc3m.moc.etiquetar.user.normaluser.activities.resources.ImageResourceActivity;
import es.uc3m.moc.etiquetar.user.normaluser.activities.resources.SelectProfileActivity;
import es.uc3m.moc.etiquetar.user.normaluser.activities.resources.comments.ResourceCommentsActivity;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;

/**
 * ExecuteInBackground
 *
 * @author Miguel Olmedo
 * @version 1.0
 * 6/25/13
 *
 * Ejecutará una serie de tareas en segundo plano (necesario en ocasiones para dejar libre el hilo principal en caso de operaciones de red)
 */
public class ExecuteInBackground extends AsyncTask<Object, Void, Object> {

    // Variables que almacenarán las actividades que ejecutarán las tareas en segundo plano
    private RegisterActivity registerActivityCalling;
    private LoginActivity loginActivityCalling;
    private SelectProfileActivity selectProfileActivityCalling;
    private ResourceCommentsActivity resourceCommentsActivity;
    private ImageResourceActivity imageResourceActivity;
    private HomeActivity homeActivity;

    public ExecuteInBackground (Activity callingActivity) {
//        if(callingActivity instanceof NormalUserMainActivity) {
//            this.normalUserActivityCalling = (NormalUserMainActivity) callingActivity;
        if(callingActivity instanceof RegisterActivity)
            registerActivityCalling = (RegisterActivity) callingActivity;
        else if(callingActivity instanceof LoginActivity)
            loginActivityCalling = (LoginActivity) callingActivity;
        else if(callingActivity instanceof SelectProfileActivity)
            selectProfileActivityCalling = (SelectProfileActivity) callingActivity;
        else if(callingActivity instanceof ResourceCommentsActivity)
            resourceCommentsActivity = (ResourceCommentsActivity) callingActivity;
        else if(callingActivity instanceof  ImageResourceActivity)
            imageResourceActivity = (ImageResourceActivity) callingActivity;
        else if(callingActivity instanceof HomeActivity)
            homeActivity = (HomeActivity) callingActivity;
    }

    @Override
    protected Object doInBackground(Object... params) {
        // Obtiene el identificador de la acción a realizar
        switch((Integer)params[0]) {
            case Constants.ID_EXECUTE_LOGIN:
                return executePost((Integer) params[0], String.valueOf(params[1]), String.valueOf(params[2]));
            case Constants.ID_REGISTER:
                return executePost((Integer) params[0], String.valueOf(params[1]), String.valueOf(params[2]), String.valueOf(params[3]));
            case Constants.ID_GET_PROFILES:
                return executePost((Integer) params[0], (Integer) (params[1]));
            case Constants.ID_GET_RESOURCE_PROFILE:
                return executePost((Integer) params[0], params[1], params[2], Constants.ID_GET_RESOURCE_PROFILE);
            case Constants.ID_GET_COMMENTS:
                return executePost((Integer) params[0], (Integer) (params[1]));
            case Constants.ID_PUT_COMMENT:
                return executePost((Integer) params[0], params[1], String.valueOf(params[2]), String.valueOf(params[3]));
            case Constants.ID_REPLY_COMMENT:
                return executePost((Integer) params[0], params[1], String.valueOf(params[2]), params[3], params[4], params[5]);
            case Constants.ID_GET_RESPONSES:
                return executePost((Integer) params[0], params[1]);
            case Constants.ID_REFRESH_DATA:
                return executePost((Integer) params[0], params[1], params[2], Constants.ID_REFRESH_DATA);
        }
        return null;
    }

    private Object executePost(int id, Object... params) {
        Object [] results = new Object[2];
        results[0] = id;
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        String URL = "";
        try {
            switch (id) {
                case Constants.ID_EXECUTE_LOGIN:
                    jsonObject.put(Constants.JSON_USER_PARAM, String.valueOf(params[0]));
                    jsonObject.put(Constants.JSON_PASSWORD_PARAM, String.valueOf(params[1]));
                    // Se añade el objeto creado al array JSON
                    URL = Constants.URL_SERVER_LOGIN;
                    break;
                case Constants.ID_REGISTER:
                    jsonObject.put(Constants.JSON_USER_PARAM, String.valueOf(params[0]));
                    jsonObject.put(Constants.JSON_USER_NAME, String.valueOf(params[1]));
                    jsonObject.put(Constants.JSON_PASSWORD_PARAM, String.valueOf(params[2]));
                    URL = Constants.URL_SERVER_REGISTER;
                    break;
                case Constants.ID_GET_PROFILES:
                    jsonObject.put(Constants.JSON_QR_ID_PARAM, params[0]);
                    URL = Constants.URL_SERVER_PROFILES;
                    break;
                case Constants.ID_GET_RESOURCE_PROFILE:
                case Constants.ID_REFRESH_DATA:
                    jsonObject.put(Constants.JSON_QR_ID_PARAM, params[0]);
                    jsonObject.put(Constants.JSON_PROFILE_ID_PARAM, params[1]);
                    results[0] = params[2];
                    URL = Constants.URL_SERVER_RESOURCE;
                    break;
                case Constants.ID_GET_COMMENTS:
                    jsonObject.put(Constants.JSON_RESOURCE_ID, params[0]);
                    URL = Constants.URL_SERVER_RESOURCE_COMMENT;
                    break;
                case Constants.ID_PUT_COMMENT:
                    jsonObject.put(Constants.JSON_RESOURCE_ID, params[0]);
                    jsonObject.put(Constants.JSON_COMMENT, String.valueOf(params[1]));
                    if(params[2] == null || String.valueOf(params[2]).equals(""))
                        jsonObject.put(Constants.JSON_USER_NAME, null);
                    else
                        jsonObject.put(Constants.JSON_USER_NAME, params[2]);
                    URL = Constants.URL_SERVER_PUT_COMMENT;
                    break;
                case Constants.ID_REPLY_COMMENT:
                    int numberResponses = (Integer) params[4];
                    jsonObject.put(Constants.JSON_RESOURCE_ID, params[0]);
                    jsonObject.put(Constants.JSON_COMMENT, params[1]);
                    jsonObject.put(Constants.JSON_COMMENT_ID, params[3]);
                    jsonObject.put(Constants.JSON_COMMENT_RESPONSES, ++numberResponses);
                    if(params[2] == null || String.valueOf(params[2]).equals(""))
                        jsonObject.put(Constants.JSON_USER_NAME, null);
                    else
                        jsonObject.put(Constants.JSON_USER_NAME, params[2]);
                    URL = Constants.URL_SERVER_REPLY_COMMENT;
                    break;
                case Constants.ID_GET_RESPONSES:
                    jsonObject.put(Constants.JSON_COMMENT_ID, params[0]);
                    URL = Constants.URL_SERVER_RESPONSES_COMMENT;
                    break;

            }
            jsonArray.put(jsonObject);
            System.out.println(jsonArray.toString());
            results[1] = Post.doPost(URL, jsonArray);
        } catch (JSONException e) {
            results[1] = -1;
        } catch (IOException e) {
            results[1] = -2;
        }
        return results;
    }


    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        Object [] results = (Object []) result;
        int process = (Integer) results[0];
        switch (process) {
            // Constantes de ejecución de tareas en segundo plano
            case Constants.ID_EXECUTE_LOGIN:
                loginActivityCalling.logOk(results[1]);
                break;
            case Constants.ID_REGISTER:
                registerActivityCalling.registerOk(results[1]);
                break;
            case Constants.ID_GET_PROFILES:
                selectProfileActivityCalling.profilesObtained(results[1]);
                break;
            case Constants.ID_GET_RESOURCE_PROFILE:
                selectProfileActivityCalling.resourceObtained(results[1]);
                break;
            case Constants.ID_REFRESH_DATA:
                homeActivity.refreshObtained(results[1]);
                break;
            case Constants.ID_GET_COMMENTS:
                resourceCommentsActivity.commentsObtained(results[1]);
                break;
            case Constants.ID_PUT_COMMENT:
                resourceCommentsActivity.commentSave(results[1]);
                break;
            case Constants.ID_REPLY_COMMENT:
                resourceCommentsActivity.commentSave(results[1]);
                break;
            case Constants.ID_GET_RESPONSES:
                resourceCommentsActivity.getResponse(results[1]);
                break;
        }
    }
}