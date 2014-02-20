package es.uc3m.moc.etiquetar.utilities.general;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TCImageLoader implements ComponentCallbacks {
    public static TCLruCache cache = null;

    public TCImageLoader(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass() * 1024 * 1024;
        if(cache == null)
            cache = new TCLruCache(memoryClass);
    }

    public void display(String url, ImageView imageview) {
        //imageview.setImageResource(defaultresource);
        Bitmap image = cache.get(url);
        if (image != null) {
            imageview.setImageBitmap(image);
        }
        else {
            new SetImageTask(imageview).execute(url);
        }
    }

    public void display(String url, ImageView imageview, int defaultresource, ProgressDialog progressDialog) {
        //imageview.setImageResource(defaultresource);
        Bitmap image = cache.get(url);
        if (image != null) {
            imageview.setImageBitmap(image);
            progressDialog.dismiss();
        }
        else {
            new SetImageTask(imageview, progressDialog).execute(url);
        }
    }

    private class TCLruCache extends LruCache<String, Bitmap> {

        public TCLruCache(int maxSize) {
            super(maxSize);
        }
    }

    private class SetImageTask extends AsyncTask<String, Void, Integer> {
        private ImageView imageview;
        private Bitmap bmp;
        ProgressDialog progressDialog;

        public SetImageTask(ImageView imageview, ProgressDialog progressDialog) {
            this.imageview = imageview;
            this.progressDialog = progressDialog;
        }

        public SetImageTask(ImageView imageview) {
            this.imageview = imageview;
        }

        @Override
        protected Integer doInBackground(String... params) {
            String url = params[0];
            try {
                bmp = getBitmapFromURL(url);
                if (bmp != null) {
                    cache.put(url, bmp);
                }
                else {
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                imageview.setImageBitmap(bmp);
                if(progressDialog != null)
                    progressDialog.dismiss();
            }
            super.onPostExecute(result);
        }

        private Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection
                        = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    @Override
    public void onLowMemory() {
    }

}