package org.dyndns.warenix.web2pdf;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends FragmentActivity implements OnClickListener {
  private static final String TAG = "MainActivity";

  boolean m = false;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.size_list);
    int[] ids = {R.id.a4, R.id.a3, R.id.legal, R.id.letter};
    for (int id : ids) {
      findViewById(id).setOnClickListener(this);
    }

  }

  @Override
  public void onClick(View view) {
    m = !m;
    // view.setSelected(m);

    Log.d(TAG, "set selected?" + m);
    // int width = (int) pxFromDp(getResources().getDimension(R.dimen.long_legal), this);
    // int height = (int) convertDpToPixel(getResources().getDimension(R.dimen.short_legal), this);
    LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
    int t = lp.width;
    lp.width = lp.height;
    lp.height = t;
    view.setLayoutParams(lp);
  }


  /**
   * This method converts dp unit to equivalent pixels, depending on device density.
   * 
   * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
   * @param context Context to get resources and device specific display metrics
   * @return A float value to represent px equivalent to dp depending on device density
   */
  public static float convertDpToPixel(float dp, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float px = dp * (metrics.densityDpi / 160f);
    return px;
  }

  /**
   * This method converts device specific pixels to density independent pixels.
   * 
   * @param px A value in px (pixels) unit. Which we need to convert into db
   * @param context Context to get resources and device specific display metrics
   * @return A float value to represent dp equivalent to px value
   */
  public static float convertPixelsToDp(float px, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float dp = px / (metrics.densityDpi / 160f);
    return dp;
  }

  private float dpFromPx(float px, Context context) {
    return px / context.getResources().getDisplayMetrics().density;
  }

  private float pxFromDp(float dp, Context context) {
    return dp * context.getResources().getDisplayMetrics().density;
  }
}
