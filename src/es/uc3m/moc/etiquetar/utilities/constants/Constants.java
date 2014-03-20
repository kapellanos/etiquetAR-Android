package es.uc3m.moc.etiquetar.utilities.constants;

/**
 * Constants
 *
 * @author JumTech
 * @version 1.0
 * 6/25/13
 *
 * Contendrá las constantes de la aplicación que no cambiarán en función del idioma
 */
public abstract class Constants {

    public static final String APP_NAME = "etiquetAR";

    // Constantes generales de la aplicación
    // TODO modificar la URL base
    public final static String URL_BASE = "http://etiquetar.com.es";
    public static final String URL_SERVER_LOGIN = URL_BASE.concat("/api/v1/tokens.json");
    public static final String URL_SERVER_REGISTER = URL_BASE.concat("/api/v1/android_app/register");
    public static final String URL_SERVER_PROFILES = URL_BASE.concat("/api/v1/android_app/selectprofile");
    public static final String URL_SERVER_RESOURCE = URL_BASE.concat("/api/v1/android_app/selectresource");
    public static final String URL_SERVER_RESOURCE_COMMENT = URL_BASE.concat("/api/v1/android_app/getcomments");
    public static final String URL_SERVER_PUT_COMMENT = URL_BASE.concat("/api/v1/android_app/insertcomment");
    public static final String URL_SERVER_REPLY_COMMENT = URL_BASE.concat("/api/v1/android_app/replycomment");
    public static final String URL_SERVER_RESPONSES_COMMENT = URL_BASE.concat("/api/v1/android_app/getresponses");
    public static final String URL_SERVER_GET_COLLECTION = URL_BASE.concat("/api/v1/etiquetar/getCollections.php");


    // Constantes de ejecución de tareas en segundo plano
    public final static int ID_EXECUTE_LOGIN = 0;
    public final static int ID_REGISTER = 2;
    public static final int ID_GET_PROFILES = 3;
    public static final int ID_GET_RESOURCE_PROFILE = 4;
    public static final int ID_GET_COMMENTS = 5;
    public static final int ID_PUT_COMMENT = 6;
    public static final int ID_REPLY_COMMENT = 7;
    public static final int ID_GET_RESPONSES = 8;
    public static final int ID_SAVE_DATA_TO_DB = 9;
    public static final int ID_REFRESH_DATA = 10;

    // Constantes que almacenarán los parámetros que se pondrán en el JSON y se enviarán al servidor
    public final static String JSON_USER_PARAM = "user";
    public final static String JSON_PASSWORD_PARAM = "password";
    public final static String JSON_SUCCESS = "success";
    public static final String JSON_USER_NAME = "name";
    public static final String JSON_QR_ID_PARAM = "qr_id";
    public static final String JSON_PROFILE_ID_PARAM = "profile_id";
    public static final String JSON_RESOURCE_ID = "resource_id";

    // Constantes que almacenarán los tipos de mensaje en el Log
    public final static String LOG_JSON_ERROR = "JSON";
    public final static String LOG_POST_ERROR = "POST";
    public static final String LOG_BUTTON_PRESS = "BOTÓN APRETADO";

    // Constantes que almacenarán variables resultado de ejecución de tareas
    public static final String LAUNCH_RESOURCE_EXTRA = "LAUNCH_THE_RESOURCE_EXTRA";
    public final static String SCAN_RESULT_EXTRA = "SCAN_RESULT_FORMAT";
    public final static String URL_QR_EXTRA = "URL_QR";
    public final static String QR_ID_EXTRA = "QR_ID";
    public final static String PROFILE_ID_EXTRA = "PROFILE_ID";
    public final static String RESOURCE_ID_EXTRA = "RESOURCE_ID";
    public final static String RESOURCE_NAME = "NAME_RESOURCE";
    public final static String RESOURCE_INFO = "INFO_RESOURCE";
    public final static String RESOURCE_URL ="URL_RESOURCE";
    public final static String RESOURCE_ID = "ID_RESOURCE";
    public static final String RESOURCE_VIEW_COUNTER = "VIEW_COUNTER";
    public static final String RESOURCE_NUMBER_COMMENTS = "NUMBER_COMMENTS";

    public final static String JSON_COMMENT = "comment";
    public static final String JSON_COMMENT_NAME = "name";
    public static final String JSON_COMMENT_DATE = "created_at";
    public static final String JSON_RESULT_COMMENTS = "JSON_RESULT_COMMENTS";
    public static final String TRANSACTION_COMPLETE = "TRANSACTION_COMPLETE";
    public static final String JSON_COMMENT_RESPONSES = "responses";
    public static final String JSON_COMMENT_ID = "id";
    public static final String JSON_COLLECTION_ID = "collection_id";

    public static final int MAXIMUM_COMMENT_SIZE = 220;

    public static final String SHARED_PREFERENCES_NAME = "userName";
    public static final String SHARED_PREFERENCES_LOGGED = "logged";
    public static final String SHARED_PREFERENCES_COLLECTION_PROFILE = "COLLECTION_PROFILE_";
    public static final String SHARED_PREFERENCES_COLLECTION_ID_FOR_QR = "COLLECTION_ID_FOR_QR_";

    public static final int REQUEST_CODE_DASHBOARD = 0;
}