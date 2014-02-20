/**
 * URLResourceActivity.java
 *
 * 11/10/13
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
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import es.uc3m.moc.etiquetar.R;
import es.uc3m.moc.etiquetar.user.normaluser.activities.resources.comments.ResourceCommentsActivity;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;
import es.uc3m.moc.etiquetar.utilities.general.FrequentMethods;

public class URLResourceActivity extends ResourceActivity {

    private WebView wbURLResource;
    private TextView tvURLResourceTitle;
    private TextView tvURLNumberComments;
    private TextView tvURLNumberOfVisits;
    private ImageView ivAddComment;
    private int resourceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_url_layout);
        getReferences();
        setResource(null);
    }

    @Override
    protected void getReferences() {
        wbURLResource = (WebView) findViewById(R.id.wbURLResource);
        tvURLResourceTitle = (TextView) findViewById(R.id.tvURLResourceTitle);
        tvURLNumberComments = (TextView) findViewById(R.id.tvURLNumberComments);
        tvURLNumberOfVisits = (TextView) findViewById(R.id.tvURLNumberOfVisits);
        ivAddComment = (ImageView) findViewById(R.id.ivAddComment);
        resourceID = getIntent().getIntExtra(Constants.RESOURCE_ID, 0);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setResource(Object resource) {
        wbURLResource.setWebViewClient(new WebViewClient());
        wbURLResource.loadUrl(getIntent().getStringExtra(Constants.RESOURCE_URL));
        tvURLResourceTitle.setText(getIntent().getStringExtra(Constants.RESOURCE_NAME));
        tvURLNumberComments.setText(String.valueOf(getIntent().getIntExtra(Constants.RESOURCE_NUMBER_COMMENTS, 0)));
        tvURLNumberOfVisits.setText(String.valueOf(getIntent().getIntExtra(Constants.RESOURCE_VIEW_COUNTER, 0)));
        ivAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentsActivity = new Intent(URLResourceActivity.this, ResourceCommentsActivity.class);
                commentsActivity.putExtra(Constants.RESOURCE_ID_EXTRA, resourceID);
                startActivity(commentsActivity);
            }
        });
    }

    @Override
    protected void restoreValues(Bundle savedInstance) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FrequentMethods.onOptiomItemSelected(this, item.getItemId());
        return super.onOptionsItemSelected(item);
    }
}
