/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.uc3m.moc.etiquetar.qrlecture.result;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ResultParser;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import es.uc3m.moc.etiquetar.R;

/**
 * A base class for the Android-specific barcode handlers. These allow the app to polymorphically
 * suggest the appropriate actions for each data type.
 *
 * This class also contains a bunch of utility methods to take common actions like opening a URL.
 * They could easily be moved into a helper object, but it can't be static because the Activity
 * instance is needed to launch an intent.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public abstract class ResultHandler {

  private static final String TAG = ResultHandler.class.getSimpleName();

  private static final DateFormat DATE_FORMAT;
  static {
    DATE_FORMAT = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    // For dates without a time, for purposes of interacting with Android, the resulting timestamp
    // needs to be midnight of that day in GMT. See:
    // http://code.google.com/p/android/issues/detail?id=8330
    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
  }
  private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH);

  private static final String GOOGLE_SHOPPER_PACKAGE = "com.google.android.apps.shopper";
  private static final String GOOGLE_SHOPPER_ACTIVITY = GOOGLE_SHOPPER_PACKAGE +
      ".results.SearchResultsActivity";
  private static final String MARKET_URI_PREFIX = "market://details?id=";
  private static final String MARKET_REFERRER_SUFFIX =
      "&referrer=utm_source%3Dbarcodescanner%26utm_medium%3Dapps%26utm_campaign%3Dscan";

  private static final int NO_TYPE = -1;

  public static final int MAX_BUTTON_COUNT = 4;

  private final ParsedResult result;
  private final Activity activity;
  private final Result rawResult;
  private final String customProductSearch;

  private final DialogInterface.OnClickListener shopperMarketListener =
      new DialogInterface.OnClickListener() {
    
    public void onClick(DialogInterface dialogInterface, int which) {
      launchIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URI_PREFIX +
          GOOGLE_SHOPPER_PACKAGE + MARKET_REFERRER_SUFFIX)));
    }
  };

  ResultHandler(Activity activity, ParsedResult result) {
    this(activity, result, null);
  }

  ResultHandler(Activity activity, ParsedResult result, Result rawResult) {
    this.result = result;
    this.activity = activity;
    this.rawResult = rawResult;
    this.customProductSearch = parseCustomSearchURL();

    // Make sure the Shopper button is hidden by default. Without this, scanning a product followed
    // by a QR Code would leave the button on screen among the QR Code actions.
  }

  public ParsedResult getResult() {
    return result;
  }

  boolean hasCustomProductSearch() {
    return customProductSearch != null;
  }

  Activity getActivity() {
    return activity;
  }

  /**
   * Indicates how many buttons the derived class wants shown.
   *
   * @return The integer button count.
   */
  public abstract int getButtonCount();

  /**
   * The text of the nth action button.
   *
   * @param index From 0 to getButtonCount() - 1
   * @return The button text as a resource ID
   */
  public abstract int getButtonText(int index);


  /**
   * Execute the action which corresponds to the nth button.
   *
   * @param index The button that was clicked.
   */
  public abstract void handleButtonPress(int index);

  /**
   * Some barcode contents are considered secure, and should not be saved to history, copied to
   * the clipboard, or otherwise persisted.
   *
   * @return If true, do not create any permanent record of these contents.
   */
  public boolean areContentsSecure() {
    return false;
  }

  /**
   * Create a possibly styled string for the contents of the current barcode.
   *
   * @return The text to be displayed.
   */
  public CharSequence getDisplayContents() {
    String contents = result.getDisplayResult();
    return contents.replace("\r", "");
  }

  /**
   * A string describing the kind of barcode that was found, e.g. "Found contact info".
   *
   * @return The resource ID of the string.
   */
  public abstract int getDisplayTitle();

  /**
   * A convenience method to get the parsed type. Should not be overridden.
   *
   * @return The parsed type, e.g. URI or ISBN
   */
  public final ParsedResultType getType() {
    return result.getType();
  }


  private static long calculateMilliseconds(String when) {
    if (when.length() == 8) {
      // Only contains year/month/day
      Date date;
      synchronized (DATE_FORMAT) {
        date = DATE_FORMAT.parse(when, new ParsePosition(0));
      }
      // Note this will be relative to GMT, not the local time zone
      return date.getTime();
    } else {
      // The when string can be local time, or UTC if it ends with a Z
      Date date;
      synchronized (DATE_TIME_FORMAT) {
       date = DATE_TIME_FORMAT.parse(when.substring(0, 15), new ParsePosition(0));
      }
      long milliseconds = date.getTime();
      if (when.length() == 16 && when.charAt(15) == 'Z') {
        Calendar calendar = new GregorianCalendar();
        int offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
        milliseconds += offset;
      }
      return milliseconds;
    }
  }


  final void shareByEmail(String contents) {
    sendEmailFromUri("mailto:", null, activity.getString(R.string.msg_share_subject_line),
        contents);
  }


  // Use public Intent fields rather than private GMail app fields to specify subject and body.
  final void sendEmailFromUri(String uri, String email, String subject, String body) {
    Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(uri));
    if (email != null) {
      intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
    }
    putExtra(intent, Intent.EXTRA_SUBJECT, subject);
    putExtra(intent, Intent.EXTRA_TEXT, body);
    intent.setType("text/plain");
    launchIntent(intent);
  }

  final void shareBySMS(String contents) {
    sendSMSFromUri("smsto:", activity.getString(R.string.msg_share_subject_line) + ":\n" +
        contents);
  }


  final void sendSMSFromUri(String uri, String body) {
    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
    putExtra(intent, "sms_body", body);
    // Exit the app once the SMS is sent
    intent.putExtra("compose_mode", true);
    launchIntent(intent);
  }



  final void openURL(String url) {
    launchIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
  }

  final void webSearch(String query) {
    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
    intent.putExtra("query", query);
    launchIntent(intent);
  }

  void launchIntent(Intent intent) {
    if (intent != null) {
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
      Log.d(TAG, "Launching intent: " + intent + " with extras: " + intent.getExtras());
      try {
        activity.startActivity(intent);
      } catch (ActivityNotFoundException e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.msg_intent_failed);
        builder.setPositiveButton(R.string.button_ok, null);
        builder.show();
      }
    }
  }

  private static void putExtra(Intent intent, String key, String value) {
    if (value != null && value.length() > 0) {
      intent.putExtra(key, value);
    }
  }

  private String parseCustomSearchURL() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    String customProductSearch = null;
    if (customProductSearch != null && customProductSearch.trim().length() == 0) {
      return null;
    }
    return customProductSearch;
  }

  String fillInCustomSearchURL(String text) {
    if (customProductSearch == null) {
      return text; // ?
    }
    String url = customProductSearch.replace("%s", text);
    if (rawResult != null) {
      url = url.replace("%f", rawResult.getBarcodeFormat().toString());
      if (url.contains("%t")) {
        ParsedResult parsedResultAgain = ResultParser.parseResult(rawResult);
        url = url.replace("%t", parsedResultAgain.getType().toString());
      }
    }
    return url;
  }

}
