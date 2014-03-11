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
package es.uc3m.moc.etiquetar.user.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import es.uc3m.moc.etiquetar.R;
import es.uc3m.moc.etiquetar.utilities.constants.DBConstants;
import es.uc3m.moc.etiquetar.utilities.database.FrequentQuerys;
import es.uc3m.moc.etiquetar.utilities.layout.LinearLayoutOutlined;

public class CommentsActivity extends Activity {

    private Cursor comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_comments);
        comments = FrequentQuerys.getComments(this);
        drawLayout();
    }

    private void drawLayout() {
        LinearLayout llForAddComments = (LinearLayout) findViewById(R.id.llForAddComments);
        while(comments.moveToNext()) {
            LinearLayout emptyLinearLayout = new LinearLayout(this);
            llForAddComments.addView(emptyLinearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
            LinearLayoutOutlined lloForComment = (LinearLayoutOutlined) getLayoutInflater().inflate(R.layout.comment_template, null);
            TextView tvComment = (TextView) lloForComment.getChildAt(0);
            LinearLayout aux = (LinearLayout) (lloForComment.getChildAt(1));
            TextView tvName = (TextView) aux.getChildAt(0);
            tvComment.setText(comments.getString(comments.getColumnIndex(DBConstants.COMMENTS_TABLE_COMMENT)));
            tvName.setText("En el recurso " + comments.getString(comments.getColumnIndex(DBConstants.COMMENTS_TABLE_RESOURCE_NAME)) + " el día " +
                    comments.getString(comments.getColumnIndex(DBConstants.COMMENTS_TABLE_DATE)));
            llForAddComments.addView(lloForComment);
        }

    }
}
