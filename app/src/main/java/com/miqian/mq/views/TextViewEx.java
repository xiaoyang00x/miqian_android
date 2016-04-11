package com.miqian.mq.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewEx extends TextView {
	private Paint paint = new Paint();

	private String[] blocks;
	private float spaceOffset = 0;
	private float horizontalOffset = 0;
	private float verticalOffset = 0;
	private float horizontalFontOffset = 0;
	private float dirtyRegionWidth = 0;
	private boolean wrapEnabled = true;

	private float strecthOffset;
	private String block;
	private String wrappedLine;
	private Object[] wrappedObj;

	private Bitmap cache = null;
	private boolean cacheEnabled = false;

	public TextViewEx(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setPadding(0, 0, 0, 0);
	}

	public TextViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setPadding(0, 0, 0, 0);
	}

	public TextViewEx(Context context) {
		super(context);
		this.setPadding(0, 0, 0, 0);
	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		super.setPadding(left, top, right, bottom);
	}

	@Override
	public void setDrawingCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

	public void setText(String st, boolean wrap) {
		wrapEnabled = wrap;
		super.setText(st);
	}

	protected static Object[] createWrappedLine(String block, Paint paint, float spaceOffset, float maxWidth) {
		float cacheWidth = maxWidth;
		int start = 0;
		String line = "";
		while (start < block.length()) {
			String word = block.substring(start, ++start);
			cacheWidth = paint.measureText(word);
			maxWidth -= cacheWidth;
			if (maxWidth <= 0) {
				return new Object[] { line, maxWidth + cacheWidth };
			}
			line += word;
		}

		return new Object[] { block, Float.MIN_VALUE };
	}

	@SuppressLint("NewApi")
	@Override
	protected void onDraw(Canvas canvas) {
		if (!wrapEnabled) {
			super.onDraw(canvas);
			return;
		}

		Canvas activeCanvas = null;

		if (cacheEnabled) {
			if (cache != null) {
				canvas.drawBitmap(cache, 0, 0, paint);
				return;
			} else {
				cache = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_4444);
				activeCanvas = new Canvas(cache);
			}
		} else {
			activeCanvas = canvas;
		}
		
		paint.setColor(getCurrentTextColor());
		paint.setTypeface(getTypeface());
		paint.setTextSize(getTextSize());
		paint.setAntiAlias(true);

		// minus out the paddings pixel
		dirtyRegionWidth = getWidth() - getPaddingLeft() - getPaddingRight();
		int maxLines = Integer.MAX_VALUE;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			maxLines = getMaxLines();
		}
		int lines = 1;
		String text = getText().toString();
		blocks = text.split("((?<=\n)|(?=\n))");
		verticalOffset = horizontalFontOffset = getLineHeight() - 0.5f; // Tempfix
		spaceOffset = paint.measureText(" ");

		for (int i = 0; i < blocks.length && lines <= maxLines; i++) {
			block = blocks[i];
			horizontalOffset = 0;

			if (block.length() == 0) {
				continue;
			} else if (block.equals("\n")) {
				verticalOffset += horizontalFontOffset;
				continue;
			}

			if (block.length() == 0) {
				continue;
			}

			wrappedObj = createWrappedLine(block, paint, spaceOffset, dirtyRegionWidth);

			wrappedLine = ((String) wrappedObj[0]);
			activeCanvas.drawText(wrappedLine, horizontalOffset, verticalOffset, paint);
			horizontalOffset += paint.measureText(wrappedLine) + spaceOffset + strecthOffset;
			lines++;
			if (blocks[i].length() > 0) {
				blocks[i] = blocks[i].substring(wrappedLine.length());
				verticalOffset += blocks[i].length() > 0 ? horizontalFontOffset : 0;
				i--;
			}
		}

		if (cacheEnabled) {
			canvas.drawBitmap(cache, 0, 0, paint);
		}
	}
}
