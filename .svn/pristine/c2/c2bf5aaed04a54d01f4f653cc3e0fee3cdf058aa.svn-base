/**
 * FrequentMethods.java
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 * 19/09/13
 *
 * Implementa una serie de métodos utilizados en diversas clases para evitar la duplicidad de código
 */
package es.uc3m.moc.etiquetar.utilities.general;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.NavUtils;

import com.google.android.youtube.player.internal.ac;

import es.uc3m.moc.etiquetar.LoginActivity;
import es.uc3m.moc.etiquetar.R;
import es.uc3m.moc.etiquetar.user.normaluser.activities.resources.SelectProfileActivity;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;

public abstract class FrequentMethods {

    /**
     * Comprueba si la URL pertenece a etiquetAR
     * @param URL la URL a verificar
     * @return true en caso de que pertenezca; false en caso de que no sea una URL válida
     */
    public static boolean isValidURL(String URL) {
        return URL.startsWith(Constants.URL_BASE);
    }

    /**
     * Parsea la fecha para mostrarla en un formato adecuado en los comentarios
     * @param originalDate la fecha en su formato original
     * @return un String que contiene la fecha en el formato adecuado para mostrarla
     */
    public static String parseDate(String originalDate) {
        String parserDate;
        String day, month, year;
        String [] allDate = originalDate.split("T");
        String [] onlyDate = allDate[0].split("-");
        year = onlyDate[0];
        month = onlyDate[1];
        day = onlyDate[2];
        parserDate = day + "/" + month + "/" + year + " a las " + allDate[1].substring(0, allDate[1].length() - 1);
        return parserDate;
    }

    /**
     * Se utilizará como método común a varias clases cuando se pulse un botón del Action Bar. De esta manera se evita la duplicidad de código en todas las clases
     * que tienen que responder ante los mismos eventos al pulsar estos botones (ir a inicio, ayuda, salir...)
     *
     * @param activity la actividad en la que se ha pulsado un determinado botón del Action Bar
     * @param id el identificador del botón que se ha pulsado
     */
    public static void onOptiomItemSelected(final Activity activity, int id) {
        switch (id) {
            case R.id.logoutIcon:
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity).
                        setMessage(activity.getString(R.string.logout)).
                        setPositiveButton(activity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent launchLoginActivity = new Intent(activity.getApplicationContext(), LoginActivity.class);
                                launchLoginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE).edit();
                                editor.putBoolean("logged", false);
                                editor.commit();
                                activity.startActivity(launchLoginActivity);
                                activity.finish();
                            }
                        }).
                        setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.helpIcon:
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(activity);
                break;
        }
    }

    /**
     * Muestra al usuario un Alert Dialog al escanear una etiqueta que no ha sido generada con la web de etiquetar
     * @param context el contexto donde mostrar el Alert Dialog
     */
    private static void invalidTag(Context context) {
        new AlertDialog.Builder(context).
                setCancelable(true).
                setMessage(context.getString(R.string.invalid_tag)).
                setTitle(context.getString(R.string.invalid_tag_title)).
                setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * Muestra un Alert Dialog al usuario cuando la URL de una etiqueta no se corresponde con una URL válida de YouTube
     * @param context el contexto donde mostrar el Alert Dialog
     */
    public static void invalidYoutubeURL(Context context) {
        new AlertDialog.Builder(context).
                setCancelable(true).
                setMessage(context.getString(R.string.invalid_youtube_url)).
                setTitle(context.getString(R.string.invalid_youtube_url_title)).
                setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * Muestra un diálogo cuando no existe recursos en la base de datos (esto se utiliza en diversas actividades donde se muestra un resumen de la actividad del usuario)
     * @param context el contexto donde mostrar el Alert Dialog
     */
    public static void noScanResources(final Context context, final Activity activity) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.no_tags_title)
                .setMessage(R.string.no_tags)
                .setPositiveButton(R.string.scan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("es.uc3m.moc.etiquetar.qrlecture.SCAN");
                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
                        activity.startActivityForResult(intent, 0);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * Método llamado cuando se escanea una etiqueta a través de etiquetar. Comprobará si la etiqueta se corresponde con una etiqueta de etiquetar y lanzará
     * la actividad de selección de perfil para mostrar finalmente el contenido del recurso
     *
     * @param activity la actividad desde la cual se ha recogido el resultado de escanear la etiqueta
     * @param result la URL que estaba codificada en la etiqueta
     */
    public static void launchSelectProfileActivity(Activity activity, String result) {
        // Obtiene la URL resultado del código QR, comprueba si es válida y lanza una nueva actividad para mostrar el contenido
        if(FrequentMethods.isValidURL(result)) {
            int id = result != null ? Integer.parseInt(result.substring(result.lastIndexOf("/") + 1)) : 0;
            Intent selectProfileActivity = new Intent(activity.getApplicationContext(), SelectProfileActivity.class);
            selectProfileActivity.putExtra(Constants.QR_ID_EXTRA, id);
            activity.startActivity(selectProfileActivity);
        } else {
            invalidTag(activity);
        }
    }

    /**
     * Muestra un Alert Dialog cuando se produce una excepción debido a que el servidor no responde
     * @param activity la actividad desde la cual se ha producido el error de conexión con el servidor
     */
    public static void serverNotResponding(final Activity activity, final boolean finishActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.server_not_responding)
                .setCancelable(true)
                .setTitle(activity.getString(R.string.server_not_responding_title))
                .setPositiveButton(activity.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(finishActivity)
                            activity.finish();
                    }
                }).create().show();
    }

    /**
     * Comprueba si el móvil está en modo Portrait
     * @return true en caso de que el móvil esté en modo Portrait; false en caso de que esté en modo LANDSCAPE (o UNDEFINED)
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}