package es.uc3m.moc.etiquetar.utilities.constants;

/**
 * DBConstants
 *
 * @author JumTech
 * @version 1.0
 * 6/25/13
 *
 * Contendrá las constantes de la base de datos (nombre de la base de datos, de las tablas, de los campos...)
 */
public abstract class DBConstants {
    public final static String DB_NAME = "etiquetAR_DB";

    public static final String COLLECTION_TABLE = "collection";
    public static final String COLLECTION_TABLE_ID = "id";
    public static final String COLLECTION_TABLE_NAME = "name";
    public static final String COLLECTION_TABLE_CREATED_AT = "created_at";
    public static final String COLLECTION_TABLE_USER_ID = "user_id";

    public static final String RESOURCE_TABLE = "resource";
    public static final String RESOURCE_QR_ID = "qr_id";
    public static final String RESOURCE_TABLE_ID = "id";
    public static final String RESOURCE_TABLE_CONTENT_TYPE = "content_type";
    public static final String RESOURCE_TABLE_URI = "URI";
    public static final String RESOURCE_TABLE_INFO = "info";
    public static final String RESOURCE_TABLE_NAME = "name";
    public static final String RESOURCE_TABLE_PROFILE_ID = "profile_id";
    public static final String RESOURCE_TABLE_PROFILE_NAME = "profile_name";
    public static final String RESOURCE_TABLE_DATE = "date";
    public static final String RESOURCE_TABLE_VIEWS = "views";
    public static final String RESOURCE_TABLE_COMMENTS = "comments";
    public static final String RESOURCE_TABLE_COLLECTION_ID = "collectionID";

    public static final String COMMENTS_TABLE = "comments";
    public static final String COMMENTS_TABLE_COMMENT_ID = "id";
    public static final String COMMENTS_TABLE_RESOURCE_ID = "resource_id";
    public static final String COMMENTS_TABLE_COMMENT = "comment";
    public static final String COMMENTS_TABLE_RESOURCE_NAME = "resource_name";
    public static final String COMMENTS_TABLE_DATE = "date";

    public static final String[] RESOURCE_COLUMNS = new String[]{RESOURCE_TABLE_ID,
            RESOURCE_TABLE_CONTENT_TYPE, RESOURCE_TABLE_URI,
            RESOURCE_TABLE_INFO, RESOURCE_TABLE_NAME, RESOURCE_TABLE_PROFILE_ID, RESOURCE_TABLE_PROFILE_NAME,
            RESOURCE_TABLE_DATE, RESOURCE_TABLE_VIEWS, RESOURCE_TABLE_COMMENTS, RESOURCE_QR_ID, RESOURCE_TABLE_COLLECTION_ID};

    public static final String[] COLLECTION_COLUMNS = new String[]{COLLECTION_TABLE_ID,
            COLLECTION_TABLE_NAME, COLLECTION_TABLE_CREATED_AT, COLLECTION_TABLE_USER_ID};
}