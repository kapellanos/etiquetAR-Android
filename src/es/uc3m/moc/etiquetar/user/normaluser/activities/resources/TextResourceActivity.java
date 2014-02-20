/**
 * TextResourceActivity.java
 *
 * 31/08/13
 *
 * <definicion de la clase>
 *
 * @author Miguel Olmedo Camacho - 100072925
 * @version 1.0
 *
 */
package es.uc3m.moc.etiquetar.user.normaluser.activities.resources;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import es.uc3m.moc.etiquetar.R;
import es.uc3m.moc.etiquetar.user.normaluser.activities.resources.comments.ResourceCommentsActivity;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;
import es.uc3m.moc.etiquetar.utilities.general.FrequentMethods;

public class TextResourceActivity extends ResourceActivity {

    private TextView tvTextResourceTitle;
    private TextView tvTextLikes;
    private TextView tvTextNumberComments;
    private TextView tvTextNumberOfVisits;
    private TextView tvTextInfo;
    private ImageView ivAddComment;

    private String name, viewCounter, info;
    private int resourceID = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getIntent().getStringExtra(Constants.RESOURCE_NAME);
        info = getIntent().getStringExtra(Constants.RESOURCE_INFO);
        resourceID = getIntent().getIntExtra(Constants.RESOURCE_ID, 0);
        getReferences();
        setResource(null);
    }

    @Override
    protected void getReferences() {
        setContentView(R.layout.resource_text_layout);
        tvTextResourceTitle = (TextView) findViewById(R.id.tvTextResourceTitle);
        tvTextInfo = (TextView) findViewById(R.id.tvTextResource);
        tvTextLikes = (TextView) findViewById(R.id.tvTextLikes);
        ivAddComment = (ImageView) findViewById(R.id.ivAddComment);
        tvTextNumberComments = (TextView) findViewById(R.id.tvTextNumberComments);
        tvTextNumberOfVisits = (TextView) findViewById(R.id.tvTextNumberOfVisits);
        tvTextNumberComments.setText(String.valueOf(getIntent().getIntExtra(Constants.RESOURCE_NUMBER_COMMENTS, 0)));
        tvTextNumberOfVisits.setText(String.valueOf(getIntent().getIntExtra(Constants.RESOURCE_VIEW_COUNTER, 0)));
        ivAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentsActivity = new Intent(TextResourceActivity.this, ResourceCommentsActivity.class);
                commentsActivity.putExtra(Constants.RESOURCE_ID_EXTRA, resourceID);
                startActivity(commentsActivity);
            }
        });
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        saveInstanceState.putString("name", name);
//        saveInstanceState.putInt("view_counter", viewCounter);
        saveInstanceState.putString("info", info);
    }

    @Override
    protected void restoreValues(Bundle savedInstanceState) {
        if(name == null)
            name = savedInstanceState.getString("name");
//        viewCounter = savedInstanceState.getInt("view_counter");
        if(info == null)
            info = savedInstanceState.getString("info");
//        if(resourceID == 0) {
//            resourceID = savedInstanceState.getInt("resourceID");
        getReferences();
        setResource(null);
    }

    @Override
    public void setResource(Object resource) {
        tvTextResourceTitle.setText(name);
        tvTextInfo.setText(info);
//        tvTextLikes.setText(String.valueOf(viewCounter));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FrequentMethods.onOptiomItemSelected(this, item.getItemId());
        return super.onOptionsItemSelected(item);
    }
}
