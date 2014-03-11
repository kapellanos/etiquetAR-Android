/**
 * ResourceActivity.java
 *
 * 13/08/13
 *
 * Implementará la interfaz de un determinado recurso. Para cada recurso, se utilizará un layout diferente, permitiendo personalizar la
 * experiencia del usuario para cada recurso en concreto.
 *
 * Para las imágenes, se realizará una petición al servidor que obtendrá la imagen y la almcenará en memoria de forma temporal para mostrarla posteriormente
 * al usuario. Utilizando memoria en vez de almacenamiento persistente se simplifica el manejo de las imágenes y permite mejorar el rendimiento.
 * Cuando se utilice el móvil en modo portrait se mostará la imagen junto con el texto asociado a dicho recurso en la base de datos. También se mostrarán
 * los botones que representarán el número de likes, visitas y comentarios. Por último, se podrá acceder a los comentarios del recurso y publicar uno propio
 * a través del botón adecuado.
 * En cuanto al modo landscape, se mostrará la imagen en pantalla completa para que el usuario pueda apreciar mejor los detalles de la misma. Estará integrada
 * en un @{ImageView} por lo que el usuario podrá hacer zoom y tratarla como si fuese una imagen de la galería.
 *
 * Para los textos, se descargará el contenido asociado y se mostrará al usuario en un @{TextView}. Se mostrarán los mismos botones que en el caso de las imágenes
 * (likes, visitas y comentarios) y de la misma forma se podrá acceder a la pantalla de los comentarios del recurso.
 * Cuando se ponga la pantalla en modo LandScape se mostrará el texto en pantalla completa pero se mantendrá el mismo layout, permitiendo de la misma manera acceder
 * a la pantalla de comentarios y visualizar de una sola vez los likes, visitas y número de comentarios.
 *
 * Para los vídeos, se utiliza la API de youtube a través de @{YouTubePlayer}, permitiendo integrar un reproductor de vídeo en la propia actividad.
 * El reproductor se mostrará en una pequeña pantalla en el modo Portrait, permitiendo ver de una sola vez tanto el vídeo como el número de likes, visitas
 * y comentarios. También se permitirá acceder a los comentarios del recurso.
 * En cuanto al modo LandScape, se mostrará el vídeo en pantalla completa permitiendo al usuario ver mayor número de detalles del mismo. Si en un momento dado el
 * usuario decide cambiar la orientación del vídeo, se permitirá continuar el mismo por el punto anterior.
 *
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 */
package es.uc3m.moc.etiquetar.user.activities.resources;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public abstract class ResourceActivity  extends Activity {

    /**
     * Comprueba si el móvil está en modo Portrait
     * @return true en caso de que el móvil esté en modo Portrait; false en caso de que esté en modo LANDSCAPE (o UNDEFINED)
     */
    protected boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    protected abstract  void getReferences();

    public abstract void setResource(Object resource);

    protected abstract void restoreValues(Bundle savedInstance);
}