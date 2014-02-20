package es.uc3m.moc.etiquetar.utilities.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import es.uc3m.moc.etiquetar.utilities.constants.DBConstants;

/**
 * DBManager
 *
 * @author Miguel Olmedo Camacho
 * @version 1.0
 * 6/25/13
 *
 * Clase que manejará una sola instancia de la base de datos
 */
public class DBManager {

    // Instancia de la base de datos que se devolerá a la clase llamadora
    private static SQLiteDatabase db;
    // Instancia de ayuda de base de datos para obtener una instancia de la base de datos de la aplicación
    private static DBHelper dbHelper;

    public static SQLiteDatabase getOpenedDatabase(Context context) {
        // Comprueba si la instancia de ayuda de la base de datos es nula
        if (dbHelper == null) {
            // En caso de ser nula la instancia, obtiene una nueva instancia para obtener la base de datos a través de la clase de ayuda
            dbHelper = new DBHelper(context, DBConstants.DB_NAME, null, 2);
        }
        // Comprueba si la instancia compartida de la base de datos es nula
        if (db == null) {
            // En caso de ser nula, obtiene una instancia de la base de datos compartida y la devuelve
            db = dbHelper.getWritableDatabase();
        }
        return db;
    }
}