/**
 * CommentsActivity.java
 *
 * 13/09/13
 *
 * <definicion de la clase>
 *
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 *
 */
package es.uc3m.moc.etiquetar.user.normaluser.activities;

import android.app.Activity;
import android.os.Bundle;

import es.uc3m.moc.etiquetar.R;

public class CommentsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_comments);
    }
}
