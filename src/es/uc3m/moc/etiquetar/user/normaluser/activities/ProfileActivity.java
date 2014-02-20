/**
 * ProfileActivity.java
 *
 * 11/10/13
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

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ProfileFragmentActivity())
                .commit();
    }
}
