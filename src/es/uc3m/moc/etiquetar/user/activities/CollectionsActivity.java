/**
 * CollectionsActivity.java
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

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import es.uc3m.moc.etiquetar.R;
import es.uc3m.moc.etiquetar.user.activities.resources.SelectProfileActivity;
import es.uc3m.moc.etiquetar.utilities.constants.Constants;
import es.uc3m.moc.etiquetar.utilities.constants.DBConstants;
import es.uc3m.moc.etiquetar.utilities.database.FrequentQuerys;
import es.uc3m.moc.etiquetar.utilities.general.FrequentMethods;
import es.uc3m.moc.etiquetar.utilities.general.TCImageLoader;

public class CollectionsActivity extends YouTubeBaseActivity {
    private final String DEVELOPER_KEY = "AIzaSyAF-Uv4jtp15zqEi2M9HZ8E_V-Ton7S-FY";
    private LinearLayout llCollectionsLinearLayout;
    private Cursor resultCollections;
    private Cursor resultResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_collections);
        getReferences();
        getElements();
    }

    private void getElements() {
        resultCollections = FrequentQuerys.getCollections(getApplicationContext());
        while(resultCollections.moveToNext()) {
            if(FrequentMethods.isPortrait(this))
                drawLayoutPortrait();
            else
                drawLayoutLandscape();
        }
    }

    private void drawLayoutLandscape() {
        resultResources = FrequentQuerys.getResourcesWithCollectionId(this, resultCollections.getInt(resultCollections.getColumnIndex(DBConstants.COLLECTION_TABLE_ID)));
        TextView collectionTitle = new TextView(this);
        collectionTitle.setText(resultCollections.getString(resultCollections.getColumnIndex(DBConstants.COLLECTION_TABLE_NAME)));
        collectionTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        collectionTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams collectionTitleLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        collectionTitleLayoutParams.setMargins(0, 0, 0, 10);
        collectionTitle.setLayoutParams(collectionTitleLayoutParams);
        llCollectionsLinearLayout.addView(collectionTitle);
        LinearLayout oneCollectionContainer = new LinearLayout(this);
        oneCollectionContainer.setWeightSum(15);
        oneCollectionContainer.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams oneCollectionContainerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        oneCollectionContainerLayoutParams.setMargins(0, 0, 0, 40);
        oneCollectionContainer.setLayoutParams(oneCollectionContainerLayoutParams);
        int counter = 0;
        while(resultResources.moveToNext()) {
            TCImageLoader imageCache = new TCImageLoader(this);
            String contentType = resultResources.getString(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_CONTENT_TYPE));
            if(contentType != null && contentType.equals("image")) {
                ImageView imageView = new ImageView(this);
                imageCache.display(resultResources.getString(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_URI)), imageView);

                LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setAdjustViewBounds(true);
                imageViewLayoutParams.weight = 3;
                if(counter == 0)
                    imageViewLayoutParams.setMargins(10, 0, 40, 0);
                else if(counter == 4)
                    imageViewLayoutParams.setMargins(0, 0, 10, 0);
                else
                    imageViewLayoutParams.setMargins(0, 0, 40, 0);
                imageView.setLayoutParams(imageViewLayoutParams);
                imageView.setId(resultResources.getInt(resultResources.getColumnIndex(DBConstants.RESOURCE_QR_ID)));
                final int profileID = resultResources.getInt(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_PROFILE_ID));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent selectProfileActivity = new Intent(getApplicationContext(), SelectProfileActivity.class);
                        selectProfileActivity.putExtra(Constants.LAUNCH_RESOURCE_EXTRA, true);
                        selectProfileActivity.putExtra(Constants.QR_ID_EXTRA, v.getId());
                        selectProfileActivity.putExtra(Constants.PROFILE_ID_EXTRA, profileID);
                        startActivity(selectProfileActivity);
                    }
                });
                oneCollectionContainer.addView(imageView);
            } else if(contentType != null && contentType.equals("video")) {
                YouTubeThumbnailView youTubeThumbnailView = new YouTubeThumbnailView(this);

                LinearLayout.LayoutParams youTubeThumbnailViewLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                youTubeThumbnailView.setAdjustViewBounds(true);
                youTubeThumbnailViewLayoutParams.weight = 3;
                youTubeThumbnailView.setLayoutParams(youTubeThumbnailViewLayoutParams);
                youTubeThumbnailView.setId(resultResources.getInt(resultResources.getColumnIndex(DBConstants.RESOURCE_QR_ID)));
                final int profileID = resultResources.getInt(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_PROFILE_ID));

                youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent selectProfileActivity = new Intent(getApplicationContext(), SelectProfileActivity.class);
                        selectProfileActivity.putExtra(Constants.LAUNCH_RESOURCE_EXTRA, true);
                        selectProfileActivity.putExtra(Constants.QR_ID_EXTRA, v.getId());
                        selectProfileActivity.putExtra(Constants.PROFILE_ID_EXTRA, profileID);
                        startActivity(selectProfileActivity);
                    }
                });
                oneCollectionContainer.addView(youTubeThumbnailView);
                if(counter == 0)
                    youTubeThumbnailViewLayoutParams.setMargins(10, 0, 40, 0);
                else if(counter == 4)
                    youTubeThumbnailViewLayoutParams.setMargins(0, 0, 10, 0);
                else
                    youTubeThumbnailViewLayoutParams.setMargins(0, 0, 40, 0);
                String URL = resultResources.getString(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_URI));
                final String id = URL.substring(URL.lastIndexOf("v=") + 2);
                youTubeThumbnailView.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo(id);
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
                //llCollectionsLinearLayout.addView(youTubeThumbnailView);
            }
            counter++;
            if(counter == 5) {
                llCollectionsLinearLayout.addView(oneCollectionContainer);
                oneCollectionContainer = new LinearLayout(this);
                oneCollectionContainer.setWeightSum(15);
                oneCollectionContainer.setOrientation(LinearLayout.HORIZONTAL);
                oneCollectionContainer.setLayoutParams(oneCollectionContainerLayoutParams);
                counter = 0;
            }
        }
        // Cuando se termina de recorrer las columnas, hay que ver si se ha añadido el último elemento o no. En caso de que no sea 3 el counter, no se habrá añadido la última fila.
        if(counter != 4 && counter != 0)
            llCollectionsLinearLayout.addView(oneCollectionContainer);
    }

    /**
     * Draw the layout in portrait mode
     */
    private void drawLayoutPortrait() {
        resultResources = FrequentQuerys.getResourcesWithCollectionId(this, resultCollections.getInt(resultCollections.getColumnIndex(DBConstants.COLLECTION_TABLE_ID)));
        TextView collectionTitle = new TextView(this);
        collectionTitle.setText(resultCollections.getString(resultCollections.getColumnIndex(DBConstants.COLLECTION_TABLE_NAME)));
        collectionTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        collectionTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams collectionTitleLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        collectionTitleLayoutParams.setMargins(0, 0, 0, 10);
        collectionTitle.setLayoutParams(collectionTitleLayoutParams);
        llCollectionsLinearLayout.addView(collectionTitle);
        LinearLayout oneCollectionContainer = new LinearLayout(this);
        oneCollectionContainer.setWeightSum(9);
        oneCollectionContainer.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams oneCollectionContainerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        oneCollectionContainerLayoutParams.setMargins(0, 0, 0, 40);
        oneCollectionContainer.setLayoutParams(oneCollectionContainerLayoutParams);
        int counter = 0;
        while(resultResources.moveToNext()) {
            TCImageLoader imageCache = new TCImageLoader(this);
            String contentType = resultResources.getString(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_CONTENT_TYPE));
            if(contentType.equals("image")) {
                ImageView imageView = new ImageView(this);
                imageCache.display(resultResources.getString(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_URI)), imageView);

                LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setAdjustViewBounds(true);
                imageViewLayoutParams.weight = 3;
                if(counter == 0)
                    imageViewLayoutParams.setMargins(10, 0, 40, 0);
                else if(counter == 2)
                    imageViewLayoutParams.setMargins(0, 0, 10, 0);
                else
                    imageViewLayoutParams.setMargins(0, 0, 40, 0);
                imageView.setLayoutParams(imageViewLayoutParams);
                imageView.setId(resultResources.getInt(resultResources.getColumnIndex(DBConstants.RESOURCE_QR_ID)));
                final int profileID = resultResources.getInt(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_PROFILE_ID));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent selectProfileActivity = new Intent(getApplicationContext(), SelectProfileActivity.class);
                        selectProfileActivity.putExtra(Constants.LAUNCH_RESOURCE_EXTRA, true);
                        selectProfileActivity.putExtra(Constants.QR_ID_EXTRA, v.getId());
                        selectProfileActivity.putExtra(Constants.PROFILE_ID_EXTRA, profileID);
                        startActivity(selectProfileActivity);
                    }
                });
                oneCollectionContainer.addView(imageView);
            } else if(contentType.equals("video")) {
                YouTubeThumbnailView youTubeThumbnailView = new YouTubeThumbnailView(this);

                LinearLayout.LayoutParams youTubeThumbnailViewLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                youTubeThumbnailView.setAdjustViewBounds(true);
                youTubeThumbnailViewLayoutParams.weight = 3;
                youTubeThumbnailView.setLayoutParams(youTubeThumbnailViewLayoutParams);
                youTubeThumbnailView.setId(resultResources.getInt(resultResources.getColumnIndex(DBConstants.RESOURCE_QR_ID)));
                final int profileID = resultResources.getInt(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_PROFILE_ID));

                youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent selectProfileActivity = new Intent(getApplicationContext(), SelectProfileActivity.class);
                        selectProfileActivity.putExtra(Constants.LAUNCH_RESOURCE_EXTRA, true);
                        selectProfileActivity.putExtra(Constants.QR_ID_EXTRA, v.getId());
                        selectProfileActivity.putExtra(Constants.PROFILE_ID_EXTRA, profileID);
                        startActivity(selectProfileActivity);
                    }
                });
                oneCollectionContainer.addView(youTubeThumbnailView);
                if(counter == 0)
                    youTubeThumbnailViewLayoutParams.setMargins(10, 0, 40, 0);
                else if(counter == 2)
                    youTubeThumbnailViewLayoutParams.setMargins(0, 0, 10, 0);
                else
                    youTubeThumbnailViewLayoutParams.setMargins(0, 0, 40, 0);
                String URL = resultResources.getString(resultResources.getColumnIndex(DBConstants.RESOURCE_TABLE_URI));
                final String id = URL.substring(URL.lastIndexOf("v=") + 2);
                youTubeThumbnailView.initialize(DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo(id);
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
                //llCollectionsLinearLayout.addView(youTubeThumbnailView);
            }

            counter++;
            // Cuando counter sea 3, significa que ya hemos añadido tres elementos al layout y que por lo tanto hay que empezar una nueva fila de elementos
            if(counter == 3) {
                llCollectionsLinearLayout.addView(oneCollectionContainer);
                oneCollectionContainer = new LinearLayout(this);
                oneCollectionContainer.setWeightSum(9);
                oneCollectionContainer.setOrientation(LinearLayout.HORIZONTAL);

                oneCollectionContainer.setLayoutParams(oneCollectionContainerLayoutParams);

                counter = 0;
            }
        }
        // Cuando se termina de recorrer las columnas, hay que ver si se ha añadido el último elemento o no. En caso de que no sea 3 el counter, no se habrá añadido la última fila.
        if(counter != 2 && counter != 0)
            llCollectionsLinearLayout.addView(oneCollectionContainer);
    }

    private void getReferences() {
        llCollectionsLinearLayout = (LinearLayout) findViewById(R.id.llCollectionsLinearLayout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        if (requestCode == 0) { // Escaneo QR
            if (resultCode == RESULT_OK) {  // Resultado correcto
                if(intent.getStringExtra(Constants.SCAN_RESULT_EXTRA).equals("QR_CODE")) {
                    FrequentMethods.launchSelectProfileActivity(this, intent.getStringExtra("SCAN_RESULT"));
                }
            }
        }
    }
}
