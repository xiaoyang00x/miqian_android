package com.miqian.mq.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;
import com.miqian.mq.R;

/**
 * Created by sunyong on 9/17/15.
 */
public class DetailsForRegularEarning extends Activity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_regular_earn_detail);
    colorizePartOfPlainText();

  }

  private void colorizePartOfPlainText() {
    TextView tv = (TextView) findViewById(R.id.annualized_return);
    final SpannableStringBuilder sb = new SpannableStringBuilder("年化率  12.5%");

    // Span to set text color to some RGB value a1a1a1=161 161 161
    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(161, 161, 161));

    // Span to make text bold
    final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

    // Set the text color for first 4 characters
    sb.setSpan(fcs, 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

    // make them also bold
    //sb.setSpan(bss, 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

    tv.setText(sb);
  }
}
