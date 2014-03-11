/**
 * ResponseActivity.java
 *
 * 11/09/13
 *
 * <definicion de la clase>
 *
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 *
 */
package es.uc3m.moc.etiquetar.user.activities.resources.comments;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import es.uc3m.moc.etiquetar.R;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;
import es.uc3m.moc.etiquetar.utilities.general.FrequentMethods;
import es.uc3m.moc.etiquetar.utilities.layout.LinearLayoutOutlined;

public class ResponseActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.response_comments_layout);
        LinearLayout llForAddComments = (LinearLayout) findViewById(R.id.llForAddComments);
        try {
            JSONArray commentsObtained = new JSONArray(getIntent().getStringExtra("JSON"));
            for(int i = commentsObtained.length() - 1; i >= 0; i--) {
                try {
                    final String name = commentsObtained.getJSONObject(i).getString(Constants.JSON_COMMENT_NAME);
                    final int id = commentsObtained.getJSONObject(i).getInt(Constants.JSON_COMMENT_ID);
                    String date = FrequentMethods.parseDate(commentsObtained.getJSONObject(i).getString(Constants.JSON_COMMENT_DATE));
                    // Hay que parsear la fecha.
                    LinearLayoutOutlined lloForComment = (LinearLayoutOutlined) getLayoutInflater().inflate(R.layout.response_comment_template, null);
                    TextView tvComment = (TextView) lloForComment.getChildAt(0);
                    TextView tvName = (TextView) lloForComment.getChildAt(1);
                    if(name.equals("null"))
                        tvName.setText("Anónimo" + " el día " + date);
                    else
                        tvName.setText(commentsObtained.getJSONObject(i).getString(Constants.JSON_COMMENT_NAME) + " el día " + date);
                    tvComment.setText(commentsObtained.getJSONObject(i).getString(Constants.JSON_COMMENT));
                    LinearLayout emptyLinearLayout = new LinearLayout(this);
                    // Se añade el comentario
                    llForAddComments.addView(lloForComment);
                    // Se añade el LinearLayout vacío para crear márgenes entre los comentarios.
                    llForAddComments.addView(emptyLinearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
