package es.uc3m.moc.etiquetar.utilities.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import es.uc3m.moc.etiquetar.user.normaluser.activities.CollectionsActivity;
import es.uc3m.moc.etiquetar.utilities.constants.DBConstants;

/**
 * FrequentQuerys
 *
 * @author Miguel Olmedo Camacho
 * @version 1.0
 * 6/25/13
 *
 * Clase abstracta que se utilizará para realizar consultas comunes a través de métodos estáticos
 */
public abstract class FrequentQuerys {

    /**
     * Introducirá los datos de una nueva etiqueta en la base de datos para que esté disponible para el usuario a través de la actividad home
     *
     * @param context               contexto necesario para utilizar la base de datos
     * @param resourceID            el identificador del recurso que se insertará en la base de datos
     * @param resourceName          el nombre del recurso que se insertará en la base de datos
     * @param resourceInfo          la información del recurso que se insertará en la base de datos
     * @param resourceURI           la URL del recurso que se insertará en la base de datos
     * @param viewCounter           el número de visitas que se ha realizado al recurso que se insertará en la base de datos
     * @param contentType           el tipo de recurso (image, video o text)
     * @param profileName           el nombre del perfil a través del cual se ha leído el recurso
     * @param profileID             el identificador del perfil a través del cual se ha leído el recurso
     * @param collectionID          el identificador de la colección a la que pertenece el recurso
     * @param collectionName        el nombre de la colección a la que pertenece el recurso
     * @param collectionCreatedAt   la fecha en la que se creo el recurso
     * @param userID                el identificador del usuario que creo la colección a la que pertenece el recurso
     */
    public static void insertTagAndGetCollection(Context context,
                                                 int qrID,
                                                 int resourceID,
                                                 String resourceName,
                                                 String resourceInfo,
                                                 String resourceURI,
                                                 int viewCounter,
                                                 String contentType,
                                                 String profileName,
                                                 int profileID,
                                                 int collectionID,
                                                 String collectionName,
                                                 String collectionCreatedAt,
                                                 int userID,
                                                 int resourceComments) {
        // Se obtiene una instancia de la base de datos
        SQLiteDatabase sqLiteDatabase = DBManager.getOpenedDatabase(context);
        // Se comprueba si existe el recurso en la base de datos (se ha introducido en el pasado)
        ContentValues resourceContentValues = new ContentValues();
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_CONTENT_TYPE, contentType);
        resourceContentValues.put(DBConstants.RESOURCE_QR_ID, qrID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_ID, resourceID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_INFO, resourceInfo);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_URI, resourceURI);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_NAME, resourceName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_ID, profileID);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_PROFILE_NAME, profileName);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_VIEWS, viewCounter);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_DATE, System.currentTimeMillis());
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COMMENTS, resourceComments);
        resourceContentValues.put(DBConstants.RESOURCE_TABLE_COLLECTION_ID, collectionID);
        if(DBHelper.exist(resourceID, DBConstants.RESOURCE_TABLE, DBConstants.RESOURCE_TABLE_ID, context)) {
            // Si existe se actualizan algunos datos del recurso
            Log.i("DB", "Ya existe un registro con este mismo identificador en la base de datos; actualizando los datos");
            // Se lleva a cabo la sentencia SQL
            sqLiteDatabase.update(DBConstants.RESOURCE_TABLE, resourceContentValues, DBConstants.RESOURCE_TABLE_ID + "= ?", new String[] {String.valueOf(resourceID) });
        } else {
            // En caso de que no exista el recurso, se inserta uno nuevo con un nuevo identificador
            Log.i("DB", "Insertando un nuevo recurso en la base de datos");
            // Se lleva a cabo la sentencia SQL
            sqLiteDatabase.insert(DBConstants.RESOURCE_TABLE, null, resourceContentValues);
        }

        ContentValues collectionContentValues = new ContentValues();
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_NAME, collectionName);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_ID, collectionID);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_CREATED_AT, collectionCreatedAt);
        collectionContentValues.put(DBConstants.COLLECTION_TABLE_USER_ID, userID);
        // Se comprueba si existe una colección con el mismo identificador en la base de datos
        if(DBHelper.exist(collectionID, DBConstants.COLLECTION_TABLE, DBConstants.COLLECTION_TABLE_ID, context)) {
            Log.i("DB", "Ya existe un registro asociado con esa colección; actualizándo los datos");
            sqLiteDatabase.update(DBConstants.COLLECTION_TABLE, collectionContentValues, DBConstants.COLLECTION_TABLE_ID + "= ?", new String[] {String.valueOf(collectionID) });
        } else {
            // Si no existe se inserta un nuevo registro en la base de datos
            Log.i("DB", "Insertando una nueva colección en la base de datos");
            // Se realiza la sentencia SQL
            sqLiteDatabase.insert(DBConstants.COLLECTION_TABLE, null, collectionContentValues);
        }
    }

    /**
     * Obtiene todos los recursos que se encuentran en la base de datos
     * @param context contexto necesario para utilizar la base de datos
     * @return un Cursor con todas las filas de la base de datos en la tabla de recursos
     */
    public static Cursor getResources(Context context) {
        SQLiteDatabase db = DBManager.getOpenedDatabase(context);
        return db.query(DBConstants.RESOURCE_TABLE,
                DBConstants.RESOURCE_COLUMNS,
                null,
                null,
                null,
                null,
                null);
    }

    public static Cursor getCollections(Context context) {
        SQLiteDatabase db = DBManager.getOpenedDatabase(context);
        return db.query(DBConstants.COLLECTION_TABLE,
                DBConstants.COLLECTION_COLUMNS,
                null,
                null,
                null,
                null,
                null);
    }
    /**
     * Almacena un nuevo comentario en la base de datos local del móvil para mostrarlo al usuario cuando quiera consultar sus comentarios o su resumen de acciones.
     *
     * @param resourceID    el identificador del recurso al que se corresponde el comentario
     * @param comment       el comentario completo
     */
    public static void saveNewComment(int resourceID, String comment, Context context) {
        SQLiteDatabase db = DBManager.getOpenedDatabase(context);
    }

    /**
     * Obtiene los recursos asociados a una colección.
     *
     * @param context la actividad que realiza la query
     * @param collectionID el identificador de la colección a la que pertenece el recurso
     * @return un cursor resultado de la query
     */
    public static Cursor getResourcesWithCollectionId(Context context, int collectionID) {
        SQLiteDatabase db = DBManager.getOpenedDatabase(context);
        return db.query(DBConstants.RESOURCE_TABLE,
                DBConstants.RESOURCE_COLUMNS,
                DBConstants.RESOURCE_TABLE_COLLECTION_ID + "=?",
                new String[] {String.valueOf(collectionID)},
                null,
                null,
                null
                );
    }
}