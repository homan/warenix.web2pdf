package org.dyndns.warenix.web2pdf;

import java.io.IOException;

import org.dyndns.warenix.expsdk.api.API;
import org.dyndns.warenix.expsdk.api.pdf.ConvertResult;
import org.dyndns.warenix.expsdk.api.pdf.ConvertService;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Backend service to talk to Web2PDF API
 * 
 * @author warenix
 * 
 */
public class Web2PDFIntentService extends IntentService {
  private static final String TAG = "Web2PDFIntentService";
  private static final String BUNDLE_ARG =
      "org.dyndns.warenix.web2pdf.Web2PDFIntentService.BUNDLE_ARG";

  public static final String ACTION_WEB2PDF =
      "org.dyndns.warenix.web2pdf.Web2PDFIntentService.ACTION_WEB2PDF";

  /**
   * notification id of conversion error
   */
  private static final int ERROR_NOTIFICATION_ID = 1;
  NotificationManager mNotificationManager;
  Notification mNotification;

  public Web2PDFIntentService() {
    super("Web2PDFIntentService");
  }

  public void onCreate() {
    super.onCreate();
    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    String action = intent.getAction();
    if (ACTION_WEB2PDF.equals(action)) {
      handleWeb2PDF(intent);
    }
  }

  /**
   * logic to pass user requirement to api service
   * 
   * @param intent
   */
  private void handleWeb2PDF(Intent intent) {
    Bundle extras = intent.getExtras();
    Web2PDFArgument arg = extras.getParcelable(BUNDLE_ARG);
    ConvertService service = new ConvertService(arg.url, arg.size, arg.orientation);

    try {
      ConvertResult result = (ConvertResult) API.makeCall(service);
      if (result.error != null) {
        Log.d(TAG, String.format("error convering [%s]occurs:%s", arg.url, result.error));
        showNotification(arg.url, result.error);
        return;
      }
      // Log.d(TAG, "pdf_url:" + result.result.pdf_url);
      // download pdf
      if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
        // only for gingerbread and newer versions
        downloadPDFUsingDownloadManager(arg, result);
      } else {
        downloadPDFUsingBrowser(arg, result);
      }
    } catch (IOException e) {
      e.printStackTrace();
      showNotification(arg.url, e.getMessage());
    }
  }

  private void showNotification(String url, String error) {
    // generate notification
    String notificationText = error;
    mNotification =
        new NotificationCompat.Builder(getApplicationContext()).setContentTitle(url)
            .setContentText(notificationText).setTicker(error).setWhen(System.currentTimeMillis())
            .setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher).build();

    mNotificationManager.notify(ERROR_NOTIFICATION_ID, mNotification);
  }

  /**
   * for device having older version than {@link VERSION_CODES#GINGERBREAD}, use device system
   * browser to open the generated pdf
   * 
   * @param arg
   * @param result
   */
  private void downloadPDFUsingBrowser(Web2PDFArgument arg, ConvertResult result) {
    Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(result.result.pdf_url));
    viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(viewIntent);
  }

  /**
   * for device having newer version than {@link VERSION_CODES#GINGERBREAD}, use
   * {@link DownloadManager} to download the generated pdf
   * 
   * @param arg
   * @param result
   */
  @SuppressWarnings("deprecation")
  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private void downloadPDFUsingDownloadManager(Web2PDFArgument arg, ConvertResult result) {
    DownloadManager.Request down = new DownloadManager.Request(Uri.parse(result.result.pdf_url));
    down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
        | DownloadManager.Request.NETWORK_WIFI);
    down.setVisibleInDownloadsUi(true);
    if (VERSION.SDK_INT < VERSION_CODES.HONEYCOMB) {
      down.setShowRunningNotification(true);
    } else {
      if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
      } else {
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
      }
    }

    down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, arg.filename);

    DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    manager.enqueue(down);
  }

  /**
   * Helper method to start this service
   * 
   * @param context
   * @param arg
   */
  public static void startService(Context context, Web2PDFArgument arg) {
    Bundle extras = new Bundle();
    extras.putParcelable(BUNDLE_ARG, arg);
    Intent intent = new Intent(Web2PDFIntentService.ACTION_WEB2PDF);
    intent.putExtras(extras);
    context.startService(intent);
  }
}
