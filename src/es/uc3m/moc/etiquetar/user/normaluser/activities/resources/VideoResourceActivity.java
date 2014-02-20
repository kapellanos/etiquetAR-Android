/**
 * VideoResourceActivity.java
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
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import es.uc3m.moc.etiquetar.R;
import es.uc3m.moc.etiquetar.user.normaluser.activities.resources.comments.ResourceCommentsActivity;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;
import es.uc3m.moc.etiquetar.utilities.general.FrequentMethods;

public class VideoResourceActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener{

    private final String DEVELOPER_KEY = "AIzaSyAF-Uv4jtp15zqEi2M9HZ8E_V-Ton7S-FY";

    private TextView tvVideoResourceTitle;
    private TextView tvInfoVideo;
    private TextView tvImageLikes;
    private TextView tvVideoNumberComments;
    private TextView tvVideoNumberOfVisits;

    private YouTubePlayerView ytVideoResource;
    private YouTubePlayer youTubePlayer;

    private LinearLayout llAddComments;
    private ScrollView svInfoVideo;
    private LinearLayout llIconsVideo;
    private LinearLayout llVideoContainer;
    private LinearLayout llLayout;
    private ImageView ivAddComment;


    private boolean fullScreen = false;
    String video;
    private int resourceID = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.resource_video_layout);
        getReferences();
        setInitialValues();
        setListeners();
        setVideo(getIntent().getStringExtra(Constants.RESOURCE_URL));
    }

    private void setListeners() {
        ivAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentsActivity = new Intent(VideoResourceActivity.this, ResourceCommentsActivity.class);
                commentsActivity.putExtra(Constants.RESOURCE_ID_EXTRA, resourceID);
                startActivity(commentsActivity);
            }
        });
    }

    private void setInitialValues() {
        Intent callingIntent = getIntent();
        tvVideoResourceTitle.setText(callingIntent.getStringExtra(Constants.RESOURCE_NAME));
        tvInfoVideo.setText(callingIntent.getStringExtra(Constants.RESOURCE_INFO));
        tvVideoNumberOfVisits.setText(String.valueOf(callingIntent.getIntExtra(Constants.RESOURCE_VIEW_COUNTER, 0)));
        tvVideoNumberComments.setText(String.valueOf(callingIntent.getIntExtra(Constants.RESOURCE_NUMBER_COMMENTS, 0)));
    }

    private void getReferences() {
        resourceID = getIntent().getIntExtra(Constants.RESOURCE_ID, 0);
        ytVideoResource = (YouTubePlayerView) findViewById(R.id.ytVideoResource);
        tvInfoVideo = (TextView) findViewById(R.id.tvInfoVideo);
        tvVideoResourceTitle = (TextView) findViewById(R.id.tvVideoResourceTitle);
        tvVideoNumberOfVisits = (TextView) findViewById(R.id.tvVideoNumberOfVisits);
        tvImageLikes = (TextView) findViewById(R.id.tvImageLikes);
        tvVideoNumberComments = (TextView) findViewById(R.id.tvVideoNumberComments);
        ivAddComment = (ImageView) findViewById(R.id.ivAddComment);
        llAddComments = (LinearLayout) findViewById(R.id.llAddComments);
        svInfoVideo = (ScrollView) findViewById(R.id.svInfoVideo);
        llIconsVideo = (LinearLayout) findViewById(R.id.llIconsVideo);
        llVideoContainer = (LinearLayout) findViewById(R.id.llVideoContainer);
        llLayout = (LinearLayout) findViewById(R.id.lloOutlinedVideo);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Obtiene el identificador del vídeo
     */
    private void setVideo(String URL) {
        // Se inicializa el vídeo con la clave de desarrollador obtenida
        ytVideoResource.initialize(DEVELOPER_KEY, this);
        // Se comprueba que la URL se corresponde con un vídeo de youtube
        if(!URL.startsWith("http://www.youtube.com/")) {
            // Error. La URL no se corresponde con un vídeo de YouTube
//            showAlertDialog(getResources().getString(R.string.alertDialogErrorURLVideoTitle), getResources().getString(R.string.alertDialogErrorURLVideo));
        } else {
            // Se obtiene el identificador del vídeo dividiendo la URL en dos partes
            video = URL.substring(URL.lastIndexOf("v=") + 2);
        }
    }

    @Override
    public void onFullscreen(boolean isFullScreen) {
        this.fullScreen = isFullScreen;
        doLayout();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doLayout();
    }

    private void doLayout() {
        LinearLayout.LayoutParams playerParams = (LinearLayout.LayoutParams) ytVideoResource.getLayoutParams();
        LinearLayout.LayoutParams containerParams = (LinearLayout.LayoutParams) llVideoContainer.getLayoutParams();
        FrameLayout.LayoutParams outlinedParams = (FrameLayout.LayoutParams) llLayout.getLayoutParams();
        if(fullScreen) {
            playerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            playerParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            playerParams.setMargins(0,0,0,0);
            containerParams.weight = 10;
            svInfoVideo.setVisibility(View.GONE);
            llIconsVideo.setVisibility(View.GONE);
            llAddComments.setVisibility(View.GONE);
            tvVideoResourceTitle.setVisibility(View.GONE);
            llLayout.setPadding(0, 0, 0, 0);
            outlinedParams.setMargins(0,0,0,0);
        } else {
            // Restore the original layout
            llLayout.setPadding((int) getResources().getDimension(R.dimen.activity_horizontal_margin), (int) getResources().getDimension(R.dimen.activity_vertical_margin), 4, (int) getResources().getDimension(R.dimen.activity_vertical_margin));
            containerParams.weight = 3.5f;
            svInfoVideo.setVisibility(View.VISIBLE);
            llIconsVideo.setVisibility(View.VISIBLE);
            llAddComments.setVisibility(View.VISIBLE);
            tvVideoResourceTitle.setVisibility(View.VISIBLE);
            playerParams.width = 0;
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if(!wasRestored)
            player.cueVideo(video);
        youTubePlayer = player;
//        if(!isPortrait()) {
//            player.setFullscreen(true);
//        } else {
//            player.setFullscreen(false);
//        }
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        int controlFlags = player.getFullscreenControlFlags();
        controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        player.setFullscreenControlFlags(controlFlags);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.e("Youtube initialize", youTubeInitializationResult.name());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FrequentMethods.onOptiomItemSelected(this, item.getItemId());
        return super.onOptionsItemSelected(item);
    }
}
